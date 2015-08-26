package com.gschat.core;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

import com.gschat.database.Client;
import com.gschat.database.RootModule;
import com.gschat.sdk.GSConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * GSChat service
 */
public class CoreService extends Service {

    enum NetState {
        NONE,WIFI_CONNECT,MOBILE_CONNECT
    }

    /**
     * CoreService logger
     */
    private static final Logger logger = LoggerFactory.getLogger("CoreService");


    private final ConcurrentHashMap<String,CoreServiceBinder> binders = new ConcurrentHashMap<>();

    /**
     *  root database
     */
    private Realm rootDatabase;

    /**
     * database config
     */
    private RealmConfiguration config;

    private NetState netState;

    public CoreService() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        rootDatabase.close();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        logger.debug("create service");

        File targetDir = new File(GSConfig.StoreRootPath);

        if(!targetDir.mkdirs()) {
            logger.debug("target dir already exists :{}",targetDir);
        }

        config = new RealmConfiguration.Builder(targetDir)
                .name("gschat.realm")
                .setModules(new RootModule())
                .build();


        rootDatabase = Realm.getInstance(config);

        RealmResults<Client> clients = rootDatabase.where(Client.class).findAll();

        for(Client client: clients) {
            logger.debug("create service binder for client {}",client.getUrl());

            CoreServiceBinder binder = binders.put(client.getUrl(), new CoreServiceBinder(this,client));

            logger.debug("create service binder for client {} -- success",client.getUrl());
        }

        try {
            AndroidTimer.startRPCTimer(getApplicationContext());
        } catch (Exception e) {
            logger.error("start rpc timer error",e);
        }

        this.netState = getNetState();
    }

    private NetState getNetState() {

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (info.getState() == NetworkInfo.State.CONNECTED) {
            return NetState.WIFI_CONNECT;
        }

        info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (info.getState() == NetworkInfo.State.CONNECTED) {
            return NetState.MOBILE_CONNECT;
        }

        return NetState.NONE;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null || intent.getAction() == null) {
            return START_STICKY;
        }

        switch (intent.getAction()){

            case GSConfig.ReconnectAction:{

                String uri = intent.getStringExtra("uri");

                CoreServiceBinder userBinder = binders.get(uri);

                if (userBinder == null) {
                    logger.warn("not found user {} for channel reconnect ", intent.getStringExtra("uri"));
                    break;
                }

                userBinder.reconnect(false);

                break;
            }
            case GSConfig.RPCTimeoutAction:{

                for(CoreServiceBinder userBinder: binders.values()) {
                    userBinder.handleRPCTimeout();
                }

                break;
            }
            case ConnectivityManager.CONNECTIVITY_ACTION:{

                NetState netState = getNetState();

                if(netState != NetState.NONE && this.netState != netState) {

                    for(CoreServiceBinder userBinder: binders.values()) {
                        userBinder.reconnect(true);
                    }
                }

                this.netState = netState;

                break;
            }
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {

        logger.debug("onBind ...");

        String uri = intent.getStringExtra("clientURI");

        if (uri == null) {
            logger.error("onBind must set clientURI extra attribute");
            return null;
        }

        logger.debug("{} call bind", uri);

        CoreServiceBinder binder = binders.get(uri);

        if(binder == null){

            logger.debug("{} call bind -- create new CoreServiceBinder", uri);

            Client client = rootDatabase.where(Client.class).equalTo("url",uri).findFirst();

            if(client == null) {

                rootDatabase.beginTransaction();

                client = rootDatabase.createObject(Client.class);

                client.setUrl(uri);

                rootDatabase.commitTransaction();
            }

            binder = new CoreServiceBinder(this,client);

            binders.put(uri, binder);
        }

        logger.debug("{} call bind -- success", uri);

        return binder;
    }

    public void updateClient(final Client client) {

        Realm realm = Realm.getInstance(this.config);

        try{
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(client);
                }
            });

        } finally {
            realm.refresh();
            realm.close();
        }


    }
}