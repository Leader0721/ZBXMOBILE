package com.zbxn.activity.organize;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.activity.login.LoginActivity;
import com.zbxn.bean.InviteEmplEntity;
import com.zbxn.bean.JobEntity;
import com.zbxn.http.BaseAsyncTask;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.data.ResultNetData;
import com.zbxn.pub.data.base.BaseAsyncTaskInterface;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvc.AbsToolbarActivity;
import com.zbxn.pub.http.RequestUtils;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.JsonUtil;
import com.zbxn.pub.utils.MyToast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.PreferencesUtils;
import utils.StringUtils;
import utils.ValidationUtil;

/**
 * Created by Administrator on 2016/12/29.
 */
public class InviteNewEmployeeActivity extends AbsToolbarActivity {


    @BindView(R.id.delete_item1)//删除item
    ImageView deleteItem1;
    @BindView(R.id.num_inviteEmpl1)//排序号
    TextView numInviteEmpl1;
    @BindView(R.id.invite_name1)//第一个员工姓名
    EditText inviteName1;
    @BindView(R.id.invite_phoneNum1)//第二个员工电话
    EditText invitePhoneNum1;


    @BindView(R.id.delete_item2)
    ImageView deleteItem2;
    @BindView(R.id.num_inviteEmpl2)
    TextView numInviteEmpl2;
    @BindView(R.id.invite_name2)
    EditText inviteName2;
    @BindView(R.id.invite_phoneNum2)
    EditText invitePhoneNum2;
    @BindView(R.id.linearlayout_invite2)
    LinearLayout linearlayoutInvite2;


    @BindView(R.id.delete_item3)
    ImageView deleteItem3;
    @BindView(R.id.num_inviteEmpl3)
    TextView numInviteEmpl3;
    @BindView(R.id.invite_name3)
    EditText inviteName3;
    @BindView(R.id.invite_phoneNum3)
    EditText invitePhoneNum3;
    @BindView(R.id.linearlayout_invite3)
    LinearLayout linearlayoutInvite3;
    @BindView(R.id.footview_textInvite)
    TextView footviewTextInvite;
    @BindView(R.id.footView_invite)
    LinearLayout footViewInvite;

    private int NUM = 1;
    boolean firstIsEmpty = true;
    boolean secondIsEmpty = true;
    List<InviteEmplEntity>mList = new ArrayList<>();

    private String name1;
    private String phoneNum1;
    private String name2;
    private String phoneNum2;
    private String name3;
    private String phoneNum3;
    private InviteEmplEntity entity1,entity2,entity3;

    @Override
    public int getMainLayout() {
        return R.layout.activity_invitenewemployee;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        initData1();
        updateSuccessView();
    }

    private void initData1() {
        entity1 = new InviteEmplEntity();
        entity2 = new InviteEmplEntity();
        entity3 = new InviteEmplEntity();
        inviteName1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input= s.toString();
                name1 = input;

            }
            });

        invitePhoneNum1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input= s.toString();
                phoneNum1 = input;
            }
        });
    }


    private void initData2() {
        inviteName2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input= s.toString();
                name2 = input;

            }
        });

        invitePhoneNum2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input= s.toString();
                phoneNum2 = input;
            }
        });
    }


    private void initData3() {
        inviteName3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input= s.toString();
                name3 = input;

            }
        });

        invitePhoneNum3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input= s.toString();
                phoneNum3 = input;
            }
        });

        if (TextUtils.isEmpty(name3)&&TextUtils.isEmpty(phoneNum3)){
            footViewInvite.setVisibility(View.GONE);
        }else {
            footViewInvite.setVisibility(View.GONE);
        }
    }




    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("邀请员工");// 定义上面的名字
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
        getMenuInflater().inflate(R.menu.menu_inviteemployee, menu);
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
            case R.id.mScheduleEdit:
