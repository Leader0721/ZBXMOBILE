package com.zbxn.activity.organize;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zbxn.R;
import com.zbxn.activity.login.LoginActivity;
import com.zbxn.bean.AddStaffEntity;
import com.zbxn.bean.DepartmentPositionEntity;
import com.zbxn.http.BaseAsyncTask;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.data.ResultNetData;
import com.zbxn.pub.data.base.BaseAsyncTaskInterface;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.MyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.PreferencesUtils;
import utils.StringUtils;

/**
 * 新增员工类
 * author:Huang
 * time:2016年12月27日10:22:51
 */
public class StaffActivity extends AbsToolbarActivity {

    int DepartmentPosition;
    int Gender = 1;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_psw)
    EditText etPsw;
    @BindView(R.id.tv_branch1)
    TextView tvBranch1;
    @BindView(R.id.tv_job1)
    TextView tvJob1;
    @BindView(R.id.ll_extra1)
    LinearLayout llExtra1;
    @BindView(R.id.delete_child1)
    ImageView deleteChild1;
    @BindView(R.id.tv_branch2)
    TextView tvBranch2;
    @BindView(R.id.tv_job2)
    TextView tvJob2;
    @BindView(R.id.ll_extra2)
    LinearLayout llExtra2;
    @BindView(R.id.delete_child2)
    ImageView deleteChild2;
    @BindView(R.id.tv_branch3)
    TextView tvBranch3;
    @BindView(R.id.tv_job3)
    TextView tvJob3;
    @BindView(R.id.ll_extra3)
    LinearLayout llExtra3;
    @BindView(R.id.et_addjob)
    TextView etAddjob;
    @BindView(R.id.rll_add)
    LinearLayout rllAdd;
    @BindView(R.id.tv_branch1_right)
    TextView tvBranch1Right;
    @BindView(R.id.imageView3)
    ImageView imageView3;
    @BindView(R.id.tv_job_right)
    TextView tvJobRight;
    @BindView(R.id.imageView4)
    ImageView imageView4;
    @BindView(R.id.tv_branch2_right)
    TextView tvBranch2Right;
    @BindView(R.id.imageView5)
    ImageView imageView5;
    @BindView(R.id.tv_job2_right)
    TextView tvJob2Right;
    @BindView(R.id.imageView6)
    ImageView imageView6;
    @BindView(R.id.imageView7)
    ImageView imageView7;
    @BindView(R.id.tv_branch3_right)
    TextView tvBranch3Right;
    @BindView(R.id.imageView8)
    ImageView imageView8;
    @BindView(R.id.tv_job3_right)
    TextView tvJob3Right;
    @BindView(R.id.layout_branch1)
    RelativeLayout layoutBranch1;
    @BindView(R.id.layout_job1)
    RelativeLayout layoutJob1;
    @BindView(R.id.layout_branch2)
    RelativeLayout layoutBranch2;
    @BindView(R.id.layout_job2)
    RelativeLayout layoutJob2;
    @BindView(R.id.layout_branch3)
    RelativeLayout layoutBranch3;
    @BindView(R.id.layout_job3)
    RelativeLayout layoutJob3;
    private String mDepartmentName1 = null;
    private String mDepartmentName2 = null;
    private String mDepartmentName3 = null;
    private int mDepartmentId1 = 0;
    private int mDepartmentId2 = 0;
    private int mDepartmentId3 = 0;
    private String mPositionName1 = null;
    private String mPositionName2 = null;
    private String mPositionName3 = null;
    private String mPositionId1 = null;
    private String mPositionId2 = null;
    private String mPositionId3 = null;
    List mList = new ArrayList<DepartmentPositionEntity>();

    @Override
    public int getMainLayout() {
        return R.layout.activity_staff;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        deleteChild1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llExtra2.setVisibility(View.GONE);
                mPositionId2 = null;
                mDepartmentId2 = 0;
                tvBranch2Right.setText("");
                tvJob2Right.setText("");
            }
        });
        deleteChild2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llExtra3.setVisibility(View.GONE);
                mPositionId2 = null;
                mDepartmentId3 = 0;
                deleteChild1.setVisibility(View.VISIBLE);
                deleteChild2.setVisibility(View.GONE);
                tvBranch3Right.setText("");
                tvJob3Right.setText("");
                rllAdd.setVisibility(View.VISIBLE);
            }
        });
        updateSuccessView();
    }

    @Override
    public void loadData() {

    }

