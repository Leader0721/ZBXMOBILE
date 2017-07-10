package com.zbxn.activity.searchcontacts;

import android.widget.BaseAdapter;

import com.zbxn.pub.frame.mvp.base.ControllerCenter;

/**
 * @author GISirFive
 * @time 2016/8/11
 */
public interface ISearchContactsView extends ControllerCenter{

    void setAdapter(BaseAdapter adapter);

    /**
     * 获取搜索内容
     * @return
     */
    String getSearchContent();
}
