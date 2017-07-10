package com.zbxn.activity.examinationandapproval;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.zbxn.R;
import com.zbxn.activity.main.contacts.ContactsChoseActivity;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.MyToast;
import com.zbxn.pub.widget.ProgressWebView;

import java.net.URLDecoder;

import butterknife.BindView;
import butterknife.OnClick;
import utils.PreferencesUtils;

/**
 * 表单详情
 *
 * @author: ysj
 * @date: 2016-10-11 14:08
 */
public class ApplyCreatActivity extends AbsToolbarActivity {

    private static final int Flag_Callback_ContactsPicker3 = 1003;//审批人
    private static final int Flag_Callback_ContactsPicker4 = 1004;//交接人
    @BindView(R.id.mWebView)
    ProgressWebView mWebView;
    @BindView(R.id.bottom)
    LinearLayout bottom;

    private ApprovalPresenter mPresenter;

    private String typeId = "";

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("创建表单");
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    public int getMainLayout() {
        return R.layout.activity_applydetail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        typeId = getIntent().getStringExtra("typeId");

        initView();

        updateSuccessView();
    }

    @Override
    public void loadData() {

    }

    private void initView() {
        mPresenter = new ApprovalPresenter(this);

        bottom.setVisibility(View.GONE);

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
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
//        1、手机向html 选完通讯录人员传  javascript:getMessageFromApp('{'userid':'','name':''}')
//        2、html向手机 fromJsMessage:1    1--跳转到通讯录选人   jsp_success--提交成功关闭页面  3--跳转到通讯录选交接人
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //view.loadUrl(url);
                Intent intent;
                if ("fromjsmessage:1".equals(url)) {//1--跳转到通讯录选人
                    //跳转到通讯录
                    intent = new Intent(ApplyCreatActivity.this, ContactsChoseActivity.class);
                    intent.putExtra("type", 2);//0-查看 1-多选 2-单选
                    startActivityForResult(intent, Flag_Callback_ContactsPicker3);
                    return true;
                } else if ("fromjsmessage:3".equals(url)) {//3--跳转到通讯录选交接人
                    //跳转到通讯录
                    intent = new Intent(ApplyCreatActivity.this, ContactsChoseActivity.class);
                    intent.putExtra("type", 2);//0-查看 1-多选 2-单选
                    startActivityForResult(intent, Flag_Callback_ContactsPicker4);
                    return true;
                } else if (url.contains("jsp_success")) {//jsp_success--提交成功关闭页面
                    MyToast.showToast("创建成功");
                    intent = new Intent(getApplicationContext(), ApplyActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
//                    setResult(RESULT_OK, intent);
//                    finish();
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
                } else if (url.contains("jsp_error")) {//jsp_success--提交失败
                    MyToast.showToast("获取数据异常");
                    return true;
                } else {
                    return true;
                }
                // return super.shouldOverrideUrlLoading(view, url);
            }
        });

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        mWebView.loadUrl(server + "/forms/form.do?tokenid=" + ssid + "&type=" + typeId);
    }

    @OnClick({R.id.applyEnd, R.id.applyNext, R.id.applyStop})
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

    /**
     * 回调
     */
    private ICustomListener mICustomListener = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            switch (obj0) {
                case 0:
                    MyToast.showToast("修改成功");
                    break;
                case 1:
                    MyToast.showToast("修改失败");
                    break;
            }
        }
    };

    /**
     * 回调需要接收的人员
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Flag_Callback_ContactsPicker3) {//审批人
            if (data != null) {
                if (resultCode == RESULT_OK) {
                    String remarkName = data.getStringExtra("name");
                    int id = data.getIntExtra("id", 0);
                    //android调用js中的getMessageFromApp方法
                    mWebView.loadUrl("javascript:getMessageFromApp('{\"toUserId\":\"" + id + "\",\"toUserName\":\"" + remarkName + "\"}')");
                }
            }
        } else if (requestCode == Flag_Callback_ContactsPicker4) {//交接人
            if (data != null) {
                if (resultCode == RESULT_OK) {
                    String remarkName = data.getStringExtra("name");
                    int id = data.getIntExtra("id", 0);
                    //android调用js中的getMessageFromApp方法
                    mWebView.loadUrl("javascript:getMessageFromAppJiaojie('{\"toUserId\":\"" + id + "\",\"toUserName\":\"" + remarkName + "\"}')");
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
