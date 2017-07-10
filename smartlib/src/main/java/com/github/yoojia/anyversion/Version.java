package com.github.yoojia.anyversion;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yoojia.Chen
 * yoojia.chen@gmail.com
 * 2015-01-04
 */
public class Version implements Parcelable {

    /**
     * 版本名称
     */
    public final String name;

    /**
     * 版本更新说明
     */
    public final String note;

    /**
     * 版本 APK 的下载地址
     */
    public final String URL;

    /**
     * 版本代码
     */
    public final int code;

    public Version(String name, String note, String url, int code) {
        this.name = name;
        this.note = note;
        this.URL = url;
        this.code = code;
    }

    @Override
    public String toString() {
        return "Version: \n" +
                "  url = " + URL + "\n" +
                "  name = " + name + "\n" +
                "  note = " + note + "\n" +
                "  code = " + code + "\n";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.note);
        dest.writeString(this.URL);
        dest.writeInt(this.code);
    }

    private Version(Parcel in) {
        this.name = in.readString();
        this.note = in.readString();
        this.URL = in.readString();
        this.code = in.readInt();
    }

    public static final Creator<Version> CREATOR = new Creator<Version>() {
        public Version createFromParcel(Parcel source) {
            return new Version(source);
        }

        public Version[] newArray(int size) {
            return new Version[size];
        }
    };
}
