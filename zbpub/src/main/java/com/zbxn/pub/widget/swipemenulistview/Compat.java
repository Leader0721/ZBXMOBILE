package com.zbxn.pub.widget.swipemenulistview;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

/**
 * Created by yuyidong on 15/12/7.
 */
class Compat {

    protected static void setBackgroundDrawable(View view, Drawable drawable) {
    	//16 = Build.VERSION_CODES.JELLY_BEAN
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }
}
