package com.zbxn.pub.utils;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import utils.StringUtils;

/**
 * Created by 王军 on 2016/11/10.
 * <p/>
 * 返回boolean
 */
public class IsEmptyUtil {
    //判断字符串是否非空,空则土司
    public static boolean isEmpty(String text, String toastStr) {
        if (StringUtils.isEmpty(text)) {
            MyToast.showToast(toastStr);
            return false;
        }
        return true;
    }//判断字符串是否非空,空则土司
    public static boolean isEmpty(EditText editText, String toastStr) {
        if (StringUtils.isEmpty(editText)) {
            MyToast.showToast(toastStr);
            return false;
        }
        return true;
    }
    //判断字符串是否非空,空则土司
    public static boolean isEmpty(TextView textView, String toastStr) {
        if (StringUtils.isEmpty(textView)) {
            MyToast.showToast(toastStr);
            return false;
        }
        return true;
    }

    //判断字符串是否非空,或者是默认值,空则土司
    public static boolean isEmptyOrDefault(String text, String toastStr, String defaultStr) {
        if (TextUtils.isEmpty(text) || text.equals(defaultStr)) {
            MyToast.showToast(toastStr);
            return false;
        }
        return true;
    }

    //判断字符串是否非空,或者是默认值,空则土司
    public static boolean isEmptyOrDefault(TextView text, String toastStr, String defaultStr) {
        if (StringUtils.isEmpty(text) || text.equals(defaultStr)) {
            MyToast.showToast(toastStr);
            return false;
        }
        return true;
    }

    //判断字符串是否非空,或者是默认值,空则土司
    public static boolean isEmptyOrDefault(EditText text, String toastStr, String defaultStr) {
        if (StringUtils.isEmpty(text) || text.equals(defaultStr)) {
            MyToast.showToast(toastStr);
            return false;
        }
        return true;
    }


}
