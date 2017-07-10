package com.zbxn.activity.main.tools.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.pub.widget.draggridview.IDragGridAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author LiangHanXin
 * @time 2016/8/5
 */
public class ToolsAdapter extends BaseAdapter implements IDragGridAdapter {

    private LayoutInflater mInflater;
    private List<RecTool> mList;
    //处于悬浮状态的item
    private int mFloatItemPosition = -1;

    private  Context mContext;
    public ToolsAdapter(Context context, List<RecTool> list) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mList = new ArrayList<>();
        if (list != null)
            mList.addAll(list);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public RecTool getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View root, ViewGroup parent) {
        if (root == null) {
            root = mInflater.inflate(R.layout.main_toolscenter_toolsview_item, parent, false);
        }
//        root.setVisibility((position == mFloatItemPosition) ? View.INVISIBLE : View.VISIBLE);

        ImageView icon = (ImageView) root.findViewById(R.id.mIcon);
        TextView title = (TextView) root.findViewById(R.id.mName);

        RecTool tool = getItem(position);
        title.setText(tool.getTitle());
//        icon.setImageResource(tool.getId());
        //动态选择图片的加载
        int drawableId = mContext.getResources().getIdentifier(tool.getImg(), "mipmap", mContext.getPackageName());
        icon.setImageResource(drawableId);

        return root;
    }

    @Override
    public void reorderItems(int oldPosition, int newPosition) {
        RecTool item = getItem(oldPosition);
        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(mList, i, i + 1);
            }
        } else if (oldPosition > newPosition) {
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(mList, i, i - 1);
            }
        }
        mList.set(newPosition, item);
    }

    @Override
    public void setHideItem(int hidePosition) {
        this.mFloatItemPosition = hidePosition;
        notifyDataSetChanged();
    }


    /**
     * 重置数据
     *
     * @param list
     * @author GISirFive
     */
    public void resetData(List<RecTool> list) {
        mList.clear();
        if (list != null && !list.isEmpty()) {
            mList.addAll(list);
//            mList.add(new RecTool("更多", R.mipmap.temp126));
           /* RecTool recTool = new RecTool() ;
            recTool.setTitle("开发中");
            recTool.setMenuid("126");
            recTool.setImg("temp126");
            mList.add(recTool);*/
        }
        notifyDataSetChanged();
    }
}
