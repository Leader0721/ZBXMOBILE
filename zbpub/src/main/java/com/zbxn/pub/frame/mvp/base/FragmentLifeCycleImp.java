package com.zbxn.pub.frame.mvp.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

/**
 * Created by GISirFive on 2016/7/26.
 */
public class FragmentLifeCycleImp implements FragmentLifeCycle {

    private Fragment mFragment;

    public FragmentLifeCycleImp(Fragment fragment) {
        this.mFragment = fragment;
    }

    @Override
    public void onAttach(Context context) {
        Logger.d("onAttach--->被加入到activity中");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.d("onCreateView--->activity要得到fragment的layout");
        return null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Logger.d("onActivityCreated--->当activity的onCreate()方法返回后调用此方法");
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroyView() {
        Logger.d("onDestroyView--->fragment的layout被销毁");
    }

    @Override
    public void onDestroy() {

    }

    /**
     * Fragment无此生命周期方法
     */
    @Deprecated
    @Override
    public void onRestart() {

    }

    @Override
    public void onDetach() {
        Logger.d("onDetach--->当fragment被从activity中删掉时被调用");
    }
}
