package com.zbxn.fragment.addrbookgroup;

import com.zbxn.pub.frame.mvp.base.ControllerCenter;

/**
 * @author GISirFive
 * @time 2016/8/8
 */
public interface IAddrBookView extends ControllerCenter{

    ContactsAdapter getAdapter();

    /**
     * 触发自动刷新
     */
    void autoRefresh();

    /**
     * 设置是否正在刷新
     */
    void refreshComplete();

}
