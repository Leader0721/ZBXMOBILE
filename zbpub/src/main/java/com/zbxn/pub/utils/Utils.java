package com.zbxn.pub.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.InputStream;

public class Utils {
    /**
     * 减掉总间距，一行显示几个,父视图是RelativeLayout
     *
     * @param context
     * @param view
     * @param bWidth
     * @param bHeight
     * @param gapCount
     * @param gapWidth
     */
    public static void setPicHeightExRelativeLayout(Context context, View view,
                                                    int bWidth, int bHeight, float gapCount, float gapWidth) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                .getLayoutParams();
        try {
            // 获取屏幕当前分辨率
            DisplayMetrics dm = new DisplayMetrics();
            dm = context.getResources().getDisplayMetrics();
            float screenWidth = (dm.widthPixels - Dp2Px(context, gapWidth))
                    / gapCount;
            float screenHeight = dm.heightPixels;
            float socalW = screenWidth / bWidth;
            float socalH = screenHeight / bHeight;
            float socal = socalW < socalH ? socalW : socalH;
            int height = (int) (bHeight * socal);
            layoutParams.height = height;
        } catch (Exception e) {
            layoutParams.height = bHeight;
        }
        view.setLayoutParams(layoutParams);
    }
    /**
     * 减掉总间距，一行显示几个,父视图是LinearLayout,并返回高度
     *
     * @param context
     * @param view
     * @param bWidth
     * @param bHeight
     * @param gapWidth
     */
    public static float getPicHeightExLinearLayout(Context context, View view,
                                                   int bWidth, int bHeight, float gapWidth) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view
                .getLayoutParams();
        try {
            // 获取屏幕当前分辨率
            DisplayMetrics dm = new DisplayMetrics();
            dm = context.getResources().getDisplayMetrics();
            float screenWidth = (dm.widthPixels - Dp2Px(context, gapWidth)) * 0.5f;
            layoutParams.width = (int) screenWidth;
            layoutParams.height = (int) screenWidth;
        } catch (Exception e) {
            layoutParams.width = LayoutParams.WRAP_CONTENT;
            layoutParams.height = LayoutParams.WRAP_CONTENT;
        }
        view.setLayoutParams(layoutParams);
        return layoutParams.height - 1;
    }

    public static void setPicHeightExLinearLayout1(Context context, View view, int bHeight) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view
                .getLayoutParams();
        try {
            layoutParams.height = bHeight;
        } catch (Exception e) {
            layoutParams.height = bHeight;
        }
        view.setLayoutParams(layoutParams);
    }

    /**
     * 图片的px转为手机中px
     *
     * @param context
     * @param bWidth
     * @return
     */
    public static float getWidth(Context context,
                                 int bWidth) {
        float screenWidth = bWidth;
        try {
            // 获取屏幕当前分辨率
            DisplayMetrics dm = new DisplayMetrics();
            dm = context.getResources().getDisplayMetrics();
            screenWidth = dm.widthPixels * ((float) bWidth / 640f);
            System.out.println("screenWidth:" + screenWidth);
        } catch (Exception e) {
        }
        return screenWidth;
    }

    /**
     * dp转px
     *
     * @param context
     * @param dp
     * @return
     */
    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 获取屏幕当前分辨率
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        try {
            // 获取屏幕当前分辨率
            DisplayMetrics dm = new DisplayMetrics();
            dm = context.getResources().getDisplayMetrics();
            int screenWidth = dm.widthPixels;
            // float screenHeight = dm.heightPixels;
            return screenWidth;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取屏幕当前分辨率
     *
     * @param context
     * @return
     */
    public static float getScreenHeight(Context context) {
        try {
            // 获取屏幕当前分辨率
            DisplayMetrics dm = new DisplayMetrics();
            dm = context.getResources().getDisplayMetrics();
//            float screenWidth = dm.widthPixels;
            float screenHeight = dm.heightPixels;
            return screenHeight;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 强制隐藏键盘
     *
     * @param v
     */
    public static void hideSoftInput(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0); // 强制隐藏键盘
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /*
     * 显示window
     */
    public static void showWindow(Window win) {
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
    }
}
