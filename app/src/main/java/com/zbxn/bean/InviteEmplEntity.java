package com.zbxn.bean;


/**
 * Created by Administrator on 2016/12/29.
 */
public class InviteEmplEntity {
    String name;
    String phoneNum;
    int count;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "InviteEmplEntity{" +
                "name='" + name + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", count=" + count +
                '}';
    }
}
