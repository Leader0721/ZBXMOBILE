package com.zbxn.bean;

/**
 * 添加员工
 * Created by U on 2016/12/27.
 */
public class AddStaffEntity {
    String UserName;
    String PassWord;
    String Gender;
    String DepartmentPosition;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getDepartmentPosition() {
        return DepartmentPosition;
    }

    public void setDepartmentPosition(String departmentPosition) {
        DepartmentPosition = departmentPosition;
    }
}
