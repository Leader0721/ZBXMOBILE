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
import android.widget.TextView;

import com.zbxn.listener.ICustomListener;
import com.zbxn.R;
import com.zbxn.bean.ApprovalInfoEntity;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.MyToast;
import com.zbxn.pub.widget.ProgressWebView;

import java.net.URLDecoder;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import utils.PreferencesUtils;
import utils.StringUtils;

/**
 * 表单详情
 *
 * @author: ysj
 * @date: 2016-10-11 14:08
 */
public class ApplyDetailActivity extends AbsToolbarActivity {

    private static final int Flag_ApplyForm_Approval = 1000;// 同意并转审
    private static final int Flag_ApplyForm_Inquire = 1001;// 同意并结束
    private static final int Flag_ApplyForm_Rejected = 1003;// 驳回
    private static final int Flag_ApplyForm_Stop = 1004;// 终止

    private static final int Flag_State_4 = 4; //撤回
    private static final int Flag_State_5 = 5; //催办

    @BindView(R.id.mWebView)
    ProgressWebView mWebView;
    @BindView(R.id.applyEnd)
    TextView applyEnd;
    @BindView(R.id.applyNext)
    TextView applyNext;
    @BindView(R.id.applyStop)
    TextView applyStop;
    @BindView(R.id.view_next)
    View viewNext;
    @BindView(R.id.view_stop)
    View viewStop;
    @BindView(R.id.bottom)
    LinearLayout bottom;

    private int flag = -1;
    private String approvalID;
    private ApprovalPresenter mPresenter;

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("申请单详情");
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    public int getMainLayout() {
        return R.layout.activity_applydetail;
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
        mPresenter = new ApprovalPresenter(this);
        Intent intent = getIntent();
        approvalID = intent.getStringExtra("approvalID");
        flag = intent.getIntExtra("flag", -1);
//        int appState = -1;
//        if (intent.getStringExtra("appState") != null) {
//            appState = Integer.decode(intent.getStringExtra("appState"));
//        }

        switch (flag) {
            case Flag_ApplyForm_Approval:
                applyStop.setVisibility(View.GONE);
                viewStop.setVisibility(View.GONE);
                applyEnd.setText("撤回");
                applyNext.setText("催办");
                break;
            case Flag_ApplyForm_Inquire:
                applyNext.setVisibility(View.GONE);
                viewNext.setVisibility(View.GONE);
                applyStop.setVisibility(View.GONE);
                viewStop.setVisibility(View.GONE);
                applyEnd.setText("终止");
                break;
        }

        mPresenter.getInfoType(this, approvalID, mICustomListener);


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
                if ("fromjsmessage:1".equals(url)) {//1--跳转到通讯录选人
                    return true;
                } else if ("fromjsmessage:2".equals(url)) {//2--提交成功关闭页面
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
        mWebView.loadUrl(server + "/oaApproveInfoController/selectApproveByid.do?tokenid=" + ssid + "&infoid=" + approvalID);
    }

    @Override
    public boolean getSwipeBackEnable() {
//        return super.getSwipeBackEnable();
        return true;
    }

    @OnClick({R.id.applyEnd, R.id.applyNext, R.id.applyStop})
    public void onClick(View view) {
        Intent intent = new Intent(this, ApprovalOpinionActivity.class);
        switch (view.getId()) {
            case R.id.applyEnd:
                if (flag == Flag_ApplyForm_Approval) { // 撤回
                    mPresenter.postApprovalOpinion(this, approvalID, Flag_State_4 + "", null, null, mICustomListener);
                    return;
                } else if (flag == Flag_ApplyForm_Inquire) { // 终止
                    intent.putExtra("flag", Flag_ApplyForm_Stop);
                    intent.putExtra("approvalID", approvalID);
                } else if (flag == -1) { // 同意并结束
                    intent.putExtra("flag", Flag_ApplyForm_Inquire);
                    intent.putExtra("approvalID", approvalID);
                    intent.putExtra("state", 1);
                    intent.putExtra("isAgree", true);
                }
                break;
            case R.id.applyNext:
                if (flag == Flag_ApplyForm_Approval) { // 催办
                    mPresenter.postApprovalOpinion(this, approvalID, Flag_State_5 + "", null, null, mICustomListener);
                    return;
                } else if (flag == -1) { // 同意并转审批
                    intent.putExtra("flag", Flag_ApplyForm_Approval);
                    intent.putExtra("approvalID", approvalID);
                    intent.putExtra("isAgree", true);
                }
                break;
            case R.id.applyStop:
                if (flag == -1) { // 驳回
                    intent.putExtra("flag", Flag_ApplyForm_Rejected);
                    intent.putExtra("approvalID", approvalID);
                }
                break;
        }
        startActivityForResult(intent, 1000);
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

                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case 1:
                    MyToast.showToast("修改失败");
                    break;
                case 2:
                    List<ApprovalInfoEntity> list = (List<ApprovalInfoEntity>) obj1;
                    if (StringUtils.isEmpty(list)) {
                        break;
                    }
                    ApprovalInfoEntity infoEntity = list.get(0);
                    if (infoEntity.getState() == 0 && infoEntity.getTheApply() != 2) {
                        bottom.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            } else {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
