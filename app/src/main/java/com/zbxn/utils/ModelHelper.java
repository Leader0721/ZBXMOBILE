package com.zbxn.utils;

import android.content.Intent;

import com.zbxn.activity.Main;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.Bulletin;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.bean.WorkBlog;
import com.zbxn.pub.frame.mvp.base.RequestResult;
import com.zbxn.pub.http.RequestUtils;
import com.zbxn.pub.http.Response;
import com.zbxn.pub.utils.JsonUtil;
import com.zbxn.pub.utils.MyToast;

import org.json.JSONObject;

import java.util.List;

/**
 * MVP模式下，Model层业务处理工具类
 *
 * @author GISirFive
 * @time 2016/8/9
 */
public class ModelHelper {

    /**
     * 封装请求成功时的数据
     *
     * @param code
     * @param response
     * @return
     */
    public static RequestResult getResultSuccess(RequestUtils.Code code, JSONObject response) {
        if (response == null) {
            return new RequestResult(1, "请求数据失败");
        }
        if ("-1".equals(response.optString(Response.SUCCESS, "1"))) {
            //被踢出登录
            MyToast.showToast(response.optString(Response.MSG));

            Intent intent = new Intent(BaseApp.getContext(), Main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("isExit",true);
            BaseApp.getContext().startActivity(intent);
            return new RequestResult(-1, "请求数据失败,请重新登录");
        }
        if (!"0".equals(response.optString(Response.SUCCESS, "1"))) {
//        if (!response.optBoolean(Response.SUCCESS, false)) {
            return getResultFailure(code, response);
        }
        RequestResult result = new RequestResult(0);
        //分离出三种数据类型
        String rows = response.optString(Response.ROWS, null);
        String data = response.optString(Response.DATA, null);
        String message = response.optString(Response.MSG, null);
        try {
            switch (code) {
                case APP_UPDATE:
                    break;
                case MOBILE_EXIST:
                    break;
                case USER_LOGIN:
                    break;
                case USER_LOGOUT:
                    break;
                case USER_REGISTER:
                    break;
                case USER_UPDATE:
                    result.message = message;
                    break;
                case USER_DETAIL:
                    break;
                case USER_RESET_PASSWORD:
                    break;
                case WORKBLOG_LIST_TOP:
                    break;
                case WORKBLOG_LIST_BOTTOM:
                    break;
                case WORKBLOG_CHECKTODAY: {//查询用户当天是否写了日志
                    WorkBlog blog = JsonUtil.fromJsonString(data, WorkBlog.class);
                    result.obj1 = blog;
                }
                break;
                case WORKBLOG_SAVE:
                    break;
                case BULLETIN_LIST_TOP:
                case BULLETIN_LIST_BOTTOM: {
                    List<Bulletin> list = JsonUtil.fromJsonList(rows, Bulletin.class);
                    result.obj1 = list;
                }
                break;
                case BULLETIN_DETAIL:
                    break;
                case BULLETIN_SETREAD:
                    result.message = "标记已读成功";
                    break;
                case ALERT_COLLECT:
                    result.message = message;
                    break;
                case ADDRESSBOOK_LIST: {
                    List<Contacts> list = JsonUtil.fromJsonList(rows, Contacts.class);
                    result.obj1 = list;
                }
                break;
                case COLLECT_LIST_TOP:
                case COLLECT_LIST_BOTTOM:
                    List<Bulletin> list = JsonUtil.fromJsonList(rows, Bulletin.class);
                    result.obj1 = list;
                    break;
            }
        } catch (Exception e) {
            result.success = 1;
            result.message = "请求数据失败";
        }
        return result;
    }

    /**
     * 封装请求失败时的数据
     *
     * @param code
     * @param error
     * @return
     */
    public static RequestResult getResultFailure(RequestUtils.Code code, JSONObject error) {
        if ("-1".equals(error.optString(Response.SUCCESS, "1"))) {
            //被踢出登录
            MyToast.showToast(error.optString(Response.MSG));

            Intent intent = new Intent(BaseApp.getContext(), Main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("isExit",true);
            BaseApp.getContext().startActivity(intent);
            return new RequestResult(-1, "请求数据失败,请重新登录");
        }
        RequestResult result = new RequestResult(1);
        if (error == null) {
            result.message = "请求失败";
            return result;
        }
        //请求失败时的消息提示
        String message = error.optString(Response.MSG, "请求失败");
        result.message = message;
        try {
            switch (code) {
                case APP_UPDATE:
                    break;
                case MOBILE_EXIST:
                    break;
                case USER_LOGIN:
                    break;
                case USER_LOGOUT:
                    break;
                case USER_REGISTER:
                    break;
                case USER_UPDATE:
                    break;

                case USER_DETAIL:
                    break;
                case USER_RESET_PASSWORD:
                    break;
                case WORKBLOG_LIST_TOP:
                case WORKBLOG_LIST_BOTTOM:
                    break;
                case WORKBLOG_CHECKTODAY:
                    break;
                case WORKBLOG_SAVE:
                    break;
                case BULLETIN_LIST_TOP:
                    break;
                case BULLETIN_LIST_BOTTOM:
                    break;
                case BULLETIN_DETAIL:
                    break;
                case BULLETIN_SETREAD:
                    break;
                case ADDRESSBOOK_LIST:
                    break;
                case COLLECT_LIST_TOP:
                case COLLECT_LIST_BOTTOM:
                    break;
            }
        } catch (Exception e) {
            result.success = 1;
            result.message = "请求数据失败";
        }
        return result;
    }

}
