package com.zbxn.utils;

import com.zbxn.pub.frame.mvp.AbsBaseModel;
import com.zbxn.pub.frame.mvp.base.ModelCallback;
import com.zbxn.pub.frame.mvp.base.RequestResult;
import com.zbxn.pub.http.RequestUtils;

import org.json.JSONObject;

/**
 * 公共的Model，解决MVP模式下，model层冗余的情况
 * @author GISirFive
 * @time 2016/8/9
 */
public class BaseModel extends AbsBaseModel{

    public BaseModel(ModelCallback callback) {
        super(callback);
    }

    @Override
    protected RequestResult getResultSuccess(RequestUtils.Code code, JSONObject response) {
        return ModelHelper.getResultSuccess(code, response);
    }

    @Override
    protected RequestResult getResultFailure(RequestUtils.Code code, JSONObject error) {
        return ModelHelper.getResultFailure(code, error);
    }
}
