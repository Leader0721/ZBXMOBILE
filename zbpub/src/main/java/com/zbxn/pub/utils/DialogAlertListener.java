package com.zbxn.pub.utils;

import android.app.Dialog;

/**
 * Created by wuzy on 2015/8/9.
 */
public interface DialogAlertListener {
    public void onDialogCreate(Dialog dlg, Object param);

    public void onDialogOk(Dialog dlg, Object param);

    public void onDialogCancel(Dialog dlg, Object param);

    public void onDialogControl(Dialog dlg, Object param);
}
