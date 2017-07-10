package com.zbxn.init.http;

import com.zbxn.init.http.filter.FilterController;
import com.zbxn.init.http.okhttputils.callback.AbsCallback;
import com.zbxn.init.http.okhttputils.request.BaseRequest;
import com.zbxn.pub.http.ICallback;
import com.zbxn.pub.http.RequestUtils.Code;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class JsonCallback extends AbsCallback<JSONObject> {

    /**
     * 接口回调
     */
    private ICallback mCallback;
    /**
     * 请求标识
     */
    private Code mCode;

    public JsonCallback(Code code, ICallback callback) {
        this.mCallback = callback;
        this.mCode = code;
    }

    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
        if (mCallback != null)
            mCallback.onStart(mCode);
    }

    @Override
    public void onAfter(boolean isFromCache, JSONObject t, Call call,
                        Response response, Exception e) {
        super.onAfter(isFromCache, t, call, response, e);
    }

    @Override
    public void onResponse(boolean isFromCache, JSONObject json,
                           Request request, Response response) {
        if (json.optInt(com.zbxn.pub.http.Response.SUCCESS) == com.zbxn.pub.http.Response.RESULT_OK) {
            if (mCallback != null) {
                mCallback.onSuccess(mCode, json);
            }
        } else {
            if (mCallback != null)
                mCallback.onFailure(mCode, json);
//            onError(isFromCache, null, response, null);
        }
    }

    @Override
    public void onError(boolean isFromCache, Call call, Response response,
                        Exception e) {
        super.onError(isFromCache, call, response, e);
        if (mCallback != null) {
            try {
                JSONObject error = parseNetworkResponse(response);
                mCallback.onFailure(mCode, error);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public JSONObject parseNetworkResponse(Response response) {
        JSONObject json = new JSONObject();
        try {
            if (response == null) {
                // json过滤
                json = FilterController.check(0, null, json);
            } else if (response.isSuccessful()) {
                String bodyStr = response.body().string();
                json = new JSONObject(bodyStr);
                // json过滤
                json = FilterController.check(response.code(), response.headers(), json);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

}
