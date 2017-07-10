package com.zbxn.pub.frame.mvp.base;

import com.zbxn.pub.http.RequestUtils;

import org.json.JSONObject;

/**
 * Created by GISirFive on 2016/7/27.
 */
public interface ModelCallback {

    /***
     * 开始请求
     *
     */
    void onStart(RequestUtils.Code code);

    /**
     * 请求成功，返回请求成果
     * @param code 请求接口标识
     * @param response 原始请求结果
     * @param result 已封装的请求结果
     */
    void onSuccess(RequestUtils.Code code, JSONObject response, RequestResult result);

    /**
     * 请求失败，返回请求成果
     * @param code 请求接口标识
     * @param error 原始请求结果
     * @param result 已封装的请求结果
     */
    void onFailure(RequestUtils.Code code, JSONObject error, RequestResult result);
}
