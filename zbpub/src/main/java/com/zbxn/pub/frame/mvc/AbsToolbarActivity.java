package com.zbxn.pub.frame.mvc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbxn.pub.R;
import com.zbxn.pub.frame.common.ToolbarParams;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * 封装ToolBar，支持自定义ToolBar的布局</br> 自动加入ToolBar，无需在Activity和其布局文件中定义ToolBar</br>
 *
 * @author GISirFive
 * @since 2015-12-11 下午3:49:16
 */
public abstract class AbsToolbarActivity extends AbsBaseActivity {

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setContentInsetsRelative(0, 0);
        return true;
    }

    public LayoutInflater m_inflater;

    public LinearLayout m_layoutMain; // 内容部分位置
    public View m_viewMain; // 内容部分
    public View m_viewPromptLoading; // 正在加载
    public View m_viewPromptLoadingError; // 加载失败

    private TextView m_textEmptyTip;

    /**
     * 获得主界面layout资源
     *
     * @return
     */
    public abstract int getMainLayout();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置键盘默认隐藏
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.base_activity);
        initBaseView();

        ButterKnife.bind(this);

        EventBus.getDefault().register(this);

        loadData();
    }

    /**
     * 初始化控件
     */
    private void initBaseView() {
        m_inflater = getLayoutInflater();

        m_layoutMain = (LinearLayout) findViewById(R.id.view_main);

        // 注册正在加载及加载失败控件
        m_viewPromptLoading = (View) findViewById(R.id.prompt_loading);
        m_viewPromptLoadingError = (View) findViewById(R.id.prompt_loading_error);

        // 加载内容视图
        m_viewMain = m_inflater.inflate(getMainLayout(), null);

        // 将标题及内容视图加载到控件中
        m_layoutMain.addView(m_viewMain, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        m_layoutMain.addView(m_viewMain, new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT));

        m_textEmptyTip = (TextView) findViewById(R.id.m_empty_tip);
        m_textEmptyTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initFrame();
                loadData();
            }
        });

        initFrame();
    }

    /**
     * 回归到初始界面效果
     */
    public void initFrame() {
        m_layoutMain.setVisibility(View.INVISIBLE);
        m_viewPromptLoading.setVisibility(View.VISIBLE);
        m_viewPromptLoadingError.setVisibility(View.INVISIBLE);
    }

    /**
     * 显示加载失败界面
     */
    public void updateErrorView() {
        m_layoutMain.setVisibility(View.INVISIBLE);
        m_viewPromptLoading.setVisibility(View.INVISIBLE);
        m_viewPromptLoadingError.setVisibility(View.VISIBLE);
    }

    /**
     * 显示加载成功界面
     */
    public void updateSuccessView() {
        m_layoutMain.setVisibility(View.VISIBLE);
        m_viewPromptLoading.setVisibility(View.INVISIBLE);
        m_viewPromptLoadingError.setVisibility(View.INVISIBLE);
    }

    /**
     * 设置空内容
     *
     * @param tip 标题内容
     */
    public void setEmpty_tip(String tip) {
        m_textEmptyTip.setText(tip);
    }

    /**
     * 取消方法体
     */
    public void onBtnCancel() {
        finish();
    }

    /**
     * 标题栏左上角按钮事件
     *
     * @param v
     */
    public void back(View v) {
        onBtnCancel();
    }

    /**
     * 加载失败后点击重新加载
     */
    public abstract void loadData();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBtnCancel();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void jumpActivity(Intent intent) {
        startActivity(intent);
//        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}