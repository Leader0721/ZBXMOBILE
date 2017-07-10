package com.zbxn.bean.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.bean.MissionDetailEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 项目名称：子任务
 * 创建人：LiangHanXin
 * 创建时间：2016/11/9 17:41
 */
public class TaskDetailsAdpter extends BaseAdapter {

    private Context mCxontext;
    private List<MissionDetailEntity> mList;
    private DeleteChildCallback callback;
    private boolean mIsCanDel;

    public TaskDetailsAdpter(Context mContext, List<MissionDetailEntity> mList, DeleteChildCallback callback, boolean isCanDel) {
        this.mCxontext = mContext;
        this.mList = mList;
        this.callback = callback;
        this.mIsCanDel = isCanDel;
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

    /**
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mCxontext, R.layout.list_item_taskdetails, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MissionDetailEntity entity = mList.get(position);
        //头像
        //ImageLoader.getInstance().displayImage(entity.getPersonCreateHeadUrl(), holder.mPortrait, DisplayImageOptions.createSimple());
//        String mBaseUrl = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
//        ImageLoader.getInstance().displayImage(mBaseUrl + entity.getPersonCreateHeadUrl(), holder.mPortrait);
        //姓名
        holder.mName.setText(entity.getPersonCreateName());
        //子任务进度
        holder.tv_progress.setText(entity.getDoneProgress() + "" + "%");
        holder.childTitle.setText(entity.getTaskTitle());
        holder.creatTime.setText(entity.getCreateTime().substring(0, 10));
        holder.endTime.setText(getWeekData(entity.getEndTime()));

        if (mIsCanDel) {
            holder.deleteChild.setVisibility(View.VISIBLE);
        } else {
            holder.deleteChild.setVisibility(View.INVISIBLE);
        }
        final String childId = entity.getID() + "";
        final String currentCompanyId = entity.getCurrentCompanyId() + "";
        holder.deleteChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.deleteChildTask(v, childId, currentCompanyId);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.mName)
        TextView mName;
        @BindView(R.id.tv_progress)
        TextView tv_progress;
        @BindView(R.id.delete_child)
        ImageView deleteChild;
        @BindView(R.id.child_title)
        TextView childTitle;
        @BindView(R.id.end_time)
        TextView endTime;
        @BindView(R.id.creat_time)
        TextView creatTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface DeleteChildCallback {
        void deleteChildTask(View view, String childId, String currentCompanyId);
    }

    /**
     * 加了星期的时间
     *
     * @param time 格式：yyyy-MM-dd HH:mm:ss
     * @return 格式：yyyy-MM-dd 星期 HH:mm:ss
     */
    public String getWeekData(String time) {
//        2016-11-29 11:44:12
        String weekDay = null;
        String day = null;
        String[] days = new String[]{"日", "一", "二", "三", "四", "五", "六"};
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 输入的日期格式必须是这种
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        Date date = null;// 把字符串转化成日期
        try {
            date = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        weekDay = "星期" + days[date.getDay()];
        String yearMonthDay = sdf1.format(date);
        String hourMinute = sdf2.format(date);
        day = yearMonthDay + " " + weekDay + " " + hourMinute;
        return day;
    }
}
