package com.zbxn.bean;

import com.google.gson.annotations.Expose;

/**
 * 项目名称：N币明细
 * 创建人：LiangHanXin
 * 创建时间：2016/11/2 10:27
 */
public class IntergralDatailsEntity {


    /**
     * balance : 0
     * createTime : 2016-10-26 01:04:47
     * userScore : 2
     * sourceReason : 签到N币
     */
    @Expose
    private int balance;
    @Expose
    private String createTime;
    @Expose
    private int userScore;
    @Expose
    private String sourceReason;

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getUserScore() {
        return userScore;
    }

    public void setUserScore(int userScore) {
        this.userScore = userScore;
    }

    public String getSourceReason() {
        return sourceReason;
    }

    public void setSourceReason(String sourceReason) {
        this.sourceReason = sourceReason;
    }
}
