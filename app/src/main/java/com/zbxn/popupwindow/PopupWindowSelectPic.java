package com.zbxn.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.listener.ICustomListener;

public class PopupWindowSelectPic extends PopupWindow {

    private TextView m_tv1, m_tv2, m_tv3;
    private View mMenuView;

    public PopupWindowSelectPic(Activity context, final ICustomListener listener) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pop_select_pic, null);
        m_tv1 = (TextView) mMenuView.findViewById(R.id.m_tv1);
        m_tv2 = (TextView) mMenuView.findViewById(R.id.m_tv2);
        m_tv3 = (TextView) mMenuView.findViewById(R.id.m_tv3);
        //取消按钮
        m_tv3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });
        //设置按钮监听
        m_tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //销毁弹出框
                dismiss();
                listener.onCustomListener(0,"",0);
            }
        });
        m_tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //销毁弹出框
                dismiss();
                listener.onCustomListener(1,"",0);
            }
        });
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y=(int) event.getY();
                if(event.getAction()== MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });

    }

}