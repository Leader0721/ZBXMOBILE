package com.zbxn.init.http.filter;

import org.json.JSONException;
import org.json.JSONObject;

import com.zbxn.pub.http.Response;
import com.zbxn.pub.http.filter.IResponseFilter;

/**
 * 处理数据中没有{@link ResponseFlag}的情况
 * 
 * @author ZH
 * 
 */
public class FlagFilter implements IResponseFilter {

	@Override
	public JSONObject check(int statusCode, JSONObject response) {
		try {
			if (response == null || response.equals("{}")
					|| response.equals("null")) {
				response = new JSONObject();
				response.put(Response.SUCCESS, false);
			}
			// 若不包含success，则插入false
			if (!response.has(Response.SUCCESS)) {
				response.put("success", false);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return response;
	}

}
