package com.zbxn.pub.frame.base;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.zbxn.pub.R;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.frame.common.IHandleMessage;
import com.zbxn.pub.frame.common.SwipeBackHelper;
import com.zbxn.pub.frame.common.ToolbarHelper;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.common.TranslucentHelper;
import com.zbxn.pub.frame.mvp.base.ActivityLifeCycle;
import com.zbxn.pub.frame.mvp.base.ActivityLifeCycleImp;
import com.zbxn.pub.http.HttpCallBack;
import com.zbxn.pub.http.UtilHttpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import utils.PreferencesUtils;
import utils.StringUtils;
import utils.SystemBarTintManager;

/**
 * 对Activity的封装，集成消息传递、Toolbar、沉浸式状态栏等
 * Created by GISirFive on 2016/7/28.
 */
public abstract class BaseActivity extends AppCompatActivity implements Handler.Callback,
        IHandleMessage, TranslucentHelper.ITranslucent, ToolbarHelper.IToolBar, SwipeBackHelper.ISwipeBack {

    private ActivityLifeCycle mLifeCycle = new ActivityLifeCycleImp(this);

    /**
     * Toolbar操作类
     */
    private ToolbarHelper mToolbarHelper;

    public ToolbarHelper getToolbarHelper() {
        return mToolbarHelper;
    }

    private SwipeBackHelper mSwipeBackHelper;

    private Handler mHandler;

    private List<Call> m_listRequest;

    private List<Call> getListRequest() {
        if (m_listRequest == null) {
            m_listRequest = new ArrayList<>();
        }
        return m_listRequest;
    }

    /**
     * 调用网络请求
     *
     * @param map
     * @param action
     * @param httpCallBack
     */
    public void callRequest(Map<String, String> map, String action, HttpCallBack httpCallBack) {
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        if (!StringUtils.isEmpty(ssid)) {
            map.put("tokenid", ssid);
        }
        Call call = UtilHttpRequest.getIResource().GetRequest(action, map);
        call.enqueue(httpCallBack);
        addRequest(call);
    }

    /**
     * 添加到请求队列，以便销毁
     *
     * @param call
     */
    public void addRequest(Call call) {
        getListRequest().add(call);
    }

    public final Handler getHandler() {
        if (mHandler == null)
            mHandler = new Handler(this);
        return mHandler;
    }

    @Override
    public void sendMessage(int what) {
        getHandler().sendEmptyMessage(what);
    }

    @Override
    public void sendMessage(Message message) {
        getHandler().sendMessage(message);
    }

    @Override
    public void sendMessageDelayed(int what, long delayMillis) {
        getHandler().sendEmptyMessageDelayed(what, delayMillis);
    }

    @Override
    public void sendMessageDelayed(Message message, long delayMillis) {
        getHandler().sendMessageDelayed(message, delayMillis);
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams param) {
        return false;
    }

    @Override
    public boolean translucent() {
        return false;
    }

    @Override
    public int getTranslucentColorResource() {
        return R.color.app_colorPrimaryDark;
    }

    @Override
    public boolean getSwipeBackEnable() {
        return false;
    }

    @Override
    public void scrollToFinish() {
        if (mSwipeBackHelper != null)
            mSwipeBackHelper.scrollToFinish();
        else
            finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLifeCycle.onCreate(savedInstanceState);
        if (getSwipeBackEnable()) {
            mSwipeBackHelper = new SwipeBackHelper(this);
            mSwipeBackHelper.onActivityCreate();
            mSwipeBackHelper.onPostCreate();
        }
        if (translucent())
            new TranslucentHelper(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        mToolbarHelper = new ToolbarHelper(this, layoutResID, this);
        setContentView(mToolbarHelper.getRootView());
        Toolbar mToolbar = mToolbarHelper.getToolBar();
        // 把toolbar设置到Activity中
        if (mToolbar != null)
            setSupportActionBar(mToolbar);
        // 自定义的一些操作
        // onCreateCustomToolBar(toolbar);
        setStatusBarColor(R.color.statusBar_color);
    }

    /**
     * 自定义状态栏颜色 适配4.4及以上
     */
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
        tintManager.setStatusBarTintResource(resId);
    }

//    @Override
//    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
//        super.onPostCreate(savedInstanceState, persistentState);
//        if(mSwipeBackHelper != null)
//            mSwipeBackHelper.onPostCreate();
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLifeCycle.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLifeCycle.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLifeCycle.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLifeCycle.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消网络请求
        if (m_listRequest != null && m_listRequest.size() > 0) {
            for (Call call : m_listRequest) {
                call.cancel();
            }
        }
        mLifeCycle.onDestroy();
    }
}
