package com.zbxn.pub.application;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDex;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lidroid.xutils.DbUtils;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.zbxn.pub.http.IRequestControl;
import com.zbxn.pub.http.IRequestParams;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.FileAccessor;
import com.zbxn.pub.utils.ImageLoaderConfig;

import cn.jpush.android.api.JPushInterface;
import utils.PreferencesUtils;
import utils.StringUtils;

public class BaseApp extends Application {

    public static Context CONTEXT = null;

    private static IRequestControl RequestControl = null;

    public static DbUtils DBLoader = null;

    private IAppInitControl mInitController = null;


    //友盟分享三方平台appkey
    {
        PlatformConfig.setWeixin("wx9bb363fafd76660e", "b399ae72821cf2b61cd31adeacee7130");
        //PlatformConfig.setSinaWeibo("", "");
//        PlatformConfig.setQQZone("1105740972", "qtrJRc4zRjfHE8Jp");
        PlatformConfig.setQQZone("1105735303", "WUYGMADwPBRSw0HB");
    }

    // 百度地图定位
    private LocationClient mLocationClient;

    public static Context getContext() {
        if (CONTEXT != null)
            return CONTEXT.getApplicationContext();
        else
            return new BaseApp().getApplicationContext();
    }

    @Override
    public void onCreate() {
        CONTEXT = this;
//		Class launcher = null;
        try {
//			launcher = Class.forName("com.zbxn.init.activity.Launcher");
            Class<?> clazz = Class
                    .forName("com.zbxn.init.application.AppInitControl");
            mInitController = (IAppInitControl) clazz.newInstance();
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        ImageLoaderConfig.initImageLoader(this);

        FileAccessor.initFileAccess();

        mInitController.init(this);
        // 初始化网络请求
        RequestControl = mInitController.getHttpClient();

        //极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        // 注册百度地图定位事件
        mLocationClient = new LocationClient(this);
        setLocationOption();

        //友盟分享
        UMShareAPI.get(this);
//        Config.REDIRECT_URL = "您新浪后台的回调地址";


        super.onCreate();
    }

    /**
     * 获取该应用公共缓存路径
     *
     * @return
     * @author GISirFive
     */
    public static String getDiskCachePath() {
        return Environment.getExternalStorageDirectory() + "/SeaDreams/";
    }

    /**
     * 获取网络请求的参数操作接口
     */
    public static IRequestParams getRequestParams() {
        try {
            Class<?> clazz = Class
                    .forName("com.zbxn.init.http.HttpRequestParams");
            return (IRequestParams) clazz.newInstance();
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 网络请求接口
     */
    public static IRequestControl getHttpClient() {
        return RequestControl;
    }

    /**
     * 获取mLocationClient是否启动
     *
     * @return
     */
    public boolean isStartedLocationClient() {
        return mLocationClient.isStarted();
    }

    /**
     * 启动定位
     *
     * @param listener
     */
    public void startLocationClient(BDLocationListener listener) {
        mLocationClient.registerLocationListener(listener);
        mLocationClient.start();
    }

    /**
     * 停止定位
     */
    public void stopLocationClient(BDLocationListener listener) {
        mLocationClient.unRegisterLocationListener(listener);
        mLocationClient.stop();
    }

    /**
     * 重新定位
     *
     * @param listener
     */
    public void requestLocationClient(BDLocationListener listener) {
        mLocationClient.registerLocationListener(listener);
        mLocationClient.requestLocation();
    }

    // 设置相关参数
    public void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
//		option.setScanSpan(600000);// 设置发起定位请求的间隔时间为5000ms
        // option.setScanSpan(3000);// 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);
    }

    //解决在Android Studio 运行的时候报E/dalvikvm: Could not find class ‘xxx’,
    //但是在android5.0以上不会报此错误能运行成功。
    //minifyEnabled false 意思是 是否进行混淆
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
