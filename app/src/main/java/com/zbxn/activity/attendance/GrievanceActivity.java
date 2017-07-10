package com.zbxn.activity.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbxn.bean.AttendanceRecordEntity;
import com.zbxn.listener.ICustomListener;
import com.zbxn.widget.slidedatetimepicker.NewSlideDateTimeDialogFragment;
import com.zbxn.widget.slidedatetimepicker.SlideDateTimeListener;
import com.zbxn.widget.slidedatetimepicker.SlideDateTimePicker;
import com.zbxn.R;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.MyToast;
import com.zbxn.widget.slidedatetimepicker.SlideDateTimeDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import utils.StringUtils;

/**
 * Created by Administrator on 2016/9/30.
 */
public class GrievanceActivity extends AbsToolbarActivity {

    private static final int Flag_Callback_ContactsPicker = 1001;//申诉类型回调

    @BindView(R.id.grievance_type)
    TextView grievanceType;
    @BindView(R.id.mType)
    LinearLayout mType;
    @BindView(R.id.grievance_edit)
    EditText grievanceEdit;
    @BindView(R.id.mState)
    TextView mState;
    @BindView(R.id.mTime)
    TextView mTime;
    @BindView(R.id.mAddr)
    TextView mAddr;
    @BindView(R.id.mAddressLayout)
    LinearLayout mAddressLayout;
    @BindView(R.id.mState2)
    TextView mState2;
    @BindView(R.id.mTime2)
    TextView mTime2;
    @BindView(R.id.mAddr2)
    TextView mAddr2;
    @BindView(R.id.mAddressLayout2)
    LinearLayout mAddressLayout2;
    @BindView(R.id.mLayout2)
    LinearLayout mLayout2;
    @BindView(R.id.text_new_time)
    TextView textNewTime;
    @BindView(R.id.mTimeLayout)
    LinearLayout mTimeLayout;
    @BindView(R.id.text_new_time_tip)
    TextView textNewTimeTip;
    @BindView(R.id.mLayout1)
    LinearLayout mLayout1;

    private MenuItem mCollect;
    //类型
    private String mAppealType = "1";//默认 补签

    private AttendanceRecordEntity entity;

    private GrievancePresenter mPresenter;

    private String type = "";//1迟到  2早退

    private SimpleDateFormat selectFormat = new SimpleDateFormat("HH:mm");

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public int getMainLayout() {
        return R.layout.activity_grievance;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new GrievancePresenter(this);

        entity = (AttendanceRecordEntity) getIntent().getSerializableExtra("item");
        type = getIntent().getStringExtra("type");

        initView();
        initData();

        //显示加载成功界面
        updateSuccessView();
    }

    @Override
    public void loadData() {

    }

    private void initView() {
        //1迟到  2早退
        if ("1".equals(type)) {
            textNewTimeTip.setText("签到时间");
            mLayout1.setVisibility(View.VISIBLE);
            mLayout2.setVisibility(View.GONE);
        } else {
            textNewTimeTip.setText("签退时间");
            mLayout1.setVisibility(View.GONE);
            mLayout2.setVisibility(View.VISIBLE);
        }
    }

    private void initData() {
        grievanceType.setText("补签");
        if (null != entity) {
            mTime.setText(StringUtils.convertDateMin(entity.getCheckintime()));
            mState.setText(entity.getCheckInStateName());
            mAddr.setText("地点:" + entity.getCheckinaddress());

            if (StringUtils.isEmpty(entity.getCheckinaddress())) {
                mAddressLayout.setVisibility(View.GONE);
            } else {
                mAddressLayout.setVisibility(View.VISIBLE);
            }
            if (StringUtils.isEmpty(entity.getCheckoutaddress())) {
                mAddressLayout2.setVisibility(View.GONE);
            } else {
                mAddressLayout2.setVisibility(View.VISIBLE);
            }

            if (entity.getCheckInStateName().equals("正常")) {
                mState.setTextColor(getResources().getColor(R.color.tvc6));
            } else {
                mState.setTextColor(getResources().getColor(R.color.orange));
            }
            if (entity.getCheckOutStateName().equals("正常")) {
                mState2.setTextColor(getResources().getColor(R.color.tvc6));
            } else {
                mState2.setTextColor(getResources().getColor(R.color.orange));
            }

            if (!"未签到".equals(entity.getCheckOutStateName())) {
                mTime2.setText(StringUtils.convertDateMin(entity.getCheckouttime()));
                mState2.setText(entity.getCheckOutStateName());
                mAddr2.setText("地点:" + entity.getCheckoutaddress());
            }
        }
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("申诉");
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    public boolean getSwipeBackEnable() {
//        return super.getSwipeBackEnable();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_establish, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        mCollect = menu.findItem(R.id.mEstablish);
        mCollect.setEnabled(true);
        mCollect.setTitle("确定");
        return true;
    }

    /**
     * 提交前验证
     *
     * @return
     */
    private boolean validate() {
        if (StringUtils.isEmpty(grievanceType)) {
            MyToast.showToast("请选择类型");
            return false;
        }

        if (StringUtils.isEmpty(textNewTime)) {
            MyToast.showToast("请选择时间");
            return false;
        }

        if (StringUtils.getEditText(grievanceEdit).length() < 10) {
            MyToast.showToast("申诉理由不能小于10个字");
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mEstablish:
                if (!validate()) {
                    break;
                }
                String appealtime = entity.getCheckTime() + " " + StringUtils.getEditText(textNewTime) + ":00";
                //完成提交
                mPresenter.save(this, entity.getId() + "", type, mAppealType, appealtime, StringUtils.getEditText(grievanceEdit), mICustomListener);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.mType, R.id.mTimeLayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mType:
                startActivityForResult(new Intent(this, GrievanceTypeActivity.class), Flag_Callback_ContactsPicker);
                break;
            case R.id.mTimeLayout:
                /**
                 * 日期选择器
                 */
                String time = "";
                if (!StringUtils.isEmpty(textNewTime)) {
                    time = StringUtils.getEditText(textNewTime);
                }
                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(new SlideDateTimeListener() {
                            @Override
                            public void onDateTimeSet(Date date) {
                                textNewTime.setText(selectFormat.format(date));
                            }
                        })
                        .setInitialDate(StringUtils.convertToDate(selectFormat, time))
                        .setIs24HourTime(true)
                        .setIsHaveTime(NewSlideDateTimeDialogFragment.Have_Time)
                        .build()
                        .show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Flag_Callback_ContactsPicker:
                if (data != null) {
                    if (resultCode == RESULT_OK) {
                        String typeName = data.getStringExtra("typeName"); // 类型名称
                        mAppealType = data.getStringExtra("appealType"); // 需要提交的字段
                        if (!typeName.equals(null)) {
                            grievanceType.setText(typeName);
                        }
                    }
                }
                break;
        }
    }

    private ICustomListener mICustomListener = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            switch (obj0) {
                case 0:
//                    message().show("提交成功");
                    MyToast.showToast("提交成功");
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
            }
        }
    };
}
