package com.github.yoojia.anyversion;

import com.smartlib.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Html;

/**
 * Created by Yoojia.Chen
 * yoojia.chen@gmail.com
 * 2015-01-04
 */
class VersionDialog {

    private final AlertDialog dialog;

    public VersionDialog(final Activity context, final Version version, final Downloads downloads) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_System_Alert)
                .setTitle(version.name)
                .setMessage(Html.fromHtml(version.note))
                .setCancelable(true)
                .setNegativeButton(R.string.anyversion_later, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton(R.string.anyversion_update_now, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downloads.submit(context, version);
                        dialog.cancel();
                    }
                })
                ;
        this.dialog = builder.create();

    }

    public void show(){
//        this.dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        try{
            dialog.show();
        }catch (Exception e){
        	e.printStackTrace();
            throw new IllegalArgumentException("Required " +
                    "'<uses-permission android:name=\"android.permission.SYSTEM_ALERT_WINDOW\" />' !");
        }
    }
}
