package com.zbxn.pub.http;

import com.zbxn.pub.http.RequestUtils.Code;

/**
 * 请求类型
 *
 * @author GISirFive
 * @since 2016-7-7 下午1:43:00
 */
public interface IRequest {
    /***
     * POST方式从服务器获取数据/向服务器提交数据
     *
     * @param code
     * @param params
     */
    void post(Code code, IRequestParams params);

    /***
     * GET方式从服务器获取数据/向服务器提交数据
     *
     * @param code
     * @param params
     */
    void get(Code code, IRequestParams params);
}
