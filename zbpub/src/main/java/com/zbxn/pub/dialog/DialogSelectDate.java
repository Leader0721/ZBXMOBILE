package com.zbxn.pub.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbxn.pub.R;
import com.zbxn.pub.utils.DialogAlertListener;
import com.zbxn.pub.widget.wheeldate.JudgeDate;
import com.zbxn.pub.widget.wheeldate.ScreenInfo;
import com.zbxn.pub.widget.wheeldate.WheelMain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 时间选择器
 */
public class DialogSelectDate extends Dialog implements OnClickListener {
    private final int FUNID1 = 100;

    private Activity context;

    private TextView m_left;
//    private TextView m_title;
    private TextView m_right;
    private DialogAlertListener listener;
    private Object param;

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private WheelMain wheelMain;

    public DialogSelectDate(Activity context, DialogAlertListener listener, Object param) {
        super(context, R.style.dialog_alert);
        this.context = context;
        this.listener = listener;
        this.param = param;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_date);
        setCanceledOnTouchOutside(false);

        m_left = (TextView) findViewById(R.id.m_left);
        m_left.setOnClickListener(this);
//        m_title = (TextView) findViewById(R.id.m_title);
//        m_title.setText("选择日期");
        m_right = (TextView) findViewById(R.id.m_right);
        m_right.setOnClickListener(this);
        m_right.setVisibility(View.VISIBLE);

        if (listener != null) {
            listener.onDialogCreate(this, param);
        }

        //设置
        initView();
    }

    private void initView() {
        LinearLayout timePicker = (LinearLayout) findViewById(R.id.timePicker);
        ScreenInfo screenInfo = new ScreenInfo(context);
        wheelMain = new WheelMain(timePicker);
        wheelMain.screenheight = screenInfo.getHeight();
        String time = param.toString();
        Calendar calendar = Calendar.getInstance();
        if (JudgeDate.isDate(time, "yyyy-MM-dd")) {
            try {
                calendar.setTime(dateFormat.parse(time));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        wheelMain.initDateTimePicker(year, month, day);
    }

    @Override
    public void onClick(View v) {
        if (v == m_left) {
            onBtnCancel();
        } else if (v == m_right) {
            listener.onDialogOk(this, wheelMain.getTime());
        }
    }

    private void onBtnCancel() {
        cancel();
        if (listener != null) {
            listener.onDialogCancel(this, param);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBtnCancel();
        }
        return super.onKeyDown(keyCode, event);
    }

}
