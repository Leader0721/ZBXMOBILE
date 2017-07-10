package com.zbxn.pub.frame.common;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import widget.swipebacklayout.SwipeBackLayout;
import widget.swipebacklayout.SwipeUtils;


/**
 * 滑动返回操作类
 *
 * @author GISirFive
 * @time 2016/8/3 18:26
 */
public class SwipeBackHelper {
    private Activity mActivity;

    private SwipeBackLayout mSwipeBackLayout;

    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout;
    }

    public SwipeBackHelper(Activity activity) {
        mActivity = activity;
    }

    @SuppressWarnings("deprecation")
    public void onActivityCreate() {
        mActivity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mActivity.getWindow().getDecorView().setBackgroundDrawable(null);
        mSwipeBackLayout = new SwipeBackLayout(mActivity);
        mSwipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListener() {
            @Override
            public void onScrollStateChange(int state, float scrollPercent) {
            }

            @Override
            public void onEdgeTouch(int edgeFlag) {
                SwipeUtils.convertActivityToTranslucent(mActivity);
            }

            @Override
            public void onScrollOverThreshold() {

            }
        });
    }

    public void onPostCreate() {
        mSwipeBackLayout.attachToActivity(mActivity);
    }

    public View findViewById(int id) {
        if (mSwipeBackLayout != null) {
            return mSwipeBackLayout.findViewById(id);
        }
        return null;
    }


    /**
     * 滑动关闭页面，并结束Activity
     */
    public void scrollToFinish() {
        SwipeUtils.convertActivityToTranslucent(mActivity);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    public interface ISwipeBack {

        /**
         * 是否启用滑动关闭Activity的功能
         */
        boolean getSwipeBackEnable();

        /**
         * 滑动关闭页面，并结束Activity
         */
        void scrollToFinish();
    }

}
