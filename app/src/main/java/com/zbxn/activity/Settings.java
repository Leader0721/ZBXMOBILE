package com.zbxn.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.yoojia.anyversion.AnyVersion;
import com.github.yoojia.anyversion.Callback;
import com.github.yoojia.anyversion.NotifyStyle;
import com.github.yoojia.anyversion.Version;
import com.zbxn.R;
import com.zbxn.pub.dialog.MessageDialog;
import com.zbxn.pub.dialog.ProgressDialog;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvc.AbsToolbarActivity;
import com.zbxn.pub.http.RequestUtils;
import com.zbxn.pub.http.RequestUtils.Code;
import com.zbxn.pub.utils.MyToast;

import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import utils.DeviceUtils;
import utils.FileUtils;

public class Settings extends AbsToolbarActivity {

    /**
     * 检查缓存
     */
    private static final int Flag_Message_LoadCache = 1001;

    /**
     * 清理缓存
     */
    private static final int Flag_Message_ClearCache = 1002;

    /**
     * 检查更新
     */
    private static final int Flag_Message_CheckUpdate = 1003;

    /**
     * 消息_检查更新
     */
    private static final int Flag_Message_SendCheckUpdate = 1004;

    @BindView(R.id.mCacheSum)
    TextView mCacheSum;
    @BindView(R.id.mVersion)
    TextView mVersion;
    @BindView(R.id.mModifyPassword)
    LinearLayout mModifyPassword;
    @BindView(R.id.mMessageSetting)
    LinearLayout mMessageSetting;

    private ProgressDialog mProgressDialog;


    @Override
    public int getMainLayout() {
        return R.layout.activity_settings;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getHandler();
        init();

        updateSuccessView();
    }

    @Override
    public void loadData() {

    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("设置");
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Flag_Message_LoadCache:
                if (msg.obj == null) {
                    mCacheSum.setText("0 MB");
                } else {
                    double cachesize = Double.valueOf(msg.obj + "");
                    String result = String.format("%.2f", cachesize);
                    mCacheSum.setText(result + " MB");
                }
                break;
            case Flag_Message_ClearCache:
                showToast("清理完成", Toast.LENGTH_SHORT);
                loadCache();
                break;
            case Flag_Message_CheckUpdate:
                boolean hasNewVersion = (msg.arg1 == 1);
                if (!hasNewVersion) {
//                    showToast("当前已是最新版本", Toast.LENGTH_SHORT);
                    MyToast.showToast("当前已是最新版本");
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                break;
            case Flag_Message_SendCheckUpdate:
                AnyVersion version = AnyVersion.getInstance(Settings.this);
                version.setURL(RequestUtils.getUrlWithFlag(Code.APP_UPDATE));
                version.setCallback(new Callback() {
                    @Override
                    public void onVersion(Version version, boolean notVersion) {
                        Message msg = Message.obtain();
                        msg.what = Flag_Message_CheckUpdate;
                        msg.obj = version;
                        msg.arg1 = notVersion ? 1 : 0;
                        sendMessage(msg);
                    }

                });
                if (!DeviceUtils.getInstance(this).hasInternet()) {//检查是否联网
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                        MyToast.showToast("网络异常，请检查网络连接");
                    }
                }
                version.check(NotifyStyle.Dialog);
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onSuccess(Code code, JSONObject response) {

    }

    @Override
    public void onFailure(Code code, JSONObject error) {

    }

    @OnClick({R.id.mClearCache, R.id.mCheckUpdate, R.id.mModifyPassword,R.id.mMessageSetting})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mClearCache:
                showClearCacheDialog();
                break;
            case R.id.mCheckUpdate:
                mProgressDialog.show("正在检查更新...");
                sendMessageDelayed(Flag_Message_SendCheckUpdate, 3000);
                break;
            case R.id.mModifyPassword://修改密码
                startActivity(new Intent(Settings.this, ModifyPassword.class));
                break;
            case R.id.mMessageSetting://消息设置
                //对当前的网络状态进行一个检查
                if (isNetworkAvailable(Settings.this)){
                    startActivity(new Intent(Settings.this, MessageSettings.class));
                }else {
                    MyToast.showToast("网络无连接状态不可进行消息设置");
                }
                break;
        }
    }


    /**
     * 检查当前网络是否可用
     *
     * @param activity
     * @return
     */

    public boolean isNetworkAvailable(Activity activity)
    {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void init() {
        mProgressDialog = new ProgressDialog(this);
//        mProgressDialog.setCancelable(false);

        mVersion.setText("版本 "
                + DeviceUtils.getInstance(this).getAppVersionName());

        // 设置缓存信息
        loadCache();
    }

    // 加载缓存大小
    private void loadCache() {
        new Thread() {
            public void run() {
                File file = Glide.getPhotoCacheDir(Settings.this);
                if (!file.exists())
                    return;
                double size = FileUtils.sizeOfDirectory(file);
                size = size / 1024 / 1024;
                Message message = Message.obtain();
                message.what = Flag_Message_LoadCache;
                message.obj = size;
                sendMessage(message);
            }
        }.start();
    }

    // 清理缓存
    private void clearCache() {
        new Thread() {
            public void run() {
                Glide.get(Settings.this).clearDiskCache();
                Message message = Message.obtain();
                message.what = Flag_Message_ClearCache;
                sendMessage(message);
            }
        }.start();
    }

    /**
     * 滑动关闭当前Activitiv
     */
    public boolean getSwipeBackEnable() {
        //return super.getSwipeBackEnable();
        return true;
    }

    private void showClearCacheDialog() {
        MessageDialog dialog = new MessageDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("你确定要清除缓存吗？");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                clearCache();
            }
        });
        dialog.setNegativeButton("取消");
        dialog.show();

    }



}
