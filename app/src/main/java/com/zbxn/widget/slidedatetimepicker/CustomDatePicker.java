package com.zbxn.widget.slidedatetimepicker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.zbxn.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * A subclass of {@link DatePicker} that uses
 * reflection to allow for customization of the default blue
 * dividers.
 *
 * @author jjobes
 */
public class CustomDatePicker extends DatePicker {
    private static final String TAG = "CustomDatePicker";

    public CustomDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        Class<?> idClass = null;
        Class<?> numberPickerClass = null;
        Field selectionDividerField = null;
        Field monthField = null;
        Field dayField = null;
        Field yearField = null;
        NumberPicker monthNumberPicker = null;
        NumberPicker dayNumberPicker = null;
        NumberPicker yearNumberPicker = null;

        try {
            // Create an instance of the id class
            idClass = Class.forName("com.android.internal.R$id");

            // Get the fields that store the resource IDs for the month, day and year NumberPickers
            monthField = idClass.getField("month");
            dayField = idClass.getField("day");
            yearField = idClass.getField("year");

            // Use the resource IDs to get references to the month, day and year NumberPickers
            monthNumberPicker = (NumberPicker) findViewById(monthField.getInt(null));
            dayNumberPicker = (NumberPicker) findViewById(dayField.getInt(null));
            yearNumberPicker = (NumberPicker) findViewById(yearField.getInt(null));

            numberPickerClass = Class.forName("android.widget.NumberPicker");

            // Set the value of the mSelectionDivider field in the month, day and year NumberPickers
            // to refer to our custom drawables
            selectionDividerField = numberPickerClass.getDeclaredField("mSelectionDivider");
            selectionDividerField.setAccessible(true);
            //边界线,目前为透明
            selectionDividerField.set(monthNumberPicker, getResources().getDrawable(R.drawable.selection_divider));
            selectionDividerField.set(dayNumberPicker, getResources().getDrawable(R.drawable.selection_divider));
            selectionDividerField.set(yearNumberPicker, getResources().getDrawable(R.drawable.selection_divider));
            /*selectionDividerField.set(monthNumberPicker, getResources().getDrawable(R.drawable.drawable_transparent));
            selectionDividerField.set(dayNumberPicker, getResources().getDrawable(R.drawable.drawable_transparent));
            selectionDividerField.set(yearNumberPicker, getResources().getDrawable(R.drawable.drawable_transparent));*/

            //反射,替换布局

            //    Field attributesArray = DatePicker.class.getDeclaredField("attributesArray");
            //Drawable d = getResources().getDrawable(R.mipmap.circle_tran);
           // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            //    monthNumberPicker.setBackground(d);
           // }
            /*monthNumberPicker.setBackgroundResource(R.mipmap.circle_tran);
            dayNumberPicker.setBackgroundResource(R.mipmap.circle_tran);
            yearNumberPicker.setBackgroundResource(R.mipmap.circle_tran);
*/
            /*monthNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                }
            });
            dayNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                }
            });
            yearNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                }
            });*/

          /*EditText editText = (EditText) monthNumberPicker.findViewById(com.android.internal.R.id.numberpicker_input);
            editText.setBackgroundResource(R.mipmap.circle_tran);*/
        //    setDividerColor(monthNumberPicker);
            /*Field layoutResourceId = DatePicker.class.getField("layoutResourceId");
            layoutResourceId.setAccessible(true);

            layoutResourceId.set(this,R.layout.new_date_picker_legacy);*/


        } catch (ClassNotFoundException e) {
            Log.e(TAG, "ClassNotFoundException in CustomDatePicker", e);
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "NoSuchFieldException in CustomDatePicker", e);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "IllegalAccessException in CustomDatePicker", e);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "IllegalArgumentException in CustomDatePicker", e);
        }



    }

    public static void setDividerColor(NumberPicker picker) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            Log.v("setDividerColor", "pf:" + pf.getName() + " type :" + pf.getGenericType());
            if (pf.getName().equals("mInputText"))//能找到这个域 （分割线视图)
            {
                try {
                    int i = pf.getInt(new R.id());
                    EditText editText = (EditText) picker.findViewById(i);
                    editText.setBackgroundResource(R.mipmap.circle_tran);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
            if (pf.getName().equals("mSelectionDividerHeight"))//找不到这个私有域，（分割线的厚度）
            {
                Log.v("PowerSet", "find......mSelectionDividerHeight.");
            }
        }
    }
}
