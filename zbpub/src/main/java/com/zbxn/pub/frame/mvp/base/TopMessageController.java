package com.zbxn.pub.frame.mvp.base;

import android.app.Activity;

import widget.topsnackbar.TSnackbar;

/**
 * 显示在页面顶部的消息提示，注意根布局使用{@link android.support.design.widget.CoordinatorLayout}
 * @author GISirFive
 * @time 2016/8/5
 */
public class TopMessageController extends MessageControllerImp{

    private TSnackbar mSnackbar;

    public TopMessageController(Activity activity) {
        super(activity);
    }

    public TSnackbar getTSnackbar() {
        if (mSnackbar != null && mSnackbar.isShown())
            mSnackbar.dismiss();
        mSnackbar = TSnackbar.make(getContainerView(), "", TSnackbar.LENGTH_SHORT);
        return mSnackbar;
    }

    @Override
    public void show(String content, int duration) {
        TSnackbar snackbar = getTSnackbar();
        snackbar.setDuration(duration);
        show(content);

    }

    @Override
    public void show(String content) {
        TSnackbar snackbar = getTSnackbar();
        snackbar.setText(content);
        snackbar.show();
    }
}
