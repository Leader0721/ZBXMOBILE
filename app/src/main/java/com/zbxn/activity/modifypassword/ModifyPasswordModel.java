package com.zbxn.activity.modifypassword;

import com.zbxn.pub.frame.mvp.AbsBaseModel;
import com.zbxn.pub.frame.mvp.base.ModelCallback;
import com.zbxn.pub.frame.mvp.base.RequestResult;
import com.zbxn.pub.http.RequestUtils;
import com.zbxn.pub.http.Response;

import org.json.JSONObject;

/**
 * 项目名称：ZBXMobile
 * 创建人：LiangHanXin
 * 创建时间：2016/8/11 11:37
 */
public class ModifyPasswordModel extends AbsBaseModel {
    public ModifyPasswordModel(ModelCallback callback) {
        super(callback);
    }

    @Override
    protected RequestResult getResultSuccess(RequestUtils.Code code, JSONObject response) {

        RequestResult result = new RequestResult(0);
        try {
            switch (code) {
                case USER_RESET_PASSWORD:
//                    if (response.optBoolean(Response.SUCCESS, false)) {
                    if ("0".equals(response.optString(Response.SUCCESS))) {
                        String data = response.optString(Response.MSG, null);
                        result.message = data;
                    } else {
                        result.success = 1;
                        result.message = "修改密码失败";
                    }
                    break;
            }
        } catch (Exception e) {
            result.success = 1;
            result.message = "修改密码失败";
        }
        return result;
    }

    @Override
    protected RequestResult getResultFailure(RequestUtils.Code code, JSONObject error) {
        RequestResult result = new RequestResult(1);
        try {
            switch (code) {
                case USER_RESET_PASSWORD:
                    if (error == null) {
                        result.message = "修改密码失败";
                    } else {
                        result.message = error.optString(Response.MSG, "修改失败");
                    }
                    break;
            }

        } catch (Exception e) {

        }
        return result;
    }
}
