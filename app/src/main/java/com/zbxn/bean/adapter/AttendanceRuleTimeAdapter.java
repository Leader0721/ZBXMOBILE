package com.zbxn.bean.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbxn.bean.AttendanceRuleTimeEntity;
import com.zbxn.listener.ICustomListener;
import com.zbxn.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import utils.StringUtils;

/**
 * 项目名称：考勤详情页面的Adapter
 * 创建人：LiangHanXin
 * 创建时间：2016/9/30 9:54
 */
public class AttendanceRuleTimeAdapter extends BaseAdapter {

    private Context mCxontext;
    private List<AttendanceRuleTimeEntity> mList;
    private ICustomListener listener;

    public AttendanceRuleTimeAdapter(Context mContext, List<AttendanceRuleTimeEntity> mList, ICustomListener listener) {
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
            convertView = View.inflate(mCxontext, R.layout.list_item_attendance_ruletime, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final AttendanceRuleTimeEntity entity = mList.get(position);

        holder.mTimeStart.setText(StringUtils.convertDateMin(entity.getCheckintime()));
        holder.mTimeEnd.setText(StringUtils.convertDateMin(entity.getCheckouttime()));

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.mTimeStart)
        TextView mTimeStart;
        @BindView(R.id.mTimeEnd)
        TextView mTimeEnd;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
