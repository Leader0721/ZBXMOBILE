package com.zbxn.activity.examinationandapproval;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.zbxn.fragment.AddrBookGroupFragment;
import com.zbxn.fragment.addrbookgroup.OnContactsPickerListener;
import com.zbxn.R;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.MyToast;

import java.util.List;

/**
 * 单选联系人
 *
 * @author: ysj
 * @date: 2016-10-11 18:10
 */
public class SelectPeopleActivity extends AbsToolbarActivity implements OnContactsPickerListener {

    private AddrBookGroupFragment mContactsFragment;

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("选择审批人");
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    public boolean getSwipeBackEnable() {
//        return super.getSwipeBackEnable();
        return true;
    }

    @Override
    public int getMainLayout() {
        return R.layout.activity_select;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactsFragment = (AddrBookGroupFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mSelectPeople);
        mContactsFragment.setOnContactsPickerListener(this);
        //显示加载成功界面
        updateSuccessView();
    }

    @Override
    public void loadData() {

    }

    @Override
    public void onSelectedContacts(List<Contacts> list, boolean containsLoginUser) {
        Contacts contacts = list.get(0);
        if (containsLoginUser) {
            MyToast.showToast("不能选择自己");
        } else {
            Intent data = new Intent();
            data.putExtra("id", contacts.getId());
            data.putExtra("name", contacts.getUserName());
            setResult(RESULT_OK, data);
            finish();
        }
    }

}
