package com.zbxn.activity.schedule;

import android.content.Context;

import com.zbxn.bean.Member;
import com.zbxn.bean.ScheduleShareEntity;
import com.zbxn.http.BaseAsyncTask;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.data.ResultData;
import com.zbxn.pub.data.ResultParse;
import com.zbxn.pub.data.base.BaseAsyncTaskInterface;
import com.zbxn.pub.frame.mvp.AbsBasePresenter;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.frame.mvp.base.ControllerCenter;
import com.zbxn.pub.frame.mvp.base.RequestResult;
import com.zbxn.pub.http.RequestUtils;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.MyToast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.PreferencesUtils;
import utils.StringUtils;

/**
 * Created by GISirFive on 2016/8/3.
 */
public class NewTaskPresenter extends AbsBasePresenter<ControllerCenter> {

    public NewTaskPresenter(AbsToolbarActivity activity) {
        super(activity);
    }

    @Override
    public void onSuccess(RequestUtils.Code code, JSONObject response, RequestResult result) {
        switch (code) {
        }

    }

    @Override
    public void onFailure(RequestUtils.Code code, JSONObject error, RequestResult result) {
        switch (code) {

        }
    }

    /**
     * 获取默认分享人
     *
     * @param context
     * @param listener
     */
    public void getScheduleShare(Context context, final ICustomListener listener) {
        Map<String, String> map = new HashMap<>();
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);
        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);
        new BaseAsyncTask(context, false, 0, server + "oaScheduleFavor/findOaScheduleFavorUserName.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultData<ScheduleShareEntity>().parse(json, ScheduleShareEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultData mResult = (ResultData) result;
                if (mResult.getSuccess().equals("0")) {
                    List<ScheduleShareEntity> list = mResult.getRows();
                    listener.onCustomListener(10, list, 0);
                } else {
                    MyToast.showToast(mResult.getMsg());
                }
            }

            @Override
            public void dataError(int funId) {
                MyToast.showToast("获取网络数据失败");
            }
        }).execute(map);
    }

    /**
     * 创建日程
     */
    public void save(Context context, String title, String starttime, String endtime
            , String precedeType, String allday, String isalarm
            , String isrepeat, String scheduleDetail, String location, String userId, String userId1, final ICustomListener listener) {
        mController.loading().show("正在提交...");
        //请求网络
        Map<String, String> map = new HashMap<String, String>();
        //将用户输入的账号保存起来，以便下次使用
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);
        map.put("title", title);//日程标题
        map.put("starttime", starttime);//开始时间
        map.put("endtime", endtime);//结束时间
        map.put("precedeType", precedeType);//提前提醒类型 (调用147接口传precedeType)
        map.put("allday", allday);//是否全天 0 ：false 1 ： true
        map.put("isalarm", isalarm);//是否提醒 0 false   1提醒
        map.put("isrepeat", "0");//是否重复　０:false 1:true
        map.put("scheduleDetail", scheduleDetail);//日程详情
        map.put("location", location);//日程地点
        map.put("sharerID", userId1);//分享日程人员数组
        map.put("participantIDStr", userId);//参与日程人员数组

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTask(context, false, 0, server + "/oaScheduleRule/save.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultParse<Member>().parse(json, Member.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultParse mResult = (ResultParse) result;
                String message = mResult.getMsg();
                if ("0".equals(mResult.getSuccess())) {//0成功
                    mController.loading().hide();
                    MyToast.showToast("创建成功");
                    listener.onCustomListener(1, null, 0);
                } else {
                    listener.onCustomListener(2, null, 0);
                    if (message == null) {
                        message = "提交失败";
                    }
                    MyToast.showToast(message);
                    mController.loading().hide();


                }
            }

            @Override
            public void dataError(int funId) {
                listener.onCustomListener(2, null, 0);
                MyToast.showToast("获取网络数据失败");
                mController.loading().hide();
            }
        }).execute(map);
    }
}
