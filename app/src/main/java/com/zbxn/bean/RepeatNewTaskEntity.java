package com.zbxn.bean;

import com.google.gson.annotations.Expose;

/**
 * 项目名称：ZBXMobile
 * 创建人：LiangHanXin
 * 创建时间：2016/9/28 9:33
 */
public class RepeatNewTaskEntity {

    /**
     * typeName : 准时
     * precedeType : 0
     */

    @Expose
    private String typeName;
    @Expose
    private int precedeType;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getPrecedeType() {
        return precedeType;
    }

    public void setPrecedeType(int precedeType) {
        this.precedeType = precedeType;
    }
}
