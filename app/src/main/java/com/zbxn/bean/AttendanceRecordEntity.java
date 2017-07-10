package com.zbxn.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * 项目名称：ZBXMobile
 * 创建人：LiangHanXin
 * 创建时间：2016/9/30 10:00
 */
public class AttendanceRecordEntity implements Serializable {

    /**
     * remark : null
     * ruletimeid : null
     * userid : 19
     * checkininitstate : null
     * checkintime : 2016-09-27 09:36:08
     * checkinattendancesource : null
     * createtime : null
     * id : 15998
     * checkoutip : null
     * checkoutstate : -1
     * checkoutlongitude : null
     * checkinip : null
     * year : null
     * zmscompanyid : null
     * ruleid : null
     * checkoutlatitude : null
     * checkoutinitstate : null
     * checkinaddress : null
     * checkoutaddress : null
     * checkinstate : 1
     * checkinlongitude : null
     * checkinlatitude : null
     * checkouttime : null
     * month : null
     * checkInStateName : 正常
     * checkoutattendancesource : null
     * day : null
     * checkOutStateName : 未签退
     */

    @Expose
    private int userid;
    @Expose
    private String checkouttime;
    @Expose
    private String checkintime;
    @Expose
    private int id;
    @Expose
    private int checkoutstate;
    @Expose
    private int checkinstate;
    @Expose
    private String checkInStateName;
    @Expose
    private String checkOutStateName;
    @Expose
    private String checkinaddress;
    @Expose
    private String checkoutinitstate;
    @Expose
    private String checkoutaddress;
    @Expose
    private String state;
    @Expose
    private String signOutState;
    @Expose
    private String createtime;
    @Expose
    private String checkTime;


    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getCheckintime() {
        return checkintime;
    }

    public void setCheckintime(String checkintime) {
        this.checkintime = checkintime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCheckoutstate() {
        return checkoutstate;
    }

    public void setCheckoutstate(int checkoutstate) {
        this.checkoutstate = checkoutstate;
    }

    public int getCheckinstate() {
        return checkinstate;
    }

    public void setCheckinstate(int checkinstate) {
        this.checkinstate = checkinstate;
    }

    public String getCheckInStateName() {
        return checkInStateName;
    }

    public void setCheckInStateName(String checkInStateName) {
        this.checkInStateName = checkInStateName;
    }

    public String getCheckOutStateName() {
        return checkOutStateName;
    }

    public void setCheckOutStateName(String checkOutStateName) {
        this.checkOutStateName = checkOutStateName;
    }

    public String getCheckinaddress() {
        return checkinaddress;
    }

    public void setCheckinaddress(String checkinaddress) {
        this.checkinaddress = checkinaddress;
    }

    public String getCheckoutinitstate() {
        return checkoutinitstate;
    }

    public void setCheckoutinitstate(String checkoutinitstate) {
        this.checkoutinitstate = checkoutinitstate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSignOutState() {
        return signOutState;
    }

    public void setSignOutState(String signOutState) {
        this.signOutState = signOutState;
    }

    public String getCheckoutaddress() {
        return checkoutaddress;
    }

    public void setCheckoutaddress(String checkoutaddress) {
        this.checkoutaddress = checkoutaddress;
    }

    public String getCheckouttime() {
        return checkouttime;
    }

    public void setCheckouttime(String checkouttime) {
        this.checkouttime = checkouttime;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }
}
