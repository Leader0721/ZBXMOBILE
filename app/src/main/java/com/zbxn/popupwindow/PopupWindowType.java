package com.zbxn.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zbxn.bean.ApprovalEntity;
import com.zbxn.bean.adapter.PopupwindowTypeListAdapter;
import com.zbxn.R;

import java.util.ArrayList;
import java.util.List;

import utils.StringUtils;

/**
 * 弹窗主类
 */
public class PopupWindowType extends PopupWindow {
    private View mView;
    private Activity mContext;
    private ListView mListView;//类型
    private OnItemClickListener mListener;
    private long mFlag;


    private List<ApprovalEntity> mList = new ArrayList<ApprovalEntity>();

    public PopupWindowType(final Activity context, View view, float width, OnItemClickListener listener,
                           List<ApprovalEntity> list, String type, long flag) {

        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.popupwindow_palace_window, null);

        mContext = context;
        mList = list;
        mListener = listener;
        mFlag = flag;


        mListView = (ListView) mView.findViewById(R.id.list_view);//类型
        PopupwindowTypeListAdapter adapter = new PopupwindowTypeListAdapter(context, mList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onItemClick(parent, view, position, mFlag);
                dismiss();
            }
        });

        if (StringUtils.isEmpty(type)) {
            type = "全部";
        }
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).equals(type)) {
//                adapter.setSelectItem(i);
                break;
            }
        }
        adapter.notifyDataSetChanged();

        setData();
    }

    private void setData() {
        //设置SelectPicPopupWindow的View
        this.setContentView(mView);
        //设置SelectPicPopupWindow弹出窗体的宽
        //this.setWidth((int) width * 2);
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimBottom);
        //this.setAnimationStyle(R.style.mypopwindow_anim_style);
        //实例化一个ColorDrawable颜色为透明
//        ColorDrawable dw = new ColorDrawable(0x00000000);
        ColorDrawable dw = new ColorDrawable(0xfff5f5f5);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                dismiss();
                return true;
            }
        });
    }
}
