package com.zbxn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.zbxn.R;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvc.AbsToolbarActivity;
import com.zbxn.pub.http.RequestUtils.Code;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class SelectPortrait extends AbsToolbarActivity {

    /**
     * 输出_选择头像_男=true，女=false
     */
    public static final String Flag_Output_Portrait = "portrait";
    @BindView(R.id.mForBoy)
    LinearLayout mForBoy;
    @BindView(R.id.mForGirl)
    LinearLayout mForGirl;


    @Override
    public int getMainLayout() {
        return R.layout.activity_selectportrait;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateSuccessView();
    }

    @Override
    public void loadData() {

    }

    /**
     * 滑动关闭当前Activitiv
     */
    @Override
    public boolean getSwipeBackEnable() {
//        return super.getSwipeBackEnable();
        return true;
    }


    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("选择头像");
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    public boolean handleMessage(Message msg) {
        finish();
        return false;
    }

    @Override
    public void onSuccess(Code code, JSONObject response) {

    }

    @Override
    public void onFailure(Code code, JSONObject error) {

    }

    @OnClick({R.id.mForBoy, R.id.mForGirl})
    public void onClick(View v) {
        boolean select = true;
        switch (v.getId()) {
            case R.id.mForBoy:
                select = true;
                break;
            case R.id.mForGirl:
                select = false;
                break;
            default:
                break;
        }
        Intent intent = new Intent();
        intent.putExtra(Flag_Output_Portrait, select);
        setResult(RESULT_OK, intent);

        /**
         * 防止点击数次，为了看到点击效果，延时跳转300毫秒
         */
        mForBoy.setClickable(false);
        mForGirl.setClickable(false);
        sendMessageDelayed(100, 300);
    }


}
