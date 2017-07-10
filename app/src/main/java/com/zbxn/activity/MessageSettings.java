package com.zbxn.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zbxn.R;
import com.zbxn.bean.MessageUpdateEntity;
import com.zbxn.http.BaseAsyncTask;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.data.ResultData;
import com.zbxn.pub.data.base.BaseAsyncTaskInterface;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.MyToast;
import com.zcw.togglebutton.ToggleButton;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import utils.PreferencesUtils;
import utils.StringUtils;

/**
 * Created by Administrator on 2016/12/6.
 */
public class MessageSettings extends AbsToolbarActivity {
    @BindView(R.id.mToggleButtonWorkBlog)
    ToggleButton mToggleButtonWorkBlog;
    @BindView(R.id.mToggleButtonBulletIn)
    ToggleButton mToggleButtonBulletIn;
    @BindView(R.id.mToggleButtonSchedule)
    ToggleButton mToggleButtonSchedule;
    @BindView(R.id.mToggleButtonApply)
    ToggleButton mToggleButtonApply;
    @BindView(R.id.mToggleButtonMission)
    ToggleButton mToggleButtonMission;
    //设置提醒与否
    private int isRemindWorkBlog = 0;//1：开启 0：关闭   默认全部开启
    private int isRemindBulletIn = 0;
    private int isRemindSchedule = 0;
    private int isRemindApply = 0;
    private int isRemindMission = 0;
    //接口参数设置
    private String pushState;
    private String isPush = 0 + "";
    private String[] changeList;
    private int ischangeSucWorkBlog = 1;//当对消息进行设置的时候，根据网络返回的数据的情况判定是否成功，然后进行相对应的修改
    private int ischangeSucBulletin = 1;//1 是成功，0是失败
    private int ischangeSucSchedule = 1;
    private int ischangeSucApply = 1;
    private int ischangeSucMission = 1;

    @Override
    public int getMainLayout() {
        return R.layout.activity_messagesetting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allTrue();

        changeUrl();

        updateSuccessView();
    }

