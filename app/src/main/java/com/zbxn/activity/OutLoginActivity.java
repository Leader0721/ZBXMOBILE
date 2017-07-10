package com.zbxn.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.pub.application.BaseApp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class OutLoginActivity extends Activity {

    @BindView(R.id.text_tip)
    TextView textTip;
    @BindView(R.id.text_content)
    TextView textContent;
    @BindView(R.id.dialog_cancel)
    TextView dialogCancel;
    @BindView(R.id.dialog_ok)
    TextView dialogOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_login);
        ButterKnife.bind(this);
        String description = getIntent().getStringExtra("description");

        textContent.setText(description);
    }

    @OnClick({R.id.dialog_cancel, R.id.dialog_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_cancel://在onDestroy中处理
                finish();
                break;
            case R.id.dialog_ok://在onDestroy中处理
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        JPushInterface.clearNotificationById(this, 11111111);
        Intent intent = new Intent(BaseApp.getContext(), Main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("isExit", true);
        BaseApp.getContext().startActivity(intent);
        super.onDestroy();
    }
}
