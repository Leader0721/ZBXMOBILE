package com.zbxn.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 2016/12/7.
 */
public class MessageUpdateEntity {

    /**
     * id : 10
     * userid : 2320
     * zmscompanyid : 1
     * pushstate : 1,11,23,25,31,32
     */
    @Expose
    private int id;
    @Expose
    private int userid;
    @Expose
    private int zmscompanyid;
    @Expose
    private String pushstate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getZmscompanyid() {
        return zmscompanyid;
    }

    public void setZmscompanyid(int zmscompanyid) {
        this.zmscompanyid = zmscompanyid;
    }

    public String getPushstate() {
        return pushstate;
    }

    public void setPushstate(String pushstate) {
        this.pushstate = pushstate;
    }
}
