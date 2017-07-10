package com.zbxn.activity.main.message.messagenew;


import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.zbxn.R;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;

/**
 * Created by Administrator on 2016/12/15.
 */
public class MessageCenterDetailActivity extends AbsToolbarActivity {

    @Override
    public int getMainLayout() {
        return R.layout.activity_messagecenterdetail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateSuccessView();
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle(MessageCenterNewPager.TITLE_MESSAGECENTER);
        return super.onToolbarConfiguration(toolbar, params);
    }


    @Override
    public void loadData() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
