package com.zbxn.activity.organize;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.zbxn.R;

import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.MyToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import utils.StringUtils;

/**
 * 邀请员工
 * Created by U on 2016/12/28.
 */
public class InviteStaffActivity extends AbsToolbarActivity {

    @BindView(R.id.tv_name1)
    TextView tvName1;
    @BindView(R.id.et_name1)
    EditText etName1;
    @BindView(R.id.tv_phoneno1)
    TextView tvPhoneno1;
    @BindView(R.id.et_phoneno1)
    EditText etPhoneno1;
    @BindView(R.id.tv_name2)
    TextView tvName2;
    @BindView(R.id.et_name2)
    EditText etName2;
    @BindView(R.id.tv_phoneno2)
    TextView tvPhoneno2;
    @BindView(R.id.et_phoneno2)
    EditText etPhoneno2;
    @BindView(R.id.tv_name3)
    TextView tvName3;
    @BindView(R.id.et_name3)
    EditText etName3;
    @BindView(R.id.tv_phoneno3)
    TextView tvPhoneno3;
    @BindView(R.id.et_phoneno3)
    EditText etPhoneno3;


    @Override
    public int getMainLayout() {
        return R.layout.activity_invite_staff;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    public void loadData() {

    }

//    @Override
//    public void initRight() {
//        setTitle("邀请员工");
//        setRight1("邀请");
//        setRight1Show(true);
//    }

    /**
     * 点击邀请按钮添加更多邀请人
     */
    public void onClick(View view) {
        if (tvName2.getVisibility() == View.GONE && tvPhoneno2.getVisibility() == View.GONE &&
                etName2.getVisibility() == View.GONE && etPhoneno2.getVisibility() == View.GONE) {
            tvName2.setVisibility(View.VISIBLE);
            etName2.setVisibility(View.VISIBLE);
            tvPhoneno2.setVisibility(View.VISIBLE);
            etPhoneno2.setVisibility(View.VISIBLE);

        } else {
            if (tvName3.getVisibility() == View.GONE && tvPhoneno3.getVisibility() == View.GONE &&
                    etName3.getVisibility() == View.GONE && etPhoneno3.getVisibility() == View.GONE) {
                tvName3.setVisibility(View.VISIBLE);
                etName3.setVisibility(View.VISIBLE);
                tvPhoneno3.setVisibility(View.VISIBLE);
                etPhoneno3.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 提交前验证
     *
     * @return
     */
    private boolean validate() {

        if (StringUtils.isEmpty(etName1)) {
            MyToast.showToast("请填写名字");
            return false;
        }

        if (StringUtils.isEmpty(etPhoneno1)) {
            MyToast.showToast("请填写电话号码");
            return false;
        }

        return true;
    }

//    //上传邀请员工资料的方法
//    public void addInviteStaff(Context context, final String employeeName, final String employeePhone
//    ) {
//
//        Call call = HttpRequest.getIResourceOA().getInviteStaff(employeeName, employeePhone);
//        callRequest(call, new HttpCallBack(InviteStaffEntity.class, context, true) {
//            @Override
//            public void onSuccess(ResultData mResult) {
//                if ("0".equals(mResult.getSuccess())) {
//                    MyToast.showToast(mResult.getMsg());
//                    finish();
//                } else {
//                    MyToast.showToast("上传失败");
//                }
//
//            }
//
//            @Override
//            public void onFailure(String string) {
//                MyToast.showToast(R.string.NETWORKERROR);
//            }
//        });
//
//
//    }
//
//    @Override
//    public void actionRight1(MenuItem menuItem) {
//        if (!validate()) {
//            return;
//        }
//        addInviteStaff(this, StringUtils.getEditText(etName1), StringUtils.getEditText(etPhoneno1));
//
//    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
