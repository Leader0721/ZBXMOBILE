package com.zbxn.activity.main.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.db.sqlite.Selector;
import com.zbxn.R;
import com.zbxn.activity.ContactsPicker;
import com.zbxn.fragment.addrbookgroup.OnContactsPickerListener;
import com.zbxn.init.eventbus.EventCustom;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.utils.KeyEvent;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import utils.StringUtils;

/**
 * @author GISirFive
 * @time 2016/8/18
 */
public class ContactsChoseActivity extends AbsToolbarActivity implements OnContactsPickerListener {

    /**
     * 输出标识java.util.List<{@link Contacts}>
     */
//    public static final String Flag_Output_Checked = "contacts";

    @BindView(R.id.mTextOk)
    TextView mTextOk;
    @BindView(R.id.mLayoutBottom)
    LinearLayout mLayoutBottom;
    @BindView(R.id.mFragment)
    FrameLayout mFragment;
    @BindView(R.id.mCheck)
    TextView mCheck;
    @BindView(R.id.mTextSelect)
    TextView mTextSelect;

    private List<Contacts> mCheckedList;

    private List<Contacts> listAll;//全部

    //0-查看 1-多选 2-单选
    private int mType;

    public static HashMap<String, String> mHashMap = new HashMap<String, String>();

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("从通讯录中选择");
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    public int getMainLayout() {
        return R.layout.activity_contacts_chose;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getIntent().getIntExtra("type", 0);
        mCheckedList = (List<Contacts>) getIntent().getSerializableExtra("list");

        try {
            listAll = BaseApp.DBLoader.findAll(Selector.from(Contacts.class).where("isDepartment", "=", null).orderBy("captialChar"));//
        } catch (Exception e) {
            listAll = new ArrayList<>();
            e.printStackTrace();
        }

        mHashMap.clear();
        if (StringUtils.isEmpty(mCheckedList)) {
            mCheckedList = new ArrayList<>();
        } else {
            for (int i = 0; i < mCheckedList.size(); i++) {
                mHashMap.put(mCheckedList.get(i).getId() + "", mCheckedList.get(i).getUserName());
            }

            //通知更新选择人
            EventCustom eventCustom = new EventCustom();
            eventCustom.setTag(KeyEvent.UPDATECONTACTSSELECT);
            eventCustom.setObj("");
            EventBus.getDefault().post(eventCustom);
        }
        init();

        if (mType == 2) {
            mLayoutBottom.setVisibility(View.GONE);
        }
        updateSuccessView();
        //隐藏阴影
        getToolbarHelper().setShadowEnable(false);
    }

    @Override
    public void loadData() {

    }

    /**
     * 创建按钮显示
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contacts_search, menu);
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
            case R.id.mSearch:
                Intent intent = new Intent(this, ContactsSearchActivity.class);
                intent.putExtra("type", mType);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public boolean getSwipeBackEnable() {
        return true;
    }

    @OnClick({R.id.mCheck, R.id.mTextOk})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mCheck:
                if (mCheck.isSelected()) {
                    mCheck.setSelected(false);
                    mHashMap.clear();
                } else {
                    mCheck.setSelected(true);
                    mHashMap.clear();
                    for (int i = 0; i < listAll.size(); i++) {
                        mHashMap.put(listAll.get(i).getId() + "", listAll.get(i).getUserName());
                    }
                }

                //通知更新选择人
                EventCustom eventCustom = new EventCustom();
                eventCustom.setTag(KeyEvent.UPDATECONTACTSSELECT);
                eventCustom.setObj("");
                EventBus.getDefault().post(eventCustom);
                break;
            case R.id.mTextOk:
                if (StringUtils.isEmpty(mCheckedList)) {
                    finish();
                } else {
                    Intent data = new Intent();
//                    data.putParcelableArrayListExtra(Flag_Output_Checked, (ArrayList<? extends Parcelable>) mCheckedList);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ContactsPicker.Flag_Output_Checked, (ArrayList) mCheckedList);
                    data.putExtras(bundle);
                    setResult(RESULT_OK, data);
                    finish();
                }
                break;
        }
    }

    //    Intent intent = new Intent(this, ContactsSearchActivity.class);
//    intent.putExtra("type", 0);
//    startActivity(intent);
    private void init() {
        ContactsFragment fragment = new ContactsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", mType);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.mFragment, fragment);
        transaction.commit();
    }

    @Override
    public void onSelectedContacts(List<Contacts> list, boolean containsLoginUser) {
        mCheckedList = list;
    }

    @Subscriber
    public void onEventMainThread(EventCustom eventCustom) {
        if (KeyEvent.UPDATECONTACTSSELECT.equals(eventCustom.getTag())) {
            mCheckedList.clear();
            Contacts contacts = null;
            String select = "";
            int count = 0;
            //取得map中所有的key和value
            for (Map.Entry<String, String> entry : mHashMap.entrySet()) {
                contacts = new Contacts();
                String key = entry.getKey();
                String value = entry.getValue();
                contacts.setId(Integer.parseInt(key));
                contacts.setUserName(value);
                mCheckedList.add(contacts);
                count++;
                if (count <= 2) {
                    select += (value + ",");
                }
            }
            if (select.length() > 0) {
                select = select.substring(0, select.length() - 1);
            }
            if (count > 2) {
                select = select + "等" + count + "人";
            } else if (count == 0) {
                select = select + "0人";
            }
            mTextSelect.setText("已选择:" + select);

            //设置全选按钮状态
            if (count >= listAll.size()) {
                mCheck.setSelected(true);
            } else {
                mCheck.setSelected(false);
            }
        } else if (KeyEvent.UPDATECONTACTSSELECTSINGLE.equals(eventCustom.getTag())) {
            mCheckedList.clear();
            Contacts contacts = null;
            String select = "";
            int count = 0;
            //取得map中所有的key和value
            for (Map.Entry<String, String> entry : mHashMap.entrySet()) {
                contacts = new Contacts();
                String key = entry.getKey();
                String value = entry.getValue();
                contacts.setId(Integer.parseInt(key));
                contacts.setUserName(value);
                mCheckedList.add(contacts);
                count++;
                if (count <= 2) {
                    select += (value + ",");
                }
            }
            if (select.length() > 0) {
                select = select.substring(0, select.length() - 1);
            }
            if (count > 2) {
                select = select + "等" + count + "人";
            } else if (count == 0) {
                select = select + "0人";
            }
//            mTextSelect.setText("已选择:" + select);
            if (StringUtils.isEmpty(mCheckedList)) {
                finish();
            } else {
                Intent data = new Intent();
                data.putExtra("id", mCheckedList.get(0).getId());
                data.putExtra("name", mCheckedList.get(0).getUserName());
                setResult(RESULT_OK, data);
                finish();
            }
        }
    }
}
