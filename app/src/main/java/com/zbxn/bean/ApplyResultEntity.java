package com.zbxn.bean;

/**
 * 项目名称：具体排行榜的实体类
 * 创建人：LiangHanXin
 * 创建时间：2016/10/12 19:11
 */
public class ApplyResultEntity {


    /**
     * Name : 审批状态
     * Typeid : 状态ID
     */

    private String Name;
    private String Typeid;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getTypeid() {
        return Typeid;
    }

    public void setTypeid(String Typeid) {
        this.Typeid = Typeid;
    }
}
