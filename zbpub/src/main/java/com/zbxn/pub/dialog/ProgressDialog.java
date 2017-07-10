package com.zbxn.pub.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.zbxn.pub.R;

public class ProgressDialog extends Dialog {

    public ProgressDialog(Context context) {
        super(context, R.style.progressDialog);
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_progressdialog);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        setCancelable(true);
    }

    public void setMessage(String message) {
        TextView tvContent = (TextView) findViewById(R.id.textview_content);
        if (tvContent == null)
            return;
        if (TextUtils.isEmpty(message))
            tvContent.setVisibility(View.GONE);
        else
            tvContent.setVisibility(View.VISIBLE);
        tvContent.setText(message + "");
    }

    @Override
    public void show() {
        TextView tvContent = (TextView) findViewById(R.id.textview_content);
        if (tvContent == null)
            return;
        if (TextUtils.isEmpty(tvContent.getText()))
            tvContent.setVisibility(View.GONE);
        else
            tvContent.setVisibility(View.VISIBLE);
        super.show();
    }

    public void show(String message) {
        setMessage(message);
        show();
    }


}
