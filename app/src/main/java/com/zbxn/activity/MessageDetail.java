package com.zbxn.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.zbxn.activity.main.message.ShowWebImageActivity;
import com.zbxn.bean.Member;
import com.zbxn.fragment.CommentFragment;
import com.zbxn.R;
import com.zbxn.init.http.RequestParams;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.Bulletin;
import com.zbxn.pub.dialog.ProgressDialog;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvc.AbsToolbarActivity;
import com.zbxn.pub.frame.mvp.base.LoadingControllerImp;
import com.zbxn.pub.http.RequestUtils.Code;
import com.zbxn.pub.http.Response;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.JsonUtil;
import com.zbxn.pub.utils.MyToast;
import com.zbxn.pub.widget.ProgressWebView;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import utils.DateUtils;
import utils.PreferencesUtils;
import utils.StringUtils;
import widget.pulltorefresh.PtrDefaultHandler;
import widget.pulltorefresh.PtrFrameLayout;
import widget.pulltorefresh.PtrHandler;
import widget.pulltorefresh.header.MaterialHeader;

@SuppressLint("SetJavaScriptEnabled")
public class MessageDetail extends AbsToolbarActivity implements CommentFragment.CallBackComment {

    /**
     * 从其它页面传来的参数{@link Bulletin}
     */
    public static final String Flag_Input_Bulletin = "bulletin";

    @BindView(R.id.layout_container)
    PtrFrameLayout mLayoutContainer;

    @BindView(R.id.layout_header)
    MaterialHeader mLayoutHeader;

    @BindView(R.id.mTitle)
    TextView mTitle;

    @BindView(R.id.mCreateTime)
    TextView mCreateTime;

    @BindView(R.id.mCreateUserName)
    TextView mCreateUserName;

    @BindView(R.id.mAlertMessage)
    TextView mAlertMessage;

    @BindView(R.id.message_scroll)
    ScrollView mScrollView;

    @BindView(R.id.mProgressWebView)
    ProgressWebView mProgressWebView;
    @BindView(R.id.mComment)
    EditText mComment;
    @BindView(R.id.mPublish)
    TextView mPublish;
    @BindView(R.id.comment_layout)
    LinearLayout commentLayout;

    private MenuItem mCollect;
    private MenuItem mStick;
    private MenuItem mDelete;

    private Bulletin mBulletin;

    //    private WebViewFragment mWebViewFragment;
    private CommentFragment mCommentFragment;

    private int msgId;
    private int dataId;
    private int position;

    private LoadingControllerImp mLoadingController;

    private boolean isReply = false;
    private int commentId;
    private int replyToId;
    private String reply;
    private ProgressDialog mProgressDialog;

    @Override
    public int getMainLayout() {
        return R.layout.activity_messagedetail;
    }

    //    @SuppressLint("SetJavaScriptEnabled")
    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String id = getIntent().getStringExtra("id");
        if (!StringUtils.isEmpty(id)) {
            dataId = Integer.parseInt(id);
        } else {
            mBulletin = getIntent().getParcelableExtra(Flag_Input_Bulletin);
            dataId = mBulletin.getRelatedid();
            msgId = mBulletin.getId();
            position = getIntent().getIntExtra("position", 0);
            mProgressDialog = new ProgressDialog(this);
            if (mBulletin.getAllowComment() == 1) {
                commentLayout.setVisibility(View.VISIBLE);
            } else if (mBulletin.getAllowComment() == 0) {
                commentLayout.setVisibility(View.GONE);
            }
        }

        init();

        mLayoutContainer.autoRefresh(true);

        // 延迟600毫秒再加载，等以后取消掉时就可以说：“我们将消息详情的加载速度提高了600毫秒”
        /*mLayoutContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLayoutContainer.autoRefresh(true);
            }
        }, 300);*/


//        WebSettings webSettings = mProgressWebView.getSettings();
//        webSettings.getJavaScriptEnabled();
        // 启用javascript
        mProgressWebView.getSettings().setJavaScriptEnabled(true);
        // 随便找了个带图片的网站
//        mProgressWebView.loadUrl("http://n.zbzbx.com/");
        // 添加js交互接口类，并起别名 imagelistner

