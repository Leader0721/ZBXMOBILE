package com.zbxn.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/8.
 */
public class MissionAttachmentEntity implements Serializable {

    /// <summary>
    /// 附件ID
    /// </summary>
    @Expose
    private String Id ;
    /// <summary>
    /// 附件下载地址
    /// </summary>
    @Expose
    private String AttachmentUrl ;
    /// <summary>
    /// 附件名称
    /// </summary>
    @Expose
    private String AttachmentName ;
    /// <summary>
    /// 附件后缀 .jpg
    /// </summary>
    @Expose
    private String AttachmentSuffix ;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getAttachmentUrl() {
        return AttachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        AttachmentUrl = attachmentUrl;
    }

    public String getAttachmentName() {
        return AttachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        AttachmentName = attachmentName;
    }

    public String getAttachmentSuffix() {
        return AttachmentSuffix;
    }

    public void setAttachmentSuffix(String attachmentSuffix) {
        AttachmentSuffix = attachmentSuffix;
    }
}
