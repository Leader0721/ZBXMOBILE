package com.github.yoojia.anyversion;

/**
 * Created by Yoojia.Chen
 * yoojia.chen@gmail.com
 * 2015-01-04
 */
public interface VersionParser {
    /**
     * 将服务端返回的版本数据解析为版本对象
     * @param response 服务端返回的数据
     */
    Version onParse(String response);
}
