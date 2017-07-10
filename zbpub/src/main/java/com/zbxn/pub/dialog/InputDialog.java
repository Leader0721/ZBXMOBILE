package com.zbxn.pub.dialog;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.zbxn.pub.R;

/**
 * 输入对话框
 *
 * @author GISirFive
 * @since 2016-1-11 下午4:35:23
 */
public class InputDialog {

    private EditText mContent;

    private Builder mBuilder = null;
    private AlertDialog mDialog = null;

    public InputDialog(Context context) {
        mBuilder = new Builder(context, R.style.Theme_AppCompat_Light_Dialog);
        init(context);
    }

    private void init(Context context) {
        mBuilder.setTitle("提示");
        View root = LayoutInflater.from(context).inflate(
                R.layout.dialog_inpputdialog, null);
        mBuilder.setView(root);
        mContent = (EditText) root.findViewById(R.id.edittext_content);
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
            showKeyboard();
        }
        if (mDialog.isShowing())
            return;
        mDialog.show();
        showKeyboard();
    }

    /**
     * 显示对话框
     *
     * @param content 输入框默认显示的内容
     * @author GISirFive
     */
    public void show(String content) {
        mContent.setText(content + "");
        show();
    }

    // public void dismiss(){
    // mDialog.dismiss();
    // }

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
     * 获取输入框控件
     */
    public EditText getEditText() {
        return mContent;
    }

    /**
     * 设置输入框显示的提示
     *
     * @param hint
     * @author GISirFive
     */
    public void setContentHint(String hint) {
        mContent.setHint(hint);
    }

    /**
     * 获取用户输入的内容
     *
     * @return
     */
    public String getContent() {
        return mContent.getText().toString();
    }

    public void setPositiveButton(String content, OnClickListener listener) {
        mBuilder.setPositiveButton(content, listener);
    }

    public void setNegativeButton(String content, OnClickListener listener) {
        mBuilder.setNegativeButton(content, listener);
    }

    public void showKeyboard() {
        if (mContent != null) {
            mContent.postDelayed(new Runnable() {

                @Override
                public void run() {
                    // 设置可获得焦点
                    mContent.setFocusable(true);
                    mContent.setFocusableInTouchMode(true);
                    // 请求获得焦点
                    mContent.requestFocus();
                    // 调用系统输入法
                    InputMethodManager inputManager = (InputMethodManager) mContent
                            .getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(mContent, 0);
                }
            }, 300);
        }
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
