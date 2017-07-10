package com.zbxn.activity.createbulletin;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbxn.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/29.
 */
public class BulletinTypeAdapter extends BaseAdapter {

    private String label[];
    private Context mContext;

    public BulletinTypeAdapter(Context mContext, String label[]) {
        this.mContext = mContext;
        this.label = label;
    }

    @Override
    public int getCount() {
        return label.length;
    }

    @Override
    public Object getItem(int position) {
        return label[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_item_bulletin, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.spinnerText.setText(label[position]);

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.spinner_text)
        TextView spinnerText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
