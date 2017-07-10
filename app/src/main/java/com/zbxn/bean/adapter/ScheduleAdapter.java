package com.zbxn.bean.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbxn.bean.ScheduleRuleEntity;
import com.zbxn.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import utils.StringUtils;

/**
 * Created by Administrator on 2016/9/27.
 */
public class ScheduleAdapter extends BaseAdapter {

    private Context mCxontext;
    private List<ScheduleRuleEntity> mList;

    public ScheduleAdapter(Context mContext, List<ScheduleRuleEntity> mList) {
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
            convertView = View.inflate(mCxontext, R.layout.list_item_schedule, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ScheduleRuleEntity entity = mList.get(position);

        holder.schedStartTime.setText(StringUtils.convertDateMin(entity.getStarttime()));
        holder.schedEndTime.setText(entity.getEndtime());
        holder.schedEndTime.setVisibility(View.GONE);
//        mholder.schedImage.setText(text);
        holder.schedName.setText(entity.getTitle());
        holder.schedContent.setText(entity.getScheduleDetail());
        holder.schedLocation.setText(entity.getLocation());
        //mholder.myName.setVisibility(View.GONE);
        holder.myContent.setVisibility(View.GONE);
        // (0:创建的日程 1：分享的日程 2：参与的日程)
        if ("0".equals(entity.getScheduleRuleType())) {
            holder.schedCircle.setImageResource(R.mipmap.schedule_circle_type1);
        } else if ("1".equals(entity.getScheduleRuleType())) {
            holder.schedCircle.setImageResource(R.mipmap.schedule_circle_type2);
        } else if ("2".equals(entity.getScheduleRuleType())) {
            holder.schedCircle.setImageResource(R.mipmap.schedule_circle_type3);
        }

        return convertView;
    }



    static class ViewHolder {
        @BindView(R.id.sched_start_time)
        TextView schedStartTime;
        @BindView(R.id.sched_end_time)
        TextView schedEndTime;
        @BindView(R.id.sched_circle)
        ImageView schedCircle;
        @BindView(R.id.sched_image)
        CircleImageView schedImage;
        @BindView(R.id.sched_name)
        TextView schedName;
        @BindView(R.id.sched_content)
        TextView schedContent;
        @BindView(R.id.sched_location)
        TextView schedLocation;
        @BindView(R.id.myName)
        LinearLayout myName;
        @BindView(R.id.myContent)
        LinearLayout myContent;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
