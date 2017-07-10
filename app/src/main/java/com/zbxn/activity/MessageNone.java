package com.zbxn.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.igexin.sdk.PushManager;
import com.zbxn.R;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.MyToast;

import butterknife.BindView;
import butterknife.OnClick;
import utils.PreferencesUtils;

/**
 * Created by Administrator on 2016/12/6.
 */
public class MessageNone extends AbsToolbarActivity {
    @BindView(R.id.imageView_Open)
    ImageView imageViewOpen;
    @BindView(R.id.imageView_OpenNight)
    ImageView imageViewOpenNight;
    @BindView(R.id.imageView_Close)
    ImageView imageViewClose;
    private  int MESSAGENONE;
    private PushManager pushManager;

    @Override
    public int getMainLayout() {
        return R.layout.activity_messagenone;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MESSAGENONE = PreferencesUtils.getInt(this,"MESSAGENONE",1);
        pushManager = PushManager.getInstance();
        setView();
        updateSuccessView();
    }

    //根据SharedPreference 中的数据进行相应的视图设置
    private void setView(){
        switch (MESSAGENONE){
            case 1:
                imageViewOpen.setVisibility(View.VISIBLE);
                imageViewOpenNight.setVisibility(View.INVISIBLE);
                imageViewClose.setVisibility(View.INVISIBLE);
                break;
            case 2:
                imageViewOpen.setVisibility(View.INVISIBLE);
                imageViewOpenNight.setVisibility(View.VISIBLE);
                imageViewClose.setVisibility(View.INVISIBLE);
                break;
            case 3:
                imageViewOpen.setVisibility(View.INVISIBLE);
                imageViewOpenNight.setVisibility(View.INVISIBLE);
                imageViewClose.setVisibility(View.VISIBLE);
                break;
            default:
                pushManager.turnOnPush(this);
                imageViewOpen.setVisibility(View.VISIBLE);
                imageViewOpenNight.setVisibility(View.INVISIBLE);
                imageViewClose.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("消息免打扰");
        return super.onToolbarConfiguration(toolbar, params);
    }
    @Override
    public void loadData() {

    }
    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
    @OnClick({R.id.mMessageNoneOpen, R.id.mMessageNoneOpenNight, R.id.mMessageNoneClose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mMessageNoneOpen:
                pushManager.turnOnPush(this);
                imageViewOpen.setVisibility(View.VISIBLE);
                imageViewOpenNight.setVisibility(View.INVISIBLE);
                imageViewClose.setVisibility(View.INVISIBLE);
                PreferencesUtils.putInt(this,"MESSAGENONE",1);
                break;
            case R.id.mMessageNoneOpenNight:
                boolean setSilentTime = pushManager.setSilentTime(this, 22, 10);
                if (!setSilentTime){
                    MyToast.showToast("设置失败，请重新设置");
                }
                imageViewOpen.setVisibility(View.INVISIBLE);
                imageViewOpenNight.setVisibility(View.VISIBLE);
                imageViewClose.setVisibility(View.INVISIBLE);
                PreferencesUtils.putInt(this,"MESSAGENONE",2);
                break;
            case R.id.mMessageNoneClose:
                pushManager.turnOffPush(this);
                imageViewOpen.setVisibility(View.INVISIBLE);
                imageViewOpenNight.setVisibility(View.INVISIBLE);
                imageViewClose.setVisibility(View.VISIBLE);
                PreferencesUtils.putInt(this,"MESSAGENONE",3);
                break;
        }
    }
}
