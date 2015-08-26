package com.gschat.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoreServiceReceiver extends BroadcastReceiver {

    private final static Logger logger = LoggerFactory.getLogger("CoreServiceReceiver");

    public CoreServiceReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        logger.info("recv broadcast {}", intent.getAction());

        intent.setClass(context,CoreService.class);

        context.startService(intent);
    }
}