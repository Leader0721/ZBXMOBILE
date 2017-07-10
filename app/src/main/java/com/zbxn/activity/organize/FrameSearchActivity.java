package com.zbxn.activity.organize;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.zbxn.R;
import com.zbxn.activity.main.contacts.ContactsChoseActivity;
import com.zbxn.bean.adapter.FrameSearchAdapter;
import com.zbxn.bean.adapter.OrganizePeopleAdapter;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.dialog.MessageDialog;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvc.AbsToolbarActivity;
import com.zbxn.pub.http.RequestUtils;
import com.zbxn.pub.utils.JsonUtil;
import com.zbxn.pub.utils.MyToast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.PreferencesUtils;
import utils.StringUtils;

/**
 * Created by Administrator on 2016/12/29.
 */
public class FrameSearchActivity extends AbsToolbarActivity {
    @BindView(R.id.textTip)
    TextView textTip;
    @BindView(R.id.image_line)
    ImageView imageLine;
    @BindView(R.id.listView_FrameSearch)
    ListView listViewFrameSearch;
    @BindView(R.id.mSearchFrameContent)
    EditText mSearchFrameContent;
    @BindView(R.id.delete_hint)
    ImageView deleteHint;
    @BindView(R.id.listView_SearchContact)
    ListView listViewSearchContact;
    private OrganizePeopleAdapter mAdapter;
    private FrameSearchAdapter mAdapterFrame;
    private List<String> mList;

    private String mSearch = "mFrameSearch";

    private String keyword = "";
    private List<String> fromJsonList;

    @Override
    public int getMainLayout() {
        return R.layout.activity_framesearch;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        keyword = getIntent().getStringExtra("keyword");
        if (StringUtils.isEmpty(keyword)) {
            keyword = "";
        }
        mSearchFrameContent.setText(keyword);
        mList = new ArrayList<>();
        mAdapterFrame = new FrameSearchAdapter(this, mList, mListener);
        listViewFrameSearch.setAdapter(mAdapterFrame);


        init();

        //initView();

        initHistory();

        updateSuccessView();
    }

    @Override
    public void loadData() {

    }


