package com.zbxn.pub.frame.mvp;

import android.os.Message;

import com.orhanobut.logger.Logger;
import com.zbxn.pub.frame.common.IHandleMessage;
import com.zbxn.pub.frame.mvp.base.ControllerCenter;
import com.zbxn.pub.frame.mvp.base.ModelCallback;
import com.zbxn.pub.http.RequestUtils;

/**
 * 业务逻辑层
 * Created by GISirFive on 2016/7/27.
 */
public abstract class AbsBasePresenter<T extends ControllerCenter> implements ModelCallback, IHandleMessage{

    protected AbsToolbarActivity mController;

    public AbsBasePresenter(AbsToolbarActivity controller) {
        this.mController = controller;
    }

    @Override
    public void onStart(RequestUtils.Code code) {
        Logger.d("发起请求--->" + code.toString());
    }

    @Override
    public void sendMessage(int what) {
        if(mController instanceof IHandleMessage)
            ((IHandleMessage)mController).sendMessage(what);
    }

    @Override
    public void sendMessage(Message message) {
        if(mController instanceof IHandleMessage)
            ((IHandleMessage)mController).sendMessage(message);
    }

    @Override
    public void sendMessageDelayed(int what, long delayMillis) {
        if(mController instanceof IHandleMessage)
            ((IHandleMessage)mController).sendMessageDelayed(what, delayMillis);
    }

    @Override
    public void sendMessageDelayed(Message message, long delayMillis) {
        if(mController instanceof IHandleMessage)
            ((IHandleMessage)mController).sendMessageDelayed(message, delayMillis);
    }
}
