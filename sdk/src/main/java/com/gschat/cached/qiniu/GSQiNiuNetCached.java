package com.gschat.cached.qiniu;

import com.google.gson.Gson;
import com.gschat.cached.GSCloud;
import com.gschat.cached.GSCloudCached;
import com.gschat.cached.GSCloudUploader;
import com.gschat.cached.GSLocalCached;
import com.gschat.sdk.GSConfig;
import com.gschat.sdk.GSError;
import com.gschat.sdk.GSException;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.KeyGenerator;
import com.qiniu.android.storage.Recorder;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.qiniu.android.storage.persistent.FileRecorder;
import com.qiniu.android.utils.Etag;
import com.qiniu.android.utils.UrlSafeBase64;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


public class GSQiNiuNetCached implements GSCloud {

    private static final Logger logger = LoggerFactory.getLogger("GSQiNiuNetCached");

    private final String domain;
    private final String accessKey;

    private final String secretkey;
    private final String bucket;

    private final UploadManager uploadManager;

    private Executor executor;

    public GSQiNiuNetCached(String domain, String accessKey, String secretkey, String bucket) {
        this.domain = domain;

        this.accessKey = accessKey;
        this.secretkey = secretkey;
        this.bucket = bucket;

        Recorder recorder = null;
        try {

            File file = new File(GSConfig.StoreRootPath + "/uploader");

            if (!file.exists()) {
                file.mkdirs();
            }

            recorder = new FileRecorder(file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }

        KeyGenerator keyGen = new KeyGenerator() {
            public String gen(String key, File file) {
                return new StringBuffer(file.getAbsolutePath()).reverse().toString();
            }
        };

        uploadManager = new UploadManager(recorder, keyGen);
    }

    @Override
    public void setExecutor(Executor executor) {

        this.executor = executor;
    }

    private String uploadToken() {

        try {
            PutPolicy putPolicy = new PutPolicy();

            putPolicy.setDeadline(new Date().getTime() / 1000 + 3600);

            putPolicy.setScope(this.bucket);

            ReturnBody returnBody = new ReturnBody();

            returnBody.magicValue(ReturnBody.ETag, "hash");

            returnBody.magicValue(ReturnBody.FSize, "size");

            putPolicy.setReturnBody(returnBody.toJson());

            Gson gson = new Gson();

            String putPolicyJson = gson.toJson(putPolicy);

            logger.debug("put policy :{}", putPolicyJson);


            String encodedPutPolicy = UrlSafeBase64.encodeToString(putPolicyJson.getBytes("UTF8"));

            Mac mac = Mac.getInstance("HmacSHA1");

            mac.init(new SecretKeySpec(this.secretkey.getBytes("UTF-8"), mac.getAlgorithm()));

            String encodedSign = UrlSafeBase64.encodeToString(mac.doFinal(encodedPutPolicy.getBytes("UTF-8")));


            return this.accessKey + ":" + encodedSign + ":" + encodedPutPolicy;

        } catch (Exception e) {
            logger.debug("create upload token error", e);

            return "";
        }
    }


    private String downloadToken(String url) {

        try {

            Mac mac = Mac.getInstance("HmacSHA1");

            mac.init(new SecretKeySpec(this.secretkey.getBytes("UTF-8"), mac.getAlgorithm()));

            return this.accessKey + ":" + UrlSafeBase64.encodeToString(mac.doFinal(url.getBytes("UTF-8")));

        } catch (Exception e) {
            logger.error("create download token for url {} error", url, e);
            return "";
        }
    }

    private Mac createMac() throws Exception{
        Mac mac = Mac.getInstance("HmacSHA1");

        mac.init(new SecretKeySpec(this.secretkey.getBytes("UTF-8"), mac.getAlgorithm()));

        return mac;
    }

    /**
     * create EncodedEntryURI
     * @param bucket private storage bucket
     * @param key resource key
     * @return urlsafe_base64_encode(entry)
     */
    private static String entry(String bucket,String key) {
        return UrlSafeBase64.encodeToString(bucket + ":" + key);
    }

    private void authorization(HashMap<String,String> header ,String url) throws Exception {


        URI uri = URI.create(url);

        String path = uri.getRawPath();

        String query = uri.getRawQuery();

        Mac mac = createMac();

        mac.update(path.getBytes("UTF-8"));

        if (query != null && query.length() != 0) {

            mac.update((byte) ('?'));

            mac.update(query.getBytes("UTF-8"));
        }

        mac.update((byte) '\n');

        String digest = UrlSafeBase64.encodeToString(mac.doFinal());

        String token = this.accessKey + ":" + digest;

        header.put("Authorization", "QBox " + token);
    }

    private QiniuFile stat(String key) throws Exception {

        String url = "http://rs.qiniu.com/stat/" + entry(this.bucket,key);

        HashMap<String,String> header = new HashMap<>();

        authorization(header, url);

        Gson gson = new Gson();

        return gson.fromJson(httpGet(new URL(url), header), QiniuFile.class);
    }

    private String httpGet(URL url,HashMap<String,String> header) throws Exception {


        HttpURLConnection connection = null;

        BufferedReader reader = null;

        try {

            connection = (HttpURLConnection) url.openConnection();

            connection.setConnectTimeout(5000);

            connection.setRequestMethod("GET");

            for(Map.Entry<String,String> entry : header.entrySet()) {
                connection.setRequestProperty(entry.getKey(),entry.getValue());
            }

            connection.setDoInput(true);

            int code = connection.getResponseCode();

            InputStream stream = null;

            if(code == 200) {
                stream = connection.getInputStream();
            } else {
                stream = connection.getErrorStream();
            }

            reader = new BufferedReader(new InputStreamReader(stream));

            String line;

            StringBuilder stringBuilder = new StringBuilder();

            while((line =reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append('\n');
            }

            if(code == 200) {
                return stringBuilder.toString();
            }

            throw new QiniuException(stringBuilder.toString(),code);

        } finally {
            if(connection != null) {
                connection.disconnect();
            }

            if (reader != null) {
                reader.close();
            }
        }

    }

    private void doDownload(URL path, File localFile, GSLocalCached downloader) {

        String fileName = path.getPath().substring(1);

        // check local file if exists

        if(localFile.exists()){
            try {
                if(Etag.file(localFile) == fileName) {
                    downloader.attachLocalFile(localFile);
                    return;
                }
            } catch (IOException e) {
                logger.error("calc etag error {}",e);
            }
        }


        InputStream inputStream = null;

        try {

            QiniuFile fileInfo = stat(fileName);

            logger.debug("download file size :{}",fileInfo.getFsize());

            downloader.beginWriteLocal(localFile);

            URL url = new URL(downloadURL(path.toString()));

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setConnectTimeout(5000);

            connection.setRequestMethod("GET");

            connection.setDoInput(true);

            if (connection.getResponseCode() == 200) {

                inputStream = connection.getInputStream();

                byte[] buffer = new byte[1024];

                int len;

                int offset = 0;

                while ((len = inputStream.read(buffer)) != -1) {

                    offset += len;

                    logger.debug("{} recv bytes {}/{}",fileInfo.getHash(),offset,fileInfo.getFsize());

                    downloader.writeLocal(buffer, 0, len, offset * 100 / fileInfo.getFsize());
                }

                downloader.endWriteLocal(null);


            } else {
                downloader.endWriteLocal(new GSException(GSError.RESOURCE));
            }


        } catch (Exception e) {

            e.printStackTrace();

            downloader.endWriteLocal(e);


        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("close stream error", e);
                }
            }
        }
    }


