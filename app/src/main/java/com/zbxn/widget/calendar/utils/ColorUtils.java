package com.zbxn.widget.calendar.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources.Theme;
import android.os.Build;
import android.widget.TextView;

/**
 * 
 * @author ZSS 2016-04-13
 *
 */
public class ColorUtils {

	@TargetApi(23)
	@SuppressWarnings("deprecation")
	public static void  setTextColor(Context mContext,TextView textview,int color,Theme theme){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			textview.setTextColor(mContext.getResources().getColor(color, theme));
		}else{
			textview.setTextColor(mContext.getResources().getColor(color));
		}
	}
}
