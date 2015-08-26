package com.gschat.database;

import io.realm.annotations.RealmModule;

@RealmModule(classes = {DBMessage.class,DBUserInfo.class,DBChatRoom.class})
public class UserModule {
}
