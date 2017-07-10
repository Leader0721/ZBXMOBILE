package com.zbxn.bean;

import com.google.gson.annotations.Expose;

/**
 * @author: ysj
 * @date: 2016-12-29 12:03
 */
public class JobEntity {

    /**
     * PositionName : 员工
     * PositionID : 111
     */
    @Expose
    private String PositionName;
    @Expose
    private int PositionID;

    public String getPositionName() {
        return PositionName;
    }

    public void setPositionName(String PositionName) {
        this.PositionName = PositionName;
    }

    public int getPositionID() {
        return PositionID;
    }

    public void setPositionID(int PositionID) {
        this.PositionID = PositionID;
    }
}
