package com.zbxn.activity.mission;

import com.google.gson.annotations.Expose;

/**
 * Created by wj on 2016/11/10.
 * 创建任务 返回的数据实体类
 */
public class MissionResultEntity {


    /**
     * id : 16
     */
    @Expose
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
