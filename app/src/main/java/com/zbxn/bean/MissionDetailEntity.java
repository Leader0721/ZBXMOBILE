package com.zbxn.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * @author: ysj
 * @date: 2016-11-14 14:35
 */
public class MissionDetailEntity implements Parcelable {

    /**
     * ID : 11
     * PersonCreateId : 19
     * PersonCreateName : 你猜啊
     * PersonCreateHeadUrl : view/upload/file/20161019/14768796457920.png
     * DifficultyLevel : 0
     * TaskState : 0
     * DoneProgress : 0
     * CreateTime : 2016-11-14 13:48:52
     * EndTime : 2016-11-15 13:47:49
     * TaskTitle : 123
     * TaskContent : 132321
     * WorkHours : 1
     * PersonLeadingId : 449
     * PersonLeadingName : 123
     * PersonExecuteIds : 1340
     * PersonExecuteNames : ,韩泽
     * PersonCheckId : 449
     * PersonCheckNames : 123
     * PersonSendIds : 74
     * PersonSendNames : ,002
     * tokenid : CE0AA39279EE03F9B48F8B430CE47AD920161114022820
     * CurrentCompanyId : 1
     */
    @Expose
    private int ID;
    @Expose
    private int PersonCreateId;
    @Expose
    private String PersonCreateName;
    @Expose
    private String PersonCreateHeadUrl;
    @Expose
    private int DifficultyLevel;
    @Expose
    private int TaskState;
    @Expose
    private int DoneProgress;
    @Expose
    private String CreateTime;
    @Expose
    private String EndTime;
    @Expose
    private String TaskTitle;
    @Expose
    private String TaskContent;
    @Expose
    private int WorkHours;
    @Expose
    private int PersonLeadingId;
    @Expose
    private String PersonLeadingName;
    @Expose
    private String PersonExecuteIds;
    @Expose
    private String PersonExecuteNames;
    @Expose
    private int PersonCheckId;
    @Expose
    private String PersonCheckNames;
    @Expose
    private String PersonSendIds;
    @Expose
    private String PersonSendNames;
    @Expose
    private String tokenid;
    @Expose
    private int CurrentCompanyId;
    @Expose
    private int UrgentLevel;
    @Expose
    private ArrayList<MissionAttachmentEntity> AttachmentList = new ArrayList<>();
    @Expose
    private int TaskPersonState;
    @Expose
    private boolean IsTaskTrunDownPerson;
    @Expose
    private String AttachmentGUID;


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getPersonCreateId() {
        return PersonCreateId;
    }

    public void setPersonCreateId(int PersonCreateId) {
        this.PersonCreateId = PersonCreateId;
    }

    public String getPersonCreateName() {
        return PersonCreateName;
    }

    public void setPersonCreateName(String PersonCreateName) {
        this.PersonCreateName = PersonCreateName;
    }

    public String getPersonCreateHeadUrl() {
        return PersonCreateHeadUrl;
    }

    public void setPersonCreateHeadUrl(String PersonCreateHeadUrl) {
        this.PersonCreateHeadUrl = PersonCreateHeadUrl;
    }

    public int getDifficultyLevel() {
        return DifficultyLevel;
    }

    public void setDifficultyLevel(int DifficultyLevel) {
        this.DifficultyLevel = DifficultyLevel;
    }

    public int getTaskState() {
        return TaskState;
    }

    public void setTaskState(int TaskState) {
        this.TaskState = TaskState;
    }

    public int getDoneProgress() {
        return DoneProgress;
    }

    public void setDoneProgress(int DoneProgress) {
        this.DoneProgress = DoneProgress;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String EndTime) {
        this.EndTime = EndTime;
    }

    public String getTaskTitle() {
        return TaskTitle;
    }

    public void setTaskTitle(String TaskTitle) {
        this.TaskTitle = TaskTitle;
    }

    public String getTaskContent() {
        return TaskContent;
    }

    public void setTaskContent(String TaskContent) {
        this.TaskContent = TaskContent;
    }

    public int getWorkHours() {
        return WorkHours;
    }

    public void setWorkHours(int WorkHours) {
        this.WorkHours = WorkHours;
    }

    public int getPersonLeadingId() {
        return PersonLeadingId;
    }

    public void setPersonLeadingId(int PersonLeadingId) {
        this.PersonLeadingId = PersonLeadingId;
    }

    public String getPersonLeadingName() {
        return PersonLeadingName;
    }

    public void setPersonLeadingName(String PersonLeadingName) {
        this.PersonLeadingName = PersonLeadingName;
    }

    public String getPersonExecuteIds() {
        return PersonExecuteIds;
    }

    public void setPersonExecuteIds(String PersonExecuteIds) {
        this.PersonExecuteIds = PersonExecuteIds;
    }

    public String getPersonExecuteNames() {
        return PersonExecuteNames;
    }

    public void setPersonExecuteNames(String PersonExecuteNames) {
        this.PersonExecuteNames = PersonExecuteNames;
    }

    public int getPersonCheckId() {
        return PersonCheckId;
    }

    public void setPersonCheckId(int PersonCheckId) {
        this.PersonCheckId = PersonCheckId;
    }

