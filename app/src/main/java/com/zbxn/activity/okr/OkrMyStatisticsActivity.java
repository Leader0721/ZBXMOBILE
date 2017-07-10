package com.zbxn.activity.okr;


import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zbxn.R;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.widget.ProgressWebView;

import butterknife.BindView;
import utils.PreferencesUtils;

/**
 * 项目名称：个人积分获取情况统计
 * 创建人：LiangHanXin
 * 创建时间：2016/11/16 9:46
 */
public class OkrMyStatisticsActivity extends AbsToolbarActivity {

    @BindView(R.id.mWebview)
    ProgressWebView mWebview;

    String url = "";
    //查看图标类型（0为折线图，其他为柱状图）
    String type = "0";

    @Override
    public int getMainLayout() {
        return R.layout.activity_okr_my_statistics;
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("个人积分获取情况统计");// 定义上面的名字
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateSuccessView();

        //将用户输入的账号保存起来，以便下次使用
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);
        url = server + "oaOKRCommonStatMonth/findPersonalOKRScore.do?tokenid=" + ssid + "&type=" + type;

        WebSettings settings = mWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebview.setWebViewClient(new MyWebViewClient());
        mWebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebview.loadUrl(url);
    }

    /**
     * 创建按钮显示
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_okr_my_histpoly, menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * 创建按钮的点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mOkrHistogramPoly:
                if ("0".equals(type)) {
                    item.setTitle("折线");
                    type = "1";
                } else {
                    item.setTitle("柱状");
                    type = "0";
                }
                //将用户输入的账号保存起来，以便下次使用
                String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
                String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);
                url = server + "oaOKRCommonStatMonth/findPersonalOKRScore.do?tokenid=" + ssid + "&type=" + type;
                mWebview.loadUrl(url);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadData() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }


    @Override
    public boolean getSwipeBackEnable() {
        return true;
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
