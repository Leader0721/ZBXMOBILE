package com.zbxn.bean.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.bean.IntergralDatailsEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 项目名称：ZBXMobile
 * 创建人：LiangHanXin
 * 创建时间：2016/11/2 10:31
 */
public class InteragrailsAdapter extends BaseAdapter {

    private Context mCxontext;
    private List<IntergralDatailsEntity> mList;

    public InteragrailsAdapter(Context mContext, List<IntergralDatailsEntity> mList) {
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
            convertView = View.inflate(mCxontext, R.layout.list_item_integraldetails, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        IntergralDatailsEntity entity = mList.get(position);

/**
 * 把获取到的数据显示出来
 */
//        mholder.mName.setText(entity.getUsername());//名字
//        mholder.mCompany.setText(entity.getZmsname());//公司名字
//        mholder.mIntegral.setText(entity.getUserScore() + "");//N币
//
//        String mBaseUrl = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
//        ImageLoader.getInstance().displayImage(mBaseUrl + entity.getTouxiang(), mholder.mPortrait);
//        mholder.mRanking.setText(position + 1 + "");

        holder.mType.setText(entity.getSourceReason() + "");//类型
        holder.mTime.setText(entity.getCreateTime() + "");//时间

        if (entity.getUserScore() > 0) {
            holder.mAddAndSubtract.setText("+" + entity.getUserScore() + "");//N币的加减
            holder.mAddAndSubtract.setTextColor(Color.parseColor("#ff9d00"));
        } else {
            holder.mAddAndSubtract.setText(entity.getUserScore() + "");//N币的加减
            holder.mAddAndSubtract.setTextColor(Color.parseColor("#666666"));
        }


        holder.mIntegral.setText(entity.getBalance() + "");//剩余N币


        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.mType)
        TextView mType;
        @BindView(R.id.mTime)
        TextView mTime;
        @BindView(R.id.mAddAndSubtract)
        TextView mAddAndSubtract;
        @BindView(R.id.mIntegral)
        TextView mIntegral;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}



