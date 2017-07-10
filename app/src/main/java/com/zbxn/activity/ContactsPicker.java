package com.zbxn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.zbxn.fragment.AddrBookGroupFragment;
import com.zbxn.fragment.addrbookgroup.OnContactsPickerListener;
import com.zbxn.R;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author GISirFive
 * @time 2016/8/18
 */
public class ContactsPicker extends AbsToolbarActivity implements OnContactsPickerListener {

    /**
     * 输出标识java.util.List<{@link Contacts}>
     */
    public static final String Flag_Output_Checked = "contacts";

    @BindView(R.id.mBtBack)
    Button mBtnBack;
    @BindView(R.id.mBtnOk)
    Button mBtnOk;

    private AddrBookGroupFragment mContactsFragment;

    private List<Contacts> mCheckedList;

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("从通讯录中选择");
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    public int getMainLayout() {
        return R.layout.activity_contactspicker;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        updateSuccessView();
    }

    @Override
    public void loadData() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public boolean getSwipeBackEnable() {
        return true;
    }

    @OnClick({R.id.mBtBack, R.id.mBtnOk})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mBtBack:
                finish();
                break;
            case R.id.mBtnOk:
                if (mCheckedList == null || mCheckedList.isEmpty()) {
                    finish();
                } else {
                    Intent data = new Intent();
//                    data.putParcelableArrayListExtra(Flag_Output_Checked, (ArrayList<? extends Parcelable>) mCheckedList);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Flag_Output_Checked, (ArrayList) mCheckedList);
                    data.putExtras(bundle);
                    setResult(RESULT_OK, data);
                    finish();
                }
                break;
        }
    }

    private void init() {
        mContactsFragment = (AddrBookGroupFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mContactsFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mContactsFragment.setMultiChoiceMode(true);
        mContactsFragment.setOnContactsPickerListener(this);
    }

    @Override
    public void onSelectedContacts(List<Contacts> list, boolean containsLoginUser) {
        mCheckedList = list;
    }
}
