package com.zbxn.activity.examinationandapproval;

import android.content.Context;

import com.zbxn.bean.ApplyEntity;
import com.zbxn.bean.ApprovalEntity;
import com.zbxn.http.BaseAsyncTask;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.data.ResultData;
import com.zbxn.pub.data.base.BaseAsyncTaskInterface;
import com.zbxn.pub.frame.mvp.AbsBasePresenterOld;
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
 * 项目名称：ZBXMobile
 * 创建人：LiangHanXin
 * 创建时间：2016/10/10 11:15
 */
public class ApplyPresenter extends AbsBasePresenterOld<ControllerCenter> {
    private ICustomListener listener;
    private int mPage = 1;
    private String State = "";
    private String Type = "";

    public ApplyPresenter(ControllerCenter controller, final ICustomListener listener) {
        super(controller);
        this.listener = listener;
    }

    @Override
    public void onSuccess(RequestUtils.Code code, JSONObject response, RequestResult result) {

    }

    @Override
    public void onFailure(RequestUtils.Code code, JSONObject error, RequestResult result) {

    }

    /**
     * 申请列表
     *
     * @param context
     * @param listener
     */
    public void dataList(Context context, final ICustomListener listener, int page, String State, String Type) {
        Map<String, String> map = new HashMap<String, String>();

        this.mPage = page;
        this.State = State;
        this.Type = Type;

        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);
        map.put("page", page + "");
        map.put("State", State);
        map.put("Type", Type);

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTask(context, false, 0, server + "/oaApproveInfoController/selectApprove.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultData<ApplyEntity>().parse(json, ApplyEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultData mResult = (ResultData) result;
                if ("0".equals(mResult.getSuccess())) {//0成功
                    if (StringUtils.isEmpty(mResult.getRows())) {
                        mPage++;
                    }
                    List<ApplyEntity> list = mResult.getRows();
                    listener.onCustomListener(0, list, 0);
                } else {
                    listener.onCustomListener(-1, "", 0);
                    String message = mResult.getMsg();
                    MyToast.showToast(message);
                }
            }

            @Override
            public void dataError(int funId) {
                listener.onCustomListener(-1, "", 0);
                MyToast.showToast("获取网络数据失败");
            }
        }).execute(map);
    }


    /**
     * 审批列表
     *
     * @param context
     * @param listener
     */
    public void dataListapproval(Context context, final ICustomListener listener, int page, String State, String Type) {
        Map<String, String> map = new HashMap<String, String>();

        this.mPage = page;
        this.State = State;
        this.Type = Type;

        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);
        map.put("page", page + "");
        map.put("State", State);
        map.put("Type", Type);

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTask(context, false, 0, server + "/oaApproveInfoController/selectMyApprove.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultData<ApplyEntity>().parse(json, ApplyEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultData mResult = (ResultData) result;
                if ("0".equals(mResult.getSuccess())) {//0成功
                    if (StringUtils.isEmpty(mResult.getRows())) {
                        mPage++;
                    }
                    List<ApplyEntity> list = mResult.getRows();
                    listener.onCustomListener(0, list, 0);
                } else {
                    listener.onCustomListener(-1, "", 0);
                    String message = mResult.getMsg();
                    MyToast.showToast(message);
                }
            }

            @Override
            public void dataError(int funId) {
                listener.onCustomListener(-1, "", 0);
                MyToast.showToast("获取网络数据失败");
            }
        }).execute(map);
    }

    /**
     * 查询列表
     *
     * @param context
     * @param page
     * @param State
     * @param Type
     * @param listener
     */
    public void dataListInquire(Context context, int page, String State, String Type, final ICustomListener listener) {
        Map<String, String> map = new HashMap<String, String>();

        this.mPage = page;

        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);
        map.put("page", page + "");
        map.put("State", State);
        map.put("Type", Type);

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTask(context, false, 0, server + "/oaApproveInfoController/selectAllApprove.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultData<ApplyEntity>().parse(json, ApplyEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultData mResult = (ResultData) result;
                if ("0".equals(mResult.getSuccess())) {//0成功
                    if (StringUtils.isEmpty(mResult.getRows())) {
                        mPage++;
                    }
                    List<ApplyEntity> list = mResult.getRows();
                    listener.onCustomListener(0, list, 0);
                } else {
                    listener.onCustomListener(-1, "", 0);
                    String message = mResult.getMsg();
                    listener.onCustomListener(4, message, 0);
                }
            }

            @Override
            public void dataError(int funId) {
                listener.onCustomListener(-1, "", 0);
                MyToast.showToast("获取网络数据失败");
                mController.loading().hide();
            }
        }).execute(map);
    }

    /**
     * 获取审批状态列表
     *
     * @param context
     * @param listener
     */
    public void selectFrame(Context context, final ICustomListener listener) {
        Map<String, String> map = new HashMap<String, String>();


        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);


        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTask(context, false, 0, server + "/oaApproveInfoController/selectFrame.do", new BaseAsyncTaskInterface() {
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
                    if (StringUtils.isEmpty(mResult.getRows())) {
                        mPage++;
                    }
                    List<ApprovalEntity> list = mResult.getRows();
                    listener.onCustomListener(2, list, 0);
                } else {
                    listener.onCustomListener(-1, "", 0);
                    String message = mResult.getMsg();
                    MyToast.showToast(message);
                }
            }

            @Override
            public void dataError(int funId) {
                listener.onCustomListener(-1, "", 0);
                MyToast.showToast("获取网络数据失败");
            }
        }).execute(map);
    }

    /**
     * 获取申请状态列表
     *
     * @param context
     * @param listener
     */
    public void selecttype(Context context, final ICustomListener listener) {
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
                    if (StringUtils.isEmpty(mResult.getRows())) {
                        mPage++;
                    }
                    List<ApprovalEntity> list = mResult.getRows();
                    listener.onCustomListener(3, list, 0);
                } else {
                    listener.onCustomListener(-1, "", 0);
                    String message = mResult.getMsg();
                    MyToast.showToast(message);
                }
            }

            @Override
            public void dataError(int funId) {
                listener.onCustomListener(-1, "", 0);
                MyToast.showToast("获取网络数据失败");
            }
        }).execute(map);
    }

    /*@Override
    public void onRefresh() {
        mPage = 1;
        dataList(context, listener, mPage, State, Type);
    }

    @Override
    public void onLoadMore() {
        dataList(context, listener, mPage, State, Type);
    }*/
}
