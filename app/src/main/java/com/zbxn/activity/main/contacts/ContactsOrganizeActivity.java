package com.zbxn.activity.main.contacts;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;
import com.zbxn.R;
import com.zbxn.activity.ContactsDetail;
import com.zbxn.bean.Member;
import com.zbxn.bean.adapter.ContactsDepartmentOnlyAdapter;
import com.zbxn.bean.adapter.ContactsPeopleAdapter;
import com.zbxn.init.eventbus.EventCustom;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.bean.ContactsDepartment;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.utils.KeyEvent;
import com.zbxn.widget.MyListView;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 项目名称：通讯录 按组织架构
 * 创建人：Wuzy
 * 创建时间：2016/11/1 8:52
 */
public class ContactsOrganizeActivity extends AbsToolbarActivity {
    @BindView(R.id.mLayout)
    LinearLayout mLayout;
    @BindView(R.id.mListViewDepartment)
    MyListView mListViewDepartment;
    @BindView(R.id.mListViewPeople)
    MyListView mListViewPeople;
    @BindView(R.id.mTextSelect)
    TextView mTextSelect;
    @BindView(R.id.mTextOk)
    TextView mTextOk;
    @BindView(R.id.mLayoutBottom)
    LinearLayout mLayoutBottom;
    @BindView(R.id.mCheck)
    TextView mCheck;

    private LayoutInflater mInflater;

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("按组织架构");
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    public int getMainLayout() {
        return R.layout.activity_contacts_organize;
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    //0-查看 1-多选 2-单选
    private int mType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mType = getIntent().getIntExtra("type", 0);

        mInflater = LayoutInflater.from(this);
        initView();
        updateSuccessView();
        //String zmsCompanyName = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYNAME);
        addView("全公司", "0");
        getDepartment("0");

        mCheck.setVisibility(View.GONE);
        mTextOk.setVisibility(View.GONE);
        if (mType == 0 || mType == 2) {
            mLayoutBottom.setVisibility(View.GONE);
        }

        //设置选中数据
        setData();

    }

    private void initView() {
    }

    private List<View> mListV = new ArrayList<>();

