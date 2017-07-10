package com.zbxn.pub.frame.mvp.base;

import android.content.DialogInterface;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * 消息提示
 * Created by GISirFive on 2016-3-22.
 */
public interface MessageController {

    /**
     * 显示一条消息
     * @param content
     */
    void show(String content);

    /**
     * 显示一条消息
     * @param content
     * @param duration
     */
    void show(String content, int duration);

    /**
     * 显示消息提示对话框
     * @param content
     */
    void showDialog(String content);

    /**
     * 显示消息提示对话框
     * @param title
     * @param content
     */
    void showDialog(String title, String content);

    /**
     * 显示消息提示对话框
     * @param title
     * @param content
     * @param listener
     */
    void showDialog(String title, String content, DialogInterface.OnClickListener listener);

    /**
     * 构建对话框
     * @return
     */
    MaterialDialog.Builder getDialogBuilder();

}