        mProgressWebView.addJavascriptInterface(new JavaScriptInterface(this), "imagelistner");
        mProgressWebView.setWebViewClient(new MyWebViewClient());
    }

    @Override
    public void loadData() {
        mLayoutContainer.addPtrUIHandler(mLayoutHeader);
        mLayoutContainer.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (mBulletin != null) {
                    RequestParams params = new RequestParams();
                    //将用户输入的账号保存起来，以便下次使用
                    String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
                    params.put("tokenid", ssid);
                    params.put("id", dataId);
                    post(Code.BULLETIN_DETAIL, params);
                }
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame,
                                             View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame,
                        mScrollView, header);
            }
        });
        mLayoutContainer.setLoadingMinTime(1500);
        mLayoutContainer.setPinContent(true);
    }


    @Override
    public boolean getSwipeBackEnable() {
        return true;
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("消息详情");
        return super.onToolbarConfiguration(toolbar, params);
    }


    // 注入js函数监听
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        mProgressWebView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistner.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }

    // js通信接口
    public class JavaScriptInterface {

        private Context context;

        public JavaScriptInterface(Context context) {
            this.context = context;
        }


        @JavascriptInterface
        public void openImage(String img) {

            System.out.println(img);
            //
            Intent intent = new Intent();
            intent.putExtra("image", img);
            intent.setClass(context, ShowWebImageActivity.class);
            context.startActivity(intent);
            System.out.println(img);
        }
    }

    // 监听
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            view.getSettings().setJavaScriptEnabled(true);

            super.onPageFinished(view, url);
            // html加载完成之后，添加监听图片的点击js函数
            addImageClickListner();

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.getSettings().setJavaScriptEnabled(true);

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            super.onReceivedError(view, errorCode, description, failingUrl);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_messagedetail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        mCollect = menu.findItem(R.id.mMenuCollect);
        mDelete = menu.findItem(R.id.mSelectMessage);
        mStick = menu.findItem(R.id.mMenuSick);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item == mCollect) {
            RequestParams params = new RequestParams();
            params.put("bulletinId", msgId);
            //将用户输入的账号保存起来，以便下次使用
            String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
            params.put("tokenid", ssid);
            if (mBulletin.isCollect()) {//已收藏，取消收藏
                //先更改本地数据
                mBulletin.setCollect(false);
                mLoadingController.show();
                params.put("isCollect", "false");
                post(Code.ALERT_COLLECT, params);
            } else {//未收藏，收藏
                //先更改本地数据
                mBulletin.setCollect(true);
                mLoadingController.show();
                params.put("isCollect", "true");
                post(Code.ALERT_COLLECT, params);
            }
        } else if (item == mStick) {
            String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
            RequestParams params = new RequestParams();
            params.put("tokenid", ssid);
            params.put("id", mBulletin.getId());
            params.put("istop", 0);
            post(Code.UPDATETOP_BULLETIN, params);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show("正在提交...");
        } else if (item == mDelete) {
            String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
            RequestParams params = new RequestParams();
            params.put("tokenid", ssid);
            params.put("id", mBulletin.getId());
            post(Code.DELETE_BULLETIN, params);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show("正在删除...");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public void onSuccess(Code code, JSONObject response) {
        try {
            switch (code) {
                case BULLETIN_DETAIL:
                    String data = response.optString(Response.DATA, null);
                    mBulletin = JsonUtil.fromJsonString(data, Bulletin.class);
                    if (mBulletin.getIstop() == 1 && mBulletin.getCreateuserid() == Member.get().getId()) {
                        mStick.setVisible(true);
                    }
                    if (mBulletin.getCreateuserid() == Member.get().getId()) {
                        mDelete.setVisible(true);
                    }
                    refreshUI();
                    updateSuccessView();
                    break;
                case ALERT_COLLECT:
                    showToast(response.optString(Response.MSG, "操作成功"), Toast.LENGTH_SHORT);
                    refreshCollectState();
                    break;
                case COMMENT_MESSAGE:
                    MyToast.showToast("评论成功");
                    refreshUI();
                    mComment.getText().clear();
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    break;
                case COMMENT_USER:
                    MyToast.showToast("回复成功");
                    refreshUI();
                    mComment.getText().clear();
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    break;
                case UPDATETOP_BULLETIN:
                    MyToast.showToast("取消置顶成功");
                    refreshUI();
                    mStick.setVisible(false);
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    break;
                case DELETE_BULLETIN:
                    MyToast.showToast("删除成功");
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    finish();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
        } finally {
            mLayoutContainer.refreshComplete();
            if (mLoadingController.isShowing())
                mLoadingController.hide();
        }

    }

    @Override
    public void onFailure(Code code, JSONObject error) {
        try {
            switch (code) {
                case BULLETIN_DETAIL:
                    updateErrorView();
                    String message = error.optString(Response.MSG, "加载失败，请重试");
                    showToast(message, Toast.LENGTH_SHORT);
                    break;
                case ALERT_COLLECT:
                    showToast(error.optString(Response.MSG, "操作失败"), Toast.LENGTH_SHORT);
                    //撤回对本地数据的修改
                    mBulletin.setCollect(!mBulletin.isCollect());
                    break;
                case COMMENT_MESSAGE:
                    MyToast.showToast("评论失败");
                    mComment.getText().clear();
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    break;
                case COMMENT_USER:
                    MyToast.showToast("回复失败");
                    mComment.getText().clear();
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    break;
                case UPDATETOP_BULLETIN:
                    MyToast.showToast("取消置顶失败");
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    break;
                case DELETE_BULLETIN:
                    MyToast.showToast("删除失败");
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
        } finally {
            mLayoutContainer.refreshComplete();
            if (mLoadingController.isShowing())
                mLoadingController.hide();
        }
    }

    private void init() {
//        mWebViewFragment = (WebViewFragment) getSupportFragmentManager().findFragmentById(R.id.mWebViewFragment);

        mProgressWebView.getSettings().setDefaultFontSize(14);
        mProgressWebView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        mProgressWebView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        mProgressWebView.getSettings().setBuiltInZoomControls(true);
        // 不显示webview缩放按钮
        mProgressWebView.getSettings().setDisplayZoomControls(false);
        // 扩大比例的缩放
        mProgressWebView.getSettings().setUseWideViewPort(true);
        // 自适应屏幕
        mProgressWebView.getSettings().setLayoutAlgorithm(
                WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mProgressWebView.getSettings().setLoadWithOverviewMode(true);
        mProgressWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                // return super.shouldOverrideUrlLoading(view, url);
                return false;
            }
        });

        mCommentFragment = (CommentFragment) getSupportFragmentManager().findFragmentById(R.id.mCommentFragment);

        mLoadingController = new LoadingControllerImp(this);


    }


    public void refreshUI() {
        if (mBulletin == null) {
        } else {
            mTitle.setText(mBulletin.getTitle() + "");
            mCreateTime.setText(DateUtils.fromTodaySimple(mBulletin
                    .getCreatetime()));

            mCreateUserName.setText(mBulletin.getCreateUserName());


            String content = "<head><meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no\" />" +
                    "<style >img{max-width:100%;height:auto;}</style>" + "</head>\n<body><div>" +
                    mBulletin.getContent() + "</div></body>";
//            mWebViewFragment.reloadDataWithContent(content);

            String mBaseUrl = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
            String mMimType = "text/html";
            String mEncoding = "utf-8";
            String mFailUrl = null;
            mProgressWebView.loadDataWithBaseURL(mBaseUrl, content, mMimType, mEncoding, mFailUrl);
            mCommentFragment.reloadData(dataId, 1);

            refreshCollectState();
        }
    }

    /**
     * 刷新收藏状态
     */
    private void refreshCollectState() {
        mCollect.setEnabled(true);
        if (mBulletin.isCollect()) {
            mCollect.setIcon(R.mipmap.bg_collect_yes);
        } else {
            mCollect.setIcon(R.mipmap.bg_collect_no);
            Intent intent = new Intent(MessageDetail.this, CollectCenter.class);
            intent.putExtra("position", position);
            setResult(RESULT_OK, intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void sendComment(String name, int commentId, int replyToId) {
        isReply = true;
        mComment.requestFocus();
        InputMethodManager imm = (InputMethodManager) mComment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

        reply = "回复 " + name + ":";
        mComment.setText(reply);
        this.commentId = commentId;
        this.replyToId = replyToId;

        //设置光标位置
        Editable editable = mComment.getText();
        Spannable spanText = editable;
        Selection.setSelection(spanText, editable.length());
        //EditText 监听
        mComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mComment.getText().length() < reply.length()) {
                    mComment.getText().clear();
                    reply = "";
                    isReply = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @OnClick({R.id.mPublish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mPublish:
                if (TextUtils.isEmpty(mComment.getText().toString().trim())) {
//            showToast("回复信息不能为空", Toast.LENGTH_SHORT);
                    MyToast.showToast("评论内容不能为空");
                } else {
                    if (isReply) {
                        int size = reply.length();
//                        if (mComment.getText().length() < size) {
//                            isReply = false;
//                        }
                        String str = mComment.getText().toString().substring(size);
                        if (StringUtils.isEmpty(str.trim())) {
                            MyToast.showToast("评论内容不能为空");
                        } else {
                            RequestParams param = new RequestParams();
                            param.put("commentId", commentId);
                            param.put("replyToId", replyToId);
                            param.put("content", str);
                            //将用户输入的保存起来，以便下次使用
                            String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
                            param.put("tokenid", ssid);
                            post(Code.COMMENT_USER, param);
                            isReply = false;
                            mProgressDialog.setCancelable(false);
                            mProgressDialog.show("正在提交...");
                        }
                    } else {
                        RequestParams params = new RequestParams();
                        params.put("type", 1);
                        params.put("dataId", dataId);
                        params.put("content", mComment.getText().toString());
                        //将用户输入的账号保存起来，以便下次使用
                        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
                        params.put("tokenid", ssid);
                        post(Code.COMMENT_MESSAGE, params);
                        mProgressDialog.setCancelable(false);
                        mProgressDialog.show("正在提交...");
                    }

                }
                break;
        }
    }

}
