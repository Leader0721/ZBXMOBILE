package com.zbxn.init.application;

import android.app.Application;

import com.github.yoojia.anyversion.AnyVersion;
import com.github.yoojia.anyversion.Version;
import com.github.yoojia.anyversion.VersionParser;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.DbUtils.DaoConfig;
import com.lidroid.xutils.DbUtils.DbUpgradeListener;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.zbxn.init.R;
import com.zbxn.init.http.RequestController;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.application.IAppInitControl;
import com.zbxn.pub.http.IRequestControl;
import com.zbxn.pub.http.Response;
import com.zbxn.pub.image.IImageLoader;
import com.zbxn.pub.utils.ConfigUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import utils.DeviceUtils;

public class AppInitControl implements IAppInitControl {

    private Application mContext = null;

    public static boolean DEBUG = false;

    @Override
    public void init(Application context) {
        mContext = context;

        String debug = ConfigUtils.getConfig(ConfigUtils.KEY.debug);
        if(debug != null && debug.equals("true"))
            DEBUG = true;

        initLog();
        getHttpClient();
        initCookieStore();
        initUpdateVersion();
        initDatabase();
    }

    @Override
    public void initLog() {
        Logger.init(mContext.getResources().getString(R.string.app_name))
                .methodCount(2).hideThreadInfo().logLevel(DEBUG ? LogLevel.FULL : LogLevel.NONE)
                .methodOffset(2);
    }

    @Override
    public void initSecurity() {

    }

    @Override
    public IRequestControl getHttpClient() {
        return RequestController.getInstance(mContext);
    }

    @Override
    public void initUpdateVersion() {
        AnyVersion.init(mContext, new VersionParser() {
            @Override
            public Version onParse(String response) {
                final JSONTokener tokener = new JSONTokener(response);
                try {
                    JSONObject json = (JSONObject) tokener.nextValue();
                    if (json.optBoolean(Response.SUCCESS, false)) {
                        json = json.getJSONObject(Response.DATA);
                        Version version = new Version(json
                                .getString("versionname"), json
                                .getString("description"), json
                                .getString("downloadurl"), json
                                .getInt("versioncode"));
                        return version;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    @Override
    public IImageLoader getImageLoader() {
        // TODO 自动生成的方法存根
        return null;
    }

    @Override
    public void initCookieStore() {
        // PersistentCookieStore cookieStore = new
        // PersistentCookieStore(mContext);
        // HttpClient.getInstance().getBase().setCookieStore(cookieStore);
    }

    @Override
    public void initDatabase() {
        // 获取应用程序版本号
        int appVersion = DeviceUtils.getInstance(mContext).getAppVersion();

        DaoConfig config = new DaoConfig(mContext);
		config.setDbName(ConfigUtils.getConfig(ConfigUtils.KEY.privateInfo) + ".db");
        config.setDbVersion(appVersion);
        config.setDbUpgradeListener(new DbUpgradeListener() {

            /** 当程序目录中的数据库版本与当前应用程序的版本不一致时，会调用此方法 */
            @Override
            public void onUpgrade(DbUtils db, int oldVersion, int newVersion) {
            }
        });
        BaseApp.DBLoader = DbUtils.create(config);
        BaseApp.DBLoader.configDebug(DEBUG);
    }

    @Override
    public void initSMS() {
        // TODO 自动生成的方法存根

    }

    @Override
    public void initSocialization() {
        // TODO 自动生成的方法存根

    }

    @Override
    public void initStatistics() {
        // TODO 自动生成的方法存根

    }

}
