package com.zbxn.pub.frame.mvp.base;

/**
 * 视图控制中心
 * Created by GISirFive on 2016/7/26.
 */
public interface ControllerCenter {

    /**
     * 获取过场视图控制对象
     * @return
     */
    LoadingController loading();

    /**
     * 获取消息控制对象
     * @return
     */
    MessageController message();

}
