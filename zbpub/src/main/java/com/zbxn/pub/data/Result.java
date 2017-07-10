package com.zbxn.pub.data;


import org.json.JSONException;
import org.json.JSONObject;

import utils.StringUtils;

/**
 * 返回结果
 *
 * @author Lenovo
 */
public class Result {
    private String success;//错误代码 0-成功 其他失败
    private String msg;//错误信息
    private String data;//返回结果  error=0时，返回jason形式的结果
    private int total;//查询到数据总数

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static Result parse(String json) {
        System.out.println("json:" + json);
        if (StringUtils.isEmpty(json))
            return null;

        Result result = new Result();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            if (!jsonObject.isNull("success")) {
                result.setSuccess(jsonObject.getString("success"));
            }
            if (!jsonObject.isNull("msg")) {
                result.setMsg(jsonObject.getString("msg"));
            }
            if (!jsonObject.isNull("data")) {
                result.setData(jsonObject.getString("data"));
            }
            if (!jsonObject.isNull("total")) {
                result.setTotal(jsonObject.getInt("total"));
            }
        } catch (JSONException e) {
            System.out.println("Result解析错误：" + e.toString());
        }

        return result;
    }
}
