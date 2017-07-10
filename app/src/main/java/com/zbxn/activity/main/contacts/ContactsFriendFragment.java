package com.zbxn.activity.main.contacts;

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
 * 通讯录按拼音排序
 *
 * @author GISirFive
 * @time 2016/8/8
 */
public class ContactsFriendFragment extends AbsBaseFragment {

    public void setFragmentTitle(String title) {
        mTitle = title;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_contacts_friend, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

}
