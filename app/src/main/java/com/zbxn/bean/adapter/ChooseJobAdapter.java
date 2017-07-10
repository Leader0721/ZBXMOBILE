package com.zbxn.bean.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.bean.JobEntity;
import com.zbxn.bean.RepeatNewTaskEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 项目名称：ZBXMobile
 * 创建人：LiangHanXin
 * 创建时间：2016/9/27 16:28
 */
public class ChooseJobAdapter extends BaseAdapter {


    private Context mCxontext;
    private List<JobEntity> mList;

    public ChooseJobAdapter(Context mContext, List<JobEntity> mList) {
        this.mCxontext = mContext;
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
            convertView = View.inflate(mCxontext, R.layout.list_item_schedule_remind, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //获取到的数据显示上去
        holder.myRemind.setText(mList.get(position).getPositionName());

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.myRemind)
        TextView myRemind;
        @BindView(R.id.mImageview)
        ImageView mImageview;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
