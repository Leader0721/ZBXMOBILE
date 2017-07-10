package com.zbxn.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * 项目名称：我的申请页面的Entity
 * 创建人：LiangHanXin
 * 创建时间：2016/10/10 10:07
 */
public class ApplyEntity implements Serializable {

    /**
     * ID : 申请单
     * IDcreateTime : 创建时间
     * remark : null
     * state : 状态类型
     * lable : null
     * curApproveUserName : 创建人姓名
     * departmentName : 部门名称
     * userID : 创建人id
     * title : 申请表标题
     * operateLog : 操作记录
     * ZMSCompanyID : 公司id
     * statetext : 请求状态
     */
    @Expose
    private String ID;
    @Expose
    private String userName;
    @Expose
    private String createTime;
    @Expose
    private Object remark;
    @Expose
    private String state;
    @Expose
    private Object lable;
    @Expose
    private String curApproveUserName;
    @Expose
    private String departmentName;
    @Expose
    private String userID;
    @Expose
    private String title;


    public ApplyEntity(String state) {
        this.state = state;
    }

    @Expose
    private String operateLog;
    @Expose
    private String ZMSCompanyID;
    @Expose
    private String statetext;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Object getLable() {
        return lable;
    }

    public void setLable(Object lable) {
        this.lable = lable;
    }

    public String getCurApproveUserName() {
        return curApproveUserName;
    }

    public void setCurApproveUserName(String curApproveUserName) {
        this.curApproveUserName = curApproveUserName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOperateLog() {
        return operateLog;
    }

    public void setOperateLog(String operateLog) {
        this.operateLog = operateLog;
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
