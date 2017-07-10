package com.zbxn.activity.organize;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.activity.login.LoginActivity;
import com.zbxn.bean.AddDepartmentEntity;
import com.zbxn.http.BaseAsyncTask;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.bean.dbutils.DBUtils;
import com.zbxn.pub.data.ResultParse;
import com.zbxn.pub.data.base.BaseAsyncTaskInterface;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.MyToast;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import utils.PreferencesUtils;
import utils.StringUtils;

public class AddDepartmentActivity extends AbsToolbarActivity {


    @BindView(R.id.et_branchname)
    EditText etBranchname;
    @BindView(R.id.et_describe)
    EditText describe;
    private String mParentID;
    @BindView(R.id.tv_branch)
    TextView tvBranch;
    private String mHeigherBranchName;

    private DBUtils<Contacts> mDBUtils;

    @Override
    public int getMainLayout() {
        return R.layout.activity_organize_addbranch;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mParentID = getIntent().getIntExtra("id", 0)+"";
        mHeigherBranchName = getIntent().getStringExtra("name");
        tvBranch.setText(mHeigherBranchName);
        updateSuccessView();
        if (mDBUtils == null) {
            mDBUtils = new DBUtils<>(Contacts.class);
        }
    }

    @Override
    public void loadData() {

    }

    /*@Override
    public void initRight() {
        setTitle("新增部门");
        setRight1("保存");
        setRight1Show(true);
    }*/


    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("新增部门");
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
                    addBranch(this, mParentID, StringUtils.getEditText(etBranchname), StringUtils.getEditText(describe));
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /* @Override
    public void actionRight1(MenuItem menuItem) {
        if (!validate()) {
            return;
        }
        addBranch(this, mParentID, StringUtils.getEditText(etBranchname), StringUtils.getEditText(describe));
    }*/

    /**
     * 提交前验证
     *
     * @return
     */
    private boolean validate() {

        if (StringUtils.isEmpty(mHeigherBranchName)) {
            MyToast.showToast("请选择上级部门");
            return false;
        }

        if (StringUtils.isEmpty(etBranchname)) {
            MyToast.showToast("请填写部门名称");
            return false;
        }

        if (StringUtils.isEmpty(describe)) {
            MyToast.showToast("请填写部门描述");
            return false;
        }

        return true;
    }

    /**
     * 上传数据
     */
    public void addBranch(Context context, String mParentID, final String DepartmentName, final String DepartmentDesc) {

        //    老版数据请求1
        String server = ConfigUtils.getConfig(ConfigUtils.KEY.NetServer_ACTION);
        Map<String, String> map = new HashMap<>();
        String tokenid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
        Log.d("====tokenid====", "======" + tokenid + "====");
        map.put("tokenid", tokenid);
        map.put("CurrentCompanyId", CurrentCompanyId);
        map.put("ParentID", mParentID);
        map.put("DepartmentName", DepartmentName);
        map.put("DepartmentDesc", DepartmentDesc);

        Log.d("=====ddddd===", server);
        //将用户输入的账号保存起来，以便下次使用

//        callRequest(map, server + "Base/createDepartment", new HttpCallBack(AddDepartmentActivity.class, context, false) {
//                    @Override
//                    public void onSuccess(ResultData mResult) {
//                        if ("0".equals(mResult.getSuccess())) {
//                            MyToast.showToast(mResult.getMsg());
//                            finish();
//                        } else {
//                            MyToast.showToast("提交失败");
//                        }
//                    }
//                    @Override
//                    public void onFailure(String string) {
//                        MyToast.showToast("网络请求异常，请稍后重试！");
//                    }
//                }
//        );

        //老版数据请求2
        new BaseAsyncTask(this, false, 0, server + "base/createDepartment", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultParse<AddDepartmentEntity>().parse(json, AddDepartmentEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultParse mResult = (ResultParse) result;
                if (mResult.getSuccess().equals("0")) {
                    MyToast.showToast("创建成功");
                    AddDepartmentEntity entityTemp = (AddDepartmentEntity) mResult.getData();
                    Contacts entity = new Contacts();
                    entity.setIsDepartment("1");
                    entity.setParentId(entityTemp.getParentID() + "");
                    entity.setDepartmentId(entityTemp.getID() + "");
                    entity.setDepartmentName(entityTemp.getDepartmentName());
                    mDBUtils.add(entity);
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
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


        //新版数据请求
        /*Call call = HttpRequest.getIResourceOA().getAddDepartment(mParentID, DepartmentName, DepartmentDesc);
        callRequest(call, new HttpCallBack(AddDepartmentEntity.class, context, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {
                    MyToast.showToast(mResult.getMsg());
                    finish();
                } else {
                    MyToast.showToast("上传失败");
                }
            }
            @Override
            public void onFailure(String string) {
                MyToast.showToast(R.string.NETWORKERROR);
            }
        });*/
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    /**
     * 点击选择上级部门
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_branch:
                Intent intent = new Intent(this, OrganizeChooseActivity.class);
                intent.putExtra("id", mParentID);
                startActivityForResult(intent, 1000);
                break;
        }
    }

    // 获取部门请求结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {
                tvBranch.setText(data.getStringExtra("name"));
                mParentID = data.getIntExtra("id",0)+"";
            }
        }

    }
}
