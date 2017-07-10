package com.zbxn.pub.http;

import org.json.JSONObject;

import com.zbxn.pub.http.RequestUtils.Code;

/**
 * 请求回调接口
 *
 * @author GISirFive
 */
public interface ICallback {

    /***
     * 开始请求
     *
     * @param code
     */
    void onStart(Code code);


    /**
     * 请求成功，返回请求成果
     *
     * @param code     请求标识
     * @param response 结果是json形式
     */
    void onSuccess(Code code, JSONObject response);

    /**
     * 请求失败
     *
     * @param code
     * @param error
     */
    void onFailure(Code code, JSONObject error);

}
