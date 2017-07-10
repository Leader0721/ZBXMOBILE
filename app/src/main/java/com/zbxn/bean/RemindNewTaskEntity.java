package com.zbxn.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/9/8.
 */
public class RemindNewTaskEntity implements Parcelable {
    private String typeName;
    private int precedeType;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getPrecedeType() {
        return precedeType;
    }

    public void setPrecedeType(int precedeType) {
        this.precedeType = precedeType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.typeName);
        dest.writeInt(this.precedeType);
    }

    public RemindNewTaskEntity() {
    }

    protected RemindNewTaskEntity(Parcel in) {
        this.typeName = in.readString();
        this.precedeType = in.readInt();
    }

    public static final Creator<RemindNewTaskEntity> CREATOR = new Creator<RemindNewTaskEntity>() {
        @Override
        public RemindNewTaskEntity createFromParcel(Parcel source) {
            return new RemindNewTaskEntity(source);
        }

        @Override
        public RemindNewTaskEntity[] newArray(int size) {
            return new RemindNewTaskEntity[size];
        }
    };
}
