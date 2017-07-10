package com.zbxn.pub.bean.adapter;

import android.content.Context;

import com.zbxn.pub.bean.Bulletin;
import com.zbxn.pub.bean.adapter.base.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 通知公告适配器
 *
 * @author GISirFive
 * @since 2016-7-13 下午9:03:13
 */
public class BulletinAdapter extends BaseAdapter<Bulletin> {

    private List<Bulletin> mList;

    public BulletinAdapter(Context context, List<Bulletin> list) {
        super(context, list);
    }

    @Override
    public int getCount() {
        return getDataList().size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public List<Bulletin> getDataList() {
        if (mList == null)
            mList = new ArrayList<>();
        return mList;
    }

    @Override
    public Bulletin getItem(int position) {
        return getDataList().get(position);
    }

}
