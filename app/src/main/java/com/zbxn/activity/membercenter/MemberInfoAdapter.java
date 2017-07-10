package com.zbxn.activity.membercenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zbxn.R;

import java.util.ArrayList;
import java.util.List;

public class MemberInfoAdapter extends BaseAdapter {
    private List<KeyValue> data;
    private LayoutInflater inflater;

    /**
     * 构造方法
     *
     * @param context 上下文对象
     * @param data    数据源
     */
    public MemberInfoAdapter(Context context, List<KeyValue> data) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return data.size();
    }

    /**
     * 设置数据源，通过该方法设置数据源，可以避免NullPointerException
     *
     * @param data 数据源
     */
    public void setData(List<KeyValue> data) {
        if (data == null) {
            data = new ArrayList<KeyValue>();
        }
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        // 判断是否重用convertView
        if (convertView == null) {
            // 不是重用 则加载模板
            convertView = inflater.inflate(R.layout.list_item_membercenter, null);
            // 创建ViewHolder对象
            holder = new ViewHolder();
            // 初始化ViewHolder中的各个控件
            holder.mKey = (TextView) convertView.findViewById(R.id.mKey);
            holder.mValue = (TextView) convertView.findViewById(R.id.mValue);
            holder.mImage = (ImageView) convertView.findViewById(R.id.mImage);
            // 将ViewHolder对象封装到convertView中
            convertView.setTag(holder);

        } else {
            // 是重用的，直接封装到convertView对象
            holder = (ViewHolder) convertView.getTag();
        }

        KeyValue contact = getItem(position);

        holder.mKey.setText(contact.getKey());
        holder.mValue.setText(contact.getValue());

        // 获取需要显示的数据

        if (position == 1 || position == 3 || position == 4 ) {
            holder.mImage.setVisibility(View.VISIBLE);
        } else {
            holder.mImage.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    class ViewHolder {
        TextView mKey;
        TextView mValue;
        ImageView mImage;
    }

    @Override
    public KeyValue getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

}
