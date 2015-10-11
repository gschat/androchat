package com.gschat.service;

import android.content.Intent;
import android.os.IBinder;
import android.test.ServiceTestCase;

import com.gschat.sdk.IGSChatService;

import org.junit.Test;


public class GSChatServiceInstrumentationTest extends ServiceTestCase<GSChatService> {

    private IGSChatService gschat;

    public GSChatServiceInstrumentationTest() {
        super(GSChatService.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Intent startIntent  = new Intent(getContext(),GSChatService.class);

        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        IBinder binder = bindService(startIntent);

        assertNotNull(binder);

        gschat = IGSChatService.Stub.asInterface(binder);
    }

    @Test
    public void testStart() throws InterruptedException {

    }
}
