package com.zbxn.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zbxn.bean.Member;
import com.zbxn.createworkblog.BlogHintView;
import com.zbxn.R;
import com.zbxn.init.http.RequestParams;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.WorkBlog;
import com.zbxn.pub.dialog.MessageDialog;
import com.zbxn.pub.dialog.ProgressDialog;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvc.AbsToolbarActivity;
import com.zbxn.pub.http.RequestUtils.Code;
import com.zbxn.pub.http.Response;
import com.zbxn.pub.widget.PerformEdit;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import utils.DateUtils;
import utils.PreferencesUtils;
import utils.StringUtils;

/**
 * 创建日志
 *
 * @author GISirFive
 * @since 2016-7-11 下午9:29:36
 */
public class CreateWorkBlog extends AbsToolbarActivity {

    /**
     * 日志的本地缓存
     */
    private static final String Flag_SharedPreference_Blog = "blog";

    /**
     * 输入内容_日志{@link WorkBlog}
     */
    public static final String Flag_Input_Blog = "blog";

    /**
     * 选择常用语的回调
     */
    public static final int Flag_callback_BlogHintView = 1001;
    /**
     * 显示创建按钮
     */
    public static final int Flag_Callback_ShowAddButton = 1002;

    /**
     * 日志长度范围
     */
    private static final int[] Flag_RangeLengthOfBlog = {20, 1000};

    private BlogHintView mBlogHintView;

    @BindView(R.id.mContent)
    EditText mContent;
    @BindView(R.id.mContentLength)
    TextView mContentLength;
    @BindView(R.id.mCreateBlog)
    TextView mCreateBlog;
    // 实现撤销/恢复功能的工具类
    private PerformEdit mPerformEdit;
    // 已完成日志提交，清空缓存
    private boolean mSubmitFinish = false;
    private ProgressDialog mProgressDialog;
    private int type;

    @Override
    public int getMainLayout() {
        return R.layout.activity_createworkblog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        init();
        // 加载已有的日志
        loadHistory();
        updateSuccessView();


    }


    @Override
    public void loadData() {

    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        String date = DateUtils.getDate("MM月dd日");
        toolbar.setTitle(date);
        return super.onToolbarConfiguration(toolbar, params);
    }


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Flag_callback_BlogHintView:
                String hint = msg.obj.toString();
                mContent.setText(mContent.getText().toString() + "\n" + hint + "\n");
                // 设置光标在末尾
                mContent.setSelection(mContent.getText().length());
                break;
            case Flag_Callback_ShowAddButton:
                showAddButton();
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onSuccess(Code code, JSONObject response) {
        try {
            switch (code) {
                case WORKBLOG_SAVE:// 保存日志
                    mSubmitFinish = true;
                    String message = response.optString(Response.MSG, "提交成功");
                    showToast(message, Toast.LENGTH_SHORT);
                    setResult(RESULT_OK);
                    finish();
                    break;

                default:
                    break;
            }
        } catch (Exception e) {

        } finally {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }
    }

