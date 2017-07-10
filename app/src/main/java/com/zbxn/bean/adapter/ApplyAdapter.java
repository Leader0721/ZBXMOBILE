package com.zbxn.bean.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbxn.bean.ApplyEntity;
import com.zbxn.listener.ICustomListener;
import com.zbxn.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 项目名称：申请列表的adapter
 * 创建人：LiangHanXin
 * 创建时间：2016/10/10 10:05
 */
public class ApplyAdapter extends BaseAdapter {

    private Context mCxontext;
    private List<ApplyEntity> mList;
    private ICustomListener listener;

    public ApplyAdapter(Context mContext, List<ApplyEntity> mList, ICustomListener listener) {
        this.mCxontext = mContext;
        this.mList = mList;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mCxontext, R.layout.list_item_myapproval, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ApplyEntity entity = mList.get(position);

/**
 * 把获取到的数据显示出来
 */
        holder.myType.setText(entity.getTitle());
        holder.mTime.setText(entity.getCreateTime());
        holder.mName.setText(entity.getUserName());
        holder.mTitle.setText(entity.getStatetext());
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.myType)
        TextView myType;
        @BindView(R.id.mTime)
        TextView mTime;
        @BindView(R.id.mName)
        TextView mName;
        @BindView(R.id.mTitle)
        TextView mTitle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
