package com.zbxn.bean;

import com.google.gson.annotations.Expose;

/**
 * @author: ysj
 * @date: 2016-10-22 17:43
 */
public class AdvertEntity {

    /**
     * createtime : 2016-10-20 13:15:31
     * id : 1
     * isdefault : 1
     * picturename : figure1
     * zmscompanyid : 267
     * picturedescription : 轮播图1
     * picturesrc : /images/temp104.jpg
     */

    @Expose
    private String createtime;
    @Expose
    private int id;
    @Expose
    private int isdefault;
    @Expose
    private String picturename;
    @Expose
    private int zmscompanyid;
    @Expose
    private String picturedescription;
    @Expose
    private String picturesrc;

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(int isdefault) {
        this.isdefault = isdefault;
    }

    public String getPicturename() {
        return picturename;
    }

    public void setPicturename(String picturename) {
        this.picturename = picturename;
    }

    public int getZmscompanyid() {
        return zmscompanyid;
    }

    public void setZmscompanyid(int zmscompanyid) {
        this.zmscompanyid = zmscompanyid;
    }

    public String getPicturedescription() {
        return picturedescription;
    }

    public void setPicturedescription(String picturedescription) {
        this.picturedescription = picturedescription;
    }

    public String getPicturesrc() {
        return picturesrc;
    }

    public void setPicturesrc(String picturesrc) {
        this.picturesrc = picturesrc;
    }
}
