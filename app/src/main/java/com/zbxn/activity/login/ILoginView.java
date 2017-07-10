package com.zbxn.activity.login;

import com.zbxn.pub.frame.mvp.base.ControllerCenter;

/**
 * Created by GISirFive on 2016/8/3.
 */
public interface ILoginView extends ControllerCenter{

    String getUserName();

    String getPassword();

    /**
     * 关闭页面并返回结果
     * @param b
     */
    void finishForResult(boolean b);

}
