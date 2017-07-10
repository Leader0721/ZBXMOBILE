package com.zbxn.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 2016/9/29.
 */
public class ScheduleDetailEntity implements Parcelable {


    /**
     * endtime : null
     * starttime : null
     * location : null
     * userid : null
     * isalarm : null
     * id : null
     * createtime : null
     * title : null
     * scheduleid : 246
     * participantid : null
     * yearStr : null
     * scheduleDetail : null
     * userName : 李雨佳
     * zmscompanyid : null
     */
    @Expose
    private String endtime;
    @Expose
    private String starttime;
    @Expose
    private String location;
    @Expose
    private String userid;
    @Expose
    private String isalarm;
    @Expose
    private String id;
    @Expose
    private String createtime;
    @Expose
    private String title;
    @Expose
    private int scheduleid;
    @Expose
    private String participantid;
    @Expose
    private String yearStr;
    @Expose
    private String scheduleDetail;
    @Expose
    private String userName;
    @Expose
    private String zmscompanyid;
    @Expose
    private int participantISAlarm;

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getIsalarm() {
        return isalarm;
    }

    public void setIsalarm(String isalarm) {
        this.isalarm = isalarm;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getScheduleid() {
        return scheduleid;
    }

    public void setScheduleid(int scheduleid) {
        this.scheduleid = scheduleid;
    }

    public String getParticipantid() {
        return participantid;
    }

    public void setParticipantid(String participantid) {
        this.participantid = participantid;
    }

    public String getYearStr() {
        return yearStr;
    }

    public void setYearStr(String yearStr) {
        this.yearStr = yearStr;
    }

    public String getScheduleDetail() {
        return scheduleDetail;
    }

    public void setScheduleDetail(String scheduleDetail) {
        this.scheduleDetail = scheduleDetail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getZmscompanyid() {
        return zmscompanyid;
    }

    public void setZmscompanyid(String zmscompanyid) {
        this.zmscompanyid = zmscompanyid;
    }

    public int getParticipantISAlarm() {
        return participantISAlarm;
    }

    public void setParticipantISAlarm(int participantISAlarm) {
        this.participantISAlarm = participantISAlarm;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.endtime);
        dest.writeString(this.starttime);
        dest.writeString(this.location);
        dest.writeString(this.userid);
        dest.writeString(this.isalarm);
        dest.writeString(this.id);
        dest.writeString(this.createtime);
        dest.writeString(this.title);
        dest.writeInt(this.scheduleid);
        dest.writeString(this.participantid);
        dest.writeString(this.yearStr);
        dest.writeString(this.scheduleDetail);
        dest.writeString(this.userName);
        dest.writeString(this.zmscompanyid);
        dest.writeInt(this.participantISAlarm);
    }

    protected ScheduleDetailEntity(Parcel in) {
        this.endtime = in.readString();
        this.starttime = in.readString();
        this.location = in.readString();
        this.userid = in.readString();
        this.isalarm = in.readString();
        this.id = in.readString();
        this.createtime = in.readString();
        this.title = in.readString();
        this.scheduleid = in.readInt();
        this.participantid = in.readString();
        this.yearStr = in.readString();
        this.scheduleDetail = in.readString();
        this.userName = in.readString();
        this.zmscompanyid = in.readString();
        this.participantISAlarm = in.readInt();
    }

    public static final Creator<ScheduleDetailEntity> CREATOR = new Creator<ScheduleDetailEntity>() {
        @Override
        public ScheduleDetailEntity createFromParcel(Parcel source) {
            return new ScheduleDetailEntity(source);
        }

        @Override
        public ScheduleDetailEntity[] newArray(int size) {
            return new ScheduleDetailEntity[size];
        }
    };

}
