package com.zbxn.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.umeng.analytics.MobclickAgent;
import com.zbxn.R;
import com.zbxn.bean.Member;
import com.zbxn.fragment.AddrBookGroupFragment;
import com.zbxn.fragment.BlogCenterFragment;
import com.zbxn.fragment.addrbookgroup.OnContactsPickerListener;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.bean.WorkBlog;
import com.zbxn.pub.dialog.MessageDialog;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.ListviewUpDownHelper;

import java.util.List;

import butterknife.BindView;

/**
 * 日志中心
 */
public class WorkBlogCenter extends AbsToolbarActivity implements OnContactsPickerListener, BlogCenterFragment.CallbackBlog {

    /**
     * 回调_创建日志
     */
    private static final int Flag_Callback_CreateWorkBlog = 1001;

    @BindView(R.id.mDrawerLayout)
    DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mDrawerToggle;

    private AddrBookGroupFragment mContactsFragment;
    private BlogCenterFragment mBlogCenterFragment;

    private MenuItem mMenuItem;
    private MessageDialog mMessageDialog;
    private WorkBlog mWorkBlog;
    private ListviewUpDownHelper mListviewHelper;

    @Override
    public int getMainLayout() {
        return R.layout.activity_workblogcenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        //显示加载成功界面
        updateSuccessView();
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("WorkBlogCenter");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("WorkBlogCenter");
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("我的日志");
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_workblog, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.blog_switch: //切换他人日志
                if (mDrawerLayout.isDrawerOpen(mContactsFragment.getView())) {
                    mDrawerLayout.closeDrawer(mContactsFragment.getView());
                } else {
                    mDrawerLayout.openDrawer(mContactsFragment.getView());
                }
                break;
            case R.id.blog_creat: //创建日程
                switch (Member.get().getBlogStateToday()) {
                    case -1://未查询
                        openCreateWorkBlog(null);
                        break;
                    case 0://未写
                        openCreateWorkBlog(null);
                        break;
                    case 1://已写
                        mMessageDialog = new MessageDialog(this);
                        mMessageDialog.setTitle("提示");
                        mMessageDialog.setMessage(getResources().getString(
                                R.string.app_main_blogcenter_message));
                        mMessageDialog.setNegativeButton("取消");
                        mMessageDialog.setPositiveButton("编辑",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        openCreateWorkBlog(mWorkBlog);
                                    }

                                });
                        mMessageDialog.show();
                        break;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // 打开创建日志页面
    private void openCreateWorkBlog(WorkBlog workBlog) {
        Intent intent = new Intent(this, CreateWorkBlog.class);
        // 将当天日志传入
        if (workBlog != null) {
            intent.putExtra(CreateWorkBlog.Flag_Input_Blog, workBlog);
        }
        startActivityForResult(intent, Flag_Callback_CreateWorkBlog);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Flag_Callback_CreateWorkBlog:
                // 保存/更新日志成功
                if (resultCode == Activity.RESULT_OK) {
                    mBlogCenterFragment.refresh();
                }
                break;

            default:
                break;
        }
    }

    private void init() {
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(false);
//        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name);
//        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mContactsFragment = (AddrBookGroupFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mContactsFragment);
        mContactsFragment.setOnContactsPickerListener(this);
        mBlogCenterFragment = (BlogCenterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mBlogCenterFragment);
    }




    @Override
    public void onSelectedContacts(List<Contacts> list, boolean containsLoginUser) {
        //关闭侧滑页
        if (mDrawerLayout.isDrawerOpen(mContactsFragment.getView())) {
            mDrawerLayout.closeDrawer(mContactsFragment.getView());
        }
        //加载日志
        Contacts contacts = list.get(0);
        mBlogCenterFragment.refreshDataByUserId(contacts.getId());
        if (containsLoginUser) {//我的日志
            getToolbarHelper().setTitle("我的日志");
        } else {//别人的日志
            getToolbarHelper().setTitle(contacts.getUserName() + "的日志");
//            mBlogCenterFragment.hideAddButton();
        }

    }





    @Override
    public void sendMessage(WorkBlog workBlog, ListviewUpDownHelper listviewHelper) {
        mWorkBlog = workBlog;
    }

}
