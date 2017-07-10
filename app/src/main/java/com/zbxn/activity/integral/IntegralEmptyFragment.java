package com.zbxn.activity.integral;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zbxn.R;
import com.zbxn.pub.frame.mvp.AbsBaseFragment;

import butterknife.ButterKnife;

/**
 * Created by wj on 2016/11/23.
 */
public class IntegralEmptyFragment extends AbsBaseFragment {

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_integral_empty, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {
    }

    public void setFragmentTitle(String title) {
        mTitle = title;
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

}
