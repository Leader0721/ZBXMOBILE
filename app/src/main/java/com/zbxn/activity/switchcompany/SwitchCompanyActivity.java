package com.zbxn.activity.switchcompany;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.zbxn.activity.login.LoginActivity;
import com.zbxn.listener.ICustomListener;
import com.zbxn.R;
import com.zbxn.activity.Main;
import com.zbxn.bean.adapter.SwitchCompanyAdapter;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.bean.ZmsCompanyListBean;
import com.zbxn.pub.bean.dbutils.DBUtils;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;

import java.util.List;

import butterknife.BindView;
import utils.PreferencesUtils;

/**
 * 切换公司
 *
 * @author Wuzy
 * @since 2016-9-8 上午10:11:35
 */
public class SwitchCompanyActivity extends AbsToolbarActivity {

    @BindView(R.id.mListView)
    ListView mListView;

    private SwitchCompanyPresenter mPresenter;
    private SwitchCompanyAdapter mAdapter;
    private List<ZmsCompanyListBean> mList;

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("切换公司");// 定义上面的名字
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    public int getMainLayout() {
        return R.layout.activity_switchcompany;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        updateSuccessView();
    }

    @Override
    public void loadData() {

    }

    private void initView() {
        mPresenter = new SwitchCompanyPresenter(this);
        mPresenter.findCompany(this, mICustomListener);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String zmsCompanyId = PreferencesUtils.getString(SwitchCompanyActivity.this, LoginActivity.FLAG_ZMSCOMPANYID);
                if (zmsCompanyId.equals(mList.get(i).getId())) {
                    Toast.makeText(SwitchCompanyActivity.this, "已是当前公司，不需切换", Toast.LENGTH_SHORT).show();
                    return;
                }
                mPresenter.updateDefaultCompany(getApplicationContext(), zmsCompanyId, mList.get(i).getId(), mICustomListener);
                DBUtils<Contacts> mDBUtils = new DBUtils<>(Contacts.class);
                mDBUtils.deleteAll();
            }
        });
    }

    private ICustomListener mICustomListener = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            switch (obj0) {
                case 0:
                    mList = (List<ZmsCompanyListBean>) obj1;
                    if (mList != null) {
                        mAdapter = new SwitchCompanyAdapter(getApplicationContext(), mList);
                        mListView.setAdapter(mAdapter);
                    }
                    break;
                case 1:
                    Intent intent = new Intent(BaseApp.getContext(), Main.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("isSwitchCompany", true);
                    BaseApp.getContext().startActivity(intent);
                    finish();
                    break;
            }
        }
    };
}
