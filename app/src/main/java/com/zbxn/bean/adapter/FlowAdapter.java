package com.zbxn.bean.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.zbxn.widget.flowlayout.OnInitSelectedPosition;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ysj on 2016/12/1.
 */
public class FlowAdapter<T> extends BaseAdapter implements OnInitSelectedPosition {

    private final Context mContext;
    private final List<T> mDataList;
    private int resource;
    private int viewId;

    public FlowAdapter(Context context, int resource, int viewId) {
        this.mContext = context;
        mDataList = new ArrayList<>();
        this.resource = resource;
        this.viewId = viewId;
    }



    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(resource, null);

        TextView textView = (TextView) view.findViewById(viewId);

        T t = mDataList.get(position);

        if (t instanceof String) {
            textView.setText((String) t);
        }
        return view;
    }

    public void onlyAddAll(List<T> datas) {
        mDataList.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 取消选择
     *
     * @param datas
     */
    public void clearAndAddAll(List<T> datas) {
        mDataList.clear();
        onlyAddAll(datas);
    }

    /**
     * return true
     * 单选模式下，默认选中第一个
     * <p/>
     * return false
     * 不默认选中
     *
     * @param position
     * @return
     */
    @Override
    public boolean isSelectedPosition(int position) {
        if (position % 2 == 0) {
            return false;
        }
        return false;
    }

}
