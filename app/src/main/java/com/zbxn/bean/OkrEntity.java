package com.zbxn.bean;

import com.google.gson.annotations.Expose;

/**
 * 项目名称： Okr
 * 创建人：LiangHanXin
 * 创建时间：2016/11/16 14:51
 */
public class OkrEntity {
    @Expose
    private int scoreAll;
    @Expose
    private int scoremonth;
    @Expose
    private int userid;
    @Expose
    private String userName;
    @Expose
    private int commonRanKing;
    @Expose
    private int scoreyear;
    @Expose
    private int bizRanKing;
    @Expose
    private int bizScoreAll;

    public int getScoreAll() {
        return scoreAll;
    }

    public void setScoreAll(int scoreAll) {
        this.scoreAll = scoreAll;
    }

    public int getScoremonth() {
        return scoremonth;
    }

    public void setScoremonth(int scoremonth) {
        this.scoremonth = scoremonth;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getCommonRanKing() {
        return commonRanKing;
    }

    public void setCommonRanKing(int commonRanKing) {
        this.commonRanKing = commonRanKing;
    }

    public int getScoreyear() {
        return scoreyear;
    }

    public void setScoreyear(int scoreyear) {
        this.scoreyear = scoreyear;
    }

    public int getBizRanKing() {
        return bizRanKing;
    }

    public void setBizRanKing(int bizRanKing) {
        this.bizRanKing = bizRanKing;
    }

    public int getBizScoreAll() {
        return bizScoreAll;
    }

    public void setBizScoreAll(int bizScoreAll) {
        this.bizScoreAll = bizScoreAll;
    }
}
