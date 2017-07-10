package com.github.yoojia.anyversion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Yoojia.Chen
 * yoojia.chen@gmail.com
 * 2015-01-07
 * 接收处理新版本的 Receiver
 */
public abstract class VersionReceiver extends BroadcastReceiver{
    
    @Override
    public final void onReceive(Context context, Intent intent) {
        final Version newVersion = intent.getParcelableExtra(Broadcasts.BROADCAST_DATA);
        onVersion(newVersion);
    }

    protected abstract void onVersion(Version newVersion);
}
