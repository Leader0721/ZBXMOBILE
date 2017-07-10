package com.zbxn.pub.bean.adapter;

import android.content.Context;

import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.bean.adapter.base.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GISirFive
 * @time 2016/8/11
 */
public class ContactsAdapter extends BaseAdapter<Contacts>{

    private List<Contacts> mList;

    /**
     * @param context
     * @param list
     */
    public ContactsAdapter(Context context, List<Contacts> list) {
        super(context, list);
    }

    @Override
    public List<Contacts> getDataList() {
        if(mList == null)
            mList = new ArrayList<>();
        return mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Contacts getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
