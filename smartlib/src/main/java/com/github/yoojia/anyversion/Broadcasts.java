package com.github.yoojia.anyversion;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;


/**
 * Created by Yoojia.Chen
 * yoojia.chen@gmail.com
 * 2015-01-04
 * 使用 NotifyStyle.Broadcast 新版本处理方式时，需使用此工具类来处理 Receiver 的注册和反注册操作。
 */
class Broadcasts {

    static final String BROADCAST_ACTION = AnyVersion.class.getName();
    static final String BROADCAST_DATA = "data";

    private Broadcasts() {}

    static void send(Application context, Version remoteVersion){
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
        Intent intent = new Intent(BROADCAST_ACTION);
        intent.putExtra(BROADCAST_DATA, remoteVersion);
        manager.sendBroadcast(intent);
    }

    /**
     * 注册广播接收处理类
     */
    static void register(Context context, BroadcastReceiver receiver){
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
        manager.registerReceiver(receiver, new IntentFilter(BROADCAST_ACTION));
    }

    /**
     * 反注册广播处理类
     */
    static void unregister(Context context, BroadcastReceiver receiver){
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
        manager.unregisterReceiver(receiver);
    }

}
