package com.zbxn.pub.utils;

public class ConfigUtils {

    public enum KEY {
        /**
         * 服务器地址
         */
        server("http://192.168.1.112:8060/ZBXMobile/"),
//        server("http://n.zbzbx.com:8088/ZBXMobile/"),
//        server("http://www.zbzbx.com:8181/ZBXMobile/"),
//        server("http://www.tt8ss.com:8080/ZBXMobile/"),
        /**
         * Web端服务器地址  由。net接口提供
         */
//        NetServer("http://192.168.1.117/webapi/"),
//        NetServer("http://192.168.1.112:8080/webapi/"),
//        NetServer("http://n.zbzbx.com/webapi/"),//正式
        //        NetServer_ACTION("http://n.zbzbx.com/webapi_action/"),//正式
        NetServer_ACTION("http://test.zbzbx.com/webapi_action/"),//测试
        NetServer("http://test.zbzbx.com/webapi/"),//测试

        /**
         * Web端服务器地址
         */
        webServer("http://n.zbzbx.com/"),
        /**
         * 配置文件名
         */
        privateInfo("zbzbx"),
        /**
         * 默认每一页可以装载的信息数量
         */
        pageSize(12),
        /**
         * 是否为调试模式
         */
        debug(true),
        /**
         * 用户信息
         */
        userInfo("tempInfo"),
        /**
         * 数据库版本
         */
        dbVersion(100);

        Object value;

        KEY(Object value) {
            this.value = value;
        }
    }

    public static final String getConfig(KEY key) {
        switch (key) {
//		case server:
//			return AESUtils.decode(BUNDLE.getString(name.toString()));
            default:
                return String.valueOf(key.value);
        }
    }

}