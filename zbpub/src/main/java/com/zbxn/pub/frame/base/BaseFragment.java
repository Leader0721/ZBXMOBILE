package com.zbxn.pub.frame.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.frame.common.IHandleMessage;
import com.zbxn.pub.frame.mvp.base.FragmentLifeCycle;
import com.zbxn.pub.frame.mvp.base.FragmentLifeCycleImp;
import com.zbxn.pub.http.HttpCallBack;
import com.zbxn.pub.http.UtilHttpRequest;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import utils.PreferencesUtils;
import utils.StringUtils;

/**
 * Created by GISirFive on 2016/8/1.
 */
public abstract class BaseFragment extends android.support.v4.app.Fragment implements IHandleMessage,
        Handler.Callback {

    /** fragment的根布局 */
    protected View mRootView= null;

    /** 标题 */
    public String mTitle = null;

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    private FragmentLifeCycle mLifeCycle = new FragmentLifeCycleImp(this);

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
    public void onAttach(Context context) {
        super.onAttach(context);
        mLifeCycle.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mLifeCycle.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mLifeCycle.onCreateView(inflater, container, savedInstanceState);
        //布局重复利用
        if(mRootView == null)
            mRootView = onCreateView(inflater, container);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLifeCycle.onActivityCreated(savedInstanceState);
        //初始化
        initialize(mRootView, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mLifeCycle.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mLifeCycle.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mLifeCycle.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mLifeCycle.onStop();
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
        mLifeCycle.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消网络请求
        if (m_listRequest != null && m_listRequest.size() > 0) {
            for (Call call : m_listRequest) {
                call.cancel();
            }
        }
        mLifeCycle.onDestroy();
        mRootView = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLifeCycle.onDetach();
    }

    /**
     * 为该Fragment绑定View<br>
     * 该方法会在{@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}中执行<br>
     * 该方法执行后，会立即执行{@link #initialize(View)}方法
     *
     * @param inflater
     * @param container
     * @return
     */
    protected abstract View onCreateView(LayoutInflater inflater, ViewGroup container);

    /**
     * 初始化Fragment相关操作<br>
     * 该方法会在{@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}中执行<br>
     * 在执行此方法前，程序会先执行{@link #onCreateView(LayoutInflater, ViewGroup)}
     *
     * @param root 该Fragment绑定的View
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    protected abstract void initialize(View root, @Nullable Bundle savedInstanceState);
}
