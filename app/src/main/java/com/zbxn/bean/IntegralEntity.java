package com.zbxn.bean;

import com.google.gson.annotations.Expose;

/**
 * 项目名称：N币的实体类
 * 创建人：LiangHanXin
 * 创建时间：2016/11/1 10:15
 */
public class IntegralEntity {


    /**
     * zmsname : 淄博智博星网络有限公司
     * touxiang : /Assets/oa_InformNotice/img/ZMS_Message/touxiang.png
     * createTime : 2016-10-05 00:00:00
     * userID : 21
     * username : 丛微
     * userScore : 54
     * ZMSCompanyID : 1
     * ID : 9
     * paiming : 1
     */
    @Expose
    private String zmsname;
    @Expose
    private String touxiang;
    @Expose
    private String createTime;
    @Expose
    private int userID;
    @Expose
    private String username;
    @Expose
    private String userScore;
    @Expose
    private int ZMSCompanyID;
    @Expose
    private int ID;
    @Expose
    private int paiming;
    @Expose
    private String leiji;
    @Expose
    private String number;

    public String getLeiji() {
        return leiji;
    }

    public void setLeiji(String leiji) {
        this.leiji = leiji;
    }

    public String getZmsname() {
        return zmsname;
    }

    public void setZmsname(String zmsname) {
        this.zmsname = zmsname;
    }

    public String getTouxiang() {
        return touxiang;
    }

    public void setTouxiang(String touxiang) {
        this.touxiang = touxiang;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserScore() {
        return userScore;
    }

    public void setUserScore(String userScore) {
        this.userScore = userScore;
    }

    public int getZMSCompanyID() {
        return ZMSCompanyID;
    }

    public void setZMSCompanyID(int ZMSCompanyID) {
        this.ZMSCompanyID = ZMSCompanyID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getPaiming() {
        return paiming;
    }

    public void setPaiming(int paiming) {
        this.paiming = paiming;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
