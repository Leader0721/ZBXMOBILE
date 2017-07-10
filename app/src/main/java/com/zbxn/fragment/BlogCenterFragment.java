package com.zbxn.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zbxn.activity.WorkBlogDetail;
import com.zbxn.bean.Member;
import com.zbxn.R;
import com.zbxn.activity.CreateWorkBlog;
import com.zbxn.init.http.RequestParams;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.WorkBlog;
import com.zbxn.pub.bean.adapter.WorkBlogAdapter;
import com.zbxn.pub.bean.adapter.base.IItemViewControl;
import com.zbxn.pub.dialog.MessageDialog;
import com.zbxn.pub.frame.mvc.AbsBaseFragment;
import com.zbxn.pub.http.RequestUtils.Code;
import com.zbxn.pub.http.Response;
import com.zbxn.pub.utils.JsonUtil;
import com.zbxn.pub.utils.ListviewUpDownHelper;
import com.zbxn.pub.utils.ListviewUpDownHelper.OnRequestDataListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.DateUtils;
import utils.PreferencesUtils;

/**
 * 消息模块
 *
 * @author GISirFive
 * @since 2016-7-6 下午1:20:12
 */
public class BlogCenterFragment extends AbsBaseFragment implements
        IItemViewControl<WorkBlog>, OnItemClickListener, OnRequestDataListener {

    /**
     * 回调_创建日志
     */
    private static final int Flag_Callback_CreateWorkBlog = 1001;

    /**
     * 消息_初始化加载数据
     */
    private static final int Flag_Message_Initialize = 1002;

    @BindView(R.id.listview_content)
    ListView mListView;

    @BindView(R.id.mCreateBlog)
    FloatingActionButton mCreateBlog;

    private ListviewUpDownHelper mListviewHelper;
    private WorkBlogAdapter mAdapter;

    private CallbackBlog callbackBlog;

    /**
     * 当天的日志
     */
    private WorkBlog mBlogToday;

    /**
     * 当前展示的列表所对应的用户id为
     */
    private int mCurrentUserId;

    private MessageDialog mMessageDialog;

    public BlogCenterFragment() {
        setmTitle("日志");
    }

    @Override
    public void onSuccess(Code code, JSONObject response) {
        try {
            switch (code) {
                case WORKBLOG_LIST_TOP:
                    String rows = response.optString(Response.ROWS, null);
                    List<WorkBlog> list = JsonUtil.fromJsonList(rows,
                            WorkBlog.class);
                    mAdapter.resetData(list);
                    mListviewHelper.refreshComplete();
                    break;
                case WORKBLOG_LIST_BOTTOM:
                    rows = response.optString(Response.ROWS, null);
                    list = JsonUtil.fromJsonList(rows, WorkBlog.class);
                    int count = mAdapter.addInBottom(list);
                    mListviewHelper.loadMoreComplete(false, count != 0);
                    break;
                case WORKBLOG_CHECKTODAY:// 当天是否写了日志
                    if ("0".equals(response.optString(Response.SUCCESS, "0"))) {
                        String dataStr = response.optString(Response.DATA, null);
                        mBlogToday = JsonUtil.fromJsonString(dataStr,
                                WorkBlog.class);
                        callbackBlog.sendMessage(mBlogToday, mListviewHelper);
                        Member.get().setBlogStateToday(mBlogToday == null ? 0 : 1);
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onFailure(Code code, JSONObject errorResponse) {
        try {
            switch (code) {
                case WORKBLOG_LIST_TOP:
                    mListviewHelper.refreshComplete();
                case WORKBLOG_LIST_BOTTOM:
                    mListviewHelper.loadMoreComplete(false, true);
                    Toast.makeText(getActivity(),
                            errorResponse.optString(Response.MSG, "请求失败,请重试"),
                            Toast.LENGTH_LONG).show();
                    break;
                case WORKBLOG_CHECKTODAY:// 当天是否写了日志
                    if (!"0".equals(errorResponse.optString(Response.SUCCESS, "0"))) {
                        mBlogToday = null;
                        callbackBlog.sendMessage(mBlogToday, mListviewHelper);
                        Member.get().setBlogStateToday(mBlogToday == null ? 0 : 1);
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {

        } finally {
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Flag_Message_Initialize:
                mListviewHelper.autoRefresh();
                break;

            default:
                break;
        }
        return false;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup group) {
        View root = inflater.inflate(R.layout.main_blogcenter, group, false);
        ButterKnife.bind(this, root);
        callbackBlog = (CallbackBlog) getActivity();
        return root;
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {
        mAdapter = new WorkBlogAdapter(getActivity(), null);
        mAdapter.setOnDataItemCallbackListener(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        mListviewHelper = new ListviewUpDownHelper(this);
        mListviewHelper.bind(root);

        mMessageDialog = new MessageDialog(getActivity());
        mMessageDialog.setTitle("提示");
        mMessageDialog.setMessage(getResources().getString(
                R.string.app_main_blogcenter_message));
        mMessageDialog.setNegativeButton("取消");
        mMessageDialog.setPositiveButton("编辑",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openCreateWorkBlog();
                    }

                });

        mCurrentUserId = Member.get().getId();

        sendMessageDelayed(Flag_Message_Initialize, 300);
        // 查询用户当天是否写了日志
        RequestParams params = new RequestParams();
        //将用户输入的账号保存起来，以便下次使用
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        params.put("tokenid", ssid);
        post(Code.WORKBLOG_CHECKTODAY, params);
    }

    @OnClick(R.id.mCreateBlog)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mCreateBlog:// 创建日志
                if (mBlogToday == null) {
                    openCreateWorkBlog();
                } else {
                    mMessageDialog.show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public View initViewItem(LayoutInflater inflater, int position,
                             View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_blogcenter,
                    parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        WorkBlog item = mAdapter.getItem(position);

        if (item.isFromMobile()) {// 来自移动端
            holder.mOrigin.setText("来自移动端");
            holder.mOriginImg.setImageResource(R.mipmap.bg_mobile);
        } else {
            holder.mOrigin.setText("来自PC端");
            holder.mOriginImg.setImageResource(R.mipmap.bg_pc);
        }
        holder.mCommentCount.setText("评论:"+item.getCommentNum());
        holder.mCreateWeek.setText(item.getCreateWeek());
        String content = item.getWorkblogcontent();
        content = content.replace("\n", "");
        holder.mContent.setText(content);
        holder.mCreateTime.setText(DateUtils.fromTodaySimple(item
                .getCreatetime()));

        return convertView;
    }

    @Override
    public void dataSetChangedListener(List<WorkBlog> data) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Intent intent = new Intent(getActivity(), WorkBlogDetail.class);

        int start = ((position - 4) < 0) ? 0 : position - 4;
        int end = ((position + 6) > mAdapter.getCount()) ? mAdapter.getCount()
                : position + 6;

        ArrayList<WorkBlog> list = (ArrayList<WorkBlog>) mAdapter.getDataList();
        List<WorkBlog> temp = list.subList(start, end);
        list = new ArrayList<WorkBlog>();
        list.addAll(temp);
        // 重新计算索引位置
        position = (start == 0) ? position : 4;

        // Toast.makeText(getActivity(), "已加载前后10天的日志",
        // Toast.LENGTH_LONG).show();

        intent.putParcelableArrayListExtra(WorkBlogDetail.Flag_Input_BlogList,
                list);
        intent.putExtra(WorkBlogDetail.Flag_Input_Position, position);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        RequestParams params = new RequestParams();
        params.put("userId", mCurrentUserId);
        params.put("page", "1");
        params.put("rows", 6);
        //将用户输入的账号保存起来，以便下次使用
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        params.put("tokenid", ssid);
        post(Code.WORKBLOG_LIST_TOP, params);
    }

    @Override
    public void onLoadMore() {
        RequestParams params = new RequestParams();
        params.put("userId", mCurrentUserId);
        params.put("page", mAdapter.getPage());
        params.put("rows", 6);
        //将用户输入的账号保存起来，以便下次使用
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        params.put("tokenid", ssid);
        post(Code.WORKBLOG_LIST_BOTTOM, params);
    }

    public void refresh() {
        mListviewHelper.autoRefresh();
        // 查询用户当天是否写了日志
        RequestParams params = new RequestParams();
        //将用户输入的账号保存起来，以便下次使用
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        params.put("tokenid", ssid);
        post(Code.WORKBLOG_CHECKTODAY, params);
    }



    /**
     * 隐藏创建日志按钮
     */
    public void hideAddButton() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(mCreateBlog, "rotation", 0, 360),
                ObjectAnimator.ofFloat(mCreateBlog, "scaleX", 1, 0),
                ObjectAnimator.ofFloat(mCreateBlog, "scaleY", 1, 0));
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.setDuration(200);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mCreateBlog.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCreateBlog.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    /**
     * 刷新数据，加载id为userId的用户的日志
     *
     * @param userId
     */
    public void refreshDataByUserId(int userId) {
        if (mCurrentUserId == userId)
            return;
        mCurrentUserId = userId;
        mListviewHelper.autoRefresh();
    }

    // 打开创建日志页面
    private void openCreateWorkBlog() {
        Intent intent = new Intent(getActivity(), CreateWorkBlog.class);
        // 将当天日志传入
        if (mBlogToday != null) {
            intent.putExtra(CreateWorkBlog.Flag_Input_Blog, mBlogToday);
        }
        startActivityForResult(intent, Flag_Callback_CreateWorkBlog);
    }

    static class ViewHolder {
        @BindView(R.id.mCreateWeek)
        TextView mCreateWeek;
        @BindView(R.id.mTitle)
        TextView mTitle;
        @BindView(R.id.mOrigin)
        TextView mOrigin;
        @BindView(R.id.mContent)
        TextView mContent;
        @BindView(R.id.mBlogNumber)
        TextView mBlogNumber;
        @BindView(R.id.mCreateTime)
        TextView mCreateTime;
        @BindView(R.id.mOriginImg)
        ImageView mOriginImg;
        @BindView(R.id.mCommentCount)
        TextView mCommentCount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
    public interface CallbackBlog {
        void sendMessage(WorkBlog workBlog, ListviewUpDownHelper mListviewHelper);
    }

}
