package com.gschat.cached.qiniu;


import java.util.HashMap;

public final class ReturnBody {

    public static final String Bucket = "$(bucket)";

    public static final String Key = "$(key)";

    public static final String ETag = "$(etag)";

    public static final String FSize = "$(fsize)";

    private final HashMap<String,String> fields = new HashMap<>();

    public void magicValue(String magic,String name) {
        fields.put(magic,name);
    }

    public String toJson() {
        StringBuilder builder = new StringBuilder();

        builder.append('{');

        for(HashMap.Entry<String,String> pair:fields.entrySet()) {
            builder.append(String.format("\"%s\":%s,",pair.getValue(),pair.getKey()));
        }

        builder.append('}');

        String json =  builder.toString();

        return json.replace(",}","}");
    }
}
