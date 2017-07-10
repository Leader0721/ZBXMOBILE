package com.zbxn.pub.bean.adapter;

import android.content.Context;

import com.zbxn.pub.bean.Comment;
import com.zbxn.pub.bean.adapter.base.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/10.
 */
public class CommentAdapter extends BaseAdapter<Comment> {

    private List<Comment> mList;

    /**
     * @param context
     * @param list
     */
    public CommentAdapter(Context context, List<Comment> list) {
        super(context, list);
    }

    @Override
    public List<Comment> getDataList() {
        if (mList == null)
            mList = new ArrayList<>();
        return mList;
    }

    @Override
    public int getCount() {
        return getDataList().size();
    }

    @Override
    public Comment getItem(int position) {
        return getDataList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
