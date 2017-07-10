package com.zbxn.bean;

import java.io.Serializable;

/**
 * 项目名称：ZBXMobile
 * 创建人：LiangHanXin
 * 创建时间：2016/10/10 13:56
 */
public class MyApprovalEntity implements Serializable {

    /**
     * ID : 申请单
     * IDcreateTime : 申请时间
     * remark : null
     * lable : null
     * userName : 申请人姓名
     * title : 申请表标题
     * ZMSCompanyID : 公司id
     * statetext : 请求状态
     */

    private String ID;
    private String IDcreateTime;
    private Object remark;
    private Object lable;
    private String userName;
    private String title;
    private String ZMSCompanyID;
    private String statetext;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getIDcreateTime() {
        return IDcreateTime;
    }

    public void setIDcreateTime(String IDcreateTime) {
        this.IDcreateTime = IDcreateTime;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    public Object getLable() {
        return lable;
    }

    public void setLable(Object lable) {
        this.lable = lable;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getZMSCompanyID() {
        return ZMSCompanyID;
    }

    public void setZMSCompanyID(String ZMSCompanyID) {
        this.ZMSCompanyID = ZMSCompanyID;
    }

    public String getStatetext() {
        return statetext;
    }

    public void setStatetext(String statetext) {
        this.statetext = statetext;
    }
}
