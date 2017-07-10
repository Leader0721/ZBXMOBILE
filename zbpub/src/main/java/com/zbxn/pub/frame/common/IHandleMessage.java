package com.zbxn.pub.frame.common;

import android.os.Message;

public interface IHandleMessage {
    /**
     * 发送消息
     *
     * @param message 消息
     */
    void sendMessage(Message message);

    /**
     * 发送空消息
     *
     * @param what 消息标识
     */
    void sendMessage(int what);

    /**
     * 延迟发送消息
     *
     * @param message     消息
     * @param delayMillis 延迟时间
     */
    void sendMessageDelayed(Message message, long delayMillis);

    /**
     * 延迟发送消息
     *
     * @param what        消息标识
     * @param delayMillis 延迟时间
     */
    void sendMessageDelayed(int what, long delayMillis);
}