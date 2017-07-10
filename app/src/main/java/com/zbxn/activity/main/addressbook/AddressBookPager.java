package com.zbxn.activity.main.addressbook;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zbxn.R;
import com.zbxn.activity.main.addressbook.addressbook.AddressBookPresenter;
import com.zbxn.activity.main.addressbook.addressbook.IAddressBookView;
import com.zbxn.fragment.AddrBookGroupFragment;
import com.zbxn.fragment.AddrBookPyFragment;
import com.zbxn.pub.frame.mvp.AbsBaseFragment;
import com.zbxn.pub.frame.mvp.base.LoadingController;
import com.zbxn.pub.frame.mvp.base.LoadingControllerImp;
import com.zbxn.pub.utils.FragmentAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import utils.ScrimUtil;
import widget.smarttablayout.SmartTabLayout;

/**
 * @author GISirFive
 * @time 2016/8/8
 */
public class AddressBookPager extends AbsBaseFragment implements IAddressBookView {

    @BindView(R.id.mSmartTabLayout)
    SmartTabLayout mSmartTabLayout;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    private FragmentAdapter mAdapter;
    private LoadingControllerImp mLoading;
    private AddressBookPresenter mPresenter;

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.main_addressbook, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {
        mPresenter = new AddressBookPresenter(this);
        mPresenter.loadData();

        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            View shader = mRootView.findViewById(R.id.mTopShader);
            Drawable drawable = ScrimUtil.makeCubicGradientScrimDrawable(
                    Color.parseColor("#28000000"), //颜色
                    8, Gravity.TOP);//渐变层数,起始方向
            shader.setBackground(drawable);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public LoadingController loading() {
        if (mLoading == null) {
            mLoading = new LoadingControllerImp(getActivity());
        }
        return mLoading;
    }

    @Override
    public void initViewPager() {
        mAdapter = new FragmentAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        AddrBookPyFragment fragment1 = new AddrBookPyFragment();
        AddrBookGroupFragment fragment2 = new AddrBookGroupFragment();
        mAdapter.addFragment(fragment1, fragment2);
        mSmartTabLayout.setViewPager(mViewPager);
    }


    /**
     * 是否初始化完成
     * @return
     */
    public boolean completeInit(){
        return (mLoading != null) && !mLoading.isShowing();
    }
}
