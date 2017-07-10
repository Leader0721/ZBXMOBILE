package com.zbxn.bean;

import com.google.gson.annotations.Expose;

/**
 * @author: ysj
 * @date: 2016-11-16 10:24
 */
public class OkrRankingEntity {


    /**
     * userId : 3
     * orderNO : 1
     * userIcon : 122sdfsd322.jpg
     * userDepartName : 122sdfsd322
     * userName : 122sdfsd322
     * comScore : 1.0
     * bizScore : 2.0
     * okr : 1.6
     */
    @Expose
    private int userId; //用户userId
    @Expose
    private int orderNO; //排名
    @Expose
    private String userIcon; //用户头像
    @Expose
    private String userDepartName; //用户所在部门名称
    @Expose
    private String userName; //用户姓名
    @Expose
    private double comScore; //通用积分
    @Expose
    private double bizScore; //业务积分
    @Expose
    private double okr; //OKR值

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOrderNO() {
        return orderNO;
    }

    public void setOrderNO(int orderNO) {
        this.orderNO = orderNO;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserDepartName() {
        return userDepartName;
    }

    public void setUserDepartName(String userDepartName) {
        this.userDepartName = userDepartName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getComScore() {
        return comScore;
    }

    public void setComScore(double comScore) {
        this.comScore = comScore;
    }

    public double getBizScore() {
        return bizScore;
    }

    public void setBizScore(double bizScore) {
        this.bizScore = bizScore;
    }

    public double getOkr() {
        return okr;
    }

    public void setOkr(double okr) {
        this.okr = okr;
    }
}