    public String downloadURL(String url) {

        if (url.contains("?")) {
            url = url + "&e=" + (new Date().getTime() / 1000 + 3600);
        } else {
            url = url + "?e=" + (new Date().getTime() / 1000 + 3600);
        }

        url = url + "&token=" + downloadToken(url);

        logger.debug("download url :{}", url);

        return url;
    }

    @Override
    public void upload(final boolean image, final File file, final GSCloudCached cached, final GSCloudUploader uploader) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                doUpload(image, file, cached,uploader);
            }
        });

    }

    private void doUpload(boolean image, File file, final GSCloudCached cached, final GSCloudUploader uploader){
        logger.debug("upload image :{}", file);


        cached.onBeginWriteCloud();

        try {
            String etag = Etag.file(file);

            QiniuFile qiniuFile = stat(etag); // if not throw exception indicate file exists in qiniu cloud

            URL url = new URL("http", domain, "/" + etag);

            URL thumbnail = new URL(url.toString() + "?imageView2/2/w/512/h/512");

            uploader.onSuccess(qiniuFile.getFsize(),url,thumbnail);

            cached.onEndWriteCloud(url, null);

            return;

        } catch (QiniuException e) {

            logger.debug("file {} not exists in qiniu cloud",file);

        } catch (Exception e) {

            cached.onEndWriteCloud(null, e);

            return;

        }

        uploadManager.put(file, null, uploadToken(), new UpCompletionHandler() {
            @Override
            public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {

                if (!responseInfo.isOK()) {
                    logger.warn("upload error {}", responseInfo.error);

                    cached.onEndWriteCloud(
                            null,
                            new Exception(String.format("qiniu upload error : %s", responseInfo.error))
                    );
                }

                try {

                    String etag = jsonObject.getString("hash");

                    int size = (int) jsonObject.getLong("size");

                    URL url = new URL("http", domain, "/" + etag);

                    URL thumbnail = new URL(url.toString() + "?imageView2/2/w/512/h/512");

                    uploader.onSuccess(size, url, thumbnail);

                    cached.onEndWriteCloud(url, null);

                    logger.debug("upload url : {}", url);

                } catch (Exception e) {
                    logger.error("get upload url error");

                    cached.onEndWriteCloud(null, e);
                }

                logger.debug("response {}", jsonObject);

            }

        }, new UploadOptions(null, null, false, new UpProgressHandler() {

            @Override
            public void progress(String s, double v) {

                logger.debug("upload process :{}", v);

                cached.onWriteCloud((int) (v * 100));
            }

        }, null));
    }


    @Override
    public void download(final URL url, final File localFile, final GSLocalCached cached) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                doDownload(url, localFile, cached);
            }
        });
    }
}
