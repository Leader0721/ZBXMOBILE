package com.zbxn.pub.frame.common;

import android.os.Handler;
import android.os.Message;

public class HandleMessage implements IHandleMessage {

    private Handler mHandler;

    public HandleMessage(Handler.Callback callback) {
        mHandler = new Handler(callback);
    }

    public Handler getHandler() {
        return mHandler;
    }

    @Override
    public void sendMessage(int what) {
        mHandler.sendEmptyMessage(what);
    }

    @Override
    public void sendMessage(Message message) {
        mHandler.sendMessage(message);
    }

    @Override
    public void sendMessageDelayed(int what, long delayMillis) {
        mHandler.sendEmptyMessageDelayed(what, delayMillis);
    }

    @Override
    public void sendMessageDelayed(Message message, long delayMillis) {
        mHandler.sendMessageDelayed(message, delayMillis);
    }
}
