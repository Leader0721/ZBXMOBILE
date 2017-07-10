package com.zbxn.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * @author: ysj
 * @date: 2016-12-02 15:00
 */
public class OkrDepartEntity implements Parcelable {

    /**
     * DepartmentId : 123
     * DepartmentName : 开发部
     */
    @Expose
    private int DepartmentId;
    @Expose
    private String DepartmentName;

    @Override
    public int describeContents() {
        return 0;
    }

    protected OkrDepartEntity(Parcel in) {
        this.DepartmentId = in.readInt();
        this.DepartmentName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.DepartmentId);
        dest.writeString(this.DepartmentName);
    }

    public int getDepartmentId() {
        return DepartmentId;
    }

    public void setDepartmentId(int DepartmentId) {
        this.DepartmentId = DepartmentId;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String DepartmentName) {
        this.DepartmentName = DepartmentName;
    }

    public static final Creator<OkrDepartEntity> CREATOR = new Creator<OkrDepartEntity>() {
        @Override
        public OkrDepartEntity createFromParcel(Parcel source) {
            return new OkrDepartEntity(source);
        }

        @Override
        public OkrDepartEntity[] newArray(int size) {
            return new OkrDepartEntity[size];
        }
    };
}
