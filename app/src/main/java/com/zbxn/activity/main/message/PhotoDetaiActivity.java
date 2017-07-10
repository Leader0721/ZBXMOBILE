package com.zbxn.activity.main.message;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbxn.R;
import com.zbxn.widget.PhotoViewPager;

import java.util.ArrayList;

import widget.photoview.PhotoView;

public class PhotoDetaiActivity extends Activity {
    private RelativeLayout m_layout;
    private PhotoViewPager mViewPager;

    private ArrayList<String> list_Ads = new ArrayList<String>(); // 需要展示的图片
    private int position = 0;

    private TextView m_tip;
    private TextView m_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        list_Ads = getIntent().getStringArrayListExtra("list");
        position = getIntent().getIntExtra("position", 0);

        m_layout = (RelativeLayout) findViewById(R.id.m_layout);
        mViewPager = (PhotoViewPager) findViewById(R.id.viewpager);
        m_tip = (TextView) findViewById(R.id.m_tip);
        m_finish = (TextView) findViewById(R.id.m_finish);

        m_tip.setText((position + 1) + "/" + String.valueOf(list_Ads.size()));

        mViewPager.setAdapter(new MyAdapter());
        mViewPager.setCurrentItem(position);

        m_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        m_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            // 页面更变
            @Override
            public void onPageSelected(int arg0) {
                m_tip.setText(String.valueOf(arg0 + 1) + "/" + String.valueOf(list_Ads.size()));
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private class MyAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            PhotoView imageView = new PhotoView(PhotoDetaiActivity.this);
            imageView.setMaximumScale(3.0f);
            imageView.setMinimumScale(1.0f);
            imageView.setScaleType(ScaleType.CENTER_CROP);
            imageView.setAdjustViewBounds(true);

            // 设置图片
            if (list_Ads.get(position).contains("http")) {
                ImageLoader.getInstance().displayImage(list_Ads.get(position), imageView);
            } else {
                try {
                    //是图片
                    ImageLoader.getInstance().displayImage("file:///" + list_Ads.get(position), imageView);
                } catch (Exception e) {

                }
            }

            container.addView(imageView, LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            return imageView;
        }

        @Override
        public int getCount() {
            return list_Ads.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((PhotoViewPager) container).removeView((View) object);
        }
    }
}
