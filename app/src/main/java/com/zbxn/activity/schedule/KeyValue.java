package com.zbxn.activity.schedule;

/**
 * 项目名称：ZBXMobile
 * 创建人：LiangHanXin
 * 创建时间：2016/9/27 15:16
 */
public class KeyValue {

    public KeyValue(String key, String value) {
        super();
        this.key = key;
        this.value = value;

    }

    private String key;
    private String value;
    /**
     * 对应网络请求传的参数的Key
     */
    private String paramKey;

    public String getKey() {

        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public String getParamKey() {
        return paramKey;
    }


}
