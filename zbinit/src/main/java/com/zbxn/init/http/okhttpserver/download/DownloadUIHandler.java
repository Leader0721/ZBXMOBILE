package com.zbxn.init.http.okhttpserver.download;

import android.os.Handler;
import android.os.Message;

import com.zbxn.init.http.okhttpserver.L;
import com.zbxn.init.http.okhttpserver.listener.DownloadListener;

/**
 * ================================================
 * 作    者：廖子尧
 * 版    本：1.0
 * 创建日期：2016/1/19
 * 描    述：用于在主线程回调下载UI
 * 修订历史：
 * ================================================
 */
public class DownloadUIHandler extends Handler {

    private DownloadListener mGlobalDownloadListener;

    @Override
    public void handleMessage(Message msg) {
        MessageBean messageBean = (MessageBean) msg.obj;
        if (messageBean != null) {
            com.zbxn.init.http.okhttpserver.download.DownloadInfo info = messageBean.downloadInfo;
            String errorMsg = messageBean.errorMsg;
            Exception e = messageBean.e;
            if (mGlobalDownloadListener != null) {
                executeListener(mGlobalDownloadListener, info, errorMsg, e);
            }
            DownloadListener listener = info.getListener();
            if (listener != null) executeListener(listener, info, errorMsg, e);
        } else {
            L.e("DownloadUIHandler DownloadInfo null");
        }
    }

    private void executeListener(DownloadListener listener, com.zbxn.init.http.okhttpserver.download.DownloadInfo info, String errorMsg, Exception e) {
        int state = info.getState();
        switch (state) {
            case com.zbxn.init.http.okhttpserver.download.DownloadManager.NONE:
            case com.zbxn.init.http.okhttpserver.download.DownloadManager.WAITING:
            case com.zbxn.init.http.okhttpserver.download.DownloadManager.DOWNLOADING:
            case com.zbxn.init.http.okhttpserver.download.DownloadManager.PAUSE:
                listener.onProgress(info);
                break;
            case com.zbxn.init.http.okhttpserver.download.DownloadManager.FINISH:
                listener.onProgress(info);   //结束前再次回调进度，避免最后一点数据没有刷新
                listener.onFinish(info);
                break;
            case com.zbxn.init.http.okhttpserver.download.DownloadManager.ERROR:
                listener.onProgress(info);   //结束前再次回调进度，避免最后一点数据没有刷新
                listener.onError(info, errorMsg, e);
                break;
        }
    }

    public void setGlobalDownloadListener(DownloadListener downloadListener) {
        this.mGlobalDownloadListener = downloadListener;
    }

    public static class MessageBean {
        public com.zbxn.init.http.okhttpserver.download.DownloadInfo downloadInfo;
        public String errorMsg;
        public Exception e;
    }
}
