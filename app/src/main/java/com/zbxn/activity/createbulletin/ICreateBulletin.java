package com.zbxn.activity.createbulletin;

import com.zbxn.pub.frame.mvp.base.ControllerCenter;

import java.util.Date;

/**
 * Created by Administrator on 2016/8/29.
 */
public interface ICreateBulletin extends ControllerCenter {
    /**
     * 标题
     *
     * @return
     */
    String getBulletinTitle();

    /**
     * 内容
     *
     * @return
     */
    String getBulletinContent();

    /**
     * 类型 （不用）
     *
     * @return
     */
    int getLabel();

    /**
     * 类型 （不用）
     *
     * @return
     */
    int getType();

    /**
     * @return
     */
    String getAttachmentguid();

    /**
     * 是否置顶 （0：false  1:true）
     *
     * @return
     */
    int getIsTop();

    /**
     * 接收人的数组
     *
     * @return
     */
    String[] getPersons();

    /**
     * 销毁回调
     *
     * @param b
     */
    void finishForResult(boolean b);

    /**
     * 置顶时间至(什么时候)
     *
     * @return
     */
    String getTopTime();

    /**
     * 是否可以评论
     */
    int getAllowComment();

}
