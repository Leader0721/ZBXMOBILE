package com.zbxn.fragment.comment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.zbxn.fragment.CommentFragment;
import com.zbxn.R;

/**
 * @author GISirFive
 * @time 2016/8/12
 */
public class CommentFooterView implements View.OnClickListener {

    private CommentFragment mCommentFragment;
    private ListView mListView;

    private TextView mMessage;

    public CommentFooterView(CommentFragment fragment, ListView listView) {
        mCommentFragment = fragment;
        mListView = listView;
    }

    private void refreshFooter() {
        try {
            //已加入footer
            if (mListView.getFooterViewsCount() != 0)
                return;
            //列表中没有数据,不加载footer
            if (mListView.getAdapter().getCount() == 0)
                return;

            View footer = LayoutInflater.from(mCommentFragment.getActivity())
                    .inflate(R.layout.fragment_comment_footer, mListView, false);
            footer.setOnClickListener(this);
            mMessage = (TextView) footer.findViewById(R.id.mFooterMessage);

            mListView.addFooterView(footer, null, false);
        } catch (Exception e) {

        }
    }

    /**
     * 加载完成
     *
     * @param hasMore
     */
    public void loadMoreComplete(boolean hasMore) {
        refreshFooter();
        if (mMessage != null) {
            String message = hasMore ? "加载更多" : "到底了";
            mMessage.setText(message);
        }
    }

    @Override
    public void onClick(View v) {
        mMessage.setText("正在加载...");
        mCommentFragment.loadMore();
    }
}
