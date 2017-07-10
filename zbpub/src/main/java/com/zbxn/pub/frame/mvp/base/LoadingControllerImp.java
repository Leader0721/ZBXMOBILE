package com.zbxn.pub.frame.mvp.base;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.zbxn.pub.dialog.ProgressDialog;

/**
 * Created by GISirFive on 2016-3-21.
 */
public class LoadingControllerImp implements LoadingController, Handler.Callback {

    /**
     * 消息_显示对话框
     */
    private static final int Flag_Message_Show = 2001;
    /**
     * 消息_隐藏对话框
     */
    private static final int Flag_Message_Hide = 2002;

    private Activity mActivity;

    private ProgressDialog mProgressDialog;

    private Handler handler = new Handler(this);

    public LoadingControllerImp(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public boolean isShowing() {
        return getDialog().isShowing();
    }

    @Override
    public void show() {
        ProgressDialog dialog = getDialog();
        if (!dialog.isShowing())
            dialog.show();
    }

    /**
     * 显示进度框或正在加载提示
     *
     * @param msg 显示在屏幕上的提示
     */
    @Override
    public void show(String msg) {
        ProgressDialog dialog = getDialog();
        if (dialog.isShowing())
            dialog.dismiss();
        if (!TextUtils.isEmpty(msg))
            dialog.setMessage(msg);
        dialog.show();
    }

    @Override
    public void showDelay(String msg, long delay) {
        ProgressDialog dialog = getDialog();
        if (dialog.isShowing())
            dialog.dismiss();
        if (!TextUtils.isEmpty(msg))
            dialog.setMessage(msg);
        handler.sendEmptyMessageDelayed(Flag_Message_Show, delay);
    }

    /**
     * 隐藏进度框或正在加载提示
     */
    @Override
    public void hide() {
        ProgressDialog dialog = getDialog();
        if (dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    public void hideDelay(long delay) {
        ProgressDialog dialog = getDialog();
        if (dialog.isShowing())
            handler.sendEmptyMessageDelayed(Flag_Message_Hide, delay);
    }

    @Override
    public ProgressDialog getDialog() {
        if (mProgressDialog == null)
            mProgressDialog = new ProgressDialog(mActivity);
        return mProgressDialog;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case Flag_Message_Show:
                show();
                break;
            case Flag_Message_Hide:
                hide();
                break;
        }
        return false;
    }
}
