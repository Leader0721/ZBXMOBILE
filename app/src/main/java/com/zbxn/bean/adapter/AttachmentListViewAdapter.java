package com.zbxn.bean.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.bean.MissionAttachmentEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wj on 2016/11/17.
 * 查看附件页的附件适配器
 */
public class AttachmentListViewAdapter extends BaseAdapter {
    private Context mCxontext;
    private List list;

    public AttachmentListViewAdapter(Context mCxontext, List<MissionAttachmentEntity> list) {
        this.mCxontext = mCxontext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mCxontext, R.layout.item_attachment, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MissionAttachmentEntity entity = (MissionAttachmentEntity) list.get(position);

        //名称
        holder.mName.setText(entity.getAttachmentName());

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.mName)
        TextView mName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
