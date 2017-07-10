package com.zbxn.activity.schedule;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.zbxn.R;
import com.zbxn.activity.ContactsPicker;
import com.zbxn.activity.main.contacts.ContactsChoseActivity;
import com.zbxn.bean.RepeatNewTaskEntity;
import com.zbxn.bean.ScheduleDetailEntity;
import com.zbxn.bean.ScheduleRuleEntity;
import com.zbxn.bean.ScheduleShareEntity;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.data.ResultData;
import com.zbxn.pub.dialog.MessageDialog;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.frame.mvp.base.ControllerCenter;
import com.zbxn.pub.http.HttpCallBack;
import com.zbxn.pub.utils.MyToast;
import com.zbxn.widget.slidedatetimepicker.NewSlideDateTimeDialogFragment;
import com.zbxn.widget.slidedatetimepicker.SlideDateTimeListener;
import com.zbxn.widget.slidedatetimepicker.SlideDateTimePicker;
import com.zcw.togglebutton.ToggleButton;

import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import utils.StringUtils;

/**
 * 项目名称：创建日程
 * 创建人：LiangHanXin
 * 创建时间：2016/9/21 8:24
 */
public class NewTaskActivity extends AbsToolbarActivity implements ControllerCenter {

    /**
     * 选择接收人回调
     */
    private static final int Flag_Callback_ContactsPicker = 1001;//参与人
    private static final int Flag_Callback_ContactsPicker1 = 1002;//重复
    private static final int Flag_Callback_ContactsPicker2 = 1003;//提醒
    private static final int Flag_Callback_ContactsPicker3 = 1004;//共享人
    private static final int Flag_Callback_ShowAddButton = 1005;


    @BindView(R.id.mStartTime)
    TextView mStartTime;
    @BindView(R.id.mEndTime)
    TextView mEndTime;
    @BindView(R.id.mySharedperson)//共享人回调
            TextView mySharedperson;
    @BindView(R.id.mSharedperson)//共享人按钮
            LinearLayout mSharedperson;
    @BindView(R.id.mReceiveUsers)//接收人
            TextView mReceiveUsers;
    @BindView(R.id.myRemind)//提醒回调参数
            TextView myRemind;
    @BindView(R.id.myRepeat)//重复回调参数
            TextView myRepeat;
    @BindView(R.id.mTitle)
    EditText mTitle;
    @BindView(R.id.mContent)
    WebView mContent;
    @BindView(R.id.mToggleButton)
    ToggleButton mToggleButton;
    @BindView(R.id.mToggleButtonRemind)
    ToggleButton mToggleButtonRemind;
    @BindView(R.id.mPlace)
    EditText mPlace;
    @BindView(R.id.mRemind)
    LinearLayout mRemind;
    @BindView(R.id.mParticipant)
    LinearLayout mParticipant;
    @BindView(R.id.mRepeat)
    LinearLayout mRepeat;
    public boolean myswitch = true;//开关 选择全天的  myswitch false 全天   true 非全天
    @BindView(R.id.mScheduleButton)
    CircularProgressButton mScheduleButton;//删除

    private String[] mReceiveArray;//接收人Id数组
    private MenuItem mCollect;

    private List<RepeatNewTaskEntity> typeList;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat selectFormat = new SimpleDateFormat("HH:mm");

    private NewTaskPresenter mPresenter;
    private RemindNewTackPresenter remindPresenter;
    private DetailTaskPresenter detailPresenter;

    private String shareduserId = "";//共享人id
    private String articipantuserId = "";//参与人id
    private String precedeType = "1";//提醒类型 0标准 默认""数据库就不会修改该字段了

    private int sign;//详情标记（0：创建,1:详情）
    private int flag;//-1 从消息界面跳入
    private int editTag;
    private String mId; //日程id

    private int mScheduleRuleType; //日程类型   0：自己创建1：分享的2：参与的 3:自己加的分享可编辑的人
    private int isalarm = -1; //是否可更改提醒   是否提醒 0 false   1提醒
    private int participantISAlarm;

