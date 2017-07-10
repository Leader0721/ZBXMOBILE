package com.zbxn.pub.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.Date;

/**
 * 回复
 * <br>
 * <b>作者：</b>GISirFive<br>
 * <b>日期：</b> 2015-8-15 <br>
 * <b>版权所有：</b><br>
 */
public class Reply implements Parcelable {

    @Expose
    private int id;//
    @Expose
    private int belongcommentid;//
    @Expose
    private String attachmentguid;//
    @Expose
    private int replytoid;//
    @Expose
    private String replycontent;//
    @Expose
    private int zmscompanyid;//
    @Expose
    private int createuserid;//
    @Expose
    private Date createtime;//
    @Expose
    private String createUserName;//回复人的昵称
    @Expose
    private String replyToUserName;//被回复人的昵称

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBelongcommentid() {
        return belongcommentid;
    }

    public void setBelongcommentid(int belongcommentid) {
        this.belongcommentid = belongcommentid;
    }

    public String getAttachmentguid() {
        return attachmentguid;
    }

    public void setAttachmentguid(String attachmentguid) {
        this.attachmentguid = attachmentguid;
    }

    public int getReplytoid() {
        return replytoid;
    }

    public void setReplytoid(int replytoid) {
        this.replytoid = replytoid;
    }

    public String getReplycontent() {
        return replycontent;
    }

    public void setReplycontent(String replycontent) {
        this.replycontent = replycontent;
    }

    public int getZmscompanyid() {
        return zmscompanyid;
    }

    public void setZmscompanyid(int zmscompanyid) {
        this.zmscompanyid = zmscompanyid;
    }

    public int getCreateuserid() {
        return createuserid;
    }

    public void setCreateuserid(int createuserid) {
        this.createuserid = createuserid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getReplyToUserName() {
        return replyToUserName;
    }

    public void setReplyToUserName(String replyToUserName) {
        this.replyToUserName = replyToUserName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.belongcommentid);
        dest.writeString(this.attachmentguid);
        dest.writeInt(this.replytoid);
        dest.writeString(this.replycontent);
        dest.writeInt(this.zmscompanyid);
        dest.writeInt(this.createuserid);
        dest.writeLong(this.createtime != null ? this.createtime.getTime() : -1);
        dest.writeString(this.createUserName);
        dest.writeString(this.replyToUserName);
    }

    public Reply() {
    }

    protected Reply(Parcel in) {
        this.id = in.readInt();
        this.belongcommentid = in.readInt();
        this.attachmentguid = in.readString();
        this.replytoid = in.readInt();
        this.replycontent = in.readString();
        this.zmscompanyid = in.readInt();
        this.createuserid = in.readInt();
        long tmpCreateTime = in.readLong();
        this.createtime = tmpCreateTime == -1 ? null : new Date(tmpCreateTime);
        this.createUserName = in.readString();
        this.replyToUserName = in.readString();
    }

    public static final Parcelable.Creator<Reply> CREATOR = new Parcelable.Creator<Reply>() {
        @Override
        public Reply createFromParcel(Parcel source) {
            return new Reply(source);
        }

        @Override
        public Reply[] newArray(int size) {
            return new Reply[size];
        }
    };
}

