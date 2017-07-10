package com.zbxn.activity.main.message.messagenew;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.activity.main.message.MessageCenterPager;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvc.AbsToolbarActivity;
import com.zbxn.pub.http.RequestUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.StringUtils;

/**
 * Created by Administrator on 2016/12/15.
 */
public class MessageCenterSearchActivity extends AbsToolbarActivity {
    @BindView(R.id.editText_newOkr_search)
    EditText editTextNewOkrSearch;
    @BindView(R.id.delete_hint)
    ImageView deleteHint;
    @BindView(R.id.newOkr_Search_Cancel)
    TextView newOkrSearchCancel;
    @BindView(R.id.linearlayout_sixLogo)
    LinearLayout linearlayoutSixLogo;
    @BindView(R.id.linearlayout_search)
    LinearLayout linearlayoutSearch;
    private FragmentManager mFragmentManager;
    private MessageCenterPager messageCenterPager;
    @Override
    public int getMainLayout() {
        return R.layout.activity_searchmessagecenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        mFragmentManager = getSupportFragmentManager();
        updateSuccessView();
        initView();

    }

    private void initView() {
        editTextNewOkrSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!StringUtils.isEmpty(editTextNewOkrSearch)) {

                        linearlayoutSearch.setVisibility(View.VISIBLE);
                        linearlayoutSixLogo.setVisibility(View.GONE);
                        MessageCenterPager.LABELKEY = "";
                        messageCenterPager.KEYWORD = StringUtils.getEditText(editTextNewOkrSearch);

                        if (null==messageCenterPager) {
                            messageCenterPager = new MessageCenterPager();
                            FragmentTransaction transaction = mFragmentManager.beginTransaction();
                            transaction.replace(R.id.linearlayout_search, messageCenterPager);
                            transaction.commit();
                        }else{
                            messageCenterPager.showRefresh(0);
                        }
                    }
                }
                return true;
            }
        });
    }


    @Override
    public void loadData() {

    }

    //设置toolbar
    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setNavigationIcon(null);
        getLayoutInflater().inflate(R.layout.searchokr_toolbar, toolbar);
        return super.onToolbarConfiguration(toolbar, params);
    }


    @OnClick({R.id.newOkr_Search_Cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.newOkr_Search_Cancel:
                messageCenterPager.KEYWORD = "";
                finish();
                break;
        }
    }

    @OnClick(R.id.delete_hint)
    public void onClick() {
        editTextNewOkrSearch.setText("");
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
