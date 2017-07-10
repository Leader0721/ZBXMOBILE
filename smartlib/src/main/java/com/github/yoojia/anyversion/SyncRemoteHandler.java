package com.github.yoojia.anyversion;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Yoojia.Chen
 * yoojia.chen@gmail.com
 * 2015-01-06
 * 可将异步操作转换为同步操作的处理类
 */
public abstract class SyncRemoteHandler extends RemoteHandler{

    @Override
    public final String request(String url) throws IOException {
        final StringBuilder buff = new StringBuilder();
        final Lock lock = new ReentrantLock();
        lock.lock();
        final CountDownLatch latch = new CountDownLatch(1);
        try {
            request(url, buff, latch);
            latch.await();
        } catch (InterruptedException e) {
            // Nothing
        } finally {
            lock.unlock();
        }
        return buff.toString();
    }

    /**
     * 执行请求，并将处理结果填充到指定对象，并需要执行指定对象的标识异步操作完成的标识方法。
     * @param url 远程服务器的 URL
     * @param resultBuff 处理结果填充到此对象
     * @param countDownIfComplete 当异步操作完成时，执行 .countDown() 方法。
     */
    public abstract void request(final String url, final StringBuilder resultBuff, final CountDownLatch countDownIfComplete);
}
