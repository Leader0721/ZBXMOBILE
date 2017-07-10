package com.zbxn.activity.createbulletin;

import com.zbxn.pub.frame.mvp.AbsBaseModel;
import com.zbxn.pub.frame.mvp.base.ModelCallback;
import com.zbxn.pub.frame.mvp.base.RequestResult;
import com.zbxn.pub.http.RequestUtils;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/8/29.
 */
public class CreatBulletinModel extends AbsBaseModel {

    public CreatBulletinModel(ModelCallback callback) {
        super(callback);
    }

    @Override
    protected RequestResult getResultSuccess(RequestUtils.Code code, JSONObject response) {
        RequestResult result = new RequestResult(0);
        result.message = "发布成功";
        return result;
    }

    @Override
    protected RequestResult getResultFailure(RequestUtils.Code code, JSONObject error) {
        RequestResult result = new RequestResult(1);
        result.success = 1;
        result.message = "发布失败";
        return result;
    }
}
