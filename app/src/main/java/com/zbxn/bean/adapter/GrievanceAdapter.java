package com.zbxn.bean.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbxn.bean.GrievanceTypeEntity;
import com.zbxn.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/30.
 */
public class GrievanceAdapter extends BaseAdapter {

    private Context mContext;
    private List<GrievanceTypeEntity> mList;

    public GrievanceAdapter(Context mContext, List<GrievanceTypeEntity> mList) {
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
            convertView = View.inflate(mContext, R.layout.list_item_schedule_remind, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.myRemind.setText(mList.get(position).getAppealTypeName());

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.myRemind)
        TextView myRemind;
        @BindView(R.id.mImageview)
        ImageView mImageview;
        @BindView(R.id.mRepeat1)
        LinearLayout mRepeat1;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
