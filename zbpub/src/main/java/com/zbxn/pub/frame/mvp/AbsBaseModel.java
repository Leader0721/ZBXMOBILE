package com.zbxn.pub.frame.mvp;

import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.frame.mvp.base.ModelCallback;
import com.zbxn.pub.frame.mvp.base.RequestResult;
import com.zbxn.pub.http.ICallback;
import com.zbxn.pub.http.IRequest;
import com.zbxn.pub.http.IRequestParams;
import com.zbxn.pub.http.RequestUtils;
import com.zbxn.pub.http.Response;

import org.json.JSONObject;

/**
 * Created by GISirFive on 2016/7/27.
 */
public abstract class AbsBaseModel implements IRequest, ICallback {

    private ModelCallback mModelCallback;

    public AbsBaseModel(ModelCallback callback) {
        this.mModelCallback = callback;
    }


    @Override
    public final void post(RequestUtils.Code code, IRequestParams params) {
        BaseApp.getHttpClient().post(code, params, this);
    }

    @Override
    public final void get(RequestUtils.Code code, IRequestParams params) {
        BaseApp.getHttpClient().post(code, params, this);
    }

    @Override
    public void onStart(RequestUtils.Code code) {
        mModelCallback.onStart(code);
    }

    @Override
    public void onSuccess(RequestUtils.Code code, JSONObject response) {
        RequestResult result = getResultSuccess(code, response);
        if (response.optInt(Response.SUCCESS) == Response.RESULT_OK) {
            mModelCallback.onSuccess(code, response, result);
        } else {
            mModelCallback.onFailure(code, response, result);
        }
    }

    @Override
    public void onFailure(RequestUtils.Code code, JSONObject error) {
        RequestResult result = getResultFailure(code, error);
        mModelCallback.onFailure(code, error, result);
    }

    /**
     * 请求成功，封装请求成果<br/>
     * <b>运行于UI线程，封装的不合理</b>
     *
     * @param code
     * @param response 结果是json形式
     * @return 返回结果封装成
     */
    protected abstract RequestResult getResultSuccess(RequestUtils.Code code, JSONObject response);

    /**
     * 请求失败，封装请求成果<br/>
     * <b>运行于UI线程，封装的不合理</b>
     *
     * @param code
     * @param error 结果是json形式
     * @return 返回结果封装成
     */
    protected abstract RequestResult getResultFailure(RequestUtils.Code code, JSONObject error);
}
