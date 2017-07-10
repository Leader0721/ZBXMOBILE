package com.zbxn.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.zbxn.R;
import com.zbxn.activity.Main;
import com.zbxn.activity.RegisterActivity;
import com.zbxn.activity.ResetPassword.ResetPasswordActivity;
import com.zbxn.bean.Member;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.data.ResultData;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.frame.mvp.base.MessageController;
import com.zbxn.pub.frame.mvp.base.TopMessageController;
import com.zbxn.pub.http.HttpCallBack;
import com.zbxn.pub.utils.MyToast;
import com.zbxn.utils.KEY;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import utils.PreferencesUtils;
import utils.StringUtils;
import utils.ValidationUtil;

/**
 * Created by GISirFive on 2016/8/3.
 */
public class LoginActivity extends AbsToolbarActivity implements View.OnClickListener, ILoginView {

    public static final String FLAG_INPUT_USERNAME = "username";
    public static final String FLAG_INPUT_PASSWORD = "password";
    public static final String FLAG_INPUT_HEADURL = "headUrl";
    public static final String FLAG_INPUT_ID = "id";
    public static final String FLAG_SSID = "ssid";
    public static final String FLAG_ZMSCOMPANYID = "zmsCompanyId";
    public static final String FLAG_ZMSCOMPANYNAME = "zmsCompanyName";
    public static final String FLAG_INFO_MSG = "info_msg";
    public static final String FLAG_INFO_MSG_COUNT = "info_msg_count";
    public static final String FLAG_PERMISSIONIDS = "permissionIds";
    @BindView(R.id.myUserName)
    ImageView myUserName;
    @BindView(R.id.myPassword)
    ImageView myPassword;

    private TopMessageController mMessageController;

