package com.zbxn.activity.collectcenter;

import android.widget.BaseAdapter;

import com.zbxn.pub.frame.mvp.base.ControllerCenter;

/**
 * @author GISirFive
 * @time 2016/8/18
 */
public interface ICollectCenterView extends ControllerCenter{

    void setAdapter(BaseAdapter adapter);

    void showRefresh(int delay);

    void refreshComplete();

    void loadMoreComplete(boolean emptyResult, boolean hasMore);
}
