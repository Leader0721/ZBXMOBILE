package com.zbxn.pub.frame.mvp.base;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by GISirFive on 2016/7/26.
 */
public class ActivityLifeCycleImp implements ActivityLifeCycle {

    private Activity mActivity;

    public ActivityLifeCycleImp(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

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
    public void onDestroy() {

    }

    @Override
    public void onRestart() {

    }
}
