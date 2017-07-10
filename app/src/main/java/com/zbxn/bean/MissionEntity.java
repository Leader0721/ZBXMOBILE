package com.zbxn.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 2016/11/8.
 */
public class MissionEntity {

    /**
     *
     *
     *
     * msg : 获取成功
     * success : 0
     * data : [{"TokenId":"sd","ID":"1","PersonCreateId":"3","PersonCreateName":"刘凯","PersonCreateHeadUrl":"","DifficultyLevel":0,"TaskState":0,"DoneProgress":50,"CreateTime":"2016-11-08 18:03:41","EndTime":"2016-11-10 18:03:41","TaskTitle":"接口测试0"}]
     */

    /**
     * TokenId : sd
     * ID : 1
     * PersonCreateId : 3
     * PersonCreateName : 刘凯
     * PersonCreateHeadUrl :
     * DifficultyLevel : 0
     * TaskState : 0
     * DoneProgress : 50
     * CreateTime : 2016-11-08 18:03:41
     * EndTime : 2016-11-10 18:03:41
     * TaskTitle : 接口测试0
     */
    @Expose
    private String TokenId;
    @Expose
    private String ID;
    @Expose
    private String PersonCreateId;
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
    private int CurrentCompanyId;
    @Expose
    private int PersonTaskState;
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
    private boolean IsTaskTrunDownPerson;

    public String getTokenId() {
        return TokenId;
    }

    public void setTokenId(String TokenId) {
        this.TokenId = TokenId;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPersonCreateId() {
        return PersonCreateId;
    }

    public void setPersonCreateId(String PersonCreateId) {
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

    public int getCurrentCompanyId() {
        return CurrentCompanyId;
    }

    public void setCurrentCompanyId(int currentCompanyId) {
        CurrentCompanyId = currentCompanyId;
    }

    public int getPersonTaskState() {
        return PersonTaskState;
    }

    public void setPersonTaskState(int personTaskState) {
        PersonTaskState = personTaskState;
    }

    public int getPersonLeadingId() {
        return PersonLeadingId;
    }

    public void setPersonLeadingId(int personLeadingId) {
        PersonLeadingId = personLeadingId;
    }

    public String getPersonLeadingName() {
        return PersonLeadingName;
    }

    public void setPersonLeadingName(String personLeadingName) {
        PersonLeadingName = personLeadingName;
    }

    public String getPersonExecuteIds() {
        return PersonExecuteIds;
    }

    public void setPersonExecuteIds(String personExecuteIds) {
        PersonExecuteIds = personExecuteIds;
    }

    public String getPersonExecuteNames() {
        return PersonExecuteNames;
    }

    public void setPersonExecuteNames(String personExecuteNames) {
        PersonExecuteNames = personExecuteNames;
    }

    public int getPersonCheckId() {
        return PersonCheckId;
    }

    public void setPersonCheckId(int personCheckId) {
        PersonCheckId = personCheckId;
    }

    public String getPersonCheckNames() {
        return PersonCheckNames;
    }

    public void setPersonCheckNames(String personCheckNames) {
        PersonCheckNames = personCheckNames;
    }

    public boolean isTaskTrunDownPerson() {
        return IsTaskTrunDownPerson;
    }

    public void setTaskTrunDownPerson(boolean taskTrunDownPerson) {
        IsTaskTrunDownPerson = taskTrunDownPerson;
    }
}