    //设置toolbar
    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setNavigationIcon(null);
        getLayoutInflater().inflate(R.layout.searchframe_toolbar, toolbar);
        return super.onToolbarConfiguration(toolbar, params);
    }

    private ICustomListener mListener = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            String result = obj1.toString();
            switch (obj0) {
                case 0://删除处理方式
                    updateListHistory(result, false);
                    break;
                default:
                    break;
            }
        }
    };


    private void initView() {
        mSearchFrameContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //历史记录
                String listHistoryStr = PreferencesUtils.getString(FrameSearchActivity.this, mSearch, "[]");
                mList = json2List(listHistoryStr);
                if (mList.size() <= 0) {
                    setVis(false);
                } else {
                    setVis(true);
                }
            }
        });
        mSearchFrameContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!StringUtils.isEmpty(mSearchFrameContent)) {
                        updateListHistory(StringUtils.getEditText(mSearchFrameContent), true);
                    }
                }
                return true;
            }
        });
    }


    private void init() {
        listViewSearchContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //判断当前点击的用户是否为登录用户
                Contacts contacts = (Contacts) listViewSearchContact.getAdapter().getItem(position);
                /*int itemId = contacts.getId();
                int memberId = Member.get().getId();
                if (memberId == itemId)
                    return;*/
                Intent intent = new Intent(FrameSearchActivity.this, EmployeeDetailActivity.class);
                intent.putExtra(EmployeeDetailActivity.Flag_Input_Contactor, contacts);
                startActivity(intent);
                return;
            }
        });

        mSearchFrameContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isEmpty = (s == null || s.length() == 0);
                deleteHint.setVisibility(isEmpty ? View.GONE : View.VISIBLE);


                //历史记录
                String listHistoryStr = PreferencesUtils.getString(FrameSearchActivity.this, mSearch, "[]");
                mList = json2List(listHistoryStr);
                if (mList.size() <= 0) {
                    setVis(false);
                } else {
                    setVis(true);
                }
            }
        });
        mSearchFrameContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    String content = mSearchFrameContent.getText().toString().trim();
                    if (TextUtils.isEmpty(content)) {
                        MyToast.showToast("请输入搜索内容");
                    } else {
                        search();
                    }
                }
                //return false;

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!StringUtils.isEmpty(mSearchFrameContent)) {
                        updateListHistory(StringUtils.getEditText(mSearchFrameContent), true);
                    }
                }
                return false;

            }
        });
    }

    public void search() {
        String content = StringUtils.getEditText(mSearchFrameContent);
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
                mAdapter = new OrganizePeopleAdapter(this, mList, null, false);
                listViewSearchContact.setAdapter(mAdapter);
                MyToast.showToast("没找到对应的记录");
            } else {
                mAdapter = new OrganizePeopleAdapter(this, mList, null, false);
                listViewSearchContact.setAdapter(mAdapter);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置是否显示
     *
     * @param isVis
     */
    private void setVis(boolean isVis) {
        if (isVis) {
            listViewFrameSearch.setVisibility(View.VISIBLE);
            textTip.setVisibility(View.VISIBLE);
            imageLine.setVisibility(View.VISIBLE);
        } else {
            listViewFrameSearch.setVisibility(View.GONE);
            textTip.setVisibility(View.GONE);
            imageLine.setVisibility(View.GONE);
        }
    }

    //历史记录
    private void initHistory() {
        //历史记录
        String listHistoryStr = PreferencesUtils.getString(this, mSearch, "[]");
        mList = json2List(listHistoryStr);
        if (mList.size() <= 0) {
            setVis(false);
            //     setHintTextSize(editTextNewOkrSearch, "搜索", 20);
        } else {
            setVis(true);
            //setHintTextSize(editTextNewOkrSearch, mList.get(0), 20);
        }
        mAdapterFrame = new FrameSearchAdapter(this, mList, mListener);
        listViewFrameSearch.addFooterView(createView());
        listViewFrameSearch.setAdapter(mAdapterFrame);
        listViewFrameSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSearchFrameContent.setText(mList.get(position));
                updateListHistory(mList.get(position), true);
                setVis(false);

                //显示键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mSearchFrameContent, InputMethodManager.SHOW_FORCED);
            }
        });
    }

    protected void updateListHistory(String keyword, boolean add) {
        for (String str : mList) {
            if (str.equals(keyword)) {
                mList.remove(str);
                break;
            }
        }
        if (add) {
            mList.add(0, keyword);
        } else {
            mAdapterFrame.notifyDataSetChanged();
            if (mList.size() <= 0) {
                setVis(false);
            } else {
                setVis(true);
            }
        }
        //保存LISTHISTORY
        PreferencesUtils.putString(this, mSearch, list2Json(mList));
    }

    private List<String> json2List(String json) {
        fromJsonList = JsonUtil.fromJsonList(json, String.class);
        return fromJsonList;
    }

    private String list2Json(List<String> list) {
        return JsonUtil.toJsonString(list);
    }

    /**
     * 设置listview 尾部
     */
    private View createView() {
        View mContainer = LayoutInflater.from(this).inflate(
                R.layout.list_item_search_history_footer, null);
        RelativeLayout m_layout = (RelativeLayout) mContainer
                .findViewById(R.id.m_layout);
        m_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageDialog messageDialog = new MessageDialog(FrameSearchActivity.this);
                messageDialog.setTitle("提示");
                messageDialog.setMessage("是否确认删除?");
                messageDialog.setNegativeButton("取消");
                messageDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mList.clear();
                        PreferencesUtils.putString(FrameSearchActivity.this, mSearch, list2Json(mList));
                        mAdapterFrame.notifyDataSetChanged();
                        if (mList.size() <= 0) {
                            setVis(false);
                        }
                    }
                });
                messageDialog.show();
            }
        });
        return mContainer;
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


    @OnClick({R.id.delete_hint,R.id.mBtnBack})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.delete_hint:
                mSearchFrameContent.setText("");
                break;
            case R.id.mBtnBack://关闭页面
                scrollToFinish();
                break;
        }
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
}
