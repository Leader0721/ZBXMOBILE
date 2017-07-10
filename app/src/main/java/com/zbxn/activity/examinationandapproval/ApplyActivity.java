package com.zbxn.activity.examinationandapproval;


import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.umeng.analytics.MobclickAgent;
import com.zbxn.R;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.FragmentAdapter;

import butterknife.BindView;
import widget.smarttablayout.SmartTabLayout;

/**
 * 项目名称：审批主页面
 * 创建人：LiangHanXin
 * 创建时间：2016/10/10 9:04
 */
public class ApplyActivity extends AbsToolbarActivity {

    @BindView(R.id.mSmartTabLayout)
    SmartTabLayout mSmartTabLayout;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    private FragmentAdapter mAdapter;
    private MenuItem mCollect;

    protected static int index = 0;

    @Override
    public int getMainLayout() {
        return R.layout.activity_apply;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateSuccessView();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ApplyActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ApplyActivity");
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("审批");
        return super.onToolbarConfiguration(toolbar, params);
    }

    /**
     * 创建按钮显示
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_schedule_create, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 创建按钮监听
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        mCollect = menu.findItem(R.id.schedule_creat);
        mCollect.setTitle("创建审批表单");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.schedule_creat: //创建审批申请
                Intent intent = new Intent(this, CreatFormActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mAdapter = new FragmentAdapter(getSupportFragmentManager());
        Bundle bundle = null;
        ApplyFragment fragment1 = new ApplyFragment();
        fragment1.setFragmentTitle("我的申请");
        bundle = new Bundle();
        bundle.putInt("types", 101);
        fragment1.setArguments(bundle);
        ApplyFragment fragment2 = new ApplyFragment();
        fragment2.setFragmentTitle("我的审批");
        bundle = new Bundle();
        bundle.putInt("types", 102);
        fragment2.setArguments(bundle);
        ApplyFragment fragment3 = new ApplyFragment();
        fragment3.setFragmentTitle("审批查询");
        bundle = new Bundle();
        bundle.putInt("types", 103);
        fragment3.setArguments(bundle);
        mAdapter.addFragment(fragment1, fragment2, fragment3);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mAdapter);
        mSmartTabLayout.setViewPager(mViewPager);
        mSmartTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                index = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @Override
    public void loadData() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
