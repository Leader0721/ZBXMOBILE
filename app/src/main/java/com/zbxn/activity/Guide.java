package com.zbxn.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.zbxn.R;
import com.zbxn.pub.bean.adapter.GuideAdapter;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvc.AbsToolbarActivity;
import com.zbxn.pub.http.RequestUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import utils.SystemBarTintManager;
import widget.smarttablayout.SmartTabLayout;
import widget.smarttablayout.utils.ViewPagerItem;
import widget.smarttablayout.utils.ViewPagerItemAdapter;
import widget.smarttablayout.utils.ViewPagerItems;

/**
 * 引导页面
 * Created by Administrator on 2016/8/16.
 */
public class Guide extends AbsToolbarActivity {

    @BindView(R.id.guide_viewpager)
    ViewPager guideViewPager;
    @BindView(R.id.guide_button)
    CircularProgressButton guideButton;
    @BindView(R.id.guide_jump)
    TextView guideJump;
    @BindView(R.id.guide_text_title)
    TextView guideTextTitle;
    @BindView(R.id.guide_text_content)
    TextView guideTextContent;
    @BindView(R.id.viewpagertab)
    SmartTabLayout viewpagertab;

    private List<ImageView> images;
    private int[] imageId = {R.mipmap.guide01,/* R.mipmap.guide02,*/
            R.mipmap.guide03, R.mipmap.guide04};
    private GuideAdapter adapter;
    private int before_tag;

    @Override
    public int getMainLayout() {
        return R.layout.activity_guide;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        initView();
        addAdapter();

        updateSuccessView();
    }

    @Override
    public void setStatusBarColor(int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //set child View not fill the system window
            ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                ViewCompat.setFitsSystemWindows(mChildView, true);
            }
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.white);
    }

    @Override
    public void loadData() {

    }

    private void addAdapter() {
        adapter = new GuideAdapter(images);
        guideViewPager.setAdapter(adapter);
        guideViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                if (arg0 != imageId.length - 1) {
                    guideButton.setVisibility(View.GONE);
                } else {
                    guideButton.setVisibility(View.VISIBLE);
                }
                before_tag = arg0;
                switch (arg0) {
                    case 0:
                        guideTextTitle.setText("记录");
                        guideTextContent.setText(R.string.app_guider_page1);
                        break;
                    case 1:
                        guideTextTitle.setText("沟通");
                        guideTextContent.setText(R.string.app_guider_page2);
                        break;
                  /*  case 2:
                        guideTextTitle.setText("高效");
                        guideTextContent.setText(R.string.app_guider_page3);
                        break;*/
                    case 2:
                        guideTextTitle.setText("五的N次方");
                        guideTextContent.setText(R.string.app_guider_page4);
                        break;
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void initView() {
        guideTextTitle.setText("记录");
        guideTextContent.setText(R.string.app_guider_page1);

        images = new ArrayList<>();
        for (int i = 0; i < imageId.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setImageResource(imageId[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            images.add(iv);
        }

        ViewPagerItems pagers = new ViewPagerItems(this);
        for (int i = 0; i < imageId.length; i++) {
            pagers.add(ViewPagerItem.of("", images.size()));
            ViewPagerItemAdapter adapter = new ViewPagerItemAdapter(pagers);
            guideViewPager.setAdapter(adapter);
            viewpagertab.setViewPager(guideViewPager);
        }
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        return false;
    }


    @Override
    public int getTranslucentColorResource() {
        return android.R.color.white;
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public void onSuccess(RequestUtils.Code code, JSONObject response) {

    }

    @Override
    public void onFailure(RequestUtils.Code code, JSONObject error) {

    }

    @OnClick({R.id.guide_jump, R.id.guide_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.guide_jump:
            case R.id.guide_button:
                setResult(1, null);
                finish();
                break;
        }
    }
}
