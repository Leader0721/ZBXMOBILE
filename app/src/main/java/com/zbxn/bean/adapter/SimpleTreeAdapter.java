package com.zbxn.bean.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.bean.Tests;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import widget.treelistview.Node;
import widget.treelistview.TreeListViewAdapter;

/**
 * Created by Administrator on 2016/9/8.
 */
public class SimpleTreeAdapter<T> extends TreeListViewAdapter<T> {

    private List<Tests> mSelectedList;
    private List<Boolean> booleenList;

    /**
     * 当前是否为多选模式
     */
    private boolean multiChoiceEnable = false;

    public SimpleTreeAdapter(ListView mTree, Context context, List<T> datas, int defaultExpandLevel) throws IllegalArgumentException, IllegalAccessException {
        super(mTree, context, datas, defaultExpandLevel);
        booleenList = new ArrayList<>();
    }

    @Override
    public View getConvertView(final Node node, final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_address, viewGroup, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (node.getIcon() == -1) {
            holder.addImage.setVisibility(View.VISIBLE);
            holder.addImage.setImageResource(R.mipmap.temp110);
            holder.addHeadImg.setVisibility(View.GONE);
        } else {
            holder.addImage.setVisibility(View.GONE);
            holder.addHeadImg.setVisibility(View.VISIBLE);
            if (node.isExpand()) {
                holder.addHeadImg.setImageResource(R.mipmap.expand);
            } else {
                holder.addHeadImg.setImageResource(R.mipmap.noexpand);
            }
        }


        //判断checkbox是否显示
        holder.mCheckBox.setVisibility(multiChoiceEnable ? View.VISIBLE : View.GONE);

        if (holder.mCheckBox.getVisibility() == View.VISIBLE) {
            if (booleenList.size() == 0) {
                for (int i = 0; i < getCount(); i++) {
                    booleenList.add(holder.mCheckBox.isChecked());
                }
            }
            holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    booleenList.set(position, isChecked);
                }
            });

            final ViewHolder finalHolder = holder;
            holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isCheck = false;
                    if (finalHolder.mCheckBox.isChecked()) {
                        isCheck = false;
                        booleenList.set(position, isCheck);
                    } else {
                        isCheck = true;
                        booleenList.set(position, isCheck);
                    }
                    if (mNodes.get(position).getIcon() != -1) {
                        for (int i = position + 1; i < position + mNodes.get(position).getChildren().size() + 1; i++) {
                            booleenList.set(i, isCheck);
                        }
                    }
                    notifyDataSetChanged();
                }

            });

            //如果child中有未选中的，则取消节点的选中状态
            if (mNodes.get(position).getIcon() != -1) {
                List<Boolean> list = new ArrayList<>();
                for (int i = position + 1; i < position + mNodes.get(position).getChildren().size() + 1; i++) {
                    list.add(booleenList.get(i));
                }
                if (list.contains(false)) {
                    booleenList.set(position, false);
                } else {
                    booleenList.set(position, true);
                }
            }

            mSelectedList.clear();
            for (int i = 0; i < mNodes.size(); i++) {
                if (mNodes.get(i).getIcon() == -1) {
                    if (booleenList.get(i)) {
                        Tests tests = new Tests();
                        tests.set_id(mNodes.get(i).getId());
                        tests.setName(mNodes.get(i).getName());
                        Log.e("adad", "_______________" + mNodes.get(i).getId() + "________" + mNodes.get(i).getName());
                        if (mSelectedList.contains(tests)) {
                            mSelectedList.remove(tests);
                        } else {
                            mSelectedList.add(tests);
                        }
                    }
                }
            }
            Log.e("qqqq", "___" + mSelectedList.size());
            holder.mCheckBox.setChecked(booleenList.get(position));
        }
//            }

        holder.addName.setText(node.getName());
        return convertView;
    }

    public boolean getMultiChoiceEnable() {
        return this.multiChoiceEnable;
    }

    /**
     * ·
     * 设置当前是否为多选模式
     *
     * @param b
     */
    public void setMultiChoiceEnable(boolean b) {
        if (multiChoiceEnable == b)
            return;
        multiChoiceEnable = b;
        if (multiChoiceEnable)
            mSelectedList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void changeItemSelectState(int position) {
        if (mNodes.get(position).getIcon() == -1) {

        } else {
            int ids = mNodes.get(position).getId();
        }
//        if (mSelectedList.contains(t))
//            mSelectedList.remove(t);
//        else
//            mSelectedList.add(t);
//        notifyDataSetChanged();
    }

    public List<Tests> getSelectedList() {
        return mSelectedList;
    }

    class ViewHolder {
        @BindView(R.id.add_image)
        CircleImageView addImage;
        @BindView(R.id.add_name)
        TextView addName;
        @BindView(R.id.add_head_img)
        ImageView addHeadImg;
        @BindView(R.id.add_layout)
        LinearLayout addLayout;
        @BindView(R.id.add_mCheckBox)
        CheckBox mCheckBox;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
