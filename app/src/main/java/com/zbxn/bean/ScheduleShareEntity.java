package com.zbxn.bean;

import com.google.gson.annotations.Expose;

/**
 * @author: ysj
 * @date: 2016-12-16 10:26
 */
public class ScheduleShareEntity {

    /**
     * favorid : 2242
     * userName : again
     */
    @Expose
    private int favorid;
    @Expose
    private String userName;

    public int getFavorid() {
        return favorid;
    }

    public void setFavorid(int favorid) {
        this.favorid = favorid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
