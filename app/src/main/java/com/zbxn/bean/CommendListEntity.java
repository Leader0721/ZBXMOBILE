package com.zbxn.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by wj on 2016/11/15.
 * 评论列表的实体类
 */
public class CommendListEntity {


    /**
     * msg : 获取成功
     * success : 0
     * data : [{"ID":"1","PersonId":"007","PersonName":"王尼玛","PersonHeadUrl":"","Content":"么么哒","CreateTime":"2016-11-09 13:02:31"}]
     */
    @Expose
    private String ID;
    @Expose
    private String PersonId;
    @Expose
    private String PersonName;
    @Expose
    private String PersonHeadUrl;
    @Expose
    private String Content;
    @Expose
    private String CreateTime;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPersonId() {
        return PersonId;
    }

    public void setPersonId(String PersonId) {
        this.PersonId = PersonId;
    }

    public String getPersonName() {
        return PersonName;
    }

    public void setPersonName(String PersonName) {
        this.PersonName = PersonName;
    }

    public String getPersonHeadUrl() {
        return PersonHeadUrl;
    }

    public void setPersonHeadUrl(String PersonHeadUrl) {
        this.PersonHeadUrl = PersonHeadUrl;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }
}
