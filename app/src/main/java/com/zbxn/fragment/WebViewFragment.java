package com.zbxn.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zbxn.R;
import com.zbxn.pub.frame.mvc.AbsBaseFragment;
import com.zbxn.pub.http.RequestUtils;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.widget.ProgressWebView;

import org.json.JSONObject;

/**
 * @author GISirFive
 * @time 2016/8/17
 */
public class WebViewFragment extends AbsBaseFragment {

    private ProgressWebView mWebView;

    private String mBaseUrl;
    private String mMimType = "text/html";
    private String mEncoding = "utf-8";
    private String mFailUrl = null;

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_webview, container, false);
        return root;
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {
        mWebView = (ProgressWebView) root.findViewById(R.id.mProgressWebView);
        mBaseUrl = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
        mMimType = "text/html";
        mEncoding = "utf-8";
        mFailUrl = null;

        mWebView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        mWebView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        mWebView.getSettings().setBuiltInZoomControls(true);
        // 不显示webview缩放按钮
        mWebView.getSettings().setDisplayZoomControls(false);
        // 扩大比例的缩放
        mWebView.getSettings().setUseWideViewPort(true);
        // 自适应屏幕
        mWebView.getSettings().setLayoutAlgorithm(
                WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                // return super.shouldOverrideUrlLoading(view, url);
                return false;
            }
        });
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

    public ProgressWebView getWebView() {
        return mWebView;
    }

    /**
     * 根据URL自动加载数据
     * @param url
     */
    public void reloadDataWithUrl(String url) {

    }

    /**
     * 加载数据
     * @param content 要加载到webview中的数据
     */
    public void reloadDataWithContent(String content) {
        mWebView.loadDataWithBaseURL(mBaseUrl, content, mMimType, mEncoding, mFailUrl);
    }
}
