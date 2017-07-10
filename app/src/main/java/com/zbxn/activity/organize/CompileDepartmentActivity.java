package com.zbxn.activity.organize;

import android.os.Bundle;
import android.os.Message;
import android.widget.EditText;

import com.zbxn.R;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.MyToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import utils.StringUtils;

public class CompileDepartmentActivity extends AbsToolbarActivity {

    @BindView(R.id.et_branch)
    EditText etBranch;
    @BindView(R.id.et_branchname)
    EditText etBranchname;
    @BindView(R.id.et_describe)
    EditText etDescribe;
    int ParentID;
    int DepartmentID;

    @Override
    public int getMainLayout() {
        return R.layout.activity_compile_department;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compile_department);
        ButterKnife.bind(this);
    }

    @Override
    public void loadData() {

    }

//    @Override
//    public void initRight() {
//        setTitle("编辑部门");
//        setRight1("保存");
//        setRight1Show(true);
//    }

    /**
     * 保存按钮点击事件
     * */
//    @Override
//      public void actionRight1(MenuItem menuItem) {
//        if (!validate()) {
//            return;
//        }
//        UpdateDepartment(this, ParentID, DepartmentID, StringUtils.getEditText(etBranchname), StringUtils.getEditText(etDescribe));
//    }

    /**
     * 提交前验证
     *
     * @return
     */
    private boolean validate() {

        if (StringUtils.isEmpty(etBranch)) {
            MyToast.showToast("请填写上级部门");
            return false;
        }

        if (StringUtils.isEmpty(etBranchname)) {
            MyToast.showToast("请填写部门名称");
            return false;

        }

        if (StringUtils.isEmpty(etDescribe)) {
            MyToast.showToast("请填写部门描述");
            return false;
        }

        return true;
    }

    /**
     * 上传数据的方法
     * */
//    public void UpdateDepartment(Context context, final int ParentID,final int DepartmentID, final String DepartmentName, final String DepartmentDesc) {
//
//        Call call = HttpRequest.getIResourceOA().getUpdateDepartment(ParentID, DepartmentID,DepartmentName, DepartmentDesc);
//        callRequest(call, new HttpCallBack(AddDepartmentEntity.class, context, true) {
//            @Override
//            public void onSuccess(ResultData mResult) {
//                if ("0".equals(mResult.getSuccess())) {
//                    MyToast.showToast(mResult.getMsg());
//                    finish();
//                } else {
//                    MyToast.showToast("上传失败");
//                }
//            }
//
//            @Override
//            public void onFailure(String string) {
//                MyToast.showToast(R.string.NETWORKERROR);
//            }
//        });
//    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
