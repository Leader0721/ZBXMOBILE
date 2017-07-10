package com.zbxn.activity.attendance;

import android.content.Context;

import com.zbxn.bean.AttendanceRecordEntity;
import com.zbxn.http.BaseAsyncTask;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.data.ResultData;
import com.zbxn.pub.data.base.BaseAsyncTaskInterface;
import com.zbxn.pub.frame.mvp.AbsBasePresenter;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.frame.mvp.base.ControllerCenter;
import com.zbxn.pub.frame.mvp.base.RequestResult;
import com.zbxn.pub.http.RequestUtils;
import com.zbxn.pub.utils.ConfigUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.PreferencesUtils;

/**
 * 项目名称：ZBXMobile
 * 创建人：LiangHanXin
 * 创建时间：2016/9/30 10:34
 */
public class AttendanceRecordPresenter extends AbsBasePresenter<ControllerCenter> {


    public AttendanceRecordPresenter(AbsToolbarActivity activity) {
        super(activity);
    }

    @Override
    public void onSuccess(RequestUtils.Code code, JSONObject response, RequestResult result) {

    }

    @Override
    public void onFailure(RequestUtils.Code code, JSONObject error, RequestResult result) {

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
                    listener.onCustomListener(0, list, 0);
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
