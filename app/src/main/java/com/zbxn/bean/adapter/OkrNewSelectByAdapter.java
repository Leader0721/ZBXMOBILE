package com.zbxn.bean.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.zbxn.R;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by Administrator on 2016/12/1.
 */
public class OkrNewSelectByAdapter extends BaseAdapter {
    /**
     * @param context
     * @param list
     */
    private Context mContext;
    private List<String>mList;

    public OkrNewSelectByAdapter(Context context, List list) {
        mContext = context;
        mList = list;
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
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = View.inflate(mContext,R.layout.grid_item_newokr_selectby,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.button_newOkr_selectBy.setText(mList.get(position));
        return convertView;
    }
    static class ViewHolder {
        @BindView(R.id.button_newOkr_selectBy)
        TextView button_newOkr_selectBy;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
