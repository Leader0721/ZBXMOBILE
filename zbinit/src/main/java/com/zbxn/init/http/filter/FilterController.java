package com.zbxn.init.http.filter;

import com.zbxn.pub.http.filter.IResponseFilter;

import org.json.JSONObject;

import okhttp3.Headers;

public class FilterController {

	private static FilterController controller = null;

	private IResponseFilter mFlagFilter = null;
	private IResponseFilter mLogoutFilter = null;
	
	private FilterController() {
		mFlagFilter = new FlagFilter();
		mLogoutFilter = new LogoutFilter();
	}
	
	private static FilterController getInstance(){
		if(controller == null)
			controller = new FilterController();
		return controller;
	}
	
	/**
	 * 数据过滤器，对返回数据进行异常处理
	 * @param statusCode
	 * @param headers
	 * @param response
	 * @return
	 */
	public static JSONObject check(int statusCode, Headers headers, JSONObject response) {
		FilterController controller = getInstance();
		
		response = controller.mFlagFilter.check(statusCode, response);
		response = controller.mLogoutFilter.check(statusCode, response);
		
		return response;
	}
	
	
	
	
}
