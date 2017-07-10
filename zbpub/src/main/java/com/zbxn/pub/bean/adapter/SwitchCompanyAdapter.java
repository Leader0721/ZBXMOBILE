package com.zbxn.pub.bean.adapter;

import android.content.Context;

import com.zbxn.pub.bean.ZmsCompanyListBean;
import com.zbxn.pub.bean.adapter.base.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GISirFive
 * @time 2016/8/11
 */
public class SwitchCompanyAdapter extends BaseAdapter<ZmsCompanyListBean>{

    private List<ZmsCompanyListBean> mList;

    /**
     * @param context
     * @param list
     */
    public SwitchCompanyAdapter(Context context, List<ZmsCompanyListBean> list) {
        super(context, list);
    }

    @Override
    public List<ZmsCompanyListBean> getDataList() {
        if(mList == null)
            mList = new ArrayList<>();
        return mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ZmsCompanyListBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
