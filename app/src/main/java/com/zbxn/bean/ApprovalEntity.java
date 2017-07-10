package com.zbxn.bean;

import com.google.gson.annotations.Expose;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 审批主界面图标等
 *
 * @author: ysj
 * @date: 2016-10-12 09:25
 */
@Table(name = "RecTool")
public class ApprovalEntity {


    /**
     * typeid : 1
     * name : 请假
     */

    @Expose
    private int typeid; // id
    @Expose
    private String name; //审批项目
    @Expose
    private String img; //图片地址

    public ApprovalEntity() {

    }

    public ApprovalEntity(int typeid, String name) {
        this.typeid = typeid;
        this.name = name;
    }

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