    public String getPersonCheckNames() {
        return PersonCheckNames;
    }

    public void setPersonCheckNames(String PersonCheckNames) {
        this.PersonCheckNames = PersonCheckNames;
    }

    public String getPersonSendIds() {
        return PersonSendIds;
    }

    public void setPersonSendIds(String PersonSendIds) {
        this.PersonSendIds = PersonSendIds;
    }

    public String getPersonSendNames() {
        return PersonSendNames;
    }

    public void setPersonSendNames(String PersonSendNames) {
        this.PersonSendNames = PersonSendNames;
    }

    public String getTokenid() {
        return tokenid;
    }

    public void setTokenid(String tokenid) {
        this.tokenid = tokenid;
    }

    public int getCurrentCompanyId() {
        return CurrentCompanyId;
    }

    public void setCurrentCompanyId(int CurrentCompanyId) {
        this.CurrentCompanyId = CurrentCompanyId;
    }

    public int getUrgentLevel() {
        return UrgentLevel;
    }

    public void setUrgentLevel(int urgentLevel) {
        UrgentLevel = urgentLevel;
    }

    public ArrayList<MissionAttachmentEntity> getAttachmentList() {
        return AttachmentList;
    }

    public void setAttachmentList(ArrayList<MissionAttachmentEntity> attachmentList) {
        AttachmentList = attachmentList;
    }

    public int getTaskPersonState() {
        return TaskPersonState;
    }

    public void setTaskPersonState(int taskPersonState) {
        TaskPersonState = taskPersonState;
    }

    public boolean isTaskTrunDownPerson() {
        return IsTaskTrunDownPerson;
    }

    public void setTaskTrunDownPerson(boolean taskTrunDownPerson) {
        IsTaskTrunDownPerson = taskTrunDownPerson;
    }

    public String getAttachmentGUID() {
        return AttachmentGUID;
    }

    public void setAttachmentGUID(String attachmentGUID) {
        AttachmentGUID = attachmentGUID;
    }

    public MissionDetailEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ID);
        dest.writeInt(this.PersonCreateId);
        dest.writeString(this.PersonCreateName);
        dest.writeString(this.PersonCreateHeadUrl);
        dest.writeInt(this.DifficultyLevel);
        dest.writeInt(this.TaskState);
        dest.writeInt(this.DoneProgress);
        dest.writeString(this.CreateTime);
        dest.writeString(this.EndTime);
        dest.writeString(this.TaskTitle);
        dest.writeString(this.TaskContent);
        dest.writeInt(this.WorkHours);
        dest.writeInt(this.PersonLeadingId);
        dest.writeString(this.PersonLeadingName);
        dest.writeString(this.PersonExecuteIds);
        dest.writeString(this.PersonExecuteNames);
        dest.writeInt(this.PersonCheckId);
        dest.writeString(this.PersonCheckNames);
        dest.writeString(this.PersonSendIds);
        dest.writeString(this.PersonSendNames);
        dest.writeString(this.tokenid);
        dest.writeInt(this.CurrentCompanyId);
        dest.writeInt(this.UrgentLevel);
        dest.writeList(this.AttachmentList);
        dest.writeInt(this.TaskPersonState);
        dest.writeByte(this.IsTaskTrunDownPerson ? (byte) 1 : (byte) 0);
        dest.writeString(this.AttachmentGUID);
    }

    protected MissionDetailEntity(Parcel in) {
        this.ID = in.readInt();
        this.PersonCreateId = in.readInt();
        this.PersonCreateName = in.readString();
        this.PersonCreateHeadUrl = in.readString();
        this.DifficultyLevel = in.readInt();
        this.TaskState = in.readInt();
        this.DoneProgress = in.readInt();
        this.CreateTime = in.readString();
        this.EndTime = in.readString();
        this.TaskTitle = in.readString();
        this.TaskContent = in.readString();
        this.WorkHours = in.readInt();
        this.PersonLeadingId = in.readInt();
        this.PersonLeadingName = in.readString();
        this.PersonExecuteIds = in.readString();
        this.PersonExecuteNames = in.readString();
        this.PersonCheckId = in.readInt();
        this.PersonCheckNames = in.readString();
        this.PersonSendIds = in.readString();
        this.PersonSendNames = in.readString();
        this.tokenid = in.readString();
        this.CurrentCompanyId = in.readInt();
        this.UrgentLevel = in.readInt();
        this.AttachmentList = new ArrayList<MissionAttachmentEntity>();
        in.readList(this.AttachmentList, MissionAttachmentEntity.class.getClassLoader());
        this.TaskPersonState = in.readInt();
        this.IsTaskTrunDownPerson = in.readByte() != 0;
        this.AttachmentGUID = in.readString();
    }

    public static final Creator<MissionDetailEntity> CREATOR = new Creator<MissionDetailEntity>() {
        @Override
        public MissionDetailEntity createFromParcel(Parcel source) {
            return new MissionDetailEntity(source);
        }

        @Override
        public MissionDetailEntity[] newArray(int size) {
            return new MissionDetailEntity[size];
        }
    };
}
