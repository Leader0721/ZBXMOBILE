package com.zbxn.activity.main.tools.advert;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.zbxn.pub.utils.ConfigUtils;

/**
 * @author GISirFive
 * @time 2016/8/5
 */
public class AdvertHolder implements Holder<String> {
    private ImageView mImageView;

    @Override
    public View createView(Context context) {
        mImageView = new ImageView(context);
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return mImageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        Glide.with(context)
                .load(ConfigUtils.getConfig(ConfigUtils.KEY.server) + data)
                .into(mImageView);
    }
}
