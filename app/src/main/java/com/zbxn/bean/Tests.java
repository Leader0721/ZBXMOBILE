package com.zbxn.bean;

import android.os.Parcel;
import android.os.Parcelable;

import widget.treelistview.TreeNodeId;
import widget.treelistview.TreeNodeLabel;
import widget.treelistview.TreeNodePid;

/**
 * Created by Administrator on 2016/9/8.
 */
public class Tests implements Parcelable {

    @TreeNodeId
    private int _id;
    @TreeNodePid
    private int parentId;
    @TreeNodeLabel
    private String name;

    public Tests() {

    }

    public Tests(int _id, int parentId, String name) {
        this._id = _id;
        this.parentId = parentId;
        this.name = name;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    protected Tests(Parcel in) {
        this._id = in.readInt();
        this.parentId = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Tests> CREATOR = new Parcelable.Creator<Tests>() {
        @Override
        public Tests createFromParcel(Parcel source) {
            return new Tests(source);
        }

        @Override
        public Tests[] newArray(int size) {
            return new Tests[size];
        }
    };
}