    private void addView(String name, String parentId) {
        View view = mInflater.inflate(R.layout.view_contacts_organize_text, mLayout, false);
        view.setTag(parentId);
        TextView textView = (TextView) view.findViewById(R.id.mText);
        textView.setText(name);
        textView.setTag(parentId);
//        Drawable drawable = getResources().getDrawable(R.drawable.drawable);
//        /// 这一步必须要做,否则不会显示.
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        if ("0".equals(parentId)) {
            textView.setCompoundDrawables(null, null, null, null);
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDepartment(v.getTag().toString() + "");
                int index = -1;
                for (int i = 0; i < mListV.size(); i++) {
                    if (v.getTag().toString().equals(mListV.get(i).getTag().toString())) {
                        index = i;
                    }
                }
                //解决每次产生的刷新产生null
                mListV = mListV.subList(0, index + 1);
                //mListV = mListV.subList(0, index );
                refresh();
            }
        });
        mListV.add(view);
        refresh();
    }

    /**
     * 刷新顶部视图
     */
    private void refresh() {
        mLayout.removeAllViews();
        Drawable drawable = getResources().getDrawable(R.mipmap.rignt_arrow);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        for (int i = 0; i < mListV.size(); i++) {
            if (mListV.size() - 1 == i) {
                ((TextView) mListV.get(i).findViewById(R.id.mText)).setCompoundDrawables(null, null, null, null);
                ((TextView) mListV.get(i).findViewById(R.id.mText)).setTextColor(getResources().getColor(R.color.tvc9));
            } else {
                ((TextView) mListV.get(i).findViewById(R.id.mText)).setCompoundDrawables(null, null, drawable, null);
                ((TextView) mListV.get(i).findViewById(R.id.mText)).setTextColor(getResources().getColor(R.color.orange));
            }
            mLayout.addView(mListV.get(i));
        }
    }

    private List<ContactsDepartment> listDepartment = new ArrayList<>();
    private List<Contacts> listContacts = new ArrayList<>();

    private void getDepartment(String parentId) {
        listDepartment.clear();
        listContacts.clear();
        //例：分组聚合查询出  Parent表中 非重复的name和它的对应数量
        try {
            //, "count(departmentName) as countPeople"
            List<DbModel> dbModelsDepartment = BaseApp.DBLoader.findDbModelAll(Selector.from(Contacts.class)
                    .select("departmentId", "departmentName")
                    .where("isDepartment", "=", "1").and("parentId", "=", parentId));
            ContactsDepartment contactsDepartment = null;
            for (int i = 0; i < dbModelsDepartment.size(); i++) {
                contactsDepartment = new ContactsDepartment();
//                contactsDepartment.setCaptialChar(StringUtils.getPinYinHeadChar(dbModelsDepartment.get(i).getString("departmentName")));
                contactsDepartment.setDepartmentId(dbModelsDepartment.get(i).getString("departmentId"));
                contactsDepartment.setDepartmentName(dbModelsDepartment.get(i).getString("departmentName"));
                listDepartment.add(contactsDepartment);
            }
            ContactsDepartmentOnlyAdapter adapter = new ContactsDepartmentOnlyAdapter(this, listDepartment, listener);
            mListViewDepartment.setAdapter(adapter);
            mListViewDepartment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    addView(listDepartment.get(position).getDepartmentName(), listDepartment.get(position).getDepartmentId());
                    getDepartment(listDepartment.get(position).getDepartmentId());
                }
            });

            listContacts = BaseApp.DBLoader.findAll(Selector.from(Contacts.class)
                    .where("isDepartment", "=", null).and("departmentId", "=", parentId));
            chechIsSelect(listContacts);
            HashMap<String, Integer> alphaIndexer = new HashMap<String, Integer>();
            String[] sections = new String[listContacts.size()];
            ContactsPeopleAdapter contactsPeopleAdapter = new ContactsPeopleAdapter(this, listContacts, listener, alphaIndexer, sections, false, mType);
            mListViewPeople.setAdapter(contactsPeopleAdapter);
            mListViewPeople.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent;
                    if (mType == 0) {
                        if (listContacts.get(position).getId() == -1000) {
                            intent = new Intent(ContactsOrganizeActivity.this, ContactsOrganizeActivity.class);
                            startActivity(intent);
                            return;
                        }
                        //判断当前点击的用户是否为登录用户
                        Contacts contacts = (Contacts) mListViewPeople.getAdapter().getItem(position);
                        int itemId = contacts.getId();
                        int memberId = Member.get().getId();
                        if (memberId == itemId) {
                            return;
                        }
                        intent = new Intent(ContactsOrganizeActivity.this, ContactsDetail.class);
                        intent.putExtra(ContactsDetail.Flag_Input_Contactor, contacts);
                        startActivity(intent);
                    } else if (mType == 1) {

                    } else if (mType == 2) {

                    }
                }
            });
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新是否选中
     *
     * @param lists
     */
    private void chechIsSelect(final List<Contacts> lists) {
        List<Contacts> listTemp = new ArrayList<>();
        listTemp.addAll(lists);
        lists.clear();
        for (int i = 0; i < listTemp.size(); i++) {
            if (ContactsChoseActivity.mHashMap.containsKey(listTemp.get(i).getId() + "")) {
                listTemp.get(i).setSelected(true);
            } else {
                listTemp.get(i).setSelected(false);
            }
        }
        lists.addAll(listTemp);
    }

    private ICustomListener listener = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            Contacts result = (Contacts) obj1;
            switch (obj0) {
                case 0:
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public void loadData() {

    }

    @Subscriber
    public void onEventMainThread(EventCustom eventCustom) {
        if (KeyEvent.UPDATECONTACTSSELECT.equals(eventCustom.getTag())) {
            //设置选中数据
            setData();
        }
    }

    /**
     * 设置选中数据
     */
    private void setData() {
        Contacts contacts = null;
        String select = "";
        int count = 0;
        //取得map中所有的key和value
        for (Map.Entry<String, String> entry : ContactsChoseActivity.mHashMap.entrySet()) {
            contacts = new Contacts();
            String key = entry.getKey();
            String value = entry.getValue();
            contacts.setId(Integer.parseInt(key));
            contacts.setUserName(value);
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
    }
}

