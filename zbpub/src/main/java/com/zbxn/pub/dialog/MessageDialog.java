package com.zbxn.pub.dialog;

import com.zbxn.pub.R;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * 信息提示对话框
 *
 * @author GISirFive
 * @since 2016-1-11 下午4:35:36
 */
public class MessageDialog {

    private Builder mBuilder = null;
    private AlertDialog mDialog = null;

    public MessageDialog(Context context) {
        mBuilder = new Builder(context, R.style.Theme_AppCompat_Light_Dialog);
        init(context);
    }

    private void init(Context context) {
        setPositiveButton("确定");
    }

    /**
     * 显示对话框
     *
     * @author GISirFive
     */
    public void show() {
        if (mDialog == null) {
            mDialog = mBuilder.show();
            resizeWindow(mDialog);
        }
        if (mDialog.isShowing())
            return;
        mDialog.show();
    }

    public void dismiss() {
        if (mDialog != null)
            mDialog.dismiss();
    }

    /**
     * 显示对话框
     *
     * @param content 输入框默认显示的内容
     * @author GISirFive
     */
    public void show(String message) {
        if (mDialog == null) {
            mBuilder.setMessage(message);
            mDialog = mBuilder.show();
            resizeWindow(mDialog);
        }
        if (mDialog.isShowing())
            return;

        if (mDialog != null) {
            mDialog.setMessage(message);
        }

        mDialog.show();
    }

    /**
     * 对话框是否正在展示
     *
     * @return
     * @author GISirFive
     */
    public boolean isShowing() {
        if (mDialog == null)
            return false;
        return mDialog.isShowing();
    }

    /**
     * 设置标题
     *
     * @param title
     * @author GISirFive
     */
    public void setTitle(String title) {
        mBuilder.setTitle(title);
    }

    /**
     * 设置提示内容
     *
     * @return
     */
    public void setMessage(int messageId) {
        mBuilder.setMessage(messageId);
    }

    /**
     * 设置提示内容
     *
     * @return
     */
    public void setMessage(String message) {
        mBuilder.setMessage(message);
    }

    public void setPositiveButton(String content) {
        mBuilder.setPositiveButton(content, null);
    }

    public void setPositiveButton(String content, OnClickListener listener) {
        mBuilder.setPositiveButton(content, listener);
    }

    public void setNegativeButton(String content) {
        mBuilder.setNegativeButton(content, null);
    }

    public void setNegativeButton(String content, OnClickListener listener) {
        mBuilder.setNegativeButton(content, listener);
    }


    /**
     * 调整窗体大小
     *
     * @author GISirFive
     */
    private void resizeWindow(AlertDialog dialog) {
        WindowManager wm = (WindowManager) dialog.getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenWidth = localDisplayMetrics.widthPixels;
        dialog.getWindow().setLayout((int) (mScreenWidth * 0.8),
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
