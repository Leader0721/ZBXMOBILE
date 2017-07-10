package com.zbxn.pub.http;

import com.zbxn.pub.http.RequestUtils.Code;

public interface IRequestControl {

    /**
     * POST方式从服务器获取数据/向服务器提交数据
     *
     * @param code
     * @param params
     * @param callback
     * @author GISirFive
     */
    void post(Code code, IRequestParams params, ICallback callback);

    /***
     * GET方式从服务器获取数据/向服务器提交数据
     *
     * @param code
     * @param params
     * @param callback
     * @author GISirFive
     */
    void get(Code code, IRequestParams params, ICallback callback);
}
