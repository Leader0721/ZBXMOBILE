package com.zbxn.activity.ResetPassword;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zbxn.bean.ResetPasswordEntity;
import com.zbxn.R;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.MyToast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import utils.StringUtils;
import utils.ValidationUtil;

/**
 * 项目名称：找回密码
 * 创建人：LiangHanXin
 * 创建时间：2016/10/28 14:11
 */
public class ResetPasswordActivity extends AbsToolbarActivity {


    @BindView(R.id.mMobilePhone)
    EditText mMobilePhone;//手机号
    @BindView(R.id.mVerificationCode)
    EditText mVerificationCode;//验证码
    @BindView(R.id.mVerification)

    TextView mVerification;
    @BindView(R.id.mNewPassword)
    EditText mNewPassword;//输入密码
    @BindView(R.id.mPasswordAgain)
    EditText mPasswordAgain;//再次输入
    @BindView(R.id.mComplete)
    Button mComplete;//确定
    private ResetPasswordPresenter mPresenter;
    private List<ResetPasswordEntity> mList;
    private ResetPasswordEntity sre;
    private int mi = 60;
    private Timer switchTimer;

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public int getMainLayout() {
        return R.layout.activity_resetpassword;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateSuccessView();
        mComplete.setEnabled(false);
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("忘记密码");
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    public void loadData() {
        mPresenter = new ResetPasswordPresenter(this);


//        mPresenter.RuleTimeDataList(this,mMobilePhone.getText().toString(), mICustomListener);
    }

    /**
     * 成功后提示以及销毁
     */
    private ICustomListener mICustomListener = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            switch (obj0) {
                case 2:
                    MyToast.showToast("修改成功");
                    finish();
//                    mList = (List<ResetPasswordEntity>) obj1;
                    // sre = (ResetPasswordEntity) obj1;
                    break;
            }
        }
    };


    /**
     * 滑动关闭当前Activitiv
     */
    @Override
    public boolean getSwipeBackEnable() {
//        return super.getSwipeBackEnable();
        return true;
    }

    @OnClick({R.id.mVerification, R.id.mComplete})
    public void onClick(View view) {
        String mmobile = mMobilePhone.getText().toString();//手机号
        String mverificationCode = mVerificationCode.getText().toString();//手机号
        switch (view.getId()) {

            case R.id.mVerification://验证码

                verification();
                if (!StringUtils.isEmpty(mmobile)) {

                    if (ValidationUtil.isMobile(mmobile)) {//验证手机号的格式
                        mMobilePhone.setEnabled(false);
                        mComplete.setEnabled(true);
                        send();
                    } else {
                        MyToast.showToast("你输入的手机号格式不对");
                    }
                } else {
                    MyToast.showToast("手机号不能为空");
                }
                break;
            case R.id.mComplete://完成

                if (ValidationUtil.isMobile(mmobile)) {//验证手机号的格式

                    if (!StringUtils.isEmpty(mverificationCode)) {
                        if (ValidationUtil.isNumeric(mverificationCode)) {
                            mMobilePhone.setEnabled(false);
                            verificationpassword();
                        } else {
                            MyToast.showToast("验证码格式不正确");
                        }
                    } else {
                        MyToast.showToast("验证码不能为空");
                    }
                } else {
                    MyToast.showToast("手机号格式不正确");
                }

                break;
        }
    }


    /**
     * 判断手机格式获取验证码
     */
    boolean isValidate = false;

    public void verification() {
        boolean isValidate = false;
        String mmobile = mMobilePhone.getText().toString();//手机号
        if (isValidate = ValidationUtil.isMobile(mmobile)) {//验证手机号的格式
            mPresenter.RuleTimeDataList(this, mmobile, mICustomListener);
        } else {
            MyToast.showToast("你输入的手机号格式不对");
        }


    }

    /**
     * 倒计时
     */
    private void send() {
        mi = 60;
        if (switchTimer != null) {
            switchTimer.cancel();
            switchTimer = null;
        }
        switchTimer = new Timer();
        switchTimer.schedule(new TimerTask() {
            public void run() {
                mi -= 1;
                imageSwitcherHandler.sendEmptyMessage(0);
            }
        }, 1000, 1000);
        mVerification.setText("(" + mi + ")重新发送");
        mVerification.setTextColor(0xffffffff);
        mVerification.setClickable(false);
//        MyToast.showToast("验证码已发送到您的手机请注意查收！");
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            if (what == 0) {
//                MyToast.showLongToast(RegisterActivity.this, "验证码已发送到您的手机请注意查收！", Toast.LENGTH_LONG);
                MyToast.showToast("验证码已发送到您的手机请注意查收！");
            } else if (what == 1) {
                Object data = msg.obj;
//                MyToast.showLongToast(RegisterActivity.this, JsonUtil.getString(((Throwable) data).getMessage(), "description"), Toast.LENGTH_LONG);
            } else if (what == 2) {

            } else if (what == 3) {

            }
        }
    };
    private Handler imageSwitcherHandler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == 0) {
                if (mi <= 0) {
                    mVerification.setText("重新发送");
                    mVerification.setTextColor(0xffffffff);
                    mVerification.setClickable(true);
                    mMobilePhone.setEnabled(true);
                    if (switchTimer != null) {
                        switchTimer.cancel();
                        switchTimer = null;
                    }
                } else {
                    mVerification.setText("(" + mi + ")重新发送");
                }
            }
        }
    };

    /**
     * 判断手机格式以及验证码以及密码的格式
     */
    public void verificationpassword() {
        String phoneNumber = mMobilePhone.getText().toString();//手机号
        String mverificationCode = mVerificationCode.getText().toString();//验证码
        String mnew = mNewPassword.getText().toString();//密码
        String mpassword = mPasswordAgain.getText().toString();//再次输入

        if (!StringUtils.isEmpty(mverificationCode)) {
            if (!StringUtils.isEmpty(mnew)) {
                if (mnew.length() >= 8 && mnew.length() <= 20) {
                    if (!StringUtils.isEmpty(mpassword)) {
                        if (ValidationUtil.isABCNumber(mpassword)) {
                            if (mnew.equals(mpassword)) {
                                //格式认证成功后

                                mPresenter.Password(this, phoneNumber, mverificationCode, mnew, mICustomListener);

                            } else {
                                MyToast.showToast("您输入的两次密码不相同");
                            }
                        } else {
                            MyToast.showToast("您输入的新密码格式有误");
                        }
                    } else {
                        MyToast.showToast("确认密码不能为空");
                    }
                } else {
                    MyToast.showToast("新密码格式有误");
                }
            } else {
                MyToast.showToast("新密码不能为空");
            }

        } else {
            MyToast.showToast("验证码不能为空");
        }
    }


}