    @Override
    public void onFailure(Code code, JSONObject error) {
        try {
            switch (code) {
                case WORKBLOG_SAVE:// 保存日志
                    String message = error.optString(Response.MSG, "提交失败,请重试");
                    MessageDialog dialog = new MessageDialog(this);
                    dialog.setTitle("提示");
                    dialog.setMessage(message);
                    dialog.setPositiveButton("确定");
                    dialog.show();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {

        } finally {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }
    }

    private void init() {
        type = getIntent().getIntExtra("type", 0);
        mBlogHintView = new BlogHintView(this,
                findViewById(R.id.mSlideBottomPanel), getHandler());

        mProgressDialog = new ProgressDialog(this);

        // 创建PerformEdit，一定要传入不为空的EditText
        mPerformEdit = new PerformEdit(mContent);
        mPerformEdit.setDefaultText("");
        // 最大输入长度
        mContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                Flag_RangeLengthOfBlog[1])});
        mContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = 0;
                if (s == null)
                    length = 0;
                String content = s.toString().trim();
                length = content.length();
                mContentLength.setText(String.valueOf(length));
            }
        });
        sendMessageDelayed(Flag_Callback_ShowAddButton, 600);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_createworkblog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_undo:// 撤销
                mPerformEdit.undo();
                break;
            case R.id.action_redo:// 恢复
                mPerformEdit.redo();
                break;
          /*  case R.id.action_hint:// 懒汉模式
                if (mBlogHintView.isShowing()) {
                    mBlogHintView.hide();
                    // 关闭输入法
                    DeviceUtils.getInstance(this).showSoftInput(mContent);
                } else {
                    mBlogHintView.show();
                    // 关闭输入法
                    DeviceUtils.getInstance(this).hideSoftInput(mContent);
                }
                break;*/
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        mPerformEdit.clearHistory();
        // 清空缓存
        if (mSubmitFinish) {
            PreferencesUtils.putString(this, Flag_SharedPreference_Blog, "");
        } else {
            String content = mContent.getText().toString();
            if (!StringUtils.isBlank(content))
                PreferencesUtils.putString(this, Flag_SharedPreference_Blog,
                        content);
        }
        super.onDestroy();
    }

    /**
     * 加载传入该页面的日志
     */
    private void loadHistory() {
        Parcelable parcelable = getIntent().getParcelableExtra(Flag_Input_Blog);
        if (parcelable == null) {// 其它页面没传入日志，从本地缓存中加载
            String cache = PreferencesUtils.getString(this,
                    Flag_SharedPreference_Blog);
            if (cache == null)// 本地无缓存
                return;
            if (Member.get().getBlogStateToday() == 1) {
                mContent.setText(cache);
            }
        } else {// 加载从其它页面传入的日志
            WorkBlog blog = (WorkBlog) parcelable;
            // 编辑当前日志
            mContent.setText(blog.getWorkblogcontent());
        }
    }

    @OnClick({R.id.mCreateBlog})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mCreateBlog:// 创建日志
                int length = mContent.getText().toString().trim().length();
                if (length < Flag_RangeLengthOfBlog[0]) {
                    showToast("日志内容不能少于" + Flag_RangeLengthOfBlog[0] + "个字",
                            Toast.LENGTH_SHORT);
                } else if (length > Flag_RangeLengthOfBlog[1]) {
                    showToast("日志内容不能多余" + Flag_RangeLengthOfBlog[1] + "个字",
                            Toast.LENGTH_SHORT);
                } else {
                    MessageDialog dialog = new MessageDialog(this);
                    dialog.setTitle("提示");
                    dialog.setMessage("确认提交?");
                    dialog.setNegativeButton("返回");
                    dialog.setPositiveButton("提交",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    submit();

                                    if (type == 1) {
                                        startActivity(new Intent(CreateWorkBlog.this, WorkBlogCenter.class));
                                    }
                                }

                            });
                    dialog.show();
                }
                break;
            default:
                break;
        }
    }

    private void showAddButton() {
        mCreateBlog.setVisibility(View.VISIBLE);
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.playTogether(
//                ObjectAnimator.ofFloat(mCreateBlog, "rotation", 360, 0),
//                ObjectAnimator.ofFloat(mCreateBlog, "scaleX", 0, 1),
//                ObjectAnimator.ofFloat(mCreateBlog, "scaleY", 0, 1));
//        animatorSet.setInterpolator(new DecelerateInterpolator());
//        animatorSet.setDuration(200);
//        animatorSet.start();
    }

    // 提交日志
    private void submit() {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
        mProgressDialog.setCancelable(false);
        mProgressDialog.show("正在提交...");
        RequestParams params = new RequestParams();
        params.put("content", mContent.getText().toString());
        //将用户输入的账号保存起来，以便下次使用
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        params.put("tokenid", ssid);
        post(Code.WORKBLOG_SAVE, params);
    }

}
