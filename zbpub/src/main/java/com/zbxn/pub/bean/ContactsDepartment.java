package com.zbxn.pub.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author GISirFive
 * @time 2016/8/8
 */
public class ContactsDepartment implements Serializable {

    private String departmentId;// 所在部门ID
    private String departmentName;// 所在部门名称
    private String captialChar;//昵称首字母
    private List<Contacts> listContacts=new ArrayList<>();//人员列表
    private  boolean isSelected;
    private int count;//人数

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

    public String getCaptialChar() {
        return captialChar;
    }

    public void setCaptialChar(String captialChar) {
        this.captialChar = captialChar;
    }

    public List<Contacts> getListContacts() {
        return listContacts;
    }

    public void setListContacts(List<Contacts> listContacts) {
        this.listContacts = listContacts;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
