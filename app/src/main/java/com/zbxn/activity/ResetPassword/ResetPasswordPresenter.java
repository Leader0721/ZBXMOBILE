package com.zbxn.activity.ResetPassword;

import android.content.Context;

import com.zbxn.bean.ResetPasswordEntity;
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
import com.zbxn.pub.utils.MyToast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.PreferencesUtils;

/**
 * 项目名称：找回密码的Presenter
 * 创建人：LiangHanXin
 * 创建时间：2016/10/31 9:29
 */
public class ResetPasswordPresenter extends AbsBasePresenter<ControllerCenter> {


    public ResetPasswordPresenter(AbsToolbarActivity controller) {
        super(controller);
    }

    @Override
    public void onSuccess(RequestUtils.Code code, JSONObject response, RequestResult result) {

    }

    @Override
    public void onFailure(RequestUtils.Code code, JSONObject error, RequestResult result) {

    }

    /**
     * 发送短信
     *
     * @param context
     * @param listener
     */
    public void RuleTimeDataList(Context context,String phoneNumber, final ICustomListener listener) {
        mController.loading().show("加载中...");
        Map<String, String> map = new HashMap<String, String>();

        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);
        map.put("phoneNumber", phoneNumber);//手机号

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTask(context, false, 0, server + "/baseUser/findPassword.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultData<ResetPasswordEntity>().parse(json, ResetPasswordEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultData mResult = (ResultData) result;
                if ("0".equals(mResult.getSuccess())) {//0成功
                    List<ResetPasswordEntity> list = mResult.getRows();
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
     * 重置密码
     *
     * @param context
     * @param listener
     */
    public void Password(Context context,String phoneNumber,String randomNumber,String passWord, final ICustomListener listener) {
        mController.loading().show("加载中...");
        Map<String, String> map = new HashMap<String, String>();

        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);
        map.put("phoneNumber", phoneNumber);//手机号
        map.put("randomNumber", randomNumber);//验证码
        map.put("passWord", passWord);//密码

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTask(context, false, 0, server + "/baseUser/validMessagePwd.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultData<ResetPasswordEntity>().parse(json, ResetPasswordEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultData mResult = (ResultData) result;
                if ("0".equals(mResult.getSuccess())) {//0成功
                    List<ResetPasswordEntity> list = mResult.getRows();
                    listener.onCustomListener(2, list, 2);
                    mController.loading().hide();

                } else {
                    String message = mResult.getMsg();
                    MyToast.showToast( message);
                    mController.loading().hide();
                }
            }

            @Override
            public void dataError(int funId) {
                listener.onCustomListener(1, null, 0);
                MyToast.showToast( "获取网络数据失败");
                mController.loading().hide();
            }
        }).execute(map);
    }









}
