package com.zbxn.fragment;

import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.pub.bean.WorkBlog;
import com.zbxn.pub.frame.mvc.AbsBaseFragment;
import com.zbxn.pub.http.RequestUtils.Code;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkBlogDetailFragment extends AbsBaseFragment {

    /**
     * 其它页面传入的参数{@link WorkBlog}
     **/
    public static final String Flag_Input_WorkBlog = "blog";

    @BindView(R.id.mContent)
    TextView mContent;
    @BindView(R.id.mContentLength)
    TextView mContentLength;
    @BindView(R.id.mOrigin)
    TextView mOrigin;
    @BindView(R.id.mScrollView)
    ScrollView mScrollView;
    @BindView(R.id.mIcon)
    ImageView mIcon;

    private WorkBlog mWorkBlog;

    private CommentFragment mCommentFragment;

    public WorkBlogDetailFragment() {

    }

    @Override
    public void onSuccess(Code code, JSONObject response) {

    }

    @Override
    public void onFailure(Code code, JSONObject error) {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_workblogdetail,
                container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initialize(View root, Bundle savedInstanceState) {
        Parcelable parcelable = getArguments().getParcelable(
                Flag_Input_WorkBlog);
        if (parcelable == null)
            return;
        mWorkBlog = (WorkBlog) parcelable;

        mCommentFragment = (CommentFragment) getChildFragmentManager().findFragmentById(R.id.mCommentFragment);

        refreshUI();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void refreshUI() {
        mCommentFragment.reloadData(mWorkBlog.getId(), 2);

        mScrollView.smoothScrollTo(0, 0);
        mContent.setText(mWorkBlog.getWorkblogcontent());
        mContentLength.setText(mWorkBlog.getWorkblogcontent().trim().length() + "字");
        if (mWorkBlog.isFromMobile()) {// 来自移动端
            mOrigin.setText("来自移动端");
            mIcon.setImageResource(R.mipmap.bg_mobile);
        } else {
            mOrigin.setText("来自PC端");
            mIcon.setImageResource(R.mipmap.bg_pc);
        }

    }

    public WorkBlog getmWorkBlog() {
        return mWorkBlog;
    }

}
