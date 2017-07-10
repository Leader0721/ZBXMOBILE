package com.zbxn.activity.main;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.activity.Main;
import com.zbxn.activity.main.contacts.ContactsFragment;
import com.zbxn.activity.main.member.MemberCenterPager;
import com.zbxn.activity.main.message.messagenew.MessageCenterNewPager;
import com.zbxn.activity.main.tools.ToolsCenterPager;
import com.zbxn.pub.frame.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PagerFragmentManager implements OnClickListener {

    private Main mMain;

    @BindView(R.id.textview_title)
    TextView mTitle;

    @BindView(R.id.layout_bottombar)
    ViewGroup mBottomBar;

    private ViewGroup[] mBottomContainer = null;
    /**
     * 不同模块的标题
     */
    private String[] mTitles = null;

    /**
     * Fragment集合
     */
    private BaseFragment[] mFragments = new BaseFragment[4];
    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager mFragmentManager;

    /**
     * 当前页面索引位置
     */
    private int mCurrentIndex;

    @BindColor(R.color.main_tab_text_gray)
    int normalColor;

    @BindColor(R.color.main_tab_text_blue)
    int checkColor;

    public PagerFragmentManager(Main main) {
        this.mMain = main;
        ButterKnife.bind(this, main.getWindow().getDecorView());
        initView();
        setTabSelection(0);
    }

    private void initView() {
        mFragmentManager = mMain.getSupportFragmentManager();

        List<ViewGroup> layoutList = new ArrayList<>();
        for (int i = 0; i < mBottomBar.getChildCount(); i++) {
            View v = mBottomBar.getChildAt(i);
            if (v instanceof ViewGroup)
                layoutList.add((ViewGroup) v);
        }
        mBottomContainer = new ViewGroup[layoutList.size()];
        layoutList.toArray(mBottomContainer);
        mTitles = new String[layoutList.size()];
        // 底部导航栏绑定点击事件
        for (int i = 0; i < mBottomContainer.length; i++) {
            ViewGroup vg = mBottomContainer[i];
            vg.setOnClickListener(this);
            // 每个模块的标题
            for (int j = 0; j < vg.getChildCount(); j++) {
                if (vg.getChildAt(j) instanceof TextView) {
                    TextView title = (TextView) vg.getChildAt(j);
                    mTitles[i] = title.getTag().toString();
                    break;
                }
            }
        }
    }

    /**
     * 底部功能菜单点击
     */
    @Override
    public void onClick(View v) {
        int position = 0;
        for (int i = 0; i < mBottomContainer.length; i++) {
            ViewGroup layout = mBottomContainer[i];
            ImageView imgTitle = (ImageView) layout.getChildAt(0);
            TextView tvTitle = (TextView) layout.getChildAt(1);
            int textColor = checkColor;
            int imageId = getImageResource(i, false);
            if (v == layout) {
                position = i;
                imageId = getImageResource(i, true);
            } else {
                textColor = normalColor;
            }
            tvTitle.setTextColor(textColor);
            imgTitle.setImageResource(imageId);
        }
        setTabSelection(position);
    }

    /**
     * 获取图片资源ID
     *
     * @param position 位置
     * @param b        是否选中
     * @return
     */
    private int getImageResource(int position, boolean b) {
        switch (position) {
            case 0:
                return b ? R.mipmap.bg_main_message_check
                        : R.mipmap.bg_main_message_uncheck;
            case 1:
                return b ? R.mipmap.bg_main_tools_check
                        : R.mipmap.bg_main_tools_uncheck;
            case 2:
                return b ? R.mipmap.bg_main_contact_check
                        : R.mipmap.bg_main_contact_uncheck;
            case 3:
                return b ? R.mipmap.bg_main_membercenter_check
                        : R.mipmap.bg_main_membercenter_uncheck;
            default:
                return R.mipmap.ic_launcher;
        }
    }

    /**
     * 取得当前显示的Fragment
     */
    public BaseFragment getSelectedPage() {
        for (BaseFragment fragment : mFragments) {
            if (fragment == null)
                continue;
            if (!fragment.isHidden())
                return fragment;
        }
        return null;
    }

    /**
     * 取得Fragment
     */
    public BaseFragment getFragment(int index) {
        if (mFragments.length > index)
            return mFragments[index];
        return null;
    }

    /**
     * 获取当前页面的索引
     *
     * @return
     */
    public int getCurrentPageIndex() {
        return mCurrentIndex;
    }

    /**
     * 此方法专供刷新推送消息使用
     *
     * @param index
     */
    public void setSelection(int index) {
        setTabSelection(index);
        /*if (index == 0) {
            MessageCenterNewPager fragment = (MessageCenterNewPager) mFragments[0];
            fragment.showRefresh(0);
        }*/
    }

    /**
     * 设置当前显示的Fragment
     *
     * @param index 需要显示的Fragment的位置索引
     */
    private void setTabSelection(int index) {
        if (index >= mFragments.length)
            return;
        mCurrentIndex = index;
        // 开启一个Fragment事务
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        BaseFragment fragment = mFragments[index];
        if (fragment == null) {
            switch (index) {
                case 0:
//                    fragment = new MessageCenterPagerNew();

                    //根据以后需求进行变化
                    fragment = new MessageCenterNewPager();
                    break;
                case 1:
                    fragment = new ToolsCenterPager();
                    break;
                case 2:
//                    fragment = new AddressBookPager();
                    fragment = new ContactsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 0);
                    fragment.setArguments(bundle);
                    break;
                case 3:
                    fragment = new MemberCenterPager();
                    break;
            }
            mFragments[index] = fragment;
            transaction.add(R.id.framlayout_content, fragment, fragment.mTitle);
        } else {
            // 如果Fragment不为空，则直接将它显示出来
            transaction.show(fragment);
        }
        transaction.commit();
        // Fragment继承了状态监听
//        if (fragment instanceof IStatusCatcher)
//            ((IStatusCatcher) fragment).onFocused(fragment);

        changeTitle(mTitles[index]);
        changeToolbarStatus(index);
        mMain.invalidateOptionsMenu();
    }

    /**
     * 隐藏所有Fragment
     *
     * @param transaction
     */
    private void hideFragments(FragmentTransaction transaction) {
        for (BaseFragment fragment : mFragments) {
            if (fragment == null)
                continue;
            transaction.hide(fragment);
        }
    }

    /**
     * 切换页面时，顶部标题随着改变
     *
     * @param title
     * @author GISirFive
     */
    private void changeTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            title = mMain.getResources().getString(R.string.app_name);
        }
        mTitle.setText(title);
    }

    /**
     * 改变Toolbar的状态
     *
     * @param position
     * @author GISirFive
     */
    public void changeToolbarStatus(int position) {
        /*if (position == 1 || position == 2) {
            mMain.getToolbarHelper().setShadowEnable(false);
        } else {
            mMain.getToolbarHelper().setShadowEnable(true);
        }*/
        if (position == 2) {
            mMain.getToolbarHelper().setShadowEnable(false);
        } else {
            mMain.getToolbarHelper().setShadowEnable(true);
        }
    }
}
