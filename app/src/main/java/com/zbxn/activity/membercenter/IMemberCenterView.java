package com.zbxn.activity.membercenter;

import com.zbxn.pub.frame.mvp.base.ControllerCenter;

/**
 * 项目名称：ZBXMobile
 * 创建人：LiangHanXin
 * 创建时间：2016/8/8 15:58
 */
public interface IMemberCenterView extends ControllerCenter {

    /**
     * 账号
     *
     */
    String getLoginName();

    /**
     * //姓名
     *
     */
    String getRemarkName();

    /**
     * //手机号
     *
     */
    String getTelephone();

    /**
     * 电话（外线）
     *
     */
    String getOfficeTelOut();



    /**
     * //邮箱
     * @return
     */
    String getEmail();

    /**
     * //生日
     * @return
     */
    String getBirthday();

    /**
     * //联系地址
     * @return
     */
    String getAddress();

    /**
     * //所在部门
     * @return
     */
    String getDepartmentName();




}
