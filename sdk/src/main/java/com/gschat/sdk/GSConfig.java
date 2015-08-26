package com.gschat.sdk;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * gschat sdk configs
 */
public final class GSConfig {
    /**
     * GSChat storage root path
     */
    public final static String StoreRootPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/gschat";

    /**
     * The SQLite  data format object
     *
     */
    public final static DateFormat SQLiteDataFormat =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);


    /**
     * keep alive action
     */
    public static final String KeepAliveAction = "com.gschat.core.KeepAlive";

    /**
     * reconnect action
     */
    public static final String ReconnectAction = "com.gschat.core.Reconnect";

    /**
     * rpc timeout action
     */
    public static final String RPCTimeoutAction = "com.gschat.core.RPCTimeout";
}
