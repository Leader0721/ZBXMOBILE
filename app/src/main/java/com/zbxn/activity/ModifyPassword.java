package com.zbxn.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zbxn.R;
import com.zbxn.activity.modifypassword.IModifyPasswordView;
import com.zbxn.activity.modifypassword.ModifyPasswordPresenter;
import com.zbxn.pub.dialog.ProgressDialog;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.MyToast;

import butterknife.BindView;
import butterknife.OnClick;
import utils.StringUtils;
import utils.ValidationUtil;

public class ModifyPassword extends AbsToolbarActivity implements IModifyPasswordView, View.OnClickListener {
    /**
     * 登录回调
     */
    private static final int Flag_Callback_Login = 1000;
    //输入的密码
    public static final String FLAG_INPUT_PASSWORD = "password";
    // private TopMessageController mMessageController;
    private ProgressDialog mProgressDialog;
    @BindView(R.id.mOriginalPassword)//原始密码
            EditText mOriginalPassword;
    @BindView(R.id.mNewPassword)//修改密码
            EditText mNewPassword;
    @BindView(R.id.mSecondPassword)//第二次输入
            EditText mSecondPassword;
    @BindView(R.id.ConfirmModification)//确认修改
            Button ConfirmModification;

    private ModifyPasswordPresenter mPresnter;

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("修改密码");// 定义上面的名字
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    public int getMainLayout() {
        return R.layout.activity_password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresnter = new ModifyPasswordPresenter(this);

        updateSuccessView();
    }

    @Override
    public void loadData() {

    }

    /**
     * 滑动关闭当前Activitiv
     */
    public boolean getSwipeBackEnable() {
        //return super.getSwipeBackEnable();
        return true;
    }

    @Override
    public boolean handleMessage(Message msg) {
        finish();
        return false;
    }

    /**
     * 创建按钮显示
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_schedule_ok, menu);
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

        switch (item.getItemId()) {
            case R.id.itemOk:
                onclickl();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick({R.id.ConfirmModification})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ConfirmModification://按钮
                onclickl();
                break;

        }
    }

    /**
     * 判断输入的格式是否正确
     */

    public void onclickl() {


        String origin = mOriginalPassword.getText().toString();
        String newpass = mNewPassword.getText().toString();
        String secondp = mSecondPassword.getText().toString();


        // 输入是否合法
        boolean isValidate = false;

        if (origin.length() != 0) {
            if (newpass != null && newpass.length() >= 8 && newpass.length() <= 20) {
                isValidate = ValidationUtil.isABCNumber(newpass);
                if (!StringUtils.isEmpty(secondp)) {
                    if (isValidate) {
                        if (secondp.equals(newpass)) {
                            mPresnter.login();
                        } else {
                            MyToast.showToast("您输入的两次密码不相同");
                        }
                    } else {
                        MyToast.showToast("您输入的新密码格式有误");
                    }
                } else {
                    MyToast.showToast("请再次输入新密码");
                }
            } else if (StringUtils.isEmpty(newpass)) {
                MyToast.showToast("新密码不能为空");
            } else {
                MyToast.showToast("您输入的新密码位数错误");
            }
        } else {
            MyToast.showToast("原始密码不能为空");
        }

    }


    @Override
    public String getOriginalPassword() {
        return mOriginalPassword.getText().toString();
    }

    @Override
    public String getNewPassword() {
        return mNewPassword.getText().toString();
    }

    @Override
    public String getSecondPassword() {
        return mSecondPassword.getText().toString();
    }

    @Override
    public void finishForResult(boolean b) {
        setResult(b ? RESULT_OK : RESULT_CANCELED);
        sendMessageDelayed(100, 1500);
    }
}
