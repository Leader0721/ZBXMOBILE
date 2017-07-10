package com.zbxn.activity.mission;

import android.view.View;

/**
 * Created by wj on 2016/11/9.
 */

public class PaddingTopAnim extends BaseAnim{

    public PaddingTopAnim(View target, int startValue, int endValue) {
        super(target, startValue, endValue);
    }

    @Override
    protected void doAnim(int animatedValue) {
        //将动画的值设置为view的当前的paddingTop
        target.setPadding(target.getPaddingLeft(),animatedValue,target.getPaddingRight()
                , target.getPaddingBottom());
    }



}
