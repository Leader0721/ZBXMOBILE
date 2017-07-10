package com.zbxn.pub.http;

public class Response {

	/** 请求成功标识 */
	public final static int RESULT_OK = 0;
	/** 请求失败标识1 */
	public final static int RESULT_ERROR = 1;
	/** 请求失败标识2 */
	public final static int RESULT_ERROR2 = 2;

	/** 请求成功/失败标识 */
	public final static String SUCCESS = "success";

	/** 消息 */
	public final static String MSG = "msg";

	/** 对应单个数据的实体类 */
	public final static String DATA = "data";

	/** 对应多个数据的实体类列表 */
	public final static String ROWS = "rows";

	public final static String KEY_LOGOUT_FLAG = "logoutFlag";
}
