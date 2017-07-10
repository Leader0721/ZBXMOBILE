package com.zbxn.pub.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.util.Date;

/**
 * 日志
 * 
 * @author GISirFive
 * @since 2016-7-11 下午2:30:58
 */
@Table(name="WorkBlog")
public class WorkBlog implements Parcelable {

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof WorkBlog))
			return false;
		WorkBlog b = (WorkBlog) o;
		if (b.getId() == id)
			return true;
		return super.equals(o);
	}

	@Expose
	@Column
	@NoAutoIncrement
	private int id;//
	@Expose
	@Column
	private String workblogcontent;//
	@Expose
	@Column
	private int blognumber;//
	@Expose
	@Column
	private Date createtime;//
	@Expose
	@Column
	private int createuserid;//
	@Expose
	@Column
	private String createWeek;// 创建周期(周一、周二...)
	@Expose
	private String createUserName;
	@Expose
	@Column
	private boolean fromMobile;//是否来自移动端
	@Expose
	@Column
	private int commentNum;// 评论数


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWorkblogcontent() {
		return workblogcontent;
	}

	public void setWorkblogcontent(String workblogcontent) {
		this.workblogcontent = workblogcontent;
	}

	public int getBlognumber() {
		return blognumber;
	}

	public void setBlognumber(int blognumber) {
		this.blognumber = blognumber;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public int getCreateuserid() {
		return createuserid;
	}

	public void setCreateuserid(int createuserid) {
		this.createuserid = createuserid;
	}

	public String getCreateWeek() {
		return createWeek;
	}

	public void setCreateWeek(String createWeek) {
		this.createWeek = createWeek;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public boolean isFromMobile() {
		return fromMobile;
	}

	public void setFromMobile(boolean isFromMobile) {
		this.fromMobile = isFromMobile;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	public WorkBlog() {
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.workblogcontent);
		dest.writeInt(this.blognumber);
		dest.writeLong(this.createtime != null ? this.createtime.getTime() : -1);
		dest.writeInt(this.createuserid);
		dest.writeString(this.createWeek);
		dest.writeString(this.createUserName);
		dest.writeByte(this.fromMobile ? (byte) 1 : (byte) 0);
		dest.writeInt(this.commentNum);
	}

	protected WorkBlog(Parcel in) {
		this.id = in.readInt();
		this.workblogcontent = in.readString();
		this.blognumber = in.readInt();
		long tmpCreatetime = in.readLong();
		this.createtime = tmpCreatetime == -1 ? null : new Date(tmpCreatetime);
		this.createuserid = in.readInt();
		this.createWeek = in.readString();
		this.createUserName = in.readString();
		this.fromMobile = in.readByte() != 0;
		this.commentNum = in.readInt();
	}

	public static final Creator<WorkBlog> CREATOR = new Creator<WorkBlog>() {
		@Override
		public WorkBlog createFromParcel(Parcel source) {
			return new WorkBlog(source);
		}

		@Override
		public WorkBlog[] newArray(int size) {
			return new WorkBlog[size];
		}
	};
}
