package com.zbxn.bean.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbxn.R;
import com.zbxn.bean.OkrRankingEntity;
import com.zbxn.pub.utils.ConfigUtils;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author: ysj
 * @date: 2016-11-16 10:23
 */
public class OkrRankingAdapter extends BaseAdapter {

    private Context mContext;
    private List<OkrRankingEntity> mList;

    public OkrRankingAdapter(Context mContext, List<OkrRankingEntity> mList) {
        this.mContext = mContext;
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
            convertView = View.inflate(mContext, R.layout.list_item_okrranking, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.okrRanking.setBackgroundResource(0);
        OkrRankingEntity entity = mList.get(position);
        String mBaseUrl = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.userhead_img)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.userhead_img)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.userhead_img)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                //.displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                .build();                                   // 创建配置过得DisplayImageOption对象
        ImageLoader.getInstance().displayImage(mBaseUrl + entity.getUserIcon(), holder.mPortrait, options);
        holder.okrName.setText(entity.getUserName());
        holder.okrSection.setText(entity.getUserDepartName());
        holder.tvGeneral.setText(entity.getComScore() + "");
        holder.tvBusiness.setText(entity.getBizScore() + "");
        double okrDouble = entity.getOkr();
        DecimalFormat df = new DecimalFormat("#.##%");
        String okrIntegral = df.format(okrDouble);
        holder.tvOkr.setText(okrIntegral);
        holder.okrRanking.setText(entity.getOrderNO() + "");
        switch (entity.getOrderNO()) {
            case 1:
                holder.okrRanking.setText("");
                holder.okrRanking.setBackgroundResource(R.mipmap.one);
                break;
            case 2:
                holder.okrRanking.setText("");
                holder.okrRanking.setBackgroundResource(R.mipmap.two);
                break;
            case 3:
                holder.okrRanking.setText("");
                holder.okrRanking.setBackgroundResource(R.mipmap.three);
                break;
            default:
                break;
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.okr_ranking)
        TextView okrRanking;
        @BindView(R.id.mPortrait)
        CircleImageView mPortrait;
        @BindView(R.id.okr_name)
        TextView okrName;
        @BindView(R.id.okr_section)
        TextView okrSection;
        @BindView(R.id.tv_general)
        TextView tvGeneral;
        @BindView(R.id.tv_business)
        TextView tvBusiness;
        @BindView(R.id.tv_okr)
        TextView tvOkr;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
