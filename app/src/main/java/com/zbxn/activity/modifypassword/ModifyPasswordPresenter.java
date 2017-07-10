package com.zbxn.activity.modifypassword;

import android.content.Context;

import com.zbxn.bean.Member;
import com.zbxn.activity.ModifyPassword;
import com.zbxn.init.http.RequestParams;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.frame.mvp.AbsBasePresenterOld;
import com.zbxn.pub.frame.mvp.base.RequestResult;
import com.zbxn.pub.http.RequestUtils;

import org.json.JSONObject;

import utils.PreferencesUtils;

/**
 * 项目名称：ZBXMobile
 * 创建人：LiangHanXin
 * 创建时间：2016/8/11 11:37
 */
public class ModifyPasswordPresenter extends AbsBasePresenterOld<IModifyPasswordView> {

    private ModifyPasswordModel model = new ModifyPasswordModel(this);

    public ModifyPasswordPresenter(IModifyPasswordView controller) {
        super(controller);
    }

    @Override
    public void onSuccess(RequestUtils.Code code, JSONObject response, RequestResult result) {

        switch (code) {
            case USER_RESET_PASSWORD:
                String data = result.message;
                mController.message().show(data);
                //将修改的密码保存起来
                PreferencesUtils.putString((Context) mController, ModifyPassword.FLAG_INPUT_PASSWORD,
                        mController.getSecondPassword());
                mController.finishForResult(true);
                break;
        }

    }

    @Override
    public void onFailure(RequestUtils.Code code, JSONObject error, RequestResult result) {
        switch (code) {
            case USER_RESET_PASSWORD:
                Member.clear();
                String data = result.message;
                mController.message().show(data);
//                mController.finishForResult(false);
                break;
        }


    }

    public void login() {
        String secondPassword = mController.getNewPassword();
        String OriginalPassword = mController.getOriginalPassword();
        RequestParams params = new RequestParams();
        params.put("password", OriginalPassword);//原始密码
        params.put("newPassword", secondPassword);//新密码
        //将用户输入的账号保存起来，以便下次使用
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        params.put("tokenid", ssid);
        model.post(RequestUtils.Code.USER_RESET_PASSWORD, params);
    }

}
