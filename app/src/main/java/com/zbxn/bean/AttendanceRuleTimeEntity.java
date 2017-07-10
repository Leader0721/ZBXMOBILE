package com.zbxn.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * 项目名称：ZBXMobile
 * 创建人：LiangHanXin
 * 创建时间：2016/9/30 10:00
 */
public class AttendanceRuleTimeEntity implements Serializable {

    /**
     * createtime : 2016-09-25 10:55:27
     * id : 29
     * checkouttime : 2016-09-25 12:00:00
     * remark : null
     * zmscompanyid : 1
     * ruleid : 12
     * checkintime : 2016-09-25 08:30:00
     */

    @Expose
    private String createtime;
    @Expose
    private int id;
    @Expose
    private String checkouttime;
    @Expose
    private String remark;
    @Expose
    private int zmscompanyid;
    @Expose
    private int ruleid;
    @Expose
    private String checkintime;

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCheckouttime() {
        return checkouttime;
    }

    public void setCheckouttime(String checkouttime) {
        this.checkouttime = checkouttime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getZmscompanyid() {
        return zmscompanyid;
    }

    public void setZmscompanyid(int zmscompanyid) {
        this.zmscompanyid = zmscompanyid;
    }

    public int getRuleid() {
        return ruleid;
    }

    public void setRuleid(int ruleid) {
        this.ruleid = ruleid;
    }

    public String getCheckintime() {
        return checkintime;
    }

    public void setCheckintime(String checkintime) {
        this.checkintime = checkintime;
    }
}
