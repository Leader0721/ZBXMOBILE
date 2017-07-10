package com.zbxn.activity.schedule;

import android.content.Context;

import com.zbxn.bean.Member;
import com.zbxn.bean.ScheduleDetailEntity;
import com.zbxn.bean.ScheduleRuleEntity;
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

/**
 * 日程详情 presenter
 * Created by Administrator on 2016/9/27.
 */
public class DetailTaskPresenter extends AbsBasePresenter<ControllerCenter> {

    public DetailTaskPresenter(AbsToolbarActivity activity) {
        super(activity);
    }

    @Override
    public void onSuccess(RequestUtils.Code code, JSONObject response, RequestResult result) {

    }

    @Override
    public void onFailure(RequestUtils.Code code, JSONObject error, RequestResult result) {

    }

    /**
     * 根据日程id获取日程详情
     */
    public void getScheduleDetail(Context context, String id, final ICustomListener listener) {
        mController.loading().show("正在加载...");
        //请求网络
        //日程详情
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        Map<String, String> map = new HashMap<String, String>();
        map.put("tokenid", ssid);
        map.put("id", id);

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        /**
         * 日程详情
         */
        new BaseAsyncTask(context, false, 0, server + "/oaScheduleRule/getId.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultParse<ScheduleRuleEntity>().parse(json, ScheduleRuleEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                mController.loading().hide();
                ResultParse mResult = (ResultParse) result;
                if ("0".equals(mResult.getSuccess())) {//0成功
                    ScheduleRuleEntity sre = (ScheduleRuleEntity) mResult.getData();
                    listener.onCustomListener(6, sre, 0);
                } else {
                    MyToast.showToast( mResult.getMsg());
                }
            }

            @Override
            public void dataError(int funId) {
                MyToast.showToast( "获取网络数据失败");
                mController.loading().hide();
            }
        }).execute(map);

        //参与人
        Map<String, String> participantMap = new HashMap<String, String>();
        participantMap.put("tokenid", ssid);
        participantMap.put("participantID", id + "");

        new BaseAsyncTask(context, false, 0, server + "/oaScheduleParticipant/findParticipantPerson.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultData<ScheduleDetailEntity>().parse(json, ScheduleDetailEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
//                mController.loading().hide();
                ResultData<ScheduleDetailEntity> mResult = (ResultData<ScheduleDetailEntity>) result;
                if ("0".equals(mResult.getSuccess())) {
                    List<ScheduleDetailEntity> sdeList = (List<ScheduleDetailEntity>) mResult.getRows();
                    listener.onCustomListener(3, sdeList, 0);
                } else {
                    MyToast.showToast( mResult.getMsg());
                }
            }

            @Override
            public void dataError(int funId) {
                MyToast.showToast( "获取网络数据失败");
                mController.loading().hide();
            }
        }).execute(participantMap);

        //共享人
        Map<String, String> scheduleMap = new HashMap<String, String>();
        scheduleMap.put("tokenid", ssid);
        scheduleMap.put("scharerID", id);

        new BaseAsyncTask(context, false, 0, server + "/oaScheduleSharer/findScharerPerson.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultData<ScheduleDetailEntity>().parse(json, ScheduleDetailEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                mController.loading().hide();
                ResultData<ScheduleDetailEntity> mResult = (ResultData<ScheduleDetailEntity>) result;
                if ("0".equals(mResult.getSuccess())) {
                    List<ScheduleDetailEntity> sdeList = (List<ScheduleDetailEntity>) mResult.getRows();
                    listener.onCustomListener(4, sdeList, 0);
                } else {
                    MyToast.showToast( mResult.getMsg());
                }
            }

            @Override
            public void dataError(int funId) {
                MyToast.showToast( "获取网络数据失败");
                mController.loading().hide();
            }
        }).execute(scheduleMap);
    }

    /**
     * 查看日程参与人
     */
    public void getScharerPerson(Context context, String id, final ICustomListener listener) {
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        //参与人
        Map<String, String> participantMap = new HashMap<String, String>();
        participantMap.put("tokenid", ssid);
        participantMap.put("participantID", id + "");

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTask(context, false, 0, server + "/oaScheduleParticipant/findParticipantPerson.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultData<ScheduleDetailEntity>().parse(json, ScheduleDetailEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
//                mController.loading().hide();
                ResultData<ScheduleDetailEntity> mResult = (ResultData<ScheduleDetailEntity>) result;
                if ("0".equals(mResult.getSuccess())) {
                    List<ScheduleDetailEntity> sdeList = (List<ScheduleDetailEntity>) mResult.getRows();
                    listener.onCustomListener(3, sdeList, 0);
                } else {
                    MyToast.showToast( mResult.getMsg());
                }
            }

            @Override
            public void dataError(int funId) {
                MyToast.showToast( "获取网络数据失败");
                mController.loading().hide();
            }
        }).execute(participantMap);
    }

    /**
     * 查看日程分享人
     */
    public void getParticipantPerson(Context context, String id, final ICustomListener listener) {
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        //共享人
        Map<String, String> scheduleMap = new HashMap<String, String>();
        scheduleMap.put("tokenid", ssid);
        scheduleMap.put("scharerID", id);

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTask(context, false, 0, server + "/oaScheduleSharer/findScharerPerson.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultData<ScheduleDetailEntity>().parse(json, ScheduleDetailEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                mController.loading().hide();
                ResultData<ScheduleDetailEntity> mResult = (ResultData<ScheduleDetailEntity>) result;
                if ("0".equals(mResult.getSuccess())) {
                    List<ScheduleDetailEntity> sdeList = (List<ScheduleDetailEntity>) mResult.getRows();
                    listener.onCustomListener(4, sdeList, 0);
                } else {
                    MyToast.showToast( mResult.getMsg());
                }
            }

            @Override
            public void dataError(int funId) {
                MyToast.showToast( "获取网络数据失败");
                mController.loading().hide();
            }
        }).execute(scheduleMap);
    }

    /**
     * 提交修改后的日程
     *
     * @param context
     * @param title          标题
     * @param starttime      开始时间
     * @param endtime        结束时间
     * @param precedeType    提前提醒类型
     * @param allday         是否全天
     * @param isalarm        是否提醒
     * @param scheduleDetail 日程详情
     * @param location       地址
     * @param listener       监听回调
     */
    public void commitScheduleDetail(Context context, String scheduleRuleType, String id, String title, String starttime, String endtime,
                                     String precedeType, String allday, String isalarm, String scheduleDetail,
                                     String location,String userId, String userId1, final ICustomListener listener) {
        mController.loading().show("正在加载...");
        //请求网络
        //修改日程
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        Map<String, String> map = new HashMap<String, String>();
        map.put("tokenid", ssid);
        map.put("scheduleRuleType", scheduleRuleType);
        map.put("id", id);
        map.put("title", title);
        map.put("starttime", starttime);
        map.put("endtime", endtime);
        map.put("precedeType", precedeType);
        map.put("allday", allday);
        map.put("isalarm", isalarm);//是否提醒 0 false   1提醒
        map.put("scheduleDetail", scheduleDetail);
        map.put("location", location);
        map.put("sharerID", userId1);//分享日程人员数组
        map.put("participantIDStr", userId);//参与日程人员数组

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTask(context, false, 0, server + "/oaScheduleRule/updateScheduleRule.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultParse<Member>().parse(json, Member.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                mController.loading().hide();
                ResultParse mResult = (ResultParse) result;
                if ("0".equals(mResult.getSuccess())) {
                    listener.onCustomListener(5, null, 0);
                } else {
                    MyToast.showToast( mResult.getMsg());
                }
            }

            @Override
            public void dataError(int funId) {
                MyToast.showToast( "获取网络数据失败");
                mController.loading().hide();
            }
        }).execute(map);
    }

    /**
     * 删除日程
     *
     * @param context
     * @param id
     * @param listener
     */
    public void deleteSchedule(Context context, String id, final ICustomListener listener) {
        mController.loading().show("正在删除...");
        //请求网络
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        Map<String, String> map = new HashMap<String, String>();
        map.put("tokenid", ssid);
        map.put("id", id);

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTask(context, false, 0, server + "/oaScheduleRule/delete.do", new BaseAsyncTaskInterface() {

            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultParse<Member>().parse(json, Member.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                mController.loading().hide();
                ResultParse mResult = (ResultParse) result;
                if ("0".equals(mResult.getSuccess())) {
                    listener.onCustomListener(7, null, 0);
                } else {
                    MyToast.showToast( mResult.getMsg());
                }
            }

            @Override
            public void dataError(int funId) {
                MyToast.showToast( "获取网络数据失败");
                mController.loading().hide();
            }
        }).execute(map);
    }

}
