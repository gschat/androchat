package com.gschat.core;

import org.slf4j.Logger;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.SystemClock;

import com.gschat.sdk.GSConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * android timer facade
 */
public class AndroidTimer {

    private final static Logger logger = LoggerFactory.getLogger("AndroidTimer");

    static PendingIntent startKeeper(Context context) throws Exception {

        Bundle bundle = getMetaData(context);

        int timeout = 60000;

        if (bundle != null) {
            if (bundle.containsKey(GSConfig.KeepAliveAction)) {
                timeout = bundle.getInt(GSConfig.KeepAliveAction);
            }
        }


        logger.debug("create chat room service keeper heartbeat");

        Intent intent = new Intent(context, CoreServiceReceiver.class);

        intent.setAction(GSConfig.KeepAliveAction);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        int triggerAtTime = (int) (SystemClock.elapsedRealtime() + timeout);

        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, timeout, pendingIntent);

        logger.debug("create chat room service keeper heartbeat -- success");

        return pendingIntent;
    }

    static void stopKeeper(Context context, PendingIntent pendingIntent) {

        logger.debug("stop chat room service keeper heartbeat");

        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarm.cancel(pendingIntent);

        logger.debug("stop chat room service keeper heartbeat -- success");
    }

    static void startReconnect(Context context, String uri) {

        Bundle bundle = getMetaData(context);

        int duration = 60000;

        if (bundle != null) {
            if (bundle.containsKey(GSConfig.ReconnectAction)) {
                duration = bundle.getInt(GSConfig.ReconnectAction);
            }
        }

//        logger.debug("create chat room channel {} reconnect heartbeat", uri);

        Intent intent = new Intent(context, CoreServiceReceiver.class);

        intent.setAction(GSConfig.ReconnectAction);

        intent.putExtra("uri", uri);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        int triggerAtTime = (int) (SystemClock.elapsedRealtime() + duration);

        alarm.set(AlarmManager.ELAPSED_REALTIME, triggerAtTime, pendingIntent);

//        logger.debug("create chat room channel {} reconnect heartbeat -- success", uri);
    }

    static Bundle getMetaData(Context context) {

        ComponentName cn = new ComponentName(context, CoreService.class);

        try {
            return context.getPackageManager().getServiceInfo(cn, PackageManager.GET_META_DATA).metaData;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }


    static PendingIntent startRPCTimer(Context context) throws Exception {

        Bundle bundle = getMetaData(context);

        int timeout = 10000;

        if (bundle != null) {
            if (bundle.containsKey(GSConfig.RPCTimeoutAction)) {
                timeout = bundle.getInt(GSConfig.RPCTimeoutAction);
            }
        }

//        logger.debug("create rpc timer({})", timeout);

        Intent intent = new Intent(context, CoreServiceReceiver.class);

        intent.setAction(GSConfig.RPCTimeoutAction);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        int triggerAtTime = (int) (SystemClock.elapsedRealtime() + timeout);

        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime, timeout, pendingIntent);

//        logger.debug("create rpc timer({}) -- success", timeout);

        return pendingIntent;
    }

    static void stopRPCTimer(Context context, PendingIntent pendingIntent) {

//        logger.debug("stop rpc timer");

        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarm.cancel(pendingIntent);

//        logger.debug("stop rpc timer -- success");
    }
}
