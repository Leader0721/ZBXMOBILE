package com.zbxn.pub.data;


import com.google.gson.JsonObject;
import com.zbxn.pub.utils.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import utils.StringUtils;

/**
 * 返回结果
 *
 * @author Lenovo
 */
@SuppressWarnings("rawtypes")
public class ResultData<T> {
    private String success;//错误代码 0-成功 其他失败
    private String msg;//错误信息
    private T data;//返回结果  返回单个对象
    private List rows = new ArrayList();//返回结果  返回列表数据
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ResultData parse(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }

        ResultData result = new ResultData();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            if (!jsonObject.isNull("success")) {
                result.setSuccess(jsonObject.getString("success"));
            }
            if (!jsonObject.isNull("msg")) {
                result.setMsg(jsonObject.getString("msg"));
            }
            if (!jsonObject.isNull("data") && JsonUtil.isGoodJson(jsonObject.getString("data"))) {
                Object oJson = new JSONTokener(jsonObject.get("data").toString()).nextValue();
                if (oJson instanceof JSONObject) {
                    result.setData(JsonUtil.fromJsonString(jsonObject.getString("data"), clazz));
                } else if (oJson instanceof JSONArray) {
                    List list = JsonUtil.fromJsonList(jsonObject.getString("data"), clazz);
                    result.getRows().addAll(list);
                }
            }
            if (!jsonObject.isNull("rows") && JsonUtil.isGoodJson(jsonObject.getString("rows"))) {
                List list = JsonUtil.fromJsonList(jsonObject.getString("rows"), clazz);
                result.getRows().addAll(list);
            }
            if (!jsonObject.isNull("total")) {
                result.setTotal(jsonObject.getInt("total"));
            }
        } catch (JSONException e) {
            System.out.println("ResultData解析错误：" + e.toString());
        }

        return result;
    }

    public ResultData parse(JsonObject jsonObject, Class<T> clazz) {
        if (null == jsonObject) {
            return null;
        }

        ResultData result = new ResultData();
        try {
            if (null != jsonObject.get("success")) {
                result.setSuccess(jsonObject.get("success").toString());
            }
            if (null != jsonObject.get("msg")) {
                result.setMsg(jsonObject.get("msg").toString());
            }
            if (null != jsonObject.get("data")) {
                //判断是实体还是列表
                Object oJson = new JSONTokener(jsonObject.get("data").toString()).nextValue();
                if (oJson instanceof JSONObject) {
                    result.setData(JsonUtil.fromJsonString(jsonObject.get("data").toString(), clazz));
                } else if (oJson instanceof JSONArray) {
                    List list = JsonUtil.fromJsonList(jsonObject.get("data").toString(), clazz);
                    result.getRows().addAll(list);
                }
            }
            if (null != jsonObject.get("rows")) {
                List list = JsonUtil.fromJsonList(jsonObject.get("rows").toString(), clazz);
                result.getRows().addAll(list);
            }
            if (null != jsonObject.get("total")) {
                result.setTotal(jsonObject.get("total").getAsInt());
            }
        } catch (Exception e) {
            System.out.println("ResultData解析错误：" + e.toString());
        }

        return result;
    }
}
