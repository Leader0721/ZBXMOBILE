/**
 * @since 2015-12-11
 */
package com.zbxn.pub.frame.common;

import android.app.Activity;

import com.zbxn.pub.R;

import utils.SystemBarTintManager;

/**
 * 沉浸式状态栏
 *
 * @author GISirFive
 * @since 2015-12-11 下午3:55:45
 */
public class TranslucentHelper {

    private Activity mActivity;
    private ITranslucent mTranslucentControl;

    /** */
    public TranslucentHelper(ITranslucent control) {
        if (control == null)
            return;
        if (!(control instanceof Activity))
            return;

        this.mTranslucentControl = control;
        this.mActivity = (Activity) control;

        translucent();
    }

    /**
     * 设置沉浸式状态栏
     *
     * @author GISirFive
     */
    public void translucent() {
        SystemBarTintManager.setTranslucentStatus(mActivity, true);
        SystemBarTintManager tintManager = new SystemBarTintManager(mActivity);
        // 上方状态栏
        tintManager.setStatusBarTintEnabled(true);

        int colorRes = mTranslucentControl.getTranslucentColorResource();
        if (colorRes == 0)
            colorRes = R.color.app_theme;
        // 设置沉浸的颜色
        tintManager.setStatusBarTintResource(colorRes);
    }

    /**
     * 沉浸式状态栏
     *
     * @author GISirFive
     * @since 2015-12-11 下午4:35:57
     */
    public interface ITranslucent {

        /**
         * 是否使用沉浸式状态栏</br>
         *
         * @return false-不使用沉浸式状态栏
         * @author GISirFive
         */
        boolean translucent();

        /**
         * 获取/设置 沉浸式状态栏的背景色
         *
         * @return
         * @author GISirFive
         */
        int getTranslucentColorResource();
    }
}
