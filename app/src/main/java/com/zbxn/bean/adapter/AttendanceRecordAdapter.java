package com.zbxn.bean.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.bean.AttendanceRecordEntity;
import com.zbxn.listener.ICustomListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import utils.StringUtils;

/**
 * 项目名称：考勤详情页面的Adapter
 * 创建人：LiangHanXin
 * 创建时间：2016/9/30 9:54
 */
public class AttendanceRecordAdapter extends BaseAdapter {

    private Context mCxontext;
    private List<AttendanceRecordEntity> mList;
    private ICustomListener listener;

    public AttendanceRecordAdapter(Context mContext, List<AttendanceRecordEntity> mList, ICustomListener listener) {
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
            convertView = View.inflate(mCxontext, R.layout.list_item_attendance_record, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final AttendanceRecordEntity entity = mList.get(position);

        holder.mTime.setText(StringUtils.convertDateMin(entity.getCheckintime()));
        holder.mState.setText(entity.getCheckInStateName());
        holder.mAddr.setText("地点:" + entity.getCheckinaddress());

        if (StringUtils.isEmpty(entity.getCheckinaddress())) {
            holder.mAddressLayout.setVisibility(View.GONE);
        } else {
            holder.mAddressLayout.setVisibility(View.VISIBLE);
        }
        if (StringUtils.isEmpty(entity.getCheckoutaddress())) {
            holder.mAddressLayout2.setVisibility(View.GONE);
        } else {
            holder.mAddressLayout2.setVisibility(View.VISIBLE);
        }

        if (entity.getCheckInStateName().equals("正常")) {
            holder.mState.setTextColor(mCxontext.getResources().getColor(R.color.tvc6));
        } else {
            holder.mState.setTextColor(mCxontext.getResources().getColor(R.color.orange));
        }
        if (entity.getCheckOutStateName().equals("正常")) {
            holder.mState2.setTextColor(mCxontext.getResources().getColor(R.color.tvc6));
        } else {
            holder.mState2.setTextColor(mCxontext.getResources().getColor(R.color.orange));
        }

        //(签到申诉状态) -2未申诉 -1：未处理  0 ：为正常1：为迟到2：为早退3：异常考勤
        if ("0".equals(entity.getState())) {
            holder.mHandleTip.setVisibility(View.INVISIBLE);
            holder.mHandle.setVisibility(View.GONE);
            holder.mHandleTip.setText("正常");//占位
        } else if ("1".equals(entity.getState())) {
            holder.mHandleTip.setVisibility(View.VISIBLE);
            holder.mHandle.setVisibility(View.GONE);
            holder.mHandleTip.setText("迟到");
        } else if ("2".equals(entity.getState())) {
            holder.mHandleTip.setVisibility(View.VISIBLE);
            holder.mHandle.setVisibility(View.GONE);
            holder.mHandleTip.setText("早退");
        } else if ("-1".equals(entity.getState())) {
            holder.mHandleTip.setVisibility(View.VISIBLE);
            holder.mHandle.setVisibility(View.GONE);
            holder.mHandleTip.setText("处理中");
        } else if ("-2".equals(entity.getState())) {
            holder.mHandleTip.setVisibility(View.GONE);
            holder.mHandle.setVisibility(View.VISIBLE);
            holder.mHandle.setText("申诉");

            if (entity.getCheckInStateName().equals("正常")) {
                holder.mHandleTip.setVisibility(View.GONE);
                holder.mHandle.setVisibility(View.INVISIBLE);
            } else {
                holder.mHandleTip.setVisibility(View.GONE);
                holder.mHandle.setVisibility(View.VISIBLE);
            }
        } else {
            holder.mHandleTip.setVisibility(View.VISIBLE);
            holder.mHandle.setVisibility(View.GONE);
            holder.mHandleTip.setText("异常考勤");
        }

        /*// -1未申诉 0 不通过， 1通过  2待批
        if ("0".equals(entity.getSignOutState())) {
            holder.mHandleTip2.setVisibility(View.VISIBLE);
            holder.mHandle2.setVisibility(View.GONE);
            holder.mHandleTip2.setText("不通过");
        } else if ("1".equals(entity.getSignOutState())) {
            holder.mHandleTip2.setVisibility(View.VISIBLE);
            holder.mHandle2.setVisibility(View.GONE);
            holder.mHandleTip2.setText("已通过");
        } else if ("2".equals(entity.getSignOutState())) {
            holder.mHandleTip2.setVisibility(View.VISIBLE);
            holder.mHandle2.setVisibility(View.GONE);
            holder.mHandleTip2.setText("待处理");
        } else {
            holder.mHandleTip2.setVisibility(View.GONE);
            holder.mHandle2.setVisibility(View.VISIBLE);
            holder.mHandle2.setText("申诉");
            if (entity.getCheckOutStateName().equals("正常")) {
                holder.mHandleTip2.setVisibility(View.GONE);
                holder.mHandle2.setVisibility(View.INVISIBLE);
            } else {
                holder.mHandleTip2.setVisibility(View.GONE);
                holder.mHandle2.setVisibility(View.VISIBLE);
            }
        }*/


        //(签到申诉状态) -2未申诉 -1：未处理  0 ：为正常1：为迟到2：为早退3：异常考勤
        if ("0".equals(entity.getSignOutState())) {
            holder.mHandleTip2.setVisibility(View.INVISIBLE);
            holder.mHandle2.setVisibility(View.GONE);
            holder.mHandleTip2.setText("正常");//占位
        } else if ("1".equals(entity.getSignOutState())) {
            holder.mHandleTip2.setVisibility(View.VISIBLE);
            holder.mHandle2.setVisibility(View.GONE);
            holder.mHandleTip2.setText("迟到");
        } else if ("2".equals(entity.getSignOutState())) {
            holder.mHandleTip2.setVisibility(View.VISIBLE);
            holder.mHandle2.setVisibility(View.GONE);
            holder.mHandleTip2.setText("早退");
        } else if ("-1".equals(entity.getSignOutState())) {
            holder.mHandleTip2.setVisibility(View.VISIBLE);
            holder.mHandle2.setVisibility(View.GONE);
            holder.mHandleTip2.setText("处理中");
        } else if ("-2".equals(entity.getSignOutState())) {
            holder.mHandleTip2.setVisibility(View.GONE);
            holder.mHandle2.setVisibility(View.VISIBLE);
            holder.mHandle2.setText("申诉");

            if (entity.getCheckOutStateName().equals("正常")) {
                holder.mHandleTip2.setVisibility(View.GONE);
                holder.mHandle2.setVisibility(View.INVISIBLE);
            } else {
                holder.mHandleTip2.setVisibility(View.GONE);
                holder.mHandle2.setVisibility(View.VISIBLE);
            }
        } else {
            holder.mHandleTip2.setVisibility(View.VISIBLE);
            holder.mHandle2.setVisibility(View.GONE);
            holder.mHandleTip2.setText("异常考勤");
        }

        holder.mHandle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCustomListener(1, entity, 0);
            }
        });
        holder.mHandle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCustomListener(2, entity, 0);
            }
        });

        if ("未签到".equals(entity.getCheckOutStateName())) {
            holder.mLayout2.setVisibility(View.GONE);
        } else {
            holder.mLayout2.setVisibility(View.VISIBLE);
            holder.mTime2.setText(StringUtils.convertDateMin(entity.getCheckouttime()));
            holder.mState2.setText(entity.getCheckOutStateName());
            holder.mAddr2.setText("地点:" + entity.getCheckoutaddress());
        }

        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.mTime)
        TextView mTime;
        @BindView(R.id.mState)
        TextView mState;
        @BindView(R.id.mAddr)
        TextView mAddr;
        @BindView(R.id.mLayout2)
        LinearLayout mLayout2;
        @BindView(R.id.mTime2)
        TextView mTime2;
        @BindView(R.id.mState2)
        TextView mState2;
        @BindView(R.id.mAddr2)
        TextView mAddr2;
        @BindView(R.id.mHandle)
        TextView mHandle;
        @BindView(R.id.mHandleTip)
        TextView mHandleTip;
        @BindView(R.id.mHandle2)
        TextView mHandle2;
        @BindView(R.id.mHandleTip2)
        TextView mHandleTip2;
        @BindView(R.id.mAddressLayout)
        LinearLayout mAddressLayout;
        @BindView(R.id.mAddressLayout2)
        LinearLayout mAddressLayout2;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
