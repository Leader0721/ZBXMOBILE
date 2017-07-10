package com.zbxn.fragment.shortcut;

import com.zbxn.pub.bean.WorkBlog;
import com.zbxn.pub.frame.mvp.base.ControllerCenter;

/**
 * @author GISirFive
 * @time 2016/8/16
 */
public interface IShortCutView extends ControllerCenter{

    /**
     * 打开编写日志的页面
     * @param blog
     */
    void openCreateWorkBlog(WorkBlog blog);
}
