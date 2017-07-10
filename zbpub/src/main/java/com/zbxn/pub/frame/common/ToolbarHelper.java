package com.zbxn.pub.frame.common;

import com.zbxn.pub.R;
import com.zbxn.pub.application.BaseApp;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityManagerCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Activity和Toolbar的关联类
 *
 * @author GISirFive
 */
public class ToolbarHelper {

    private final TextView mTitle;
    /**
     * 上下文，创建view的时候需要用到
     */
    private Context mContext;

    /* 视图构造器 */
    private LayoutInflater mInflater;

    /**
     * 根布局
     */
    private ViewGroup mRootView;

    /**
     * 根布局
     */
    public ViewGroup getRootView() {
        return mRootView;
    }

    /**
     * toolbar的布局文件
     */
    private View mToolbarView;

    /**
     * toolbar的布局文件
     */
    public View getToolbarView() {
        return mToolbarView;
    }

    /**
     * 用户定义的view
     */
    private View mContentView;

    /**
     * 用户定义的view
     */
    public View getContentView() {
        return mContentView;
    }

    private CoordinatorLayout.LayoutParams mContentParams;

    private AppBarLayout mAppBarLayout;

    private Toolbar mToolBar;

    public Toolbar getToolBar() {
        return mToolBar;
    }

    private IToolBar mController;

    private ToolbarParams mToolbarParams;

    /**
     * Toolbar是否显示
     */
    private boolean mToolbarEnable = true;

    /**
     * Toolbar是否显示
     */
    public boolean isToolbarEnable() {
        return mToolbarEnable;
    }

    /**
     * 两个属性 1、toolbar是否悬浮在窗口之上 2、toolbar的高度获取
     */
    private static int[] ATTRS = {R.attr.windowActionBarOverlay,
            R.attr.actionBarSize};

    public ToolbarHelper(Context context, int layoutId, IToolBar controller) {
        this.mContext = context;
        this.mController = controller;
        mInflater = LayoutInflater.from(mContext);
        //每个页面都会初始化一个Toolbar
        mToolbarView = mInflater.inflate(R.layout.pub_toolbar_default, null);
        mToolBar = (Toolbar) mToolbarView.findViewById(R.id.mToolbar);
        mTitle = ((TextView) mToolbarView.findViewById(R.id.toolbarTitle));


        mAppBarLayout = (AppBarLayout) mToolbarView.findViewById(R.id.mAppBarLayout);

        if (mController != null) {
            initToolbarParams();
            mToolbarEnable = mController.onToolbarConfiguration(mToolBar, mToolbarParams);
            setShadowEnable(mToolbarParams.shadowEnable);
        }
        if (mToolBar.getTitle() != null) {
            mTitle.setText(mToolBar.getTitle().toString());
        }
        mToolBar.setTitle("");
        if (mToolBar.getNavigationIcon() != null) {
            mToolBar.setNavigationIcon(R.mipmap.back);
        }

        //Toolbar不可用，将其父布局设置为不可见
        if (!mToolbarEnable) {
            setToolbarViewVisibility(View.GONE);
        }

        /* 初始化整个内容 */
        initRootView();

        /* 初始化用户定义的布局 */
        initContentView(layoutId);
        /* 初始化toolbar */
        initToolBar();
    }

    private void initToolbarParams() {
        mToolbarParams = new ToolbarParams();
        TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(ATTRS);
        /* 获取主题中定义的悬浮标志 */
        mToolbarParams.overlay = typedArray.getBoolean(0, false);
        /* 获取主题中定义的toolbar的高度 */
        mToolbarParams.toolBarSize = (int) typedArray.getDimension(1, (int) mContext.getResources()
                .getDimension(R.dimen.abc_action_bar_default_height_material));
        typedArray.recycle();
    }

    /* 初始化根布局 */
    private void initRootView() {
        /* 直接创建一个帧布局，作为视图容器的父容器 */
        mRootView = new CoordinatorLayout(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        // 适应沉浸式状态栏，若不设置，应用的UI会顶上去，顶进system UI
        mRootView.setFitsSystemWindows(true);
        mRootView.setLayoutParams(params);
    }

    /* 初始化内容布局 */
    private void initContentView(int layoutId) {
        mContentView = mInflater.inflate(layoutId, mRootView, false);
        mContentParams = new CoordinatorLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        /* 如果是悬浮状态，则不需要设置间距 */
        mContentParams.topMargin = (!mToolbarEnable || mToolbarParams.overlay) ? 0 : mToolbarParams.toolBarSize;
        mRootView.addView(mContentView, mContentParams);
    }

    /* 初始化Toolbar的布局 */
    private void initToolBar() {
        if (mToolbarParams.colorResId != 0)
            mToolBar.setBackgroundResource(mToolbarParams.colorResId);
        if (mToolbarEnable) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mRootView.addView(mToolbarView, params);
        }
    }

    /**
     * 设置Toolbar的背景色
     *
     * @param resId
     * @author GISirFive
     */
    public void setBackgroudColor(int resId) {
        if (mToolBar == null)
            return;
        mToolBar.setBackgroundResource(resId);
    }

    /**
     * 设置Toolbar是否悬浮
     *
     * @param b
     * @author GISirFive
     */
    public void setOverlay(boolean b) {
        if (mToolBar == null)
            return;
        mContentParams.topMargin = b ? 0 : mToolbarParams.toolBarSize;
    }

    /**
     * 设置toolbar透明度
     *
     * @param f 0-1
     * @author GISirFive
     */
    public void setToolbarAlpha(float f) {
        if (mToolBar == null)
            return;
        mToolBar.setAlpha(f);
    }

    public void setShadowEnable(boolean b) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (b) {
//                mAppBarLayout.setElevation(12f);
                mAppBarLayout.setElevation(1f);
            }else{
                mAppBarLayout.setElevation(0);
            }
        }
    }

    /**
     * 设置Toolbar的父布局是否可见
     *
     * @param visibility
     */
    public void setToolbarViewVisibility(int visibility) {
        mToolbarView.setVisibility(visibility);
    }

    /**
     * 设置标题(居中显示)
     *
     * @param title
     */
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    /**
     * toolbar操作接口
     *
     * @author GISirFive
     * @since 2016-1-21 上午8:55:53
     */
    public interface IToolBar {

        /**
         * 自定义Toolbar相关的操作<br>
         * <b>自定义Toolbar中的布局:</b><br>
         * getLayoutInflater().inflate(R.layout.pub_toobar_1, toolbar);
         *
         * @param toolbar Toolbar的实例
         * @param param   设置Toolbar的一些属性
         * @return <b>true</b>-使用Toolbar &nbsp <b>false</b>-不使用Toolbar(默认)
         * @author GISirFive
         */
        boolean onToolbarConfiguration(Toolbar toolbar,
                                       ToolbarParams param);

    }

}