//    @Override
//    public void initRight() {
//        setTitle("新增员工");
//        setRight1("保存");
//        setRight1Show(true);
//    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("新增员工");
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
        getMenuInflater().inflate(R.menu.menu_org_staff, menu);
        return super.onCreateOptionsMenu(menu);
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
            case R.id.org_staff:
                if (validate()) {
                    addStaff(this, StringUtils.getEditText(etName), StringUtils.getEditText(etPsw), Gender, DepartmentPosition);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //重写按钮的监听事件，上传新增员工资料
//    @Override
//    public void actionRight1(MenuItem menuItem) {
//        if (!validate()) {
//            return;
//        }
//        addStaff(this, StringUtils.getEditText(staffnameEt), StringUtils.getEditText(staffPswEt), Gender, DepartmentPosition);
//    }

    /**
     * 提交前验证
     *
     * @return
     */
    private boolean validate() {

        if (StringUtils.isEmpty(etName)) {
            MyToast.showToast("请输入姓名");
            return false;
        }

        if (StringUtils.isEmpty(etPsw)) {
            MyToast.showToast("请输入密码");
            return false;
        }

        return true;
    }

    //上传员工资料的方法
    public void addStaff(Context context, final String UserName, final String PassWord, final int Gender, final int DepartmentPosition) {
        //mList 添加entity
        if (mDepartmentId1 != 0 && !StringUtils.isEmpty(mPositionId1)) {
            DepartmentPositionEntity entity = new DepartmentPositionEntity();
            entity.setUDPID("");
            entity.setPositionId(mPositionId1);
            entity.setDepartmentId(mDepartmentId1 + "");
            mList.add(entity);
        }
        if (mDepartmentId2 != 0 && !StringUtils.isEmpty(mPositionId2)) {
            DepartmentPositionEntity entity = new DepartmentPositionEntity();
            entity.setUDPID("");
            entity.setPositionId(mPositionId2);
            entity.setDepartmentId(mDepartmentId2 + "");
            mList.add(entity);
        }
        if (mDepartmentId3 != 0 && !StringUtils.isEmpty(mPositionId3)) {
            DepartmentPositionEntity entity = new DepartmentPositionEntity();
            entity.setUDPID("");
            entity.setPositionId(mPositionId3);
            entity.setDepartmentId(mDepartmentId3 + "");
            mList.add(entity);
        }

        Gson gson = new Gson();
        //    老版数据请求
        String server = ConfigUtils.getConfig(ConfigUtils.KEY.NetServer_ACTION);
        Map<String, String> map = new HashMap<>();
        String tokenid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
        map.put("tokenid", tokenid);
        map.put("CurrentCompanyId", CurrentCompanyId);
        map.put("Gender", String.valueOf(Gender));
        map.put("DepartmentPosition", gson.toJson(mList));
        map.put("UserName", UserName);
        map.put("PassWord", PassWord);
//        callRequest(map, server + "Base/createEmployee", new HttpCallBack(AddDepartmentActivity.class, context, true) {
//                    @Override
//                    public void onSuccess(ResultData mResult) {
//                        if ("0".equals(mResult.getSuccess())) {
//                            MyToast.showToast(mResult.getMsg());
//                            finish();
//                        } else {
//                            MyToast.showToast("上传失败");
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(String string) {
//                        MyToast.showToast("网络请求异常，请稍后重试！");
//                    }
//                }
//
//        );

        //老版数据请求2
        new BaseAsyncTask(this, false, 0, server + "Orgnization/createEmployee", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultNetData<AddStaffEntity>().parse(json, AddStaffEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultNetData mResult = (ResultNetData) result;
                if (mResult.getSuccess().equals("0")) {
                    MyToast.showToast("创建成功");
                    finish();
                } else {
                    MyToast.showToast(mResult.getMsg());
                }
            }

            @Override
            public void dataError(int funId) {
                MyToast.showToast("获取网络数据失败");
            }
        }).execute(map);


        //新版数据上传
//        Call call = HttpRequest.getIResourceOA().getAddStaff(UserName, PassWord, Gender, DepartmentPosition);
//        callRequest(call, new HttpCallBack(AddStaffEntity.class, context, true) {
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
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    //onclick点击事件的监听事件
    private void method(Intent intent, int code) {
        intent.setClass(this, ChooseJobActivity.class);
        startActivityForResult(intent, code);
    }

    //    onclick1点击事件的部门部分公共部分提取出来作为方法
    private void method1(Intent intent, int code) {
        intent.putExtra("id", getIntent().getStringExtra("id"));
        intent.setClass(this, OrganizeChooseActivity.class);
        startActivityForResult(intent, code);
    }

    @OnClick({R.id.layout_branch1, R.id.layout_branch2, R.id.layout_branch3, R.id.layout_job1, R.id.layout_job2, R.id.layout_job3, R.id.rll_add})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            //部门的点击事件
            case R.id.layout_branch1:
                method1(intent, 1001);
                break;
            case R.id.layout_branch2:
                method1(intent, 1002);
                break;
            case R.id.layout_branch3:
                method1(intent, 1003);
                break;
            //职位的点击事件
            case R.id.layout_job1:
                method(intent, 2001);
                break;
            case R.id.layout_job2:
                method(intent, 2002);
                break;
            case R.id.layout_job3:
                method(intent, 2003);
                break;
            case R.id.rll_add: //点击添加兼职部门

                if (llExtra2.getVisibility() == view.VISIBLE && llExtra3.getVisibility() == view.GONE
                        && !"".equals(tvBranch2Right.getText().toString().trim())) {
                    llExtra3.setVisibility(View.VISIBLE);
                    deleteChild1.setVisibility(View.GONE);
                    deleteChild2.setVisibility(View.VISIBLE);
                    rllAdd.setVisibility(View.GONE);
                }
                if (llExtra2.getVisibility() == View.GONE && !"".equals(tvBranch1Right.getText().toString().trim())) {
                    llExtra2.setVisibility(View.VISIBLE);
                    deleteChild1.setVisibility(View.VISIBLE);
                    deleteChild2.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //部门
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            mDepartmentName1 = data.getStringExtra("name").trim();
            mDepartmentId1 = data.getIntExtra("id", 0);
            tvBranch1Right.setText(mDepartmentName1);
        }
        if (requestCode == 1002 && resultCode == RESULT_OK) {
            mDepartmentName2 = data.getStringExtra("name").trim();
            mDepartmentId2 = data.getIntExtra("id", 0);
            tvBranch2Right.setText(mDepartmentName2);
        }
        if (requestCode == 1003 && resultCode == RESULT_OK) {
            mDepartmentName3 = data.getStringExtra("name").trim();
            mDepartmentId3 = data.getIntExtra("id", 0);
            tvBranch3Right.setText(mDepartmentName3);
        }
        //职位
        if (requestCode == 2001 && resultCode == RESULT_OK) {
            mPositionName1 = data.getStringExtra("positionName");
            mPositionId1 = data.getStringExtra("positionId");
            tvJobRight.setText(mPositionName1);
        }
        if (requestCode == 2002 && resultCode == RESULT_OK) {
            mPositionName2 = data.getStringExtra("positionName");
            mPositionId2 = data.getStringExtra("positionId");
            tvJob2Right.setText(mPositionName2);
        }
        if (requestCode == 2003 && resultCode == RESULT_OK) {
            mPositionName3 = data.getStringExtra("positionName");
            mPositionId3 = data.getStringExtra("positionId");
            tvJob3Right.setText(mPositionName3);
        }
    }
}
