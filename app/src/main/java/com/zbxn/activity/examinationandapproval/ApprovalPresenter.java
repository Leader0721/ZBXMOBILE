package com.zbxn.activity.examinationandapproval;

import android.content.Context;

import com.zbxn.bean.ApprovalEntity;
import com.zbxn.bean.ApprovalInfoEntity;
import com.zbxn.bean.Member;
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
import com.zbxn.pub.utils.JsonUtil;
import com.zbxn.pub.utils.MyToast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.PreferencesUtils;

/**
 * ApprovalActivity 的 presenter
 *
 * @author: ysj
 * @date: 2016-10-10 14:56
 */
public class ApprovalPresenter extends AbsBasePresenter<ControllerCenter> {

    public ApprovalPresenter(AbsToolbarActivity activity) {
        super(activity);
    }

    @Override
    public void onSuccess(RequestUtils.Code code, JSONObject response, RequestResult result) {

    }

    @Override
    public void onFailure(RequestUtils.Code code, JSONObject error, RequestResult result) {

    }

    /**
     * 获取审批主界面的图标等
     *
     * @param context
     */
    public void getApprovalByAuthor(Context context, final ICustomListener listener) {
        mController.loading().show("加载中...");
        Map<String, String> map = new HashMap<String, String>();

        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTask(context, false, 0, server + "/oaApproveInfoController/selecttype.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultData<ApprovalEntity>().parse(json, ApprovalEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultData mResult = (ResultData) result;
                if ("0".equals(mResult.getSuccess())) {//0成功
                    // 获取返回的值付给data
                    String rows = JsonUtil.toJsonString(((ResultData) result).getRows());


                    // 保存rows到本地
                    PreferencesUtils.putString(BaseApp.CONTEXT, "ApprovalDataList", rows);

                    List<ApprovalEntity> list = new ArrayList<>();
                    list = JsonUtil.fromJsonList(rows, ApprovalEntity.class);
                    listener.onCustomListener(0, list, 0);
                } else {
                    String message = mResult.getMsg();
                    MyToast.showToast( message);
                }
                mController.loading().hide();
            }

            @Override
            public void dataError(int funId) {
                MyToast.showToast( "获取网络数据失败");
                mController.loading().hide();
            }
        }).execute(map);
    }

    public void getInfoType(Context context, String infoid, final ICustomListener listener) {
        mController.loading().show("加载中...");
        Map<String, String> map = new HashMap<String, String>();

        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);
        map.put("infoid", infoid);

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTask(context, false, 0, server + "/oaApproveInfoController/selectinfoType.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultData<ApprovalInfoEntity>().parse(json, ApprovalInfoEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultData mResult = (ResultData) result;
                if ("0".equals(mResult.getSuccess())) {//0成功
                    List<ApprovalInfoEntity> list = mResult.getRows();
                    listener.onCustomListener(2, list, 0);
                } else {
                    listener.onCustomListener(1, null, 0);
                }
                mController.loading().hide();
            }

            @Override
            public void dataError(int funId) {
                MyToast.showToast( "获取网络数据失败");
                mController.loading().hide();
            }
        }).execute(map);
    }

    /**
     * 修改申请状态
     *
     * @param context
     * @param id          申请表ID
     * @param state       想要修改的状态
     * @param comment     审批意见
     * @param approveUser 转审人ID(转审时再传)
     * @param listener    回调
     */
    public void postApprovalOpinion(Context context, String id, String state, String comment, String approveUser, final ICustomListener listener) {
        mController.loading().show("加载中...");
        Map<String, String> map = new HashMap<String, String>();

        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);
        map.put("id", id);
        map.put("state", state);
        map.put("comment", comment);
        map.put("approveUser", approveUser);

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTask(context, false, 0, server + "/oaApproveInfoController/UpdateFlow.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultData<Member>().parse(json, Member.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultData mResult = (ResultData) result;
                if ("0".equals(mResult.getSuccess())) {//0成功
                    listener.onCustomListener(0, null, 0);
                } else {
                    listener.onCustomListener(1, null, 0);
                }
                mController.loading().hide();
            }

            @Override
            public void dataError(int funId) {
                MyToast.showToast( "获取网络数据失败");
                mController.loading().hide();
            }
        }).execute(map);
    }

}
