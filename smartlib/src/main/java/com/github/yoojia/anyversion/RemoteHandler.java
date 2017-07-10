package com.github.yoojia.anyversion;

import java.io.IOException;

/**
 * Created by Yoojia.Chen
 * yoojia.chen@gmail.com
 * 2015-01-05
 * 远程请求处理接口
 */
public abstract class RemoteHandler implements Runnable {

    private String url;
    private VersionParser parser;
    private Callback callback;

    final void setOptions(String url, VersionParser parser, Callback callback){
        this.url =  url;
        this.parser = parser;
        this.callback = callback;
    }

    @Override
    final public void run() {
        Version version = null;
        try{
            version = parser.onParse(request(this.url));
        }catch (Exception ex){ /* Nothing */ }
        callback.onVersion(version,false);
    }

    /**
     * 处理连接服务器的请求，并返回内容
     * @param url 服务器地址
     * @return 服务器返回的内容
     */
    public abstract String request(String url) throws IOException;

}
