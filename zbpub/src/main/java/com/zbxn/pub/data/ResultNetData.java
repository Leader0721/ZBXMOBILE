package com.zbxn.pub.data;


import com.google.gson.JsonObject;
import com.zbxn.pub.utils.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import utils.StringUtils;

/**
 * 返回结果
 *
 * @author Lenovo
 */
@SuppressWarnings("rawtypes")
public class ResultNetData<T> {
    private String success;//错误代码 0-成功 其他失败
    private String msg;//错误信息
    private List data = new ArrayList();//返回结果  返回单个对象
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

    public List getData() {
        return data;
    }

    public void setData(List data) {
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

    public ResultNetData parse(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }

        ResultNetData result = new ResultNetData();
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
                List list = JsonUtil.fromJsonList(jsonObject.getString("data"), clazz);
                result.getData().addAll(list);
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

    public ResultNetData parse(JsonObject jsonObject, Class<T> clazz) {
        if (null == jsonObject) {
            return null;
        }

        ResultNetData result = new ResultNetData();
        try {
            if (null != jsonObject.get("success")) {
                result.setSuccess(jsonObject.get("success").toString());
            }
            if (null != jsonObject.get("msg")) {
                result.setMsg(jsonObject.get("msg").toString());
            }
            if (null != jsonObject.get("data")) {
                List list = JsonUtil.fromJsonList(jsonObject.get("rows").toString(), clazz);
                result.getRows().addAll(list);
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
