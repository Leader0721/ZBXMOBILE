package com.zbxn.pub.bean.adapter;

import android.content.Context;

import com.zbxn.pub.bean.Reply;
import com.zbxn.pub.bean.adapter.base.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/15.
 */
public class ReplyAdapter extends BaseAdapter<Reply> {

    private List<Reply> mList;

    /**
     * @param context
     * @param list
     */
    public ReplyAdapter(Context context, List<Reply> list) {
        super(context, list);
    }

    @Override
    public List<Reply> getDataList() {
        if (mList == null)
            mList = new ArrayList<>();
        return mList;
    }

    @Override
    public int getCount() {
        return getDataList().size();
    }

    @Override
    public Reply getItem(int position) {
        return getDataList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
