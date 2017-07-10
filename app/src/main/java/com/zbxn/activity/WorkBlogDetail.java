package com.zbxn.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.zbxn.fragment.WorkBlogDetailFragment;
import com.zbxn.R;
import com.zbxn.fragment.CommentFragment;
import com.zbxn.init.http.RequestParams;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.WorkBlog;
import com.zbxn.pub.dialog.ProgressDialog;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvc.AbsToolbarActivity;
import com.zbxn.pub.http.RequestUtils.Code;
import com.zbxn.pub.utils.FragmentAdapter;
import com.zbxn.pub.utils.MyToast;
import com.zbxn.pub.widget.NoScrollViewPager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import utils.DateUtils;
import utils.PreferencesUtils;
import utils.StringUtils;
import widget.smarttablayout.SmartTabLayout;

public class WorkBlogDetail extends AbsToolbarActivity implements CommentFragment.CallBackComment {

    /**
     * 传入的日志列表中，需要将哪一个位置的日志显示出来
     */
    public static final String Flag_Input_Position = "position";

    /**
     * 输入参数_日志列表 ArrayList<{@link WorkBlog}>
     */
    public static final String Flag_Input_BlogList = "blogList";

    @BindView(R.id.mSmartTabLayout)
    SmartTabLayout mSmartTabLayout;
    @BindView(R.id.mViewPager)
    NoScrollViewPager mViewPager;
    @BindView(R.id.mComment)
    EditText mComment;
    @BindView(R.id.mPublish)
    TextView mPublish;

    private FragmentAdapter mAdapter;

    private List<WorkBlog> mList = null;
    // 初始化各页面
    List<Fragment> list = new ArrayList<Fragment>();

    private int dataId;
    private boolean isReply = false;
    private int commentId;
    private int replyToId;
    private String reply;

    private ProgressDialog mProgressDialog;

    @Override
    public int getMainLayout() {
        return R.layout.activity_workblogdetail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        updateSuccessView();
    }

    @Override
    public void loadData() {

    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("日志详情");
        params.shadowEnable = false;
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    public boolean getSwipeBackEnable() {
        return true;
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public void onSuccess(Code code, JSONObject response) {
        try {
            switch (code) {
                case COMMENT_MESSAGE:
                    MyToast.showToast("评论成功");
                    mComment.getText().clear();
                    ((WorkBlogDetailFragment) list.get(0)).refreshUI();
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    break;
                case COMMENT_USER:
                    MyToast.showToast("回复成功");
                    mComment.getText().clear();
                    ((WorkBlogDetailFragment) list.get(0)).refreshUI();
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onFailure(Code code, JSONObject error) {
        try {
            switch (code) {
                case COMMENT_MESSAGE:
                    MyToast.showToast("评论失败");
                    mComment.getText().clear();
                    break;
                case COMMENT_USER:
                    MyToast.showToast("回复失败");
                    mComment.getText().clear();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
        }

    }

    private void init() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在提交");

        mAdapter = new FragmentAdapter(getSupportFragmentManager());
        // 取得传入的日志列表
        List<WorkBlog> blogList = getIntent().getParcelableArrayListExtra(
                Flag_Input_BlogList);
        if (blogList == null || blogList.size() == 0)
            finish();

        mList = blogList;
        for (Parcelable parcelable : blogList) {
            WorkBlog blog = (WorkBlog) parcelable;
            Bundle bundle = new Bundle();
            bundle.putInt("messageId", dataId);
            bundle.putInt("bulletinOrLogType", 2);
            bundle.putParcelable(WorkBlogDetailFragment.Flag_Input_WorkBlog,
                    blog);
            WorkBlogDetailFragment fragment = (WorkBlogDetailFragment) Fragment
                    .instantiate(this, WorkBlogDetailFragment.class.getName(),
                            bundle);
            fragment.setmTitle(blog.getCreateWeek());
            list.add(fragment);
        }
        Fragment[] fragments = new Fragment[list.size()];
        list.toArray(fragments);
        //mAdapter.addView(fragments);
        mAdapter.addFragment(fragments);
        mViewPager.setAdapter(mAdapter);
        mSmartTabLayout.setViewPager(mViewPager);

        // 当前显示第几页
        int position = getIntent().getIntExtra(Flag_Input_Position, 0);
        if (mAdapter.getCount() > position) {
            mViewPager.setCurrentItem(position, true);
            WorkBlog blog = mList.get(position);
            String date = DateUtils.getDate(blog.getCreatetime(), "MM月dd日");
            //mToolbar.setTitle(date);
            dataId = blog.getId();
        }

        mSmartTabLayout.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                WorkBlog blog = mList.get(position);
                String date = DateUtils.getDate(blog.getCreatetime(), "MM月dd日");
                //mToolbar.setTitle(date);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }

    @Override
    public void sendComment(String name, int commentId, int replyToId) {
        mComment.requestFocus();
        InputMethodManager imm = (InputMethodManager) mComment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

        reply = "回复 " + name + ":";
        mComment.setText(reply);
        this.commentId = commentId;
        this.replyToId = replyToId;
        isReply = true;

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
                        if (mComment.getText().length() < size) {
                            mComment.setText("");
                            isReply = false;
                        }
                        String str = mComment.getText().toString().substring(size);

                        if (StringUtils.isEmpty(str.trim())) {
                            MyToast.showToast("评论内容不能为空");
                        } else {
                            mProgressDialog.show();
                            RequestParams param = new RequestParams();
                            param.put("commentId", commentId);
                            param.put("replyToId", replyToId);
                            param.put("content", str);
                            //将用户输入的保存起来，以便下次使用
                            String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
                            param.put("tokenid", ssid);
                            post(Code.COMMENT_USER, param);
                            isReply = false;
                        }


                    } else {
                        mProgressDialog.show();
                        RequestParams params = new RequestParams();
                        params.put("type", 2);
                        params.put("dataId", dataId);
                        params.put("content", mComment.getText().toString());
                        //将用户输入的账号保存起来，以便下次使用
                        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
                        params.put("tokenid", ssid);
                        post(Code.COMMENT_MESSAGE, params);
                    }

                }

                break;
        }
    }

}
