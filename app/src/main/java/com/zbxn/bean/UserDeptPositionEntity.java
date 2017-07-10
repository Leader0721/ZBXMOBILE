package com.zbxn.bean;

import com.google.gson.annotations.Expose;

/**
 * @author: ysj
 * @date: 2016-12-29 19:32
 */
public class UserDeptPositionEntity {

    /**
     * UDPID :
     * departmentId : 111
     * positionId : 111222
     */
    @Expose
    private String UDPID;
    @Expose
    private String departmentId;
    @Expose
    private String positionId;

    public String getUDPID() {
        return UDPID;
    }

    public void setUDPID(String UDPID) {
        this.UDPID = UDPID;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }
}
