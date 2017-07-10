package com.zbxn.pub.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 项目名称：ViewPager禁止滑动
 * 创建人：LiangHanXin
 * 创建时间：2016/10/20 15:37
 */
public class NoScrollViewPager extends ViewPager {

    private boolean mScrollble = false;


    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!mScrollble) {
            return false;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mScrollble) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public boolean isCanScrollble() {
        return mScrollble;
    }

    public void setCanScrollble(boolean scrollble) {
        this.mScrollble = scrollble;
    }
}
