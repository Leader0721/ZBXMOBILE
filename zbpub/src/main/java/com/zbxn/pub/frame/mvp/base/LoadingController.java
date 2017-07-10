package com.zbxn.pub.frame.mvp.base;

import com.zbxn.pub.dialog.ProgressDialog;

/**
 * 过场视图控制
 * Created by GISirFive on 2016-3-21.
 */
public interface LoadingController {

    /**
     * 进度框是否正在显示
     * @return
     */
    boolean isShowing();

    /** 显示进度框 */
    void show();

    /**
     * 显示进度框或正在加载提示
     * @param msg 显示在屏幕上的提示
     */
    void show(String msg);

    /**
     * 延迟显示进度框或正在加载提示
     * @param msg
     * @param delay 延迟多少毫秒显示
     */
    void showDelay(String msg, long delay);

    /** 隐藏进度框 */
    void hide();

    /**
     * 延迟多少毫秒后隐藏进度框
     * @param delay 延迟毫秒
     */
    void hideDelay(long delay);

    /**
     * 获取自定义对话框
     * @return
     */
    ProgressDialog getDialog();

}
