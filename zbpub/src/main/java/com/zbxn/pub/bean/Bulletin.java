package com.zbxn.pub.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.util.Date;

/**
 * 通知公告
 *
 * @author GISirFive
 * @since 2016-7-13 下午8:45:21
 */
@Table(name = "Bulletin")
public class Bulletin implements Parcelable {

    public Bulletin() {

    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Bulletin))
            return false;
        Bulletin b = (Bulletin) o;
        if (b.id == this.id)
            return true;
        return super.equals(o);
    }

    @Expose
    @Column
    @NoAutoIncrement
    private int id;//
    @Expose
    @Column
    private String title;// 标题
    @Expose
    private String content;// 内容
    @Expose
    @Column
    private int createuserid;// 创建人ID
    @Expose
    @Column
    private String createUserName;// 创建人昵称
    @Expose
    @Column
    private Date createtime;// 创建时间
    @Expose
    private int label;// 重要程度
    @Expose
    @Column
    private int type;// 公告类型
    @Expose
    @Column
    private String typeName;// 公告类型对应的名称
    @Expose
    @Column
    private String attachmentguid;// 附件ID
    @Expose
    private int istop;// 是否置顶
    @Expose
    private int receiveUserId;// 接收人ID
    @Expose
    @Column
    private String receiveUserName;// 接收人昵称
    @Expose
    @Column
    private int read;//是否已读
    @Column
    @Expose
    private boolean collect;//是否收藏
    @Column
    @Expose
    private int relatedid;//日志源id
    @Column
    @Expose
    private int allowComment;//是否可以评论

    public int getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(int allowComment) {
        this.allowComment = allowComment;
    }

    public int getRelatedid() {
        return relatedid;
    }

    public void setRelatedid(int relatedid) {
        this.relatedid = relatedid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCreateuserid() {
        return createuserid;
    }

    public void setCreateuserid(int createuserid) {
        this.createuserid = createuserid;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getAttachmentguid() {
        return attachmentguid;
    }

    public void setAttachmentguid(String attachmentguid) {
        this.attachmentguid = attachmentguid;
    }

    public int getIstop() {
        return istop;
    }

    public void setIstop(int istop) {
        this.istop = istop;
    }

    public int getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(int receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getReceiveUserName() {
        return receiveUserName;
    }

    public void setReceiveUserName(String receiveUserName) {
        this.receiveUserName = receiveUserName;
    }

    public int isRead() {
        return read;
    }

    public void setRead(int isRead) {
        this.read = isRead;
    }

    public boolean isCollect() {
        return collect;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeInt(this.createuserid);
        dest.writeString(this.createUserName);
        dest.writeLong(this.createtime != null ? this.createtime.getTime() : -1);
        dest.writeInt(this.label);
        dest.writeInt(this.type);
        dest.writeString(this.typeName);
        dest.writeString(this.attachmentguid);
        dest.writeInt(this.istop);
        dest.writeInt(this.receiveUserId);
        dest.writeString(this.receiveUserName);
        dest.writeInt(this.read);
        dest.writeByte(this.collect ? (byte) 1 : (byte) 0);
        dest.writeInt(this.relatedid);
        dest.writeInt(this.allowComment);
    }

    protected Bulletin(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.content = in.readString();
        this.createuserid = in.readInt();
        this.createUserName = in.readString();
        long tmpCreateTime = in.readLong();
        this.createtime = tmpCreateTime == -1 ? null : new Date(tmpCreateTime);
        this.label = in.readInt();
        this.type = in.readInt();
        this.typeName = in.readString();
        this.attachmentguid = in.readString();
        this.istop = in.readInt();
        this.receiveUserId = in.readInt();
        this.receiveUserName = in.readString();
        this.read = in.readInt();
        this.collect = in.readByte() != 0;
        this.relatedid = in.readInt();
        this.allowComment = in.readInt();
    }

    public static final Creator<Bulletin> CREATOR = new Creator<Bulletin>() {
        @Override
        public Bulletin createFromParcel(Parcel source) {
            return new Bulletin(source);
        }

        @Override
        public Bulletin[] newArray(int size) {
            return new Bulletin[size];
        }
    };
}