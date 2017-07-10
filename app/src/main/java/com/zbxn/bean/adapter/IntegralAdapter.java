package com.zbxn.bean.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbxn.R;
import com.zbxn.activity.login.LoginActivity;
import com.zbxn.bean.IntegralEntity;
import com.zbxn.pub.utils.ConfigUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import utils.PreferencesUtils;

/**
 * 项目名称：排行详情
 * 创建人：LiangHanXin
 * 创建时间：2016/11/1 11:08
 */
public class IntegralAdapter extends BaseAdapter {
    private Context mCxontext;
    private List<IntegralEntity> mList;

    public IntegralAdapter(Context mContext, List<IntegralEntity> mList) {
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
            convertView = View.inflate(mCxontext, R.layout.list_item_integral, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mRanking.setBackgroundResource(0);
        IntegralEntity entity = mList.get(position);
        /**
         * 把获取到的数据显示出来
         */
        holder.mName.setText(entity.getUsername());//名字
        holder.mIntegral.setText(entity.getUserScore() + "");//N币
        holder.tvId.setText("ID:"+entity.getNumber() + "");          //
        String mBaseUrl = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.userhead_img)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.userhead_img)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.userhead_img)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                //.displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                .build();                                   // 创建配置过得DisplayImageOption对象
        ImageLoader.getInstance().displayImage(mBaseUrl + entity.getTouxiang(), holder.mPortrait, options);
        holder.mRanking.setText(entity.getPaiming() + "");

        switch (entity.getPaiming()) {
            case 1:
                holder.mRanking.setText("");
                holder.mRanking.setBackgroundResource(R.mipmap.one);
                break;
            case 2:
                holder.mRanking.setText("");
                holder.mRanking.setBackgroundResource(R.mipmap.two);
                break;
            case 3:
                holder.mRanking.setText("");
                holder.mRanking.setBackgroundResource(R.mipmap.three);
                break;
        }

        String userID = PreferencesUtils.getString(mCxontext, LoginActivity.FLAG_INPUT_ID);
        if (userID.equals(entity.getUserID() + "")) {
            holder.mLayout.setBackgroundColor(mCxontext.getResources().getColor(R.color.main_tab_text_wathet));
        } else {
            holder.mLayout.setBackgroundColor(mCxontext.getResources().getColor(R.color.white));
        }

        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.mPortrait)
        CircleImageView mPortrait;
        @BindView(R.id.mRanking)
        TextView mRanking;
        @BindView(R.id.mName)
        TextView mName;
        @BindView(R.id.tv_id)
        TextView tvId;
        @BindView(R.id.mIntegral)
        TextView mIntegral;
        @BindView(R.id.mLayout)
        LinearLayout mLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
