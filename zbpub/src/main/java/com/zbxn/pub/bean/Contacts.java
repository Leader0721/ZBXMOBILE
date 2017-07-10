package com.zbxn.pub.bean;

import com.google.gson.annotations.Expose;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * @author GISirFive
 * @time 2016/8/8
 */
@Table(name = "Contacts")
public class Contacts implements Serializable {

    @Id
    @Expose
    @Column
    private int indexID;// indexID   定义此字段为了解决默认id要自增且要唯一的问题  解决方案 给此字段加个@Id  顺序@Id id _id
    //@NoAutoIncrement  不自增
    @Expose
    @Column
    private int id;// ID
    @Expose
    @Column
    private String loginname;// 用户名 手机号也用这个
    @Expose
    @Column
    private String email;// 邮箱
    @Expose
    @Column
    private String userName;// 昵称
    @Expose
    private String number;//用户号码
    @Expose
    @Column
    private String extTel;// 外线电话
    @Expose
    @Column
    private String workCode;//工号
    @Expose
    @Column
    private int gender;//性别
    @Expose
    private Date createtime;//创建时间
    @Column
    @Expose
    private String telephone;// 电话号码
    @Column
    @Expose
    private String departmentId;// 所在部门ID
    @Column
    @Expose
    private String departmentName;// 所在部门名称
    @Column
    @Expose
    private int positionId;//职位id
    @Column
    @Expose
    private String positionName;//职位名称
    @Column
    @Expose
//    @SerializedName("photo")
    private String portrait; // 头像
    @Column
    @Expose
    private String captialChar;//昵称首字母
    @Column
    @Expose
    private String isDepartment;//是否是部门 1--是部门 2--是人员
    @Column
    @Expose
    private String parentId;//部门父级id

    @Column
    @Expose
    private boolean isSelected;//自定义 是否选中
    @Column
    @Expose
    private int isactive;//是否启用 1--启用  0--停用
    @Column
    @Expose
    private String permissionIds;//权限 以","分隔，包含4代表可以查看组织架构



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getExtTel() {
        return extTel;
    }

    public void setExtTel(String extTel) {
        this.extTel = extTel;
    }

    public String getWorkCode() {
        return workCode;
    }

    public void setWorkCode(String workCode) {
        this.workCode = workCode;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getCaptialChar() {
        return captialChar;
    }

    public void setCaptialChar(String captialChar) {
        this.captialChar = captialChar;
    }

    public String getIsDepartment() {
        return isDepartment;
    }

    public void setIsDepartment(String isDepartment) {
        this.isDepartment = isDepartment;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getIsactive() {
        return isactive;
    }

    public void setIsactive(int isactive) {
        this.isactive = isactive;
    }

    public String getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(String permissionIds) {
        this.permissionIds = permissionIds;
    }
}
