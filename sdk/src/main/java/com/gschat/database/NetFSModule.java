package com.gschat.database;

import io.realm.annotations.RealmModule;

@RealmModule(classes = {DBDownloadSyncFile.class,DBUploadSyncFile.class })
public class NetFSModule {
}
