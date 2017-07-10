package com.zbxn.bean.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.listener.ICustomListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/29.
 */
public class FrameSearchAdapter extends BaseAdapter{
    private Context mContext;
    private List<String> mList;
    private ICustomListener mListener;// 自定义项视图源

    public FrameSearchAdapter(Context mContext, List<String> mList, ICustomListener listener) {
        this.mContext = mContext;
        this.mList = mList;
        this.mListener = listener;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_item_newokr_search, null);
            viewholder = new ViewHolder(convertView);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        // 设置文字和图片
        final String result = mList.get(position);

        viewholder.mName.setText(result);
        viewholder.mDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onCustomListener(0, result, position);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.m_name)
        TextView mName;
        @BindView(R.id.m_del)
        ImageView mDel;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
