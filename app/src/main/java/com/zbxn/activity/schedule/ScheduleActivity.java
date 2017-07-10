package com.zbxn.activity.schedule;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zbxn.bean.ScheduleRuleEntity;
import com.zbxn.bean.adapter.ScheduleAdapter;
import com.zbxn.widget.MyListView;
import com.zbxn.widget.calendar.ScrollLayout;
import com.zbxn.widget.calendar.views.MonthView;
import com.zbxn.widget.calendar.views.WeekView;
import com.zbxn.R;
import com.zbxn.pub.data.ResultData;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.http.HttpCallBack;
import com.zbxn.pub.utils.MyToast;
import com.zbxn.widget.calendar.bizs.calendars.Lauar;
import com.zbxn.widget.calendar.bizs.calendars.ZSSChineseCalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import utils.StringUtils;

/**
 * 项目名称：日程
 * 创建人：wuzy
 * 创建时间：2016/9/21 8:24
 */
public class ScheduleActivity extends AbsToolbarActivity implements
        MonthView.OnMonthChangeEventListener, MonthView.OnSelectDayEventListener,
        View.OnClickListener, ScrollLayout.OnWeekMonthStyleChangeEventListener, AdapterView.OnItemClickListener {

    private static final int Flag_Callback_Add = 1001;

    @BindView(R.id.main_title)
    TextView mainTitle;
    @BindView(R.id.fragmentschedule_titleDateLinearLayout)
    LinearLayout fragmentscheduleTitleDateLinearLayout;
    @BindView(R.id.month_calendar)
    MonthView monthCalendar;
    @BindView(R.id.content_layout)
    LinearLayout contentLayout;
    @BindView(R.id.main_layout)
    LinearLayout mainLayout;
    @BindView(R.id.week_calendar)
    WeekView weekCalendar;
    @BindView(R.id.main_scrolllayout)
    ScrollLayout mainScrolllayout;
    @BindView(R.id.cotent_listview)
    MyListView contentList;
    @BindView(R.id.image_line)
    View imageLine;
    @BindView(R.id.mCalendar)
    TextView mCalendar;//农历

    private Calendar now;
    private List<ScheduleRuleEntity> listDay;
    private ScheduleAdapter mAdapter;

    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private List<ScheduleRuleEntity> listMonth;


    private String date = "";
    private String selectData;

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("日程");// 定义上面的名字
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ScheduleActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ScheduleActivity");
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_schedule_create, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.schedule_creat: //创建日程
                Intent intent = new Intent(this, NewTaskActivity.class);
                intent.putExtra("selectData", selectData);
                startActivityForResult(intent, Flag_Callback_Add);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 滑动关闭当前Activitiv
     */
    @Override
    public boolean getSwipeBackEnable() {
//        return super.getSwipeBackEnable();
        return true;
    }

    @Override
    public int getMainLayout() {
        return R.layout.activity_schedule;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listMonth = new ArrayList<>();

        initView();
        initData();

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");//设置日期格式
        date = sf.format(new Date());
        //mPresenter.findSchedule(this, date, mICustomListener);

        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        selectData = sf1.format(new Date());

        //显示加载成功界面
        updateSuccessView();
    }

    @Override
    public void loadData() {

    }

    private void initView() {
        listDay = new ArrayList<>();
        if (listDay != null) {
            mAdapter = new ScheduleAdapter(this, listDay);
            contentList.setAdapter(mAdapter);
        }
        contentList.setOnItemClickListener(this);

        monthCalendar.setTodayDisplay(true);
        monthCalendar.setOnMonthChangeEventListener(this);
        monthCalendar.setOnDatePickedListener(this);

        weekCalendar.setTodayDisplay(true);
        weekCalendar.setOnSelectDayEventListener(this);

        mainScrolllayout.setOnWeekMonthStyleChangeEventListener(this);

        fragmentscheduleTitleDateLinearLayout.setOnClickListener(this);

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initData() {
        now = Calendar.getInstance();
        mainTitle.setText(now.get(Calendar.YEAR) + "年" + (now.get(Calendar.MONTH) + 1) + "月");
        getToolbarHelper().setTitle("日程   " + now.get(Calendar.YEAR) + "年" + (now.get(Calendar.MONTH) + 1) + "月");

        monthCalendar.setDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1);
        weekCalendar.setDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1);
    }

    @OnClick({R.id.fragmentschedule_titleDateLinearLayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragmentschedule_titleDateLinearLayout://标题
                break;
        }
    }

    @Override
    public void onMonthChangeEvent(int year, int month) {
        Calendar c = Calendar.getInstance();
        int nowYear = c.get(Calendar.YEAR);
        int nowMonth = c.get(Calendar.MONTH) + 1;
        /*if ((nowYear == year) && (nowMonth == month)) {
            schedule_topbar_comenow_button.setVisibility(View.INVISIBLE);
        } else {
            schedule_topbar_comenow_button.setVisibility(View.VISIBLE);
        }*/
        mainTitle.setText(year + "年" + month + "月");
        getToolbarHelper().setTitle("日程   " + year + "年" + month + "月");
//        Toast.makeText(this, "切换月啦", Toast.LENGTH_LONG).show();

        listDay.clear();
        mAdapter.notifyDataSetChanged();

        date = year + "-" + (month < 10 ? "0" + month : month);
        findSchedule(this, date);

    }

    @Override
    public void onSelectDayEvent(String date) {
        date = StringUtils.convertDate(date.replace(".", "-"));
        try {
            if (TextUtils.isEmpty(date)) {
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            mCalendar.setText("农历" + new Lauar(calendar).toString());
            listDay.clear();
            for (int i = 0; i < listMonth.size(); i++) {
                if (listMonth.get(i).getStarttime().startsWith(date)) {
                    listDay.add(listMonth.get(i));
                }
            }

            Collections.sort(listDay, new Comparator<ScheduleRuleEntity>() {
                @Override
                public int compare(ScheduleRuleEntity a, ScheduleRuleEntity b) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Date dateA = sdf.parse(a.getStarttime());
                        Date dateB = sdf.parse(b.getStarttime());
                        if (dateA.before(dateB)) {
                            return -1;
                        } else if (!dateA.before(dateB)) {
                            return 1;
                        } else {
                            return 0;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return 0;
                    }
                }
            });

            mAdapter.notifyDataSetChanged();
            //Toast.makeText(this, "" + date.replace(".", "-"), Toast.LENGTH_LONG).show();
            System.out.println("当前选择的天：" + date.replace(".", "-"));
            selectData = date;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWeekMonthStyleChangeEvent(int style) {
        /*if (style == 1) {
//            Toast.makeText(this, "切换成周啦", Toast.LENGTH_LONG).show();
        } else {
//            Toast.makeText(this, "切换成月啦", Toast.LENGTH_LONG).show();
        }*/
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, NewTaskActivity.class);
        intent.putExtra("sign", 1);
        if (listDay != null) {
            intent.putExtra("id", listDay.get(position).getId() + "");
            intent.putExtra("mScheduleRuleType", listDay.get(position).getScheduleRuleType());
//            intent.putExtra("scheduleId",listDay.get(position).)
        }
        startActivityForResult(intent, Flag_Callback_Add);
    }

    /**
     * 回调需要接收的人员
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Flag_Callback_Add) {//参与人
            if (data != null) {
                if (resultCode == RESULT_OK) {
                    findSchedule(this, date);
                }
            } else {
                return;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 按月份查询用户当月全部的日程
     */
    public void findSchedule(Context context, String starttime) {
        //请求网络
        Map<String, String> map = new HashMap<String, String>();
        map.put("starttime", starttime);//开始时间

        callRequest(map, "oaScheduleRule/findSchedule.do", new HttpCallBack(ScheduleRuleEntity.class, context) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    List<ScheduleRuleEntity> list = mResult.getRows();
                    listMonth.clear();
                    listMonth.addAll(list);
                    List<String> listTemp = new ArrayList<>();
                    int year = 0;
                    int month = 0;
                    for (int i = 0; i < list.size(); i++) {
                        try {
                            Date date = sf.parse(list.get(i).getStarttime());
                            int day = date.getDate();
                            year = date.getYear();
                            month = date.getMonth();
                            listTemp.add(day + "");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    ZSSChineseCalendar.SCHEDULE.clear();
                    ZSSChineseCalendar.SCHEDULE.put(month + "", listTemp);

                    monthCalendar.setInvalidate();
                    weekCalendar.setInvalidate();

                    onSelectDayEvent(selectData);
                } else {
                    MyToast.showToast(mResult.getMsg());
                }
            }

            @Override
            public void onFailure(String string) {
            }
        });
    }
}
