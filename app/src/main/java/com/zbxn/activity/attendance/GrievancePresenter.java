package com.zbxn.activity.attendance;

import android.content.Context;

import com.zbxn.http.BaseAsyncTask;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.data.Result;
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
import java.util.Map;

import utils.PreferencesUtils;

/**
 * 项目名称：ZBXMobile
 * 创建人：LiangHanXin
 * 创建时间：2016/9/30 10:34
 */
public class GrievancePresenter extends AbsBasePresenter<ControllerCenter> {


    public GrievancePresenter(AbsToolbarActivity activity) {
        super(activity);
    }

    @Override
    public void onSuccess(RequestUtils.Code code, JSONObject response, RequestResult result) {

    }

    @Override
    public void onFailure(RequestUtils.Code code, JSONObject error, RequestResult result) {

    }

    /**
     * 考勤申诉
     *
     * @param context
     * @param attendanceid
     * @param appealsource
     * @param appealType
     * @param appealtime
     * @param appealreason
     * @param listener
     */
    public void save(Context context, String attendanceid, String appealsource
            , String appealType, String appealtime, String appealreason, final ICustomListener listener) {
        mController.loading().show("加载中...");
        Map<String, String> map = new HashMap<String, String>();

        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);
        map.put("attendanceid", attendanceid);//考勤记录id
        map.put("appealsource", appealsource);//异常来源
        map.put("appealtype", appealType);//申诉类型
        map.put("appealtime", appealtime);//签到时间
        map.put("appealreason", appealreason);//申诉理由

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTask(context, false, 0, server + "/oaAttendanceAppeal/save.do", new BaseAsyncTaskInterface() {
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
                    listener.onCustomListener(0, null, 0);
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


}
