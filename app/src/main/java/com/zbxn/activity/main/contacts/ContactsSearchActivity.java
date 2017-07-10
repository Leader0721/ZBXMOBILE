package com.zbxn.activity.main.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.zbxn.R;
import com.zbxn.activity.ContactsDetail;
import com.zbxn.bean.Member;
import com.zbxn.bean.adapter.ContactsPeopleAdapter;
import com.zbxn.init.eventbus.EventCustom;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.MyToast;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.OnClick;
import utils.StringUtils;

/**
 * @author GISirFive
 * @time 2016/8/11
 */
public class ContactsSearchActivity extends AbsToolbarActivity {

    @BindView(R.id.mBtnBack)
    ImageView mBtnBack;
    @BindView(R.id.mSearchContent)
    EditText mSearchContent;
    @BindView(R.id.mDeletContent)
    ImageView mDeletContent;
    @BindView(R.id.mListView)
    ListView mListView;

    @BindColor(R.color.app_secondary_text)
    int mColorDepartment;
    @BindView(R.id.mTextSelect)
    TextView mTextSelect;
    @BindView(R.id.mTextOk)
    TextView mTextOk;
    @BindView(R.id.mLayoutBottom)
    LinearLayout mLayoutBottom;
    @BindView(R.id.mCheck)
    TextView mCheck;

    private SpannableStringBuilder mStringBuilder = new SpannableStringBuilder();

    private ContactsPeopleAdapter mAdapter;

    //0-查看 1-多选 2-单选
    private int mType;

    @Override
    public int getMainLayout() {
        return R.layout.activity_contacts_search;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getIntent().getIntExtra("type", 0);
        init();
        updateSuccessView();

        mCheck.setVisibility(View.GONE);
        mTextOk.setVisibility(View.GONE);
        if (mType == 0 || mType == 2) {
            mLayoutBottom.setVisibility(View.GONE);
        }

        //设置选中数据
        setData();
    }

    @Override
    public void loadData() {

    }

    @Override
    public boolean getSwipeBackEnable() {
        return true;
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setNavigationIcon(null);
        getLayoutInflater().inflate(R.layout.searchcontacts_toolbar, toolbar);
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @OnClick({R.id.mBtnBack, R.id.mDeletContent})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mBtnBack://关闭页面
                scrollToFinish();
                break;
            case R.id.mDeletContent:
                mSearchContent.setText("");
                break;
        }
    }

    public void search() {
        String content = StringUtils.getEditText(mSearchContent);
        try {
            content = "%" + content + "%";
            List<Contacts> mList = BaseApp.DBLoader.findAll(
                    Selector.from(Contacts.class)
                            .where("userName", "like", content)
                            .or("telephone", "like", content)
                            .or("loginname", "like", content)
                            .or("departmentName", "like", content)
                            .orderBy("captialChar", true));
            chechIsSelect(mList);
            if (StringUtils.isEmpty(mList)) {
                mList = new ArrayList<>();
                HashMap<String, Integer> alphaIndexer = new HashMap<String, Integer>();
                String[] sections = new String[mList.size()];
                mAdapter = new ContactsPeopleAdapter(this, mList, null, alphaIndexer, sections, false, mType);
                mListView.setAdapter(mAdapter);
                MyToast.showToast("没找到对应的记录");
            } else {
                HashMap<String, Integer> alphaIndexer = new HashMap<String, Integer>();
                String[] sections = new String[mList.size()];
                mAdapter = new ContactsPeopleAdapter(this, mList, null, alphaIndexer, sections, false, mType);
                mListView.setAdapter(mAdapter);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mType == 0) {
                    //判断当前点击的用户是否为登录用户
                    Contacts contacts = (Contacts) mListView.getAdapter().getItem(position);
                    int itemId = contacts.getId();
                    int memberId = Member.get().getId();
                    if (memberId == itemId)
                        return;
                    Intent intent = new Intent(ContactsSearchActivity.this, ContactsDetail.class);
                    intent.putExtra(ContactsDetail.Flag_Input_Contactor, contacts);
                    startActivity(intent);
                    return;
                }
            }
        });

        mSearchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isEmpty = (s == null || s.length() == 0);
                mDeletContent.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
            }
        });
        mSearchContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    String content = mSearchContent.getText().toString().trim();
                    if (TextUtils.isEmpty(content)) {
                        MyToast.showToast("请输入搜索内容");
                    } else {
                        search();
                    }
                }
                return false;
            }
        });
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

    @Subscriber
    public void onEventMainThread(EventCustom eventCustom) {
        if (com.zbxn.utils.KeyEvent.UPDATECONTACTSSELECT.equals(eventCustom.getTag())) {
            //设置选中数据
            setData();
        }
    }
    /**
     * 设置选中数据
     */
    private void setData(){
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
        }  else if (count == 0) {
            select = select + "0人";
        }
        mTextSelect.setText("已选择:" + select);
    }
}
