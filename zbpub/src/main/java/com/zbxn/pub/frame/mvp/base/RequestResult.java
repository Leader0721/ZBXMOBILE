package com.zbxn.pub.frame.mvp.base;

import android.os.Bundle;

/**
 * 请求结果
 * Created by GISirFive on 2016/7/27.
 */
public class RequestResult {

    public RequestResult(int success) {
        this.success = success;
    }

    public RequestResult(int success, Object obj1) {
        this.success = success;
        this.obj1 = obj1;
    }

    public RequestResult(int success, String message) {
        this.success = success;
        this.message = message;
    }

    /** 请求成功/失败 */
    public int success = 0;
    public Object obj1 = null;
    public Object obj2 = null;
    public String message = null;

    private Bundle data;

    public Bundle getData() {
        return data;
    }

    public void setData(Bundle data) {
        this.data = data;
    }
}
