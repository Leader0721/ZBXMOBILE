package com.zbxn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbxn.activity.searchcontacts.ISearchContactsView;
import com.zbxn.activity.searchcontacts.SearchContactsPresenter;
import com.zbxn.bean.Member;
import com.zbxn.R;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.bean.adapter.base.IItemViewControl;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.MyToast;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author GISirFive
 * @time 2016/8/11
 */
public class SearchContacts extends AbsToolbarActivity implements ISearchContactsView,
        IItemViewControl<Contacts>, AdapterView.OnItemClickListener {

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

    private SearchContactsPresenter mPresenter;

    private SpannableStringBuilder mStringBuilder = new SpannableStringBuilder();

    @Override
    public int getMainLayout() {
        return R.layout.activity_searchcontacts;
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

    private void init() {
        mPresenter = new SearchContactsPresenter(this);
        mListView.setOnItemClickListener(this);

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
                        mPresenter.search();
                    }
                }
                return false;
            }
        });
    }

    @Override
    public View initViewItem(LayoutInflater inflater, int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_addrbookpy, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Contacts item = (Contacts) mListView.getAdapter().getItem(position);


        if (TextUtils.isEmpty(item.getDepartmentName())) {
            //姓名
            holder.mRemarkName.setText(item.getUserName());
        } else {
            mStringBuilder.clear();
            mStringBuilder.append(item.getUserName())
                    .append("(").append(item.getDepartmentName()).append(")");
            mStringBuilder.setSpan(new ForegroundColorSpan(mColorDepartment),
                    item.getUserName().length(), mStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mStringBuilder.setSpan(new RelativeSizeSpan(0.8f),
                    item.getUserName().length(), mStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.mRemarkName.setText(mStringBuilder);
        }
        //手机
        holder.mMobileNumber.setVisibility(View.VISIBLE);
        holder.mMobileNumber.setText(item.getLoginname());

        String mBaseUrl = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
        ImageLoader.getInstance().displayImage(mBaseUrl + item.getPortrait(), holder.mPortrait);

        return convertView;
    }

    @Override
    public void dataSetChangedListener(List<Contacts> data) {

    }

    @Override
    public void setAdapter(BaseAdapter adapter) {
        mListView.setAdapter(adapter);
    }

    @Override
    public String getSearchContent() {
        return mSearchContent.getText().toString();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //判断当前点击的用户是否为登录用户
        Contacts contacts = (Contacts) mListView.getAdapter().getItem(position);
        int itemId = contacts.getId();
        int memberId = Member.get().getId();
        if (memberId == itemId)
            return;
        Intent intent = new Intent(this, ContactsDetail.class);
        intent.putExtra(ContactsDetail.Flag_Input_Contactor, contacts);
        startActivity(intent);
    }

    static class ViewHolder {
        @BindView(R.id.mPortrait)
        CircleImageView mPortrait;
        @BindView(R.id.mRemarkName)
        TextView mRemarkName;
        @BindView(R.id.mMobileNumber)
        TextView mMobileNumber;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
