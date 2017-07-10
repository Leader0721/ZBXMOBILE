package com.zbxn.activity.main.message.messagepager;


import com.zbxn.pub.frame.mvp.base.ControllerCenter;

/**
 * Created by Administrator on 2016/8/3.
 */
public interface IMessageView extends ControllerCenter {

    void showRefresh(int delay);

    void refreshComplete();

    void loadMoreComplete(boolean emptyResult, boolean hasMore);
}
