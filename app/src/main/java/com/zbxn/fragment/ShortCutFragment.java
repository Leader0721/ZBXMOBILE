package com.zbxn.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.zbxn.R;
import com.zbxn.activity.CreateBulletin;
import com.zbxn.activity.CreateWorkBlog;
import com.zbxn.fragment.shortcut.IShortCutView;
import com.zbxn.fragment.shortcut.OnItemSelectListener;
import com.zbxn.fragment.shortcut.ShortCutPresenter;
import com.zbxn.pub.bean.WorkBlog;
import com.zbxn.pub.frame.mvp.AbsBaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 快捷功能入口
 *
 * @author GISirFive
 * @time 2016/8/15
 */
public class ShortCutFragment extends AbsBaseFragment implements IShortCutView {

    @BindView(R.id.mCreateAnnouncement)
    FrameLayout mCreateAnnouncement;
    @BindView(R.id.mCreateWorkBlog)
    FrameLayout mCreateWorkBlog;
    @BindView(R.id.mAnnouncementLayout)
    LinearLayout mAnnouncementLayout;
    @BindView(R.id.mBlogLayout)
    LinearLayout mBlogLayout;
    private ShortCutPresenter mPresenter;

    private OnItemSelectListener mOnItemSelectListener;

    /**
     * 设置Item选中监听
     *
     * @param listener
     */
    public void setOnItemSelectListener(OnItemSelectListener listener) {
        this.mOnItemSelectListener = listener;
    }

    public ShortCutFragment() {
        mTitle = "快捷功能入口";
    }

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_shortcut, container, false);
        mPresenter = new ShortCutPresenter(this);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {

        show();
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @OnClick({R.id.mRootLayout, R.id.mCreateAnnouncement, R.id.mCreateWorkBlog})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mRootLayout:
                sendMessageDelayed(200, 1000);
                if (mOnItemSelectListener != null)
                    mOnItemSelectListener.OnItemSelected();
                break;
            case R.id.mCreateAnnouncement:
                sendMessageDelayed(200, 1000);
                startActivity(new Intent(getActivity(), CreateBulletin.class));
                if (mOnItemSelectListener != null)
                    mOnItemSelectListener.OnItemSelected();
                break;
            case R.id.mCreateWorkBlog:
                sendMessageDelayed(200, 1000);
                mPresenter.checkBlogToday();
                break;
        }
    }

    // 打开创建日志页面
    @Override
    public void openCreateWorkBlog(WorkBlog blog) {
        Intent intent = new Intent(getActivity(), CreateWorkBlog.class);
        // 将当天日志传入
        if (blog != null) {
            intent.putExtra(CreateWorkBlog.Flag_Input_Blog, blog);
        }
        intent.putExtra("type", 1);
        startActivity(intent);

        if (mOnItemSelectListener != null)
            mOnItemSelectListener.OnItemSelected();
    }

    public void show() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(mAnnouncementLayout, "translationY", 80, 0).setDuration(300),
                ObjectAnimator.ofFloat(mBlogLayout, "translationY", 80, 0).setDuration(400));
        animatorSet.setInterpolator(new OvershootInterpolator());
        animatorSet.start();
    }

}
