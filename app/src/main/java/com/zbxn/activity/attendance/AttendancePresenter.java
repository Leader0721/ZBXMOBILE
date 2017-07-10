package com.zbxn.activity.attendance;

import android.content.Context;

import com.zbxn.bean.AttendanceRecordEntity;
import com.zbxn.bean.AttendanceRuleTimeEntity;
import com.zbxn.http.BaseAsyncTask;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.data.Result;
import com.zbxn.pub.data.ResultData;
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
 * Created by GISirFive on 2016/8/3.
 */
public class AttendancePresenter extends AbsBasePresenter<ControllerCenter> {

    public AttendancePresenter(AbsToolbarActivity activity) {
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
     * 获取考勤规则时间
     *
     * @param context
     * @param listener
     */
    public void RuleTimeDataList(Context context, final ICustomListener listener) {
        mController.loading().show("加载中...");
        Map<String, String> map = new HashMap<String, String>();

        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTask(context, false, 0, server + "/oaAttendanceRuleTime/dataList.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultData<AttendanceRuleTimeEntity>().parse(json, AttendanceRuleTimeEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultData mResult = (ResultData) result;
                if ("0".equals(mResult.getSuccess())) {//0成功
                    List<AttendanceRuleTimeEntity> list = mResult.getRows();
                    listener.onCustomListener(0, list, 0);
                    mController.loading().hide();
                } else {
                    String message = mResult.getMsg();
                    MyToast.showToast( message);
                    mController.loading().hide();
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
     * 添加考勤
     *
     * @param context
     * @param checktime
     * @param ip
     * @param address
     * @param longitude
     * @param latitude
     * @param listener
     */
    public void save(Context context, String checktime, String ip, String address
            , String longitude, String latitude, final ICustomListener listener) {
        mController.loading().show("加载中...");
        Map<String, String> map = new HashMap<String, String>();

        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);
//        map.put("checktime", checktime);//打卡时间
        map.put("ip", ip);//打卡IP
        map.put("address", address);//考勤地点
        map.put("longitude", longitude);//经度
        map.put("latitude", latitude);//维度

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTask(context, false, 0, server + "/oaAttendanceRecord/save.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return Result.parse(json);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                Result mResult = (Result) result;
                if ("0".equals(mResult.getSuccess())) {//0成功
                    mController.loading().hide();
                    listener.onCustomListener(6, null, 0);
                    mController.loading().hide();
                } else {
                    String message = mResult.getMsg();
                    MyToast.showToast( message);
                    mController.loading().hide();
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
     * 查看考勤记录
     *
     * @param context
     * @param date
     * @param listener
     */
    public void dataList(Context context, String date, final ICustomListener listener) {
        Map<String, String> map = new HashMap<String, String>();

        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);
        map.put("date", date);

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTask(context, false, 0, server + "/oaAttendance/dataList.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultData<AttendanceRecordEntity>().parse(json, AttendanceRecordEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultData mResult = (ResultData) result;
                if ("0".equals(mResult.getSuccess())) {//0成功
                    List<AttendanceRecordEntity> list = mResult.getRows();
                    listener.onCustomListener(3, list, 0);
//                    mController.loading().hide();
                    mController.updateSuccessView();
                } else {
                    String message = mResult.getMsg();
//                    MyToast.showToast( message);
//                    mController.loading().hide();
                    mController.updateErrorView();
                }
            }

            @Override
            public void dataError(int funId) {
//                MyToast.showToast( "获取网络数据失败");
//                mController.loading().hide();
                mController.updateErrorView();
            }
        }).execute(map);
    }
}
