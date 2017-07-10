package com.zbxn.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zbxn.bean.Member;
import com.zbxn.R;
import com.zbxn.fragment.comment.CommentFooterView;
import com.zbxn.init.http.RequestParams;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.Comment;
import com.zbxn.pub.bean.Reply;
import com.zbxn.pub.bean.adapter.CommentAdapter;
import com.zbxn.pub.bean.adapter.base.IItemViewControl;
import com.zbxn.pub.frame.mvc.AbsBaseFragment;
import com.zbxn.pub.http.RequestUtils;
import com.zbxn.pub.http.Response;
import com.zbxn.pub.utils.JsonUtil;
import com.zbxn.pub.utils.MyToast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import utils.DateUtils;
import utils.PreferencesUtils;

/**
 * 评论fragment
 * <p/>
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends AbsBaseFragment implements IItemViewControl<Comment>,
        AdapterView.OnItemClickListener {

    @BindView(R.id.mListView)
    ListView mListView;

    private CommentAdapter mAdapter;

    private CommentFooterView mFooterView;
    CallBackComment callBackComment;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callBackComment = (CallBackComment) getActivity();
    }

    /**
     * 其他页面传来的参数
     */
    private int dataId;
    private int type;

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        ButterKnife.bind(this, view);
        mAdapter = new CommentAdapter(getActivity(), null);
        return view;
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {

        mAdapter.setOnDataItemCallbackListener(this);
        mListView.setFocusable(false);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        mFooterView = new CommentFooterView(this, mListView);

        initData();
    }

    @Override
    public boolean handleMessage(Message msg) {
        mFooterView.loadMoreComplete(false);
        return false;
    }

    @Override
    public void onSuccess(RequestUtils.Code code, JSONObject response) {
        List<Comment> data = new ArrayList<>();
        switch (code) {
            case COMMENT_LIST:
                String rows = response.optString(Response.ROWS, null);
                data = JsonUtil.fromJsonList(rows, Comment.class);
                mAdapter.resetData(data);
                mFooterView.loadMoreComplete(true);
                break;
            case COMMENT_LISTBOTTOM:
                rows = response.optString(Response.ROWS, null);
                List<Comment> list = JsonUtil.fromJsonList(rows, Comment.class);
                data.addAll(list);
                mAdapter.addInBottom(list);
                break;
        }
    }


    @Override
    public void onFailure(RequestUtils.Code code, JSONObject error) {

    }

    public void reloadData(int dataId, int type) {
        this.dataId = dataId;
        this.type = type;
        initData();
    }

    public void initData() {
        RequestParams params = new RequestParams();
        params.put("type", type);
        params.put("dataId", dataId);
        params.put("page", 1);
        params.put("rows", 8);
        //将用户输入的账号保存起来，以便下次使用
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        params.put("tokenid", ssid);
        post(RequestUtils.Code.COMMENT_LIST, params);
    }


    @Override
    public View initViewItem(final LayoutInflater inflater, int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_comment,
                    parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Comment comment = mAdapter.getItem(position);
        holder.commentsMemberid.setText(comment.getCreateUserName());
        holder.commentsContent.setText(comment.getCommentcontent().toString());
        holder.commentsCreatetime.setText((DateUtils.fromTodaySimple(comment.getCreatetime())));
//        String mBaseUrl = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
//        ImageLoader.getInstance().displayImage(mBaseUrl + headUrl, mholder.iamgeViewPortrait);


        final List<Reply> list = comment.getReplyList();
        int count = list.size();
        holder.layout.removeAllViews();
        if (count != 0) {
            for (int i = 0; i < count; i++) {
                TextView t = new TextView(getContext());
                t.setLayoutParams(new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)));
                t.setTextColor(getResources().getColor(R.color.tvc6));
                t.setTextSize(14f);

                String replyTo = list.get(i).getReplyToUserName();
                String replyContent = list.get(i).getReplycontent();
                final String creatName = list.get(i).getCreateUserName();
                if (Member.isLogin()) {
                    String name = Member.get().getUserName().equals(creatName) ? "我" : creatName;
                    String data = DateUtils.fromTodaySimple(list.get(i).getCreatetime());
                    if (name == null) {
                        name = "";
                    }
//                    t.setText(name + "  回复  "+replyTo+": "+replyContent+"   "+data);
                    String text = name + "  回复  " + replyTo + ": " + replyContent + "   " + data;
                    if (text != null) {
                        SpannableString spanText = new SpannableString(text);
                        spanText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange)), 0, name.length(),
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        spanText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange)), name.length() + 6, name.length() + 6 + replyTo.length(),
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        t.append(spanText);
                    }
                }
                holder.layout.addView(t);

                //回复评论中的回复
                final int finalI = i;
                t.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Member.get().getId() == list.get(finalI).getCreateuserid()) {
                            showToast("不能回复自己", Toast.LENGTH_SHORT);
                        } else {
                            Reply reply = list.get(finalI);
                            callBackComment.sendComment(reply.getCreateUserName(), comment.getId(), reply.getCreateuserid());
                        }
                    }
                });
            }
        }

        return convertView;
    }

    @Override
    public void dataSetChangedListener(List<Comment> data) {

    }

    //回复某条评论
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Comment comment = mAdapter.getItem(position);
        if (Member.get().getId() == comment.getCreateuserid()) {
            MyToast.showToast("不能回复自己");
        } else {
            callBackComment.sendComment(comment.getCreateUserName(), comment.getId(), comment.getCreateuserid());
        }
        //replyComment(mAdapter.getItem(position));
    }

    //加载更多
    public void loadMore() {
        RequestParams params = new RequestParams();
        params.put("type", type);
        params.put("dataId", dataId);
        params.put("page", mAdapter.getPage());
        params.put("rows", 8);
        //将用户输入的账号保存起来，以便下次使用
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        params.put("tokenid", ssid);
        post(RequestUtils.Code.COMMENT_LISTBOTTOM, params);
        sendMessageDelayed(101, 1000);
    }


    static class ViewHolder {
        @BindView(R.id.iamgeView_portrait)
        CircleImageView iamgeViewPortrait;
        @BindView(R.id.comments_memberid)
        TextView commentsMemberid;
        @BindView(R.id.comments_createtime)
        TextView commentsCreatetime;
        @BindView(R.id.comments_content)
        TextView commentsContent;
        @BindView(R.id.comment_reply)
        TextView commentReply;
        @BindView(R.id.layout)
        LinearLayout layout;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface CallBackComment {
        void sendComment(String name, int commentId, int replyToId);
    }

}
