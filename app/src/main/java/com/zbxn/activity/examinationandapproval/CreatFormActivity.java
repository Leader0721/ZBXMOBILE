package com.zbxn.activity.examinationandapproval;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.zbxn.bean.ApprovalEntity;
import com.zbxn.bean.adapter.CreatFormAdpter;
import com.zbxn.R;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.JsonUtil;
import com.zbxn.pub.widget.NoScrollListview;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import utils.StringUtils;

/**
 * Created by ysj on 2016/11/5.
 */
public class CreatFormActivity extends AbsToolbarActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.mListView)
    NoScrollListview mListView;

    private CreatFormAdpter mToolsAdapter;
    private List<ApprovalEntity> listTemp;
    private String toolsList = "[{\"typeid\":1,\"img\":\"approval_ico_qingjia\",\"name\":\"请假\"},{\"typeid\":2,\"img\":\"approval_ico_baoxiao\",\"name\":\"报销\"},{\"typeid\":3,\"img\":\"approval_ico_wupin\",\"name\":\"物品申请\"},{\"typeid\":4,\"img\":\"approval_ico_gongzuoqingshi\",\"name\":\"工作请示\"},{\"typeid\":5,\"img\":\"approval_ico_chuchai\",\"name\":\"出差申请\"},{\"typeid\":6,\"img\":\"approval_ico_waichu\",\"name\":\"外出申请\"},{\"typeid\":7,\"img\":\"approval_ico_caigou\",\"name\":\"采购申请\"},{\"typeid\":8,\"img\":\"approval_ico_fukuan\",\"name\":\"付款申请\"},{\"typeid\":9,\"img\":\"approval_ico_yongyin\",\"name\":\"用印申请\"},{\"typeid\":10,\"img\":\"approval_ico_zhuanzheng\",\"name\":\"转正申请\"},{\"typeid\":11,\"img\":\"approval_ico_lizhi\",\"name\":\"离职申请\"},{\"typeid\":12,\"img\":\"approval_ico_jiaban\",\"name\":\"加班申请\"}]";

    @Override
    public int getMainLayout() {
        return R.layout.activity_creatform;
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("审批表单");
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateSuccessView();

        initView();
    }

    private void initView() {
        listTemp = new ArrayList<>();
        mListView.setOnItemClickListener(this);
        try {
            listTemp = JsonUtil.fromJsonList(toolsList, ApprovalEntity.class);
            if (!StringUtils.isEmpty(listTemp)) {
                mToolsAdapter = new CreatFormAdpter(this, listTemp);
                mListView.setAdapter(mToolsAdapter);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void loadData() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            int typeId = listTemp.get(position).getTypeid();
            Intent intent = new Intent(this, ApplyCreatActivity.class);
            intent.putExtra("typeId", typeId + "");
            startActivity(intent);
        } catch (Exception e) {

        }
    }
}
