package com.zbxn.activity.main.tools.tools;

import com.zbxn.pub.frame.mvp.base.ControllerCenter;

import java.util.List;

/**
 * @author GISirFive
 * @time 2016/8/5
 */
public interface IToolsView extends ControllerCenter{


    /**
     * 重新绑定插件
     * @param list
     */
    void resetData(List<RecTool> list);
}
