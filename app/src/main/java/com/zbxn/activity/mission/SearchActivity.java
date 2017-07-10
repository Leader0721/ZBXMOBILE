package com.zbxn.activity.mission;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: ysj
 * @date: 2016-11-24 19:16
 */
public class SearchActivity extends AbsToolbarActivity {

    @BindView(R.id.mission_search_edit)
    EditText missionSearchEdit;
    @BindView(R.id.mission_search_cancel)
    TextView missionSearchCancel;

    @Override
    public int getMainLayout() {
        return R.layout.activity_mission_search;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateSuccessView();
        initView();
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("搜索");
        return super.onToolbarConfiguration(toolbar, params);
    }

    private void initView() {
        missionSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    missionSearchCancel.setTextColor(getResources().getColor(R.color.tvc6));
                } else {
                    missionSearchCancel.setTextColor(getResources().getColor(R.color.main_tab_text_blue));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            /*隐藏软键盘*/
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            }
            String content = missionSearchEdit.getText().toString().trim();
            Intent data = new Intent();
            data.putExtra("content", content);
            setResult(RESULT_OK, data);
            finish();

            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void loadData() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @OnClick(R.id.mission_search_cancel)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mission_search_cancel:
                missionSearchEdit.setText(null);
                missionSearchCancel.setTextColor(getResources().getColor(R.color.tvc6));
                break;
        }
    }
}