    @Override
    public void loadData() {

    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("消息设置");
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    //设置所有的toggleButton为on
    public void allTrue() {
        mToggleButtonBulletIn.setToggleOn(true);
        mToggleButtonWorkBlog.setToggleOn(true);
        mToggleButtonApply.setToggleOn(true);
        mToggleButtonMission.setToggleOn(true);
        mToggleButtonSchedule.setToggleOn(true);
        isRemindMission = 1;
        isRemindApply = 1;
        isRemindSchedule = 1;
        isRemindBulletIn = 1;
        isRemindWorkBlog = 1;
    }

    //参数设置的方法
    public void changeUrl() {
        Map<String, String> map = new HashMap<>();
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTask(this, false, 0, server + "mobilePush/dataList.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultData<MessageUpdateEntity>().parse(json, MessageUpdateEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultData mResult = (ResultData) result;
                if (mResult.getSuccess().equals("0")) {
                    MessageUpdateEntity entity = (MessageUpdateEntity) mResult.getData();
                    String pushUpdate = entity.getPushstate();

                    if (StringUtils.isEmpty(pushUpdate)) {
                        allTrue();
                    } else {
                        changeList = pushUpdate.split(",");

                        for (int i = 0; i < changeList.length; i++) {
                            switch (changeList[i]) {
                                case 23 + "":
                                    if (isRemindWorkBlog == 1) {
                                        mToggleButtonWorkBlog.setToggleOff(true);
                                        isRemindWorkBlog = 0;
                                    } else {
                                        mToggleButtonWorkBlog.setToggleOn(true);
                                        isRemindWorkBlog = 1;
                                    }
                                    break;
                                case 11 + "":
                                    if (isRemindBulletIn == 1) {
                                        mToggleButtonBulletIn.setToggleOff(true);
                                        isRemindBulletIn = 0;
                                    } else {
                                        mToggleButtonBulletIn.setToggleOn(true);
                                        isRemindBulletIn = 1;
                                    }
                                    break;
                                case 25 + "":
                                    if (isRemindSchedule == 1) {
                                        mToggleButtonSchedule.setToggleOff(true);
                                        isRemindSchedule = 0;
                                    } else {
                                        mToggleButtonSchedule.setToggleOn(true);
                                        isRemindSchedule = 1;
                                    }
                                    break;
                                case 31 + "":
                                    if (isRemindApply == 1) {
                                        mToggleButtonApply.setToggleOff(true);
                                        isRemindApply = 0;
                                    } else {
                                        mToggleButtonApply.setToggleOn(true);
                                        isRemindApply = 1;
                                    }
                                    break;
                                case 32 + "":
                                    if (isRemindMission == 1) {
                                        mToggleButtonMission.setToggleOff(true);
                                        isRemindMission = 0;
                                    } else {
                                        mToggleButtonMission.setToggleOn(true);
                                        isRemindMission = 1;
                                    }
                                    break;
                            }
                        }
                    }
                    // changeList = pushUpdate.split(",");
                } else {
                    MyToast.showToast("请求失败");
                    finish();
                }


            }

            @Override
            public void dataError(int funId) {
                MyToast.showToast("联网失败，请确定网络是否连接");
                finish();
            }
        }).execute(map);
    }


    //参数变化的方法
    public void submitUrl() {
        Map<String, String> map = new HashMap<>();
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);
        map.put("pushState", pushState);
        map.put("isPush", isPush);

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);
        new BaseAsyncTask(this, false, 0, server + "mobilePush/updatePushState.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultData<MessageUpdateEntity>().parse(json, MessageUpdateEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultData mResult = (ResultData) result;
                if ("0".equals(mResult.getSuccess())) {//成功
                    switch (pushState) {
                        case 23 + "":
                            ischangeSucWorkBlog = 1;
                            break;
                        case 11 + "":
                            ischangeSucBulletin = 1;
                            break;
                        case 25 + "":
                            ischangeSucSchedule = 1;
                            break;
                        case 31 + "":
                            ischangeSucApply = 1;
                            break;
                        case 32 + "":
                            ischangeSucMission = 1;
                            break;
                    }
                } else {//失败
                    MyToast.showToast("修改失败");
                    switch (pushState) {
                        case 23 + "":
                            ischangeSucWorkBlog = 0;
                            if (isRemindWorkBlog == 1) {
                                mToggleButtonWorkBlog.setToggleOff(true);
                                isRemindWorkBlog = 0;
                            } else {
                                mToggleButtonWorkBlog.setToggleOn(true);
                                isRemindWorkBlog = 1;
                            }
                            break;
                        case 11 + "":
                            ischangeSucBulletin = 0;
                            if (isRemindBulletIn == 1) {
                                mToggleButtonBulletIn.setToggleOff(true);
                                isRemindBulletIn = 0;
                            } else {
                                mToggleButtonBulletIn.setToggleOn(true);
                                isRemindBulletIn = 1;
                            }
                            break;
                        case 25 + "":
                            ischangeSucSchedule = 0;
                            if (isRemindSchedule == 1) {
                                mToggleButtonSchedule.setToggleOff(true);
                                isRemindSchedule = 0;
                            } else {
                                mToggleButtonSchedule.setToggleOn(true);
                                isRemindSchedule = 1;
                            }
                            break;
                        case 31 + "":
                            ischangeSucApply = 0;
                            if (isRemindApply == 1) {
                                mToggleButtonApply.setToggleOff(true);
                                isRemindApply = 0;
                            } else {
                                mToggleButtonApply.setToggleOn(true);
                                isRemindApply = 1;
                            }
                            break;
                        case 32 + "":
                            ischangeSucMission = 0;
                            if (isRemindMission == 1) {
                                mToggleButtonMission.setToggleOff(true);
                                isRemindMission = 0;
                            } else {
                                mToggleButtonMission.setToggleOn(true);
                                isRemindMission = 1;
                            }
                            break;
                    }
                }
            }

            @Override
            public void dataError(int funId) {
                switch (pushState) {
                    case 23 + "":
                        ischangeSucWorkBlog = 0;
                        break;
                    case 11 + "":
                        ischangeSucBulletin = 0;
                        break;
                    case 25 + "":
                        ischangeSucSchedule = 0;
                        break;
                    case 31 + "":
                        ischangeSucApply = 0;
                        break;
                    case 32 + "":
                        ischangeSucMission = 0;
                        break;
                }
                MyToast.showToast("获取网络数据失败");
            }
        }).execute(map);
    }


    /**
     * 检查当前网络是否可用
     *
     * @param activity
     * @return
     */
    public boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    @OnClick({R.id.mMessageNone, R.id.mToggleButtonWorkBlog, R.id.mToggleButtonBulletIn, R.id.mToggleButtonSchedule, R.id.mToggleButtonApply, R.id.mToggleButtonMission})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mMessageNone:
                startActivity(new Intent(MessageSettings.this, MessageNone.class));
                break;
            case R.id.mToggleButtonWorkBlog://日志
                if (isRemindWorkBlog == 1) {//0关闭， 1开启
                    pushState = "23";
                    isPush = "0";
                    submitUrl();
                    if (ischangeSucWorkBlog == 1 && isNetworkAvailable(this)) {
                        mToggleButtonWorkBlog.setToggleOff(true);
                        isRemindWorkBlog = 0;
                    }
                } else {
                    pushState = "23";
                    isPush = "1";
                    submitUrl();
                    if (ischangeSucWorkBlog == 1 && isNetworkAvailable(this)) {
                        mToggleButtonWorkBlog.setToggleOn(true);
                        isRemindWorkBlog = 1;
                    }
                }
                break;
            case R.id.mToggleButtonBulletIn://公告
                if (isRemindBulletIn == 1) {
                    pushState = "11";
                    isPush = "0";
                    submitUrl();
                    if (ischangeSucBulletin == 1 && isNetworkAvailable(this)) {
                        mToggleButtonBulletIn.setToggleOff(true);
                        isRemindBulletIn = 0;
                    }
                } else {
                    pushState = "11";
                    isPush = "1";
                    submitUrl();
                    if (ischangeSucBulletin == 1 && isNetworkAvailable(this)) {
                        mToggleButtonBulletIn.setToggleOn(true);
                        isRemindBulletIn = 1;
                    }
                }
                break;
            case R.id.mToggleButtonSchedule://日程
                if (isRemindSchedule == 1) {
                    pushState = "25";
                    isPush = "0";
                    submitUrl();
                    if (ischangeSucSchedule == 1 && isNetworkAvailable(this)) {
                        mToggleButtonSchedule.setToggleOff(true);
                        isRemindSchedule = 0;
                    }
                } else {
                    pushState = "25";
                    isPush = "1";
                    submitUrl();
                    if (ischangeSucSchedule == 1 && isNetworkAvailable(this)) {
                        mToggleButtonSchedule.setToggleOn(true);
                        isRemindSchedule = 1;
                    }
                }
                break;
            case R.id.mToggleButtonApply://审批
                if (isRemindApply == 1) {
                    pushState = "31";
                    isPush = "0";
                    submitUrl();
                    if (ischangeSucApply == 1 && isNetworkAvailable(this)) {
                        mToggleButtonApply.setToggleOff(true);
                        isRemindApply = 0;
                    }
                } else {
                    pushState = "31";
                    isPush = "1";
                    submitUrl();
                    if (ischangeSucApply == 1 && isNetworkAvailable(this)) {
                        mToggleButtonApply.setToggleOn(true);
                        isRemindApply = 1;
                    }
                }
                break;
            case R.id.mToggleButtonMission://任务
                if (isRemindMission == 1) {
                    pushState = "32";
                    isPush = "0";
                    submitUrl();
                    if (ischangeSucMission == 1 && isNetworkAvailable(this)) {
                        mToggleButtonMission.setToggleOff(true);
                        isRemindMission = 0;
                    }
                } else {
                    pushState = "32";
                    isPush = "1";
                    submitUrl();
                    if (ischangeSucMission == 1 && isNetworkAvailable(this)) {
                        mToggleButtonMission.setToggleOn(true);
                        isRemindMission = 1;
                    }
                }
                break;
        }
    }
}
