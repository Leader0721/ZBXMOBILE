package com.zbxn.activity.main.member;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.zbxn.R;
import com.zbxn.bean.Member;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.data.ResultData;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.http.HttpCallBack;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.MyToast;
import com.zbxn.pub.widget.ProgressWebView;

import java.net.URLDecoder;

import butterknife.BindView;
import utils.PreferencesUtils;

/**
 * 抽奖
 *
 * @author: ysj
 * @date: 2016-10-11 14:08
 */
public class ShopLuckActivity extends AbsToolbarActivity {


    @BindView(R.id.mWebView)
    ProgressWebView mWebView;

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("抽奖");
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    public int getMainLayout() {
        return R.layout.activity_shopluck;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        //显示加载成功界面
        updateSuccessView();

    }

    @Override
    public void loadData() {

    }

    private void initView() {
        mWebView.getSettings().setDefaultFontSize(14);
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
//        1、手机向html 选完通讯录人员传  javascript:getMessageFromApp('{'userid':'','name':''}')
//        2、html向手机 fromJsMessage:1    1--跳转到通讯录选人   2--提交成功关闭页面
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //view.loadUrl(url);
                if (url.startsWith("sharefromhtml:")) {//弹出消息
                    new ShareAction(ShopLuckActivity.this)
                            .withTitle("五的N次方")
                            .withText("享受工作的每一分")
                            .withMedia(new UMImage(ShopLuckActivity.this, R.mipmap.ic_launcher))
                            .withTargetUrl("http://n.zbzbx.com/Login/Login/Register?SharerNO=" + Member.get().getNumber())
                            .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                            .setCallback(umShareListener).open();
                    return true;
                } else if (url.startsWith("alertfromhtml:")) {//弹出消息
                    try {
                        String msg = URLDecoder.decode(url.substring(14).toString(), "UTF-8");
                        MyToast.showToast(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyToast.showToast("请输入正确格式");
                    }
                    return true;
                } else if (url.contains("jsp_error")) {//弹出消息
                    MyToast.showToast("获取数据异常");
                    finish();
                    return true;
                } else {
                    return true;
                }

                // return super.shouldOverrideUrlLoading(view, url);
            }
        });


        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        mWebView.loadUrl(server + "/view/luckshop/shopluck.jsp?tokenid=" + ssid);
    }

    @Override
    public boolean getSwipeBackEnable() {
//        return super.getSwipeBackEnable();
        return true;
    }


    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            MyToast.showToast(platform + " 分享成功啦");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            MyToast.showToast(platform + " 分享失败啦");
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(Share.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
            Log.e("cancael", "platform" + platform);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        Log.d("result", "onActivityResult");
    }

}
