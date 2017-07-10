package com.zbxn.pub.http.filter;

import org.json.JSONObject;

/**
 * 请求结果过滤接口
 * @author ZH
 *
 */
public interface IResponseFilter {
	
	JSONObject check(int statusCode, JSONObject response);
	
}
