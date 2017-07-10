package com.zbxn.bean;

/**
 * Created by U on 2016/12/29.
 */
public class DepartmentPositionEntity {
    String UDPID;
    String departmentId;
    String positionId;
//    //单例
//    private static DepartmentPositionEntity entity;
//    public static  DepartmentPositionEntity getInstance(){
//        if (entity==null){
//            entity=new DepartmentPositionEntity();
//        }
//        return entity;
//    }
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

    @Override
    public String toString() {
        return
                "{UDPID:'" + UDPID + '\'' +
                ", departmentId:'" + departmentId + '\'' +
                ", positionId:'" + positionId + '\'' +
                '}';
    }
}
