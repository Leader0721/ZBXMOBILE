package com.zbxn.bean;


import java.io.Serializable;

public class PhotosEntity implements Serializable {
    private String id;
    private String appname;
    private String imgurl;
    private String imgurlNet;//服务器返回的图片地址

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getImgurlNet() {
        return imgurlNet;
    }

    public void setImgurlNet(String imgurlNet) {
        this.imgurlNet = imgurlNet;
    }
}
