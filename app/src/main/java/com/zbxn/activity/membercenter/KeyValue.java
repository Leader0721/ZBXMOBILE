package com.zbxn.activity.membercenter;

/**
 * @author LiangHanXin
 */
public class KeyValue {

    public KeyValue(String key, String value, String paramKey) {
        super();
        this.key = key;
        this.value = value;
        this.paramKey = paramKey;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

}