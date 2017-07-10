package com.zbxn.init.http;

import android.app.Application;

import com.zbxn.init.application.AppInitControl;
import com.zbxn.init.http.okhttputils.OkHttpUtils;
import com.zbxn.init.http.okhttputils.cookie.store.PersistentCookieStore;
import com.zbxn.init.http.okhttputils.interceptor.LoggerInterceptor;
import com.zbxn.init.http.okhttputils.model.HttpHeaders;
import com.zbxn.init.http.okhttputils.model.HttpParams;
import com.zbxn.pub.http.ICallback;
import com.zbxn.pub.http.IRequestControl;
import com.zbxn.pub.http.IRequestParams;
import com.zbxn.pub.http.RequestUtils;
import com.zbxn.pub.http.RequestUtils.Code;

/**
 * @author GISirFive
 * @since 2016-7-7 上午9:22:35
 */
public class RequestController implements IRequestControl {

    private static Application context = null;
    private static RequestController controller = null;

    private RequestController() {
        init();
    }

    public static IRequestControl getInstance(Application application) {
        context = application;
        if (controller == null)
            controller = new RequestController();
        return controller;
    }

    public OkHttpUtils getBase() {
        return OkHttpUtils.getInstance();
    }

    private void init() {
        HttpHeaders headers = new HttpHeaders();
//		headers.put(
//				"User-Agent",
//				"Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83)"
//						+ "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
        // 必须调用初始化
        OkHttpUtils.init(context);
        // 以下都不是必须的，根据需要自行选择
        OkHttpUtils instance = OkHttpUtils.getInstance()//
                .debug("OkHttpUtils") // 是否打开调试
                .setConnectTimeout(OkHttpUtils.DEFAULT_MILLISECONDS) // 全局的连接超时时间
                .setReadTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS) // 全局的读取超时时间
                .setWriteTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS) // 全局的写入超时时间
                // .setCookieStore(new MemoryCookieStore())
                // cookie使用内存缓存（app退出后，cookie消失）
                .setCookieStore(new PersistentCookieStore())
                // //cookie持久化存储，如果cookie不过期，则一直有效
                .addInterceptor(new LoggerInterceptor("asdasdas", true))
                .addCommonHeaders(headers); // 设置全局公共头
        // .addCommonParams(params); // 设置全局公共参数
        if (AppInitControl.DEBUG)
            instance.debug("OkHttpUtils"); // 是否打开调试
    }

    @Override
    public void post(Code code, IRequestParams params, ICallback callback) {
        String url = RequestUtils.getUrlWithFlag(code);
        OkHttpUtils
                .post(url)
                .params((params == null) ? null : (HttpParams) params
                        .getParams()).execute(new JsonCallback(code, callback));
    }

    @Override
    public void get(Code code, IRequestParams params, ICallback callback) {
        post(code, params, callback);
    }

}
