package com.zbxn.bean;

import com.google.gson.annotations.Expose;
import com.lidroid.xutils.db.annotation.Column;

/**
 * Created by U on 2016/12/28.
 */
public class AddDepartmentEntity {

    /**
     * ID : 920
     * Name : sdfsd
     * Desc : sdf
     * ParentID : 0
     * IsDeptNode : true
     * TreePath : ID920/
     * DeptPath : ID920/
     * CreateTime : 2016-12-30T19:06:47.4330625+08:00
     * ZMSCompanyID : 1
     */

    @Expose
    @Column
    private int ID;
    @Expose
    @Column
    private String DepartmentName;
    @Expose
    @Column
    private String DepartmentDesc;
    @Expose
    @Column
    private int ParentID;
    @Expose
    @Column
    private boolean IsDeptNode;
    //    private String TreePath;
//    private String DeptPath;
    @Expose
    @Column
    private String CreateTime;
    @Expose
    @Column
    private int ZMSCompanyID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String Name) {
        this.DepartmentName = Name;
    }

    public String getDepartmentDesc() {
        return DepartmentDesc;
    }

    public void setDepartmentDesc(String Desc) {
        this.DepartmentDesc = Desc;
    }

    public int getParentID() {
        return ParentID;
    }

    public void setParentID(int ParentID) {
        this.ParentID = ParentID;
    }

    public boolean isIsDeptNode() {
        return IsDeptNode;
    }

    public void setIsDeptNode(boolean IsDeptNode) {
        this.IsDeptNode = IsDeptNode;
    }

//    public String getTreePath() {
//        return TreePath;
//    }
//
//    public void setTreePath(String TreePath) {
//        this.TreePath = TreePath;
//    }
//
//    public String getDeptPath() {
//        return DeptPath;
//    }
//
//    public void setDeptPath(String DeptPath) {
//        this.DeptPath = DeptPath;
//    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public int getZMSCompanyID() {
        return ZMSCompanyID;
    }

    public void setZMSCompanyID(int ZMSCompanyID) {
        this.ZMSCompanyID = ZMSCompanyID;
    }
}
