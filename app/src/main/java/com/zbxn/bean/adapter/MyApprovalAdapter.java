package com.zbxn.bean.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.bean.MissionEntity;
import com.zbxn.listener.ICustomListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名称：审批列表的adapter
 * 创建人：LiangHanXin
 * 创建时间：2016/10/10 13:58
 */
public class MyApprovalAdapter extends BaseAdapter {

    private Context mCxontext;
    private List<MissionEntity> mList;
    private ICustomListener listener;

    public MyApprovalAdapter(Context mContext, List<MissionEntity> mList, ICustomListener listener) {
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
        final MissionEntity entity = mList.get(position);


        return null;
    }

    static class ViewHolder {
        @BindView(R.id.mPortrait)
        CircleImageView mPortrait;
        @BindView(R.id.mName)
        TextView MyName;
        @BindView(R.id.myType)
        TextView myType;
        @BindView(R.id.mTime)
        TextView mTime;
        @BindView(R.id.mTitle)
        TextView mTitle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