    private int isalarmToggle = 0;//是否提醒  默认不提醒  是否提醒 0 false   1提醒

    private String myStartTime;//拼接完的开始时间
    private String myEndTime;//结束时间

    private String selectData;
    private String selectDataEnd;
    private String selectDay;
    private String selectDayEnd;

    private Date dateStart = null;
    private Date dateEnd = null;
    private String strStart = "";
    private String strEnd = "";

    private ScheduleRuleEntity entityDetail;

    //选择人员共享人
    private ArrayList<Contacts> mListContactsScharer = new ArrayList<>();
    //选择人员参与人
    private ArrayList<Contacts> mListContactsParticipant = new ArrayList<>();

    private String mContentStr = "请输入日程详情";
    private final String mContentTip = "请输入日程详情";

    //0--没开始加载  1--加载中  2--加载完成
    private int mWebviewState = 0;

    @Override
    public boolean handleMessage(Message msg) {

        switch (msg.what) {
            case Flag_Callback_ShowAddButton:
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }

        return false;
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("创建日程");// 定义上面的名字
        return super.onToolbarConfiguration(toolbar, params);
    }

    /**
     * 创建按钮显示
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (sign == 1) {
            getMenuInflater().inflate(R.menu.menu_schedule_edit, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_establish, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 创建按钮监听
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (sign == 1) {
            mCollect = menu.findItem(R.id.mScheduleEdit);
            switch (mScheduleRuleType) {
                case 0:
                case 3:
                    mCollect.setEnabled(true);
                    break;
                case 2:
                    mCollect.setEnabled(true);
                    break;
                case 1:
                    mCollect.setEnabled(false);
                    break;
            }
        } else {
            mCollect = menu.findItem(R.id.mEstablish);
            mCollect.setEnabled(true);
        }
        return true;
    }

    /**
     * 创建按钮的点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date d1 = null;
        Date d2 = null;
        boolean flag;
        switch (item.getItemId()) {
            case R.id.mEstablish:
                if (myswitch) {// myswitch false 全天   true 非全天
                    myStartTime = StringUtils.getEditText(mStartTime) + ":00";
                    myEndTime = StringUtils.getEditText(mEndTime) + ":00";
                } else {//全天加时间
                    myStartTime = StringUtils.getEditText(mStartTime) + " 00:00:00";//拼接时间格式
                    myEndTime = StringUtils.getEditText(mEndTime) + " 23:59:00";//
                }

                try {
                    d1 = sdf.parse(myStartTime);
                    d2 = sdf.parse(myEndTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                flag = d2.before(d1);
                if (flag) {
                    // System.out.print("早于今天");
                    message().show("结束时间不能早于开始时间");
                    break;
                }
                //是否提醒 0 false   1提醒
                if ("0".equals(isalarmToggle + "")) {
                    precedeType = "1";
                }
                mWebviewState = 1;
                //获取内容
                mContent.loadUrl("javascript:getHTML()");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //0--没开始加载  1--加载中  2--加载完成
                        while (mWebviewState == 1) {
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        mWebviewState = 1;
                        handler.sendEmptyMessage(0);
                    }
                }).start();
                break;

            case R.id.mScheduleEdit:
                if (editTag == 0) {
                    item.setTitle("完成");
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    getToolbarHelper().setTitle("编辑");
                    if (mScheduleRuleType == 0) {
                        setViewEnabled(true);
                    } else if (mScheduleRuleType == 3) {
                        setViewEnabled(true);
                        mScheduleButton.setVisibility(View.GONE);
                    } else if (mScheduleRuleType == 2) {
                        setViewEnabled(false);
                        // 是否提醒 0 false   1提醒
                        if (isalarm == 1) {
                            mToggleButtonRemind.setEnabled(true);
                        } else {
                            mToggleButtonRemind.setEnabled(false);
                        }
                        if (participantISAlarm == 1) {
                            mRemind.setVisibility(View.VISIBLE);
                        } else {
                            mRemind.setVisibility(View.GONE);
                        }
                    }

                    editTag++;
                } else {
                    if (myswitch) {// myswitch false 全天   true 非全天
                        myStartTime = StringUtils.getEditText(mStartTime) + ":00";
                        myEndTime = StringUtils.getEditText(mEndTime) + ":00";
                    } else {//全天加时间
                        myStartTime = StringUtils.getEditText(mStartTime) + " 00:00:00";//拼接时间格式
                        myEndTime = StringUtils.getEditText(mEndTime) + " 23:59:00";//
                    }

                    try {
                        d1 = sdf.parse(myStartTime);
                        d2 = sdf.parse(myEndTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    flag = d2.before(d1);
                    if (flag) {
                        // System.out.print("早于今天");
                        message().show("结束时间不能早于开始时间");
                        break;
                    }

                    if (mScheduleRuleType == 3) {
                        mScheduleRuleType = 0;
                    }
                    //是否提醒 0 false   1提醒
                    if ("0".equals(isalarmToggle + "")) {
                        precedeType = "1";
                    }
                    mWebviewState = 1;
                    //获取内容
                    mContent.loadUrl("javascript:getHTML()");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //0--没开始加载  1--加载中  2--加载完成
                            while (mWebviewState == 1) {
                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            mWebviewState = 1;
                            handler.sendEmptyMessage(1);
                        }
                    }).start();
                }
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
        return R.layout.activity_newtask;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToggleButtonRemind.setToggleOn(true);
        mPresenter = new NewTaskPresenter(this);

        //获取当前时间方法
        Calendar calendar = Calendar.getInstance();
        dateStart = calendar.getTime();
        strStart = format.format(dateStart);
        myStartTime = strStart + ":00";

        calendar.add(Calendar.MINUTE, 30);
        dateEnd = calendar.getTime();
        strEnd = format.format(dateEnd);
        myEndTime = strEnd + ":00";
        mRepeat.setVisibility(View.GONE);//隐藏了 是否重复 的功能下一个版本开发

        //获取当前时间方法
        selectDay = selectFormat.format(dateStart);
        selectDayEnd = selectFormat.format(dateEnd);
        initData();
        initDetailData();
        //显示加载成功界面
        updateSuccessView();
    }

    @Override
    public void loadData() {

    }

    /**
     * 获取当前时间显示上去
     */
    private void initData() {
        mContent.setWebViewClient(new WebViewClient());
        WebSettings webSettings = mContent.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSupportZoom(false);
        webSettings.setDisplayZoomControls(false);
        mWebviewState = 1;
        mContent.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //view.loadUrl(url);
                mWebviewState = 2;
                if (url.startsWith("gethtml:")) {
                    System.out.println("gethtml:|||" + url);
                    try {
                        mContentStr = URLDecoder.decode(url.substring(8).toString(), "UTF-8");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("gethtml:强强强强|||" + mContentStr);
                    return true;
                } else {
                    return true;
                }
                // return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mWebviewState = 2;
                super.onPageFinished(view, url);
            }
        });
        mContent.loadUrl("file:///android_asset/Demo.html");
        /**
         * 开关的监听
         *
         * @param savedInstanceState
         */
        mToggleButton.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {//全天
                    myswitch = false;
                    if (sign == 1) {
                        selectData = format1.format(dateStart);
                    }
                    mStartTime.setText(selectData);
                    mEndTime.setText(selectData);
                    myStartTime = selectData + " 00:00:00";//拼接时间格式
                    myEndTime = selectDataEnd + " 23:59:00";//
                } else {//非全天加时间
                    myswitch = true;
                    if (sign == 1) {
                        selectData = format1.format(dateStart);
                    }
                    mStartTime.setText(selectData + " " + selectDay);
                    mEndTime.setText(selectData + " " + selectDayEnd);


                    myStartTime = selectData + " " + selectDay + ":00";
                    myEndTime = selectData + " " + selectDayEnd + ":00";
                }
            }
        });
        isalarmToggle = 1;//默认打开提醒
        mToggleButtonRemind.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {//提醒
                    isalarmToggle = 1;
                    mRemind.setVisibility(View.VISIBLE);
                } else {//不提醒
                    isalarmToggle = 0;
                    mRemind.setVisibility(View.GONE);
                }
            }
        });
        mStartTime.setText(strStart);
        mEndTime.setText(strEnd);

    }

    private void initDetailData() {
        detailPresenter = new DetailTaskPresenter(this);
        remindPresenter = new RemindNewTackPresenter(this);
        Intent intent = getIntent();
        mId = intent.getStringExtra("id");
        sign = intent.getIntExtra("sign", 0);
        flag = intent.getIntExtra("flag", 0);
        mScheduleRuleType = intent.getIntExtra("mScheduleRuleType", -1);
        selectData = intent.getStringExtra("selectData");

        if (!StringUtils.isEmpty(selectData)) {
            mStartTime.setText(selectData + " " + selectDay);
            mEndTime.setText(selectData + " " + selectDayEnd);
        }
        if (sign == 0) {
            mPresenter.getScheduleShare(this, mICustomListener);
        }
        //详情标记（0：创建,1:详情）
        if (sign == 1) {
            remindPresenter.save(this, mICustomListener);
            getScheduleDetail(this, mId);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            getToolbarHelper().setTitle("日程详情");
            setViewEnabled(false);
            mScheduleButton.setVisibility(View.VISIBLE);
            if (mScheduleRuleType != 0) {
                mScheduleButton.setVisibility(View.GONE);
            }
            mReceiveUsers.setHint(null);
            mySharedperson.setHint(null);
            mTitle.setHint(null);
            handler.sendEmptyMessage(2);
//            mContent.setHint(null);
            /*new Thread(new Runnable() {
                @Override
                public void run() {
                    //0--没开始加载  1--加载中  2--加载完成
                    while (mWebviewState == 1) {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mWebviewState = 1;
                    handler.sendEmptyMessage(2);
                }
            }).start();*/
        } else {
            mRemind.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 根据日程id获取日程详情
     */
    public void getScheduleDetail(Context context, String id) {
        //请求网络
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);

        callRequest(map, "oaScheduleRule/getId.do", new HttpCallBack(ScheduleRuleEntity.class, context) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    entityDetail = (ScheduleRuleEntity) mResult.getData();
                    mTitle.setText(entityDetail.getTitle());
                    //                    mContent.setText(entityDetail.getScheduleDetail());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //0--没开始加载  1--加载中  2--加载完成
                            while (mWebviewState == 1) {
                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            mWebviewState = 1;
                            handler.sendEmptyMessage(3);
                        }
                    }).start();
                    int allDay = entityDetail.getAllday();
                    if (allDay == 0) {
                        mToggleButton.setToggleOff(true);
                        mStartTime.setText(entityDetail.getStarttime().substring(0, 16));
                        mEndTime.setText(entityDetail.getEndtime().substring(0, 16));
                        myStartTime = entityDetail.getStarttime();
                        myEndTime = entityDetail.getEndtime();
                    } else {
                        mToggleButton.setToggleOn(true);
                        myStartTime = entityDetail.getStarttime().substring(0, 10) + " 00:00:00";
                        myEndTime = entityDetail.getEndtime().substring(0, 10) + " 23:59:00";

                        mStartTime.setText(myStartTime);
                        mEndTime.setText(myEndTime);
                    }
                    if (StringUtils.isEmpty(entityDetail.getLocation())) {
                        mPlace.setText("\u3000");
                    } else {
                        mPlace.setText(entityDetail.getLocation());
                    }

                    if (flag == -1) {
                        mScheduleRuleType = entityDetail.getScheduleRuleType();
                        if (mScheduleRuleType == 1) {
                            mCollect.setEnabled(false);
                        }
                    }

                    isalarm = entityDetail.getIsalarm();//是否提醒
                    participantISAlarm = entityDetail.getParticipantISAlarm();
                    if (mScheduleRuleType == 0) {
                        mScheduleButton.setVisibility(View.VISIBLE);
                    }

                    isalarmToggle = entityDetail.getIsalarm();//是否提醒  上传时用
                    //是否提醒 0 false   1提醒


                    if (isalarm == 0) {
                        mToggleButtonRemind.setToggleOff();
                        mRemind.setVisibility(View.GONE);
                        if (mScheduleRuleType == 2) {
                            mCollect.setEnabled(false);
                        }
                    } else {
                        mToggleButtonRemind.setToggleOn();
                        mRemind.setVisibility(View.VISIBLE);
                        if (typeList != null) {
                            for (int i = 0; i < typeList.size(); i++) {
                                if (entityDetail.getPrecedeType() == i) {
                                    myRemind.setText(typeList.get(i).getTypeName());
                                    precedeType = i + "";
                                }
                            }
                        }
                    }

                    if (mScheduleRuleType == 2) {
                        if (participantISAlarm == 0) {
                            mToggleButtonRemind.setToggleOff();
                            mRemind.setVisibility(View.GONE);
                        } else if (participantISAlarm == 1) {
                            mToggleButtonRemind.setToggleOn();
                            mRemind.setVisibility(View.VISIBLE);
                        }
                    }

                    //(共享人是否可编辑) 0:false 1:true
                    if ("1".equals(entityDetail.getShareIsEdit())) {
                        mScheduleRuleType = 3;
                        mScheduleButton.setVisibility(View.GONE);
                        mCollect.setEnabled(true);
                    }

                    //参与人
                    List<ScheduleDetailEntity> participantList = entityDetail.getParticipantUserList();
                    String particicipant = "";
                    for (int i = 0; i < participantList.size(); i++) {
                        String name = participantList.get(i).getUserName();
                        particicipant = particicipant + name + " ";
                        String particicipantId = participantList.get(i).getParticipantid() + "";
                        if (i == participantList.size() - 1) {
                            articipantuserId = articipantuserId + particicipantId;
                        } else {
                            articipantuserId = particicipantId + ",";
                        }
                    }
                    mReceiveUsers.setText(particicipant);

                    //共享人
                    List<ScheduleDetailEntity> scharerList = entityDetail.getShareUserList();
                    String scharer = "";
                    for (int i = 0; i < scharerList.size(); i++) {
                        String name = scharerList.get(i).getUserName();
                        scharer = scharer + name + " ";
                        String sharId = scharerList.get(i).getScheduleid() + "";
                        if (i == scharerList.size() - 1) {
                            shareduserId = shareduserId + sharId;
                        } else {
                            shareduserId = sharId + ",";
                        }
                    }
                    mySharedperson.setText(scharer);
                } else {
                    MyToast.showToast(mResult.getMsg());
                }
            }

            @Override
            public void onFailure(String string) {
            }
        });
    }

    public void setViewEnabled(boolean b) {
        mTitle.setEnabled(b);
        mContent.setEnabled(b);
        mToggleButton.setEnabled(b);
        mToggleButtonRemind.setEnabled(b);
        mPlace.setEnabled(b);
        mRemind.setEnabled(b);
        mRepeat.setEnabled(b);
        mStartTime.setEnabled(b);
        mEndTime.setEnabled(b);
        mySharedperson.setEnabled(b); //分享人
        mParticipant.setEnabled(b); //参与人
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
        if (requestCode == Flag_Callback_ContactsPicker) {//参与人
            if (data != null) {
                if (resultCode == RESULT_OK) {
//                    List<Contacts> list = data.getParcelableArrayListExtra(ContactsPicker.Flag_Output_Checked);
                    mListContactsParticipant = (ArrayList<Contacts>) data.getExtras().getSerializable(ContactsPicker.Flag_Output_Checked);
                    mReceiveArray = new String[mListContactsParticipant.size()];
                    String content = "";
                    articipantuserId = "";
                    for (int i = 0; i < mListContactsParticipant.size(); i++) {
                        mReceiveArray[i] = mListContactsParticipant.get(i).getId() + "";
                        content += mListContactsParticipant.get(i).getUserName() + ",";
                        articipantuserId += mListContactsParticipant.get(i).getId() + ",";
                    }
                    if (!StringUtils.isEmpty(articipantuserId)) {
                        content = content.substring(0, content.length() - 1);
                        articipantuserId = articipantuserId.substring(0, articipantuserId.length() - 1);
                    }
                    mReceiveUsers.setText(content);
                }
            } else {
                return;
            }
        } else if (requestCode == Flag_Callback_ContactsPicker1) {//提醒
            if (data != null) {
                String typeName = data.getStringExtra("typeName");
                precedeType = data.getStringExtra("precedeType");
                myRemind.setText(typeName);
            } else {
                return;
            }
        } else if (requestCode == Flag_Callback_ContactsPicker2) {//重复
            String datt = data.getStringExtra("ontacts1");
            myRepeat.setText(datt);
        } else if (requestCode == Flag_Callback_ContactsPicker3) {//共享人
            if (data != null) {
//                List<Contacts> list = data.getParcelableArrayListExtra(ContactsPicker.Flag_Output_Checked);
                mListContactsScharer = (ArrayList<Contacts>) data.getExtras().getSerializable(ContactsPicker.Flag_Output_Checked);
                mReceiveArray = new String[mListContactsScharer.size()];
                String content = "";
                shareduserId = "";
                for (int i = 0; i < mListContactsScharer.size(); i++) {
                    mReceiveArray[i] = mListContactsScharer.get(i).getId() + "";
                    content += mListContactsScharer.get(i).getUserName() + ",";
                    shareduserId += mListContactsScharer.get(i).getId() + ",";
                }
                if (!StringUtils.isEmpty(shareduserId)) {
                    content = content.substring(0, content.length() - 1);
                    shareduserId = shareduserId.substring(0, shareduserId.length() - 1);
                }

                mySharedperson.setText(content);
            } else {
                return;
            }


        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 点击事件
     *
     * @param view
     */

    @OnClick({R.id.mStartTime, R.id.mEndTime, R.id.mPlace, R.id.mRemind, R.id.mRepeat, R.id.mParticipant, R.id.mySharedperson, R.id.mScheduleButton})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.mStartTime://开始时间
                if (myswitch == true) {
                    /**
                     * 日期+时间 选择器
                     */
                    String time = StringUtils.getEditText(mStartTime);
                    new SlideDateTimePicker.Builder(getSupportFragmentManager())
                            .setListener(new SlideDateTimeListener() {
                                @Override
                                public void onDateTimeSet(Date date) {
                                    try {
                                        mStartTime.setText(format.format(date));
                                        //开始时间大于结束时间,将结束时间一起赋值
                                        Date endDate = format.parse(StringUtils.getEditText(mEndTime));
                                        Date startDate = format.parse(StringUtils.getEditText(mStartTime));
                                        if (endDate.before(startDate)) {
                                            //将结束时间赋值为开始时间
                                            mEndTime.setText(format.format(date));
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                }
                            })
                            .setInitialDate(StringUtils.convertToDate(format, time))
                            .setIs24HourTime(true)
                            .setIsHaveTime(NewSlideDateTimeDialogFragment.Have_Date_Time)
                            .build()
                            .show();
                } else {
                    /**
                     * 日期 选择器
                     */
                    String time = StringUtils.getEditText(mStartTime);
                    new SlideDateTimePicker.Builder(getSupportFragmentManager())
                            .setListener(new SlideDateTimeListener() {
                                @Override
                                public void onDateTimeSet(Date date) {
                                    try {
                                        mStartTime.setText(format1.format(date));
                                        //开始时间大于结束时间,将结束时间一起赋值
                                        Date endDate = format1.parse(StringUtils.getEditText(mEndTime));
                                        Date startDate = format1.parse(StringUtils.getEditText(mStartTime));
                                        if (endDate.before(startDate)) {
                                            //将结束时间赋值为开始时间
                                            mEndTime.setText(format1.format(date));
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setInitialDate(StringUtils.convertToDate(format1, time))
                            .setIs24HourTime(true)
                            .setIsHaveTime(NewSlideDateTimeDialogFragment.Have_Date)
                            .build()
                            .show();
                }
                break;
            case R.id.mEndTime://结束时间
                if (myswitch == true) {
                    /**
                     * 时间选择器
                     */
                    String time = StringUtils.getEditText(mEndTime);
                    new SlideDateTimePicker.Builder(getSupportFragmentManager())
                            .setListener(new SlideDateTimeListener() {
                                @Override
                                public void onDateTimeSet(Date date) {
                                    try {
                                        //开始时间大于结束时间,将结束时间一起赋值
                                        Date startDate = format.parse(StringUtils.getEditText(mStartTime));
                                        if (date.before(startDate)) {
                                            MyToast.showToast("结束时间不能早于开始时间");
                                            return;
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    mEndTime.setText(format.format(date));
                                }
                            })
                            .setInitialDate(StringUtils.convertToDate(format, time))
                            .setIs24HourTime(true)
                            .setIsHaveTime(NewSlideDateTimeDialogFragment.Have_Date_Time)
                            .build()
                            .show();
                } else {
                    /**
                     * 日期选择器
                     */
                    String time = StringUtils.getEditText(mEndTime);
                    new SlideDateTimePicker.Builder(getSupportFragmentManager())
                            .setListener(new SlideDateTimeListener() {
                                @Override
                                public void onDateTimeSet(Date date) {
                                    try {
                                        //开始时间大于结束时间,将结束时间一起赋值
                                        Date startDate = format1.parse(StringUtils.getEditText(mStartTime));
                                        if (date.before(startDate)) {
                                            MyToast.showToast("结束时间不能早于开始时间");
                                            return;
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    mEndTime.setText(format1.format(date));
                                }
                            })
                            .setInitialDate(StringUtils.convertToDate(format1, time))
                            .setIs24HourTime(true)
                            .setIsHaveTime(NewSlideDateTimeDialogFragment.Have_Date)
                            .build()
                            .show();
                }
                break;
            case R.id.mPlace://地点
                String mplace = mPlace.getText().toString();
                break;
            case R.id.mRemind://提醒
                startActivityForResult(new Intent(this, RemindNewTaskActivity.class), Flag_Callback_ContactsPicker1);
                break;
            case R.id.mRepeat://重复
//                startActivityForResult(new Intent(this, RepeatNewTaskActivity.class), Flag_Callback_ContactsPicker2);
                break;
            case R.id.mySharedperson://共享人
                //跳转到通讯录
//                startActivityForResult(new Intent(this, ContactsPicker.class), Flag_Callback_ContactsPicker3);
                intent = new Intent(this, ContactsChoseActivity.class);
                intent.putExtra("list", mListContactsScharer);
                intent.putExtra("type", 1);//0-查看 1-多选 2-单选
                startActivityForResult(intent, Flag_Callback_ContactsPicker3);
                if (!mySharedperson.isEnabled()) {

                }
                break;
            case R.id.mParticipant://参与人
                //跳转到通讯录
//                startActivityForResult(new Intent(this, ContactsPicker.class), Flag_Callback_ContactsPicker);
                intent = new Intent(this, ContactsChoseActivity.class);
                intent.putExtra("list", mListContactsParticipant);
                intent.putExtra("type", 1);//0-查看 1-多选 2-单选
                startActivityForResult(intent, Flag_Callback_ContactsPicker);
                break;
            case R.id.mScheduleButton:
                //删除
                final MessageDialog messageDialog = new MessageDialog(this);
                messageDialog.setMessage("确认删除吗？");
                messageDialog.setTitle("提示");
                messageDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        detailPresenter.deleteSchedule(getApplicationContext(), mId, mICustomListener);
                        messageDialog.dismiss();
                    }
                });
                messageDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        messageDialog.dismiss();
                    }
                });
                messageDialog.show();
                break;
        }
    }


    /**
     * 设置一个回调参数用来关闭当前页面
     */
    private ICustomListener mICustomListener = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            switch (obj0) {
                case 1:
                    sendMessageDelayed(Flag_Callback_ShowAddButton, 600);
//                    message().show("创建成功");
                    MyToast.showToast("创建成功");
                    break;
                case 2:
//                    message().show("创建失败");
                    MyToast.showToast("创建失败");
                    break;
                case 0:
                    typeList = (List<RepeatNewTaskEntity>) obj1;
                    if (entityDetail == null) {
                        break;
                    }
                    if (typeList != null) {
                        for (int i = 0; i < typeList.size(); i++) {
                            if (entityDetail.getPrecedeType() == i) {
                                myRemind.setText(typeList.get(i).getTypeName());
                                precedeType = i + "";
                            }
                        }
                    }
                    break;
                case 5:
                    //提交修改后的日程
                    sendMessageDelayed(Flag_Callback_ShowAddButton, 600);
//                    message().show("修改成功");
                    MyToast.showToast("修改成功");
                    break;
                case 7:
                    //删除后的处理
                    sendMessageDelayed(Flag_Callback_ShowAddButton, 600);
//                    message().show("删除成功");
                    MyToast.showToast("删除成功");
                    break;
                case 10:
                    //获取默认分享人
                    List<ScheduleShareEntity> listShare = (List<ScheduleShareEntity>) obj1;
                    if (!StringUtils.isEmpty(listShare)) {
                        StringBuffer sbName = new StringBuffer();
                        StringBuffer sbId = new StringBuffer();
                        for (int i = 0; i < listShare.size(); i++) {
                            sbId.append(listShare.get(i).getFavorid());
                            sbName.append(listShare.get(i).getUserName());
                            if (i != listShare.size() - 1) {
                                sbId.append(",");
                                sbName.append(",");
                            }
                            Contacts contacts = new Contacts();
                            contacts.setId(listShare.get(i).getFavorid());
                            contacts.setUserName(listShare.get(i).getUserName());
                            mListContactsScharer.add(contacts);
                        }
                        shareduserId = sbId.toString();
                        mySharedperson.setText(sbName.toString());
                    }
                    break;
            }
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0://
                    System.out.println("mContentStr1:|||" + mContentStr);
                    if (mContentStr.equals(mContentTip)) {
                        mContentStr = "";
                    }
                    mPresenter.save(NewTaskActivity.this, StringUtils.getEditText(mTitle)
                            , myStartTime
                            , myEndTime
                            , precedeType
                            , myswitch ? "0" : "1"
                            , isalarmToggle + ""
                            , "0"
                            , mContentStr
                            , StringUtils.getEditText(mPlace)
                            , articipantuserId
                            , shareduserId, mICustomListener);
                    break;
                case 1://
                    System.out.println("mContentStr2:|||" + mContentStr);
                    if (mContentStr.equals(mContentTip)) {
                        mContentStr = "";
                    }
                    detailPresenter.commitScheduleDetail(NewTaskActivity.this, mScheduleRuleType + ""
                            , mId
                            , StringUtils.getEditText(mTitle)
                            , myStartTime
                            , myEndTime
                            , precedeType
                            , myswitch ? "0" : "1"
                            , isalarmToggle + ""
                            , mContentStr
                            , StringUtils.getEditText(mPlace)
                            , articipantuserId
                            , shareduserId, mICustomListener);
                    break;
                case 2://
                    //设置内容
                    mContent.loadUrl("javascript:setHTML('" + mContentTip + "')");
                    break;
                case 3://
                    //设置内容
                    mContent.loadUrl("javascript:setHTML('" + entityDetail.getScheduleDetail() + "')");
                    break;
            }
        }
    };
}
