package com.zbxn.fragment.addrbookpy;

import android.widget.BaseAdapter;

import com.zbxn.pub.frame.mvp.base.ControllerCenter;

/**
 * @author GISirFive
 * @time 2016/8/8
 */
public interface IAddrBookView extends ControllerCenter{

    /**
     * 触发自动刷新
     */
    void autoRefresh();

    void setAdapter(BaseAdapter adapter);

    /**
     * 设置是否正在刷新
     */
    void refreshComplete();

}
