package com.zbxn.activity.organize;

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
import com.zbxn.bean.adapter.OrganizeDepartmentAdapter;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.bean.ContactsDepartment;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.widget.MyListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import utils.StringUtils;

/**
 * 项目名称：通讯录 按组织架构
 * 创建人：Wuzy
 * 创建时间：2016/11/1 8:52
 */
public class OrganizeChooseActivity extends AbsToolbarActivity {
    @BindView(R.id.mLayout)
    LinearLayout mLayout;
    @BindView(R.id.mListViewDepartment)
    MyListView mListViewDepartment;

    private LayoutInflater mInflater;

    private String mIdSelect = "";

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("选择部门");
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    public int getMainLayout() {
        return R.layout.activity_organize_choose;
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIdSelect = getIntent().getStringExtra("id");
        if (StringUtils.isEmpty(mIdSelect)){
            mIdSelect = "";
        }

        mInflater = LayoutInflater.from(this);
        initView();
        updateSuccessView();
        //String zmsCompanyName = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYNAME);
        addView("全公司", "0");
        getDepartment("0");

    }

    private void initView() {
    }

    private List<View> mListV = new ArrayList<>();

    private void addView(String name, String parentId) {
        View view = mInflater.inflate(R.layout.view_organize_text, mLayout, false);
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
        Drawable drawable = getResources().getDrawable(R.mipmap.my_org_arrows);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        for (int i = 0; i < mListV.size(); i++) {
            if (mListV.size() - 1 == i) {
                ((TextView) mListV.get(i).findViewById(R.id.mText)).setCompoundDrawables(null, null, null, null);
                ((TextView) mListV.get(i).findViewById(R.id.mText)).setTextColor(getResources().getColor(R.color.tvc9));
            } else {
                ((TextView) mListV.get(i).findViewById(R.id.mText)).setCompoundDrawables(null, null, drawable, null);
                ((TextView) mListV.get(i).findViewById(R.id.mText)).setTextColor(getResources().getColor(R.color.cpb_blue));
            }
            mLayout.addView(mListV.get(i));
        }
    }

    private List<ContactsDepartment> listDepartment = new ArrayList<>();

    private void getDepartment(String parentId) {
        listDepartment.clear();
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
                if (mIdSelect.equals(contactsDepartment.getDepartmentId())) {
                    contactsDepartment.setSelected(true);
                }
                listDepartment.add(contactsDepartment);
            }
            OrganizeDepartmentAdapter adapter = new OrganizeDepartmentAdapter(this, listDepartment, listener);
            mListViewDepartment.setAdapter(adapter);
            mListViewDepartment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    addView(listDepartment.get(position).getDepartmentName(), listDepartment.get(position).getDepartmentId());
                    getDepartment(listDepartment.get(position).getDepartmentId());
                }
            });

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private ICustomListener listener = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {

            switch (obj0) {
                case 0:
                    ContactsDepartment result = (ContactsDepartment) obj1;
                    int id = 0;
                    try {
                        id = Integer.parseInt(result.getDepartmentId());
                    } catch (Exception e) {

                    }
                    Intent intent = new Intent();
                    intent.putExtra("name", result.getDepartmentName());
                    intent.putExtra("id", id);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public void loadData() {

    }
}

