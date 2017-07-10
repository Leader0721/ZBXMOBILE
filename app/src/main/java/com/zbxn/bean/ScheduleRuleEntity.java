package com.zbxn.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class ScheduleRuleEntity implements Parcelable {

    /**
     * endtime : 2016-09-24 21:05:23
     * scheduleSource : 0
     * noticetime : 1900-01-01 17:55:50
     * starttime : null
     * finishtype : 0
     * location : 秘密
     * precedeType : 2
     * userid : 19
     * scheduleRuleType : 0
     * weekstr : 2016-09-23,2016-09-24
     * frequency : 1
     * isalarm : 1
     * finishtime : 2016-09-26 18:10:50
     * isrepeat : 0
     * title : 移动端测试1
     * scheduleDetail : 移动端测试Source
     * yearStr : 2016-09-26 18:10:50.24
     * allday : 1
     * finishtimes : 1
     * zmscompanyid : 1
     * repeattype : 0
     */

    @Expose
    private String endtime;
    @Expose
    private String scheduleSource;
    @Expose
    private String noticetime;
    @Expose
    private String starttime;
    @Expose
    private int finishtype;
    @Expose
    private String location;
    @Expose
    private int precedeType;
    @Expose
    private int userid;
    @Expose
    private int scheduleRuleType;
    @Expose
    private String weekstr;
    @Expose
    private int frequency;
    @Expose
    private int isalarm;
    @Expose
    private String finishtime;
    @Expose
    private int isrepeat;
    @Expose
    private String title;
    @Expose
    private String scheduleDetail;
    @Expose
    private String yearStr;
    @Expose
    private int allday;
    @Expose
    private int finishtimes;
    @Expose
    private int zmscompanyid;
    @Expose
    private int repeattype;
    @Expose
    private int id;
    @Expose
    private String createtime;

    @Expose
    private List<ScheduleDetailEntity> participantUserList = new ArrayList<>();
    @Expose
    private List<ScheduleDetailEntity> shareUserList = new ArrayList<>();
    @Expose
    private String shareIsEdit;
    @Expose
    private int participantISAlarm;


    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getScheduleSource() {
        return scheduleSource;
    }

    public void setScheduleSource(String scheduleSource) {
        this.scheduleSource = scheduleSource;
    }

    public String getNoticetime() {
        return noticetime;
    }

    public void setNoticetime(String noticetime) {
        this.noticetime = noticetime;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public int getFinishtype() {
        return finishtype;
    }

    public void setFinishtype(int finishtype) {
        this.finishtype = finishtype;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPrecedeType() {
        return precedeType;
    }

    public void setPrecedeType(int precedeType) {
        this.precedeType = precedeType;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getScheduleRuleType() {
        return scheduleRuleType;
    }

    public void setScheduleRuleType(int scheduleRuleType) {
        this.scheduleRuleType = scheduleRuleType;
    }

    public String getWeekstr() {
        return weekstr;
    }

    public void setWeekstr(String weekstr) {
        this.weekstr = weekstr;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getIsalarm() {
        return isalarm;
    }

    public void setIsalarm(int isalarm) {
        this.isalarm = isalarm;
    }

    public String getFinishtime() {
        return finishtime;
    }

    public void setFinishtime(String finishtime) {
        this.finishtime = finishtime;
    }

    public int getIsrepeat() {
        return isrepeat;
    }

    public void setIsrepeat(int isrepeat) {
        this.isrepeat = isrepeat;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getScheduleDetail() {
        return scheduleDetail;
    }

    public void setScheduleDetail(String scheduleDetail) {
        this.scheduleDetail = scheduleDetail;
    }

    public String getYearStr() {
        return yearStr;
    }

    public void setYearStr(String yearStr) {
        this.yearStr = yearStr;
    }

    public int getAllday() {
        return allday;
    }

    public void setAllday(int allday) {
        this.allday = allday;
    }

    public int getFinishtimes() {
        return finishtimes;
    }

    public void setFinishtimes(int finishtimes) {
        this.finishtimes = finishtimes;
    }

    public int getZmscompanyid() {
        return zmscompanyid;
    }

    public void setZmscompanyid(int zmscompanyid) {
        this.zmscompanyid = zmscompanyid;
    }

    public int getRepeattype() {
        return repeattype;
    }

    public void setRepeattype(int repeattype) {
        this.repeattype = repeattype;
    }

    public int getParticipantISAlarm() {
        return participantISAlarm;
    }

    public void setParticipantISAlarm(int participantISAlarm) {
        this.participantISAlarm = participantISAlarm;
    }

    public ScheduleRuleEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.endtime);
        dest.writeString(this.scheduleSource);
        dest.writeString(this.noticetime);
        dest.writeString(this.starttime);
        dest.writeInt(this.finishtype);
        dest.writeString(this.location);
        dest.writeInt(this.precedeType);
        dest.writeInt(this.userid);
        dest.writeInt(this.scheduleRuleType);
        dest.writeString(this.weekstr);
        dest.writeInt(this.frequency);
        dest.writeInt(this.isalarm);
        dest.writeString(this.finishtime);
        dest.writeInt(this.isrepeat);
        dest.writeString(this.title);
        dest.writeString(this.scheduleDetail);
        dest.writeString(this.yearStr);
        dest.writeInt(this.allday);
        dest.writeInt(this.finishtimes);
        dest.writeInt(this.zmscompanyid);
        dest.writeInt(this.repeattype);
        dest.writeInt(this.id);
        dest.writeString(this.createtime);
        dest.writeInt(this.participantISAlarm);
    }

    protected ScheduleRuleEntity(Parcel in) {
        this.endtime = in.readString();
        this.scheduleSource = in.readString();
        this.noticetime = in.readString();
        this.starttime = in.readString();
        this.finishtype = in.readInt();
        this.location = in.readString();
        this.precedeType = in.readInt();
        this.userid = in.readInt();
        this.scheduleRuleType = in.readInt();
        this.weekstr = in.readString();
        this.frequency = in.readInt();
        this.isalarm = in.readInt();
        this.finishtime = in.readString();
        this.isrepeat = in.readInt();
        this.title = in.readString();
        this.scheduleDetail = in.readString();
        this.yearStr = in.readString();
        this.allday = in.readInt();
        this.finishtimes = in.readInt();
        this.zmscompanyid = in.readInt();
        this.repeattype = in.readInt();
        this.id = in.readInt();
        this.createtime = in.readString();
        this.participantISAlarm = in.readInt();
    }

    public static final Creator<ScheduleRuleEntity> CREATOR = new Creator<ScheduleRuleEntity>() {
        @Override
        public ScheduleRuleEntity createFromParcel(Parcel source) {
            return new ScheduleRuleEntity(source);
        }

        @Override
        public ScheduleRuleEntity[] newArray(int size) {
            return new ScheduleRuleEntity[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public List<ScheduleDetailEntity> getParticipantUserList() {
        return participantUserList;
    }

    public void setParticipantUserList(List<ScheduleDetailEntity> participantUserList) {
        this.participantUserList = participantUserList;
    }

    public List<ScheduleDetailEntity> getShareUserList() {
        return shareUserList;
    }

    public void setShareUserList(List<ScheduleDetailEntity> shareUserList) {
        this.shareUserList = shareUserList;
    }

    public String getShareIsEdit() {
        return shareIsEdit;
    }

    public void setShareIsEdit(String shareIsEdit) {
        this.shareIsEdit = shareIsEdit;
    }
}
