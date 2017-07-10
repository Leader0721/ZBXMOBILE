package com.zbxn.bean;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/12/2.
 */
public class SearchEntity implements Parcelable{

    private String historyRecord;

    public String getHistoryRecord() {
        return historyRecord;
    }

    public void setHistoryRecord(String historyRecord) {
        this.historyRecord = historyRecord;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
