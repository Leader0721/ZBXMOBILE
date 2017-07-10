package com.zbxn.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 2016/9/30.
 */
public class GrievanceTypeEntity implements Parcelable {

    /**
     * appealTypeName : 补签
     * createtime : null
     * id : null
     * appealtype : 1
     * appealtime : null
     * remark : null
     * attendanceid : null
     * userid : null
     * state : null
     * appealsource : null
     * zmscompanyid : null
     * appealreason : null
     * appealSourceName : null
     */

    @Expose
    private String appealTypeName;
    @Expose
    private int appealtype;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appealTypeName);
        dest.writeInt(this.appealtype);
    }

    protected GrievanceTypeEntity(Parcel in) {
        this.appealTypeName = in.readString();
        this.appealtype = in.readInt();
    }

    public static final Creator<GrievanceTypeEntity> CREATOR = new Creator<GrievanceTypeEntity>() {
        @Override
        public GrievanceTypeEntity createFromParcel(Parcel source) {
            return new GrievanceTypeEntity(source);
        }

        @Override
        public GrievanceTypeEntity[] newArray(int size) {
            return new GrievanceTypeEntity[size];
        }
    };

    public String getAppealTypeName() {
        return appealTypeName;
    }

    public void setAppealTypeName(String appealTypeName) {
        this.appealTypeName = appealTypeName;
    }

    public int getAppealtype() {
        return appealtype;
    }

    public void setAppealtype(int appealtype) {
        this.appealtype = appealtype;
    }
}
