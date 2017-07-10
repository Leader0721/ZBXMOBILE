package com.zbxn.bean;

import com.google.gson.annotations.Expose;

/**
 * @author: ysj
 * @date: 2016-12-29 19:23
 */
public class CompileStaffEntity {
    /**
     * createtime : 2016-09-01 09:22:45
     * id : 690
     * positionid : 327
     * departmentid : 484
     * isactive : true
     * isdefault : 1
     * userid : 596
     * zmscompanyid : 267
     * positionName : 员工
     * departmentName : 移动端开发组
     */
    @Expose
    private String createtime;
    @Expose
    private int id;
    @Expose
    private int positionid;
    @Expose
    private int departmentid;
    @Expose
    private boolean isactive;
    @Expose
    private int isdefault;
    @Expose
    private int userid;
    @Expose
    private int zmscompanyid;
    @Expose
    private String positionName;
    @Expose
    private String departmentName;

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

    public int getPositionid() {
        return positionid;
    }

    public void setPositionid(int positionid) {
        this.positionid = positionid;
    }

    public int getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(int departmentid) {
        this.departmentid = departmentid;
    }

    public boolean isIsactive() {
        return isactive;
    }

    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }

    public int getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(int isdefault) {
        this.isdefault = isdefault;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getZmscompanyid() {
        return zmscompanyid;
    }

    public void setZmscompanyid(int zmscompanyid) {
        this.zmscompanyid = zmscompanyid;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
