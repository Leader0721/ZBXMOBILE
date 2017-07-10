package com.zbxn.bean;

import com.google.gson.annotations.Expose;

/**
 * @author: ysj
 * @date: 2016-11-08 11:07
 */
public class ApprovalInfoEntity {

    /**
     * userID : 1374
     * positionID : 1
     * theApply : 0
     * ZMSCompanyID : 1
     * ID : 384
     * state : 0
     * curApproveUserID : 0
     * type : 9
     * departmentID : 0
     */
    @Expose
    private int userID;
    @Expose
    private int positionID;
    @Expose
    private int theApply;
    @Expose
    private int ZMSCompanyID;
    @Expose
    private int ID;
    @Expose
    private int state;
    @Expose
    private int curApproveUserID;
    @Expose
    private int type;
    @Expose
    private int departmentID;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getPositionID() {
        return positionID;
    }

    public void setPositionID(int positionID) {
        this.positionID = positionID;
    }

    public int getTheApply() {
        return theApply;
    }

    public void setTheApply(int theApply) {
        this.theApply = theApply;
    }

    public int getZMSCompanyID() {
        return ZMSCompanyID;
    }

    public void setZMSCompanyID(int ZMSCompanyID) {
        this.ZMSCompanyID = ZMSCompanyID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCurApproveUserID() {
        return curApproveUserID;
    }

    public void setCurApproveUserID(int curApproveUserID) {
        this.curApproveUserID = curApproveUserID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }
}
