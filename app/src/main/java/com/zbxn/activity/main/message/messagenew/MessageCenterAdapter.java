package com.zbxn.activity.main.message.messagenew;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbxn.R;
import com.zbxn.activity.main.message.MessageCenterPager;
import com.zbxn.bean.OaAlertList;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import utils.StringUtils;

/**
 * Created by Administrator on 2016/12/19.
 */
public class MessageCenterAdapter extends BaseAdapter {
    private List<OaAlertList> mLists;
    private Context mContext;

    public MessageCenterAdapter(List<OaAlertList> mLists, Context mContext) {
        this.mLists = mLists;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int position) {
        return mLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_item_newmessagecenter, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OaAlertList entity = mLists.get(position);
        holder.title_messagecenter.setText(entity.getTitle());
        holder.content_messagecenter.setText(entity.getContent() + "");
        if (entity.getUnReadCount() > 0) {
            holder.redicon_messagecenter.setVisibility(View.VISIBLE);
        } else {
            holder.redicon_messagecenter.setVisibility(View.GONE);
        }
        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);
        ImageLoader.getInstance().displayImage(server + entity.getImg(), holder.icon_messagecenter);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.linearlayout_MessageCenter)
        LinearLayout linearLayout_messageCenter;
        @BindView(R.id.imageView_MessageCenter)
        ImageView icon_messagecenter;
        @BindView(R.id.imageView1_MessageCenter)
        ImageView redicon_messagecenter;
        @BindView(R.id.title_MessageCenter)
        TextView title_messagecenter;
        @BindView(R.id.content_MessageCenter)
        TextView content_messagecenter;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
