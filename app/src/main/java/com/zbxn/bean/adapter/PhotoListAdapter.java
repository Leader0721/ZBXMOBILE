package com.zbxn.bean.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbxn.R;
import com.zbxn.bean.PhotosEntity;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.utils.Utils;

import java.util.List;

/**
 * 适配器Adapter类
 */
public class PhotoListAdapter extends BaseAdapter {
    private Context context;
    private List<PhotosEntity> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    private int itemViewResource;// 自定义项视图源
    private ICustomListener listener;//
    private boolean mIsVisDel;//

    /**
     * 实例化Adapter
     *
     * @param context
     * @param data
     * @param resource
     */
    public PhotoListAdapter(Context context, List<PhotosEntity> data, int resource, ICustomListener listener, boolean isVisDel) {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.itemViewResource = resource;
        this.listItems = data;
        this.listener = listener;
        this.mIsVisDel = isVisDel;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    /**
     * ListView Item设置
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = listContainer.inflate(this.itemViewResource, parent, false);
        }

        ImageView m_img = (ImageView) convertView.findViewById(R.id.m_img);
        ImageView m_del = (ImageView) convertView.findViewById(R.id.m_del);


        // 设置文字和图片
        final PhotosEntity result = listItems.get(position);
        if ("tzs_paizhao".equals(result.getImgurl())) {
            m_img.setImageResource(R.mipmap.select_add);
            m_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCustomListener(1, result, position);//添加
                }
            });
            m_del.setVisibility(View.GONE);
        } else {
            if (result.getImgurl().contains("http")) {
                m_del.setVisibility(View.GONE);
                ImageLoader.getInstance().displayImage(result.getImgurl(), m_img);
            } else {
                m_del.setVisibility(View.VISIBLE);
                try {
                    //是图片
                    ImageLoader.getInstance().displayImage("file:///" + result.getImgurl(), m_img);
                } catch (Exception e) {

                }
            }
            m_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCustomListener(3, result, position);//显示
                }
            });
        }
        if (!mIsVisDel) {
            m_del.setVisibility(View.GONE);
        } else {
            m_del.setVisibility(View.VISIBLE);
            if ("tzs_paizhao".equals(result.getImgurl())) {
                m_del.setVisibility(View.GONE);
            }
        }
        m_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCustomListener(0, result, position);//删除
            }
        });

        Utils.setPicHeightExRelativeLayout(context, m_img, 50, 50, 4f, 50f);

        return convertView;
    }

}