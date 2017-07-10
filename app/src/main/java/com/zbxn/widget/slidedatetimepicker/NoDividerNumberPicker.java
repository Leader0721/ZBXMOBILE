package com.zbxn.widget.slidedatetimepicker;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.NumberPicker;

import java.lang.reflect.Field;

/**
 * Created by wj on 2016/11/22.
 * 取消了分割线,暂时不用
 */
public class NoDividerNumberPicker extends NumberPicker {


    public NoDividerNumberPicker(Context context) {
        //    super(context);
        this(context, null);
    }

    public NoDividerNumberPicker(Context context, AttributeSet attrs) {
        //    super(context, attrs);
        this(context, attrs, 0);
    }

    public NoDividerNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        try {
            Field divider_height = NumberPicker.class.getDeclaredField("UNSCALED_DEFAULT_SELECTION_DIVIDER_HEIGHT");
            divider_height.setAccessible(true);
            divider_height.set(this,0);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }


}
