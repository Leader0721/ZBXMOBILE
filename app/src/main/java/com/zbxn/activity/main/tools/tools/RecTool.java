package com.zbxn.activity.main.tools.tools;

import com.google.gson.annotations.Expose;
import com.lidroid.xutils.db.annotation.Table;

/**
 * @author LiangHanXin
 * @time 2016/9/12
 */
@Table(name = "RecTool")
public class RecTool {

    /*public RecTool(String title, int id) {
        mTitle = title;
        this.id = id;
    }*/

    @Expose
    private String menuid;//id
    @Expose
    private boolean isVisible;//返回的值否有权限显示
    @Expose
    private String title;//名字
    @Expose
    private String img;//图片

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}