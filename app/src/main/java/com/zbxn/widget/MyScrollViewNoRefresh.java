package com.zbxn.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by wuzy on 2014/7/26.
 */
public class MyScrollViewNoRefresh extends ScrollView {
    private boolean isRefresh = false;

    public MyScrollViewNoRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //禁止ScrollView内的控件改变之后自动滚动
    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        if (!isRefresh) {
            return 0;
        } else {
            return super.computeScrollDeltaToGetChildRectOnScreen(rect);
        }
    }

    /**
     * 设置是否让ScrollView内的控件改变之后自动滚动
     * @param flag
     */
    public void setIsRefresh(boolean flag){
        isRefresh = flag;
    }
}
