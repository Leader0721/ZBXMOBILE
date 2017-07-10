package com.zbxn.http;

import android.content.Context;

import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.data.base.BaseAsyncTaskInterface;
import com.zbxn.pub.utils.ConfigUtils;

import java.util.Map;

import utils.PreferencesUtils;

public class HttpRequestUtil {


    public static void RequestGet(Context context, boolean showProgress, String action, Map<String, String> map, BaseAsyncTaskInterface baseAsyncTaskInterface) {
        //将用户输入的账号保存起来，以便下次使用
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);
        String url = server + action;//请求的url地址

        //请求网络
        new BaseAsyncTask(context, showProgress, 0, url, baseAsyncTaskInterface).execute(map);
    }
}
