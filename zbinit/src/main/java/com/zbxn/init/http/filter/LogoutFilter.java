package com.zbxn.init.http.filter;

import com.zbxn.pub.http.Response;
import com.zbxn.pub.http.filter.IResponseFilter;

import org.json.JSONObject;

/**
 * 登录超时过滤器
 * 
 * @author ZH
 * 
 */
public class LogoutFilter implements IResponseFilter {

	public LogoutFilter() {

	}

	@Override
	public JSONObject check(int statusCode, JSONObject response) {
		// 判断登录状态并设置自动登录
		if (isLogout(statusCode, response)) {
			/*// 登录超时，自动登录
			Member member = Member.getLocalCache();
			if (member != null) {// 本地有缓存，用缓存登录
				Member.login(member, null);
			}
			// 重新定义json数据
			try {
				response = new JSONObject();
				response.put(Response.SUCCESS, false);
				response.put(Response.MSG, "请求失败，请重新刷新页面");
			} catch (Exception e) {
				e.printStackTrace();
			}*/
		}
		return response;
	}

	/***
	 * 根据返回的statusCode 和 response 判断登录状态
	 * 
	 * @param statusCode
	 *            状态码
	 * @param response
	 *            返回内容
	 * @return true:已退出登录 <br>
	 *         false:已登录
*/
private boolean isLogout(int statusCode, JSONObject response) {
		// 标识符存在
		boolean hasFlag = response.has(Response.KEY_LOGOUT_FLAG);
		// 网关超时代码 504
		return hasFlag && statusCode == 504;
		}
		}
