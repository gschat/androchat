package com.gschat.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * GSChat background service to support persistent connection and communication with chat server
 */
public class GSChatService : Service() {


    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent): IBinder? {
        return GSChatServiceBinder()
    }

    override fun onUnbind(intent: Intent): Boolean {
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
