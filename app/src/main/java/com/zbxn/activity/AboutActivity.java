package com.zbxn.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.umeng.analytics.MobclickAgent;
import com.zbxn.R;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;

import butterknife.BindView;

public class AboutActivity extends AbsToolbarActivity {

    @BindView(R.id.web_about)
    WebView webAbout;

    String url = "http://www.zbzbx.com/zms_app_intro/ZBX_introduce.aspx";

    @Override
    public int getMainLayout() {
        return R.layout.activity_about;
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("关于智博星");
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webAbout.loadUrl(url);
        WebSettings settings = webAbout.getSettings();
        settings.setJavaScriptEnabled(true);
        webAbout.setWebViewClient(new MyWebViewClient());

        updateSuccessView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void loadData() {

    }

    @Override
    public boolean getSwipeBackEnable() {
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webAbout.canGoBack()) {
            webAbout.goBack();
            return true;
        }
        if (!webAbout.canGoBack()) {
            finish();
        }
        return false;
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    // 监听 所有点击的链接，如果拦截到我们需要的，就跳转到相对应的页面。
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return super.shouldOverrideUrlLoading(view, url);
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageFinished(view, url);

        }
    }

}
