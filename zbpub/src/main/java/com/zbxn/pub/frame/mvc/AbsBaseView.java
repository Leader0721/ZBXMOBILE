package com.zbxn.pub.frame.mvc;


import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.http.ICallback;
import com.zbxn.pub.http.IRequest;
import com.zbxn.pub.http.IRequestParams;
import com.zbxn.pub.http.RequestUtils.Code;

import com.zbxn.pub.frame.common.IHandleMessage;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.LayoutInflater;
import android.view.View;

public abstract class AbsBaseView implements IVisibleControl,
        IHandleMessage, Callback, IRequest, ICallback {

    private Handler mHandler;

    public Handler getmHandler() {
        if (mHandler == null)
            mHandler = new Handler(this);
        return mHandler;
    }

    private View mRoot;

    private Context mContext;

    /**
     * 构造函数，适用于view已经在其父布局中<br>
     *
     * @param view 该窗体布局的根节点
     */
    public AbsBaseView(Context context, View view) {
        this.mContext = context;
        this.mRoot = view;
        if (mRoot == null) {
            throw new NullPointerException("窗体布局的根节点为空！");
        }
        initialize(mRoot);
    }

    /**
     * 构造函数，适用于父布局不包含layout所在的布局，需要将layout所在的布局添加到父布局中<br>
     *
     * @param layout 窗体的布局文件
     */
    public AbsBaseView(Context context, int layout) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(layout, null);
        if (view == null) {
            throw new NullPointerException("窗体布局的根节点为空！");
        }
        this.mRoot = view;
        initialize(mRoot);
    }

    @Override
    public void setVisibility(boolean b) {
        if (b)
            show();
        else
            hide();
    }

    @Override
    public boolean isShowing() {
        return this.mRoot.getVisibility() == View.VISIBLE ? true : false;
    }

    @Override
    public void show() {
        this.mRoot.setVisibility(View.VISIBLE);
    }

    @Override
    public void hide() {
        this.mRoot.setVisibility(View.GONE);
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

    @Override
    public void onStart(Code code) {
    }

    @Override
    public void post(Code code, IRequestParams params) {
        BaseApp.getHttpClient().post(code, params, this);
    }

    @Override
    public void get(Code code, IRequestParams params) {
        post(code, params);
    }


    /**
     * 获取该窗体布局的根节点
     *
     * @return
     */
    public View getRootView() {
        return mRoot;
    }

    /**
     * 设置该窗体布局的根节点
     *
     * @param rootView
     */
    public final void setRootView(View rootView) {
        this.mRoot = rootView;
    }

    public Context getContext() {
        return this.mContext;
    }

    /**
     * 初始化该窗体相关参数<br>
     * 该方法在该类的子类调用构造函数{@link #AbsBaseView(View)}时执行，
     * 即该方法在初次定义根节点后立即执行<br>
     * <b>注意：该方法不应该被直接调用</b>
     *
     * @param root 根节点
     */
    public abstract void initialize(View root);

}
