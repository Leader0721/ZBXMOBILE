package com.zbxn.activity.modifypassword;

import com.zbxn.pub.frame.mvp.base.ControllerCenter;

/**
 * 项目名称：ZBXMobile
 * 创建人：LiangHanXin
 * 创建时间：2016/8/11 11:36
 */
public interface IModifyPasswordView extends ControllerCenter {
    /**
     * 原密码
     */

    String getOriginalPassword();
    /**
     * 修改后的密码
     */
    String getNewPassword();
    /**
     * 再次输入的密码
     */
    String getSecondPassword();

    /**
     * 关闭页面并返回结果
     * @param b
     */
    void finishForResult(boolean b);
}