/*
                switch (NUM){
                    case 1:
                        if(!StringUtils.isEmpty(inviteName1)){
                            MyToast.showToast("请输入姓名");
                            break;
                        }
                        if(!ValidationUtil.isMobile(StringUtils.getEditText(invitePhoneNum1))){
                            MyToast.showToast("请输入正确的手机号");
                            break;
                        }
                        break;
                    case 2:
                        if(!StringUtils.isEmpty(inviteName2)){
                            MyToast.showToast("请输入姓名");
                            break;
                        }
                        if(!ValidationUtil.isMobile(StringUtils.getEditText(invitePhoneNum2))){
                            MyToast.showToast("请输入正确的手机号");
                            break;
                        }
                        break;
                    case 3:
                        if(!StringUtils.isEmpty(inviteName3)){
                            MyToast.showToast("请输入姓名");
                            break;
                        }
                        if(!ValidationUtil.isMobile(StringUtils.getEditText(invitePhoneNum3))){
                            MyToast.showToast("请输入正确的手机号");
                            break;
                        }
                        break;
                }*/

                if(StringUtils.isEmpty(inviteName1)){
                    MyToast.showToast("请输入姓名");
                    break;
                }
                if(!ValidationUtil.isMobile(StringUtils.getEditText(invitePhoneNum1))){
                    MyToast.showToast("请输入正确的手机号");
                    break;
                }

                if (linearlayoutInvite2.getVisibility()==View.VISIBLE){
                    if(StringUtils.isEmpty(inviteName2)){
                        MyToast.showToast("请输入姓名");
                        break;
                    }
                    if(!ValidationUtil.isMobile(StringUtils.getEditText(invitePhoneNum2))){
                        MyToast.showToast("请输入正确的手机号");
                        break;
                    }
                }
                if (linearlayoutInvite3.getVisibility()==View.VISIBLE){
                    if(StringUtils.isEmpty(inviteName3)){
                        MyToast.showToast("请输入姓名");
                        break;
                    }
                    if(!ValidationUtil.isMobile(StringUtils.getEditText(invitePhoneNum3))){
                        MyToast.showToast("请输入正确的手机号");
                        break;
                    }
                }

                if (TextUtils.isEmpty(name3)&&TextUtils.isEmpty(phoneNum3)){

                }else {
                    entity3.setName(name3);
                    entity3.setPhoneNum(phoneNum3);
                    mList.add(2,entity3);
                }

                //进行网络数据传输
                Map<String, String> map = new HashMap<>();
                String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
                String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
                map.put("tokenid", ssid);
                map.put("CurrentCompanyId", CurrentCompanyId);
                map.put("JsoninviteEmployee", JsonUtil.toJsonString(mList));
                String server = ConfigUtils.getConfig(ConfigUtils.KEY.NetServer_ACTION);
                new BaseAsyncTask(this, false, 0, server + "Orgnization/inviteEmployee", new BaseAsyncTaskInterface() {
                    @Override
                    public void dataSubmit(int funId) {

                    }

                    @Override
                    public Object dataParse(int funId, String json) throws Exception {
                        return new ResultNetData<JobEntity>().parse(json,JobEntity.class);
                    }
                    @Override
                    public void dataSuccess(int funId, Object result) {
                        ResultNetData mResult = (ResultNetData) result;
                        if ("0".equals(mResult.getSuccess())) {
                            MyToast.showToast("添加成功");
                            finish();
                        } else {
                            MyToast.showToast("添加失败，请重试 ！");
                        }
                    }

                    @Override
                    public void dataError(int funId) {
                        MyToast.showToast("获取网络数据失败");
                    }
                }).execute(map);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadData() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public void onSuccess(RequestUtils.Code code, JSONObject response) {

    }

    @Override
    public void onFailure(RequestUtils.Code code, JSONObject error) {

    }

    @OnClick({ R.id.delete_item2, R.id.delete_item3, R.id.footview_textInvite, R.id.footView_invite})
    public void onClick(View view) {
        if (mList.size() == 1){
            firstIsEmpty = TextUtils.isEmpty(name1)&&TextUtils.isEmpty(phoneNum1);
        }else if (mList.size() == 2){
            secondIsEmpty = TextUtils.isEmpty(name2)&&TextUtils.isEmpty(phoneNum2);
        }

        switch (view.getId()) {
            case R.id.delete_item2:

                linearlayoutInvite2.setVisibility(View.GONE);
                mList.clear();
                mList.add(0,entity1);
                NUM = 1;
                name2 = "";
                phoneNum2 = "";
                inviteName2.setText("");
                invitePhoneNum2.setText("");

                break;
            case R.id.delete_item3:
                deleteItem2.setVisibility(View.VISIBLE);
                linearlayoutInvite3.setVisibility(View.GONE);
                mList.clear();
                mList.add(0,entity1);
                mList.add(1,entity2);
                footViewInvite.setVisibility(View.VISIBLE);
                NUM = 2;
                name3 = "";
                phoneNum3 = "";
                inviteName3.setText("");
                invitePhoneNum3.setText("");
                secondIsEmpty = true;
                break;
            case R.id.footview_textInvite:
                if (NUM == 1){
                    if(!ValidationUtil.isMobile(phoneNum1)){
                        MyToast.showToast("请填写正确的手机号");
                        break;
                    }
                    if (TextUtils.isEmpty(name1)&&TextUtils.isEmpty(phoneNum1)){
                        MyToast.showToast("请完善第一个员工信息");
                    }else {
                        entity1.setName(name1);
                        entity1.setPhoneNum(phoneNum1);
                        MyToast.showToast("第一个"+name1);
                        linearlayoutInvite2.setVisibility(View.VISIBLE);
                        NUM = 2;
                        initData2();
                        mList.add(0,entity1);
                    }
                }else if (NUM == 2){
                    if(!ValidationUtil.isMobile(phoneNum2)){
                        MyToast.showToast("请填写正确的手机号");
                        break;
                    }
                    if (TextUtils.isEmpty(name2)&&TextUtils.isEmpty(phoneNum2)){
                        MyToast.showToast("请完善第二个员工信息");
                    }else {
                        linearlayoutInvite3.setVisibility(View.VISIBLE);
                        deleteItem2.setVisibility(View.GONE);
                        entity2.setName(name2);
                        entity2.setPhoneNum(phoneNum2);
                        NUM = 3;
                        initData3();
                        MyToast.showToast("第二个"+name2);
                        mList.add(1,entity2);
                    }
                }
                break;
            case R.id.footView_invite:
                if (NUM == 1){
                    if(!ValidationUtil.isMobile(phoneNum1)){
                        MyToast.showToast("请填写正确的手机号");
                        break;
                    }
                    if (TextUtils.isEmpty(name1)&&TextUtils.isEmpty(phoneNum1)){
                        MyToast.showToast("请完善第一个员工信息");
                    }else {
                        entity1.setName(name1);
                        entity1.setPhoneNum(phoneNum1);
                        MyToast.showToast("第一个"+name1);
                        linearlayoutInvite2.setVisibility(View.VISIBLE);
                        NUM = 2;
                        initData2();
                        mList.add(0,entity1);
                    }
                }else if (NUM == 2){
                    if(!ValidationUtil.isMobile(phoneNum2)){
                        MyToast.showToast("请填写正确的手机号");
                        break;
                    }
                    if (TextUtils.isEmpty(name2)&&TextUtils.isEmpty(phoneNum2)){
                        MyToast.showToast("请完善第二个员工信息");
                    }else {
                        linearlayoutInvite3.setVisibility(View.VISIBLE);
                        deleteItem2.setVisibility(View.GONE);
                        entity2.setName(name2);
                        entity2.setPhoneNum(phoneNum2);
                        NUM = 3;
                        initData3();
                        MyToast.showToast("第二个"+name2);
                        mList.add(1,entity2);
                    }
                }
                break;
        }
    }
}