    private EditText mUserName;
    private EditText mPassword;
    // private ImageView mPasswordStatus;
    private CircularProgressButton mBtnLogin;
    private TextView mForgetPassword;
    private TextView mRegister;//注册


    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("五的N次方");
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    public int getMainLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //显示加载成功界面
        updateSuccessView();
        init();
        in();
        mUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    myUserName.setImageResource(R.mipmap.bg_user_1);
                } else {
                    myUserName.setImageResource(R.mipmap.bg_user);
                }
            }
        });
        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    myPassword.setImageResource(R.mipmap.bg_password_1);
                } else {
                    myPassword.setImageResource(R.mipmap.bg_password);
                }
            }
        });

        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, ""));
        JPushInterface.stopPush(LoginActivity.this);
    }


    @Override
    public void loadData() {

    }

    private void in() {
        String username = PreferencesUtils.getString(this, LoginActivity.FLAG_INPUT_USERNAME);
        String password = PreferencesUtils.getString(this, LoginActivity.FLAG_INPUT_PASSWORD);
        if (!StringUtils.isEmpty(username)) {
            mUserName.setText(username);
        }
        if (!StringUtils.isEmpty(password)) {
            mPassword.setText(password);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        System.exit(0);
        finish();
        return false;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.mBtnLogin) {

        }
        switch (view.getId()) {
            case R.id.mBtnLogin:
                if (StringUtils.isEmpty(mUserName)) {
//                message().show("用户名不能为空");
                    MyToast.showToast("用户名不能为空");
                } else if (StringUtils.isEmpty(mPassword)) {
//                message().show("密码不能为空");
                    MyToast.showToast("密码不能为空");
                } else if ( ValidationUtil.isNumeric(StringUtils.getEditText(mUserName))) {
                    if (ValidationUtil.isABCNumber(StringUtils.getEditText(mPassword))) {
                        login(this, StringUtils.getEditText(mUserName), StringUtils.getEditText(mPassword));
                    } else {
                        MyToast.showToast("密码输入的格式有误");
                    }

                } else {
                    MyToast.showToast("账号输入的格式有误");
                }
                break;
        }
    }


    private void init() {
        mUserName = (EditText) findViewById(R.id.mUserName);
        mPassword = (EditText) findViewById(R.id.mPassword);
        mRegister = (TextView) findViewById(R.id.mRegister);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));//跳转注册界面
            }
        });
        mForgetPassword = (TextView) findViewById(R.id.mForgetPassword);
        // mPasswordStatus = (ImageView) findViewById(R.id.mPasswordStatus);
        // mPasswordStatus.setOnClickListener(this);
        mBtnLogin = (CircularProgressButton) findViewById(R.id.mBtnLogin);
        mBtnLogin.setOnClickListener(this);
        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });


    }

    @Override
    public String getUserName() {
        if (Member.get() != null) {
            String n = Member.get().getLoginname();
            mUserName.setText(n);
        }
        return mUserName.getText().toString();
    }

    @Override
    public String getPassword() {
        return mPassword.getText().toString();
    }

    @Override
    public void finishForResult(boolean b) {
        if (b) {
            Intent intent = new Intent(this, Main.class);
            startActivity(intent);
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    public MessageController message() {
        if (mMessageController == null) {
            mMessageController = new TopMessageController(this);
            View v = findViewById(R.id.mContentLayout);
            mMessageController.setContainerView(v);
        }
        return mMessageController;
    }


    /**
     * 登录
     */
    public void login(Context context, final String userName, final String password) {
        //请求网络
        Map<String, String> map = new HashMap<String, String>();
        map.put("loginName", userName);
        map.put("password", password);
        map.put("ip", "android");
        map.put("uuid", StringUtils.getPushGuid(context));
        callRequest(map, "baseUser/doLogin.do", new HttpCallBack(Member.class, context, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess()) || "100".equals(mResult.getSuccess())) {//0成功   100未完善信息
                    Member entity = (Member) mResult.getData();
                    //将用户输入的密码保存起来，以便下次使用
                    PreferencesUtils.putString(BaseApp.getContext(), LoginActivity.FLAG_INPUT_PASSWORD, password);
                    //将用户输入的账号保存起来，以便下次使用
                    PreferencesUtils.putString(BaseApp.getContext(),
                            LoginActivity.FLAG_INPUT_USERNAME, userName);
                    //登录用户头像
                    PreferencesUtils.putString(BaseApp.getContext(),
                            LoginActivity.FLAG_INPUT_HEADURL, entity.getPortrait());
                    //登录用户id
                    PreferencesUtils.putString(BaseApp.getContext(),
                            LoginActivity.FLAG_INPUT_ID, entity.getId() + "");
                    //用户默认职位权限
                    PreferencesUtils.putString(BaseApp.getContext(),
                            LoginActivity.FLAG_PERMISSIONIDS, entity.getPermissionIds() + "");

                    //将用户输入的账号保存起来，以便下次使用
                    PreferencesUtils.putString(BaseApp.getContext(),
                            LoginActivity.FLAG_SSID, entity.getSsid());

                    PreferencesUtils.putString(BaseApp.getContext(),
                            LoginActivity.FLAG_ZMSCOMPANYID, entity.getZmsCompanyId());
                    PreferencesUtils.putString(BaseApp.getContext(),
                            LoginActivity.FLAG_ZMSCOMPANYNAME, entity.getZmsCompanyName());

                    if ("100".equals(mResult.getSuccess())) {
                        PreferencesUtils.putString(BaseApp.getContext(),
                                LoginActivity.FLAG_INFO_MSG, mResult.getMsg());
                    } else {
                        PreferencesUtils.putString(BaseApp.getContext(),
                                LoginActivity.FLAG_INFO_MSG, "");
                    }

                    Member.save(entity);

                    finishForResult(true);
                } else {
                    Member.clear();
                    MyToast.showToast(mResult.getMsg().substring(1,mResult.getMsg().length()-1));
                }
            }

            @Override
            public void onFailure(String string) {
                Member.clear();
            }
        });
    }

    /**
     * ************* 设置别名 *************************
     */
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    System.out.println("Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(LoginActivity.this,
                            (String) msg.obj, null, mAliasCallback);
                    break;
                default:
                    System.out.println("Unhandled msg - " + msg.what);
            }
        }
    };
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    System.out.println("alia退出:" + alias + "|");
                    System.out.println(logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。

                    // 保存是否设置别名状态
                    PreferencesUtils.putBoolean(LoginActivity.this, KEY.ISSETALIAS, false);

                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    System.out.println(logs);

                    // 保存是否设置别名状态
                    // 保存是否设置别名状态
                    PreferencesUtils.putBoolean(LoginActivity.this, KEY.ISSETALIAS, false);

                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(
                            mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    System.out.println(logs);
            }
        }
    };


}
