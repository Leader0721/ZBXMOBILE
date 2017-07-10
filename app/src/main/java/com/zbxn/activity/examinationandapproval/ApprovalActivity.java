package com.zbxn.activity.examinationandapproval;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.umeng.analytics.MobclickAgent;
import com.zbxn.R;
import com.zbxn.bean.ApprovalEntity;
import com.zbxn.bean.adapter.ApprovalAdapter;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 审批主界面
 * <p/>
 * Created by Administrator on 2016/10/10.
 */
public class ApprovalActivity extends AbsToolbarActivity implements AdapterView.OnItemClickListener {

    /**
     * 回调处理
     */
    private static final int Flag_Callback_1 = 1001;

    public static final String FLAG_INPUT_USERNAME = "username";
    private static final int Type_Apply = 101;      // 我的申请
    private static final int Type_Approval = 102;   // 我的审批
    private static final int Type_Inquire = 103;    // 审批查询
//
//    @BindView(R.id.mEntry)
//    LinearLayout mEntry;
//    @BindView(R.id.mApproval)
//    LinearLayout mApproval;
//    @BindView(R.id.mQuery)
//    LinearLayout mQuery;
//    @BindView(R.id.mGridView)
//    MyGridView mGridView;

    private ApprovalAdapter mToolsAdapter;

    private ApprovalPresenter mToolsPresenter;

    private List<ApprovalEntity> listTemp = new ArrayList<>();

    private String toolsList = "[{\"typeid\":1,\"img\":\"approval_ico_qingjia\",\"name\":\"请假\"},{\"typeid\":2,\"img\":\"approval_ico_baoxiao\",\"name\":\"报销\"},{\"typeid\":3,\"img\":\"approval_ico_wupin\",\"name\":\"物品申请\"},{\"typeid\":4,\"img\":\"approval_ico_gongzuoqingshi\",\"name\":\"工作请示\"},{\"typeid\":5,\"img\":\"approval_ico_chuchai\",\"name\":\"出差申请\"},{\"typeid\":6,\"img\":\"approval_ico_waichu\",\"name\":\"外出申请\"},{\"typeid\":7,\"img\":\"approval_ico_caigou\",\"name\":\"采购申请\"},{\"typeid\":8,\"img\":\"approval_ico_fukuan\",\"name\":\"付款申请\"},{\"typeid\":9,\"img\":\"approval_ico_yongyin\",\"name\":\"用印申请\"},{\"typeid\":10,\"img\":\"approval_ico_zhuanzheng\",\"name\":\"转正申请\"},{\"typeid\":11,\"img\":\"approval_ico_lizhi\",\"name\":\"离职申请\"},{\"typeid\":12,\"img\":\"approval_ico_jiaban\",\"name\":\"加班申请\"}]";

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public int getMainLayout() {
        return R.layout.fragment_apply;
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

    @Override
    public boolean getSwipeBackEnable() {
//        return super.getSwipeBackEnable();
        return true;
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("审批");
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ApprovalActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ApprovalActivity");
        MobclickAgent.onPause(this);
    }

    private void initView() {
//        mToolsAdapter = new ApprovalAdapter(this, null);
//        mGridView.setAdapter(mToolsAdapter);
//        mGridView.setOnItemClickListener(this);
//        mGridView.setFocusable(false);
//        mToolsPresenter = new ApprovalPresenter(this);
////        mToolsPresenter.getApprovalByAuthor(this, mICustomListener);
//
//        try {
//            listTemp = JsonUtil.fromJsonList(toolsList, ApprovalEntity.class);
//            if (!StringUtils.isEmpty(listTemp)) {
//                setData(listTemp);
//            }
//        } catch (Exception e) {
//
//        }

    }

    public void setData(List<ApprovalEntity> list) {
//        int size = list.size() + 1;
//        int line = size / 3;//一共有几行
//        if (size % 3 > 0)
//            line += 1;
//
//        int height = ScreenUtils.dipToPx(getApplicationContext(), line * 96);
//        ViewGroup.LayoutParams params = mGridView.getLayoutParams();
//        params.height = height;
//        mGridView.setLayoutParams(params);
//
//        mToolsAdapter.resetData(list);

    }

//    @OnClick({R.id.mEntry, R.id.mApproval, R.id.mQuery})
//    public void onClick(View view) {
//        Intent intent = new Intent(this, ApplyActivity.class);
//        switch (view.getId()) {
//            case R.id.mEntry://申请
//                intent.putExtra("type", Type_Apply);
//                break;
//            case R.id.mApproval://审批
//                intent.putExtra("type", Type_Approval);
//                break;
//            case R.id.mQuery://审批查询
//                intent.putExtra("type", Type_Inquire);
//                break;
//
//        }
//        startActivity(intent);
//
//    }

    /**
     * 回调
     */
    private ICustomListener mICustomListener = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            switch (obj0) {
                case 0:
                    listTemp = (List<ApprovalEntity>) obj1;
                    setData(listTemp);
                    break;
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            int typeId = listTemp.get(position).getTypeid();
            Intent intent = new Intent(this, ApplyCreatActivity.class);
            intent.putExtra("typeId", typeId + "");
            startActivityForResult(intent, Flag_Callback_1);
        } catch (Exception e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == Flag_Callback_1) {

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
