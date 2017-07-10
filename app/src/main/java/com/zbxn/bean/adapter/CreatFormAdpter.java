package com.zbxn.bean.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbxn.bean.ApprovalEntity;
import com.zbxn.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ysj on 2016/11/5.
 */
public class CreatFormAdpter extends BaseAdapter {

    private Context mContext;
    private List<ApprovalEntity> mList;

    public CreatFormAdpter(Context mContext, List<ApprovalEntity> mList) {
        this.mContext = mContext;
        this.mList = mList;
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
            convertView = View.inflate(mContext, R.layout.list_item_creatform, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.creatForm.setText(mList.get(position).getName());

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.creat_form)
        TextView creatForm;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
