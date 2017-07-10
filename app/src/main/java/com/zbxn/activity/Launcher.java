package com.zbxn.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zbxn.activity.login.LoginActivity;
import com.zbxn.bean.Member;
import com.zbxn.listener.ICustomListener;
import com.zbxn.R;
import com.zbxn.pub.frame.mvc.AbsBaseActivity;
import com.zbxn.pub.http.RequestUtils.Code;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import utils.DeviceUtils;
import utils.PreferencesUtils;
import utils.SystemBarTintManager;

public class Launcher extends AbsBaseActivity {

    /**
     * 关闭启动页，打开指定的Activity
     */
    private static final int Flag_Message_OpenActivity = 100;

    /**
     * 启动页停留时间
     */
    private static final int DURATION = 2000;

    /**
     * 记录是否第一次启动
     */
    private static final String SHARE_APP_TAG = "launcher";
    @BindView(R.id.mLogo)
    ImageView mLogo;
    @BindView(R.id.mVersion)
    TextView mVersion;
    @BindView(R.id.launch_layout)
    RelativeLayout launchLayout;
    /**
     * 启动时的系统时间
     */
    private long mStartTime;

    int i = 0;

    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
        setContentView(R.layout.activity_launcher);
        ButterKnife.bind(this);

        ObjectAnimator animator = ObjectAnimator.ofFloat(mLogo, "alpha", 0f, 1.0f);
        animator.setDuration(1600);
        animator.start();

        init();
    }

    @Override
    public void setStatusBarColor(int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //set child View not fill the system window
            ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                ViewCompat.setFitsSystemWindows(mChildView, true);
            }
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.white);
    }

    @Override
    public int getTranslucentColorResource() {
        return android.R.color.white;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Flag_Message_OpenActivity:
                SharedPreferences setting = getSharedPreferences(SHARE_APP_TAG, 0);
                Boolean user_first = setting.getBoolean("FIRST", true);
                if (user_first) {
                    setting.edit().putBoolean("FIRST", false).commit();
                    Intent intent = new Intent(this, Guide.class);
                    startActivityForResult(intent, 1);
                }
                if (!user_first) {
                    Class clazz = (Class) msg.obj;
                    Intent intent = new Intent(this, clazz);
                    startActivity(intent);
                    finish();
                }
                break;

            default:
                break;
        }
        return false;
    }

    @Override
    public void onSuccess(Code code, JSONObject response) {
        openActivity(Main.class);
    }

    @Override
    public void onFailure(Code code, JSONObject errorResponse) {
        openActivity(LoginActivity.class);
    }

    private void init() {

        mVersion.setText("版本 " + DeviceUtils.getInstance(this).getAppVersionName());

        mStartTime = System.currentTimeMillis();

        Member member = Member.getLocalCache();
        if (member == null) {// 本地无缓存，直接登录
            openActivity(LoginActivity.class);
        } else {// 本地有缓存，用缓存登录
            String username = PreferencesUtils.getString(this, LoginActivity.FLAG_INPUT_USERNAME, null);
            String password = PreferencesUtils.getString(this, LoginActivity.FLAG_INPUT_PASSWORD, null);
            member.setPassword(password);
            member.login(this, username, password, new ICustomListener() {
                @Override
                public void onCustomListener(int obj0, Object obj1, int position) {
                    switch (obj0) {
                        case 0:
                            openActivity(Main.class);
                            break;
                        case 1:
                            openActivity(LoginActivity.class);
                            break;
                    }
                }
            });
        }
    }

    /**
     * 打开某个Activity（登录或主页面）
     *
     * @param clazz
     * @author GISirFive
     */
    public void openActivity(Class clazz) {
        Message message = Message.obtain();
        message.what = Flag_Message_OpenActivity;
        message.obj = clazz;
        long delay = System.currentTimeMillis() - mStartTime;
        if (delay < DURATION)
            sendMessageDelayed(message, DURATION - delay);
        else
            sendMessage(message);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            openActivity(LoginActivity.class);
            i--;
        }
        i++;
        if (i == 1) {
            finish();
            i--;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
