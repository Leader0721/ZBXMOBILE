package com.zbxn.activity.examinationandapproval;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbxn.activity.main.contacts.ContactsChoseActivity;
import com.zbxn.listener.ICustomListener;
import com.zbxn.R;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.MyToast;

import butterknife.BindView;
import butterknife.OnClick;
import utils.StringUtils;

/**
 * 审批意见
 *
 * @author: ysj
 * @date: 2016-10-11 16:04
 */
public class ApprovalOpinionActivity extends AbsToolbarActivity {

    private static final int Flag_ApplyForm_Next = 1000; //同意并转审
    private static final int Flag_ApplyForm_End = 1001;  //同意并结束
    private static final int Flag_CallBack_Select = 1002; //选择转审人
    private static final int Flag_ApplyForm_Rejected = 1003;// 驳回
    private static final int Flag_ApplyForm_Stop = 1004;// 终止

    private static final int Flag_State_0 = 0; //审批中
    private static final int Flag_State_1 = 1; //同意
    private static final int Flag_State_2 = 2; //驳回
    private static final int Flag_State_3 = 3; //终止

    @BindView(R.id.opinion_text)
    TextView opinionText;
    @BindView(R.id.opinion_edit)
    EditText opinionEdit;
    @BindView(R.id.opinion_name)
    TextView opinionName;
    @BindView(R.id.opinion_layout)
    LinearLayout opinionLayout;

    private int flag = -1;//页面标记
    private MenuItem mCollect;

    private String mApprovalName;//审批人name
    private int mApprovalId;//审批人id
    private String approvalID;

    private boolean isAgree; // 是否同意

    private ApprovalPresenter mPresenter;

    @Override
    public boolean handleMessage(Message msg) {
        finish();
        return false;
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("审批意见");
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    public boolean getSwipeBackEnable() {
//        return super.getSwipeBackEnable();
        return true;
    }

    /**
     * 创建按钮显示
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_schedule_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 创建按钮监听
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        mCollect = menu.findItem(R.id.mScheduleEdit);
        mCollect.setTitle("确定");
        mCollect.setEnabled(true);
        return true;
    }

    /**
     * 创建按钮的点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String content = opinionEdit.getText().toString();
        switch (item.getItemId()) {
            case R.id.mScheduleEdit:
                if (StringUtils.isEmpty(content) && !isAgree) {
                    MyToast.showToast("内容不能为空");
                } else {
                    switch (flag) {
                        case Flag_ApplyForm_Next://同意并转审
                            mPresenter.postApprovalOpinion(this, approvalID, Flag_State_1 + "", content, mApprovalId + "", mICustomListener);
                            break;
                        case Flag_ApplyForm_End://同意并结束
                            mPresenter.postApprovalOpinion(this, approvalID, Flag_State_1 + "", content, null, mICustomListener);
                            break;
                        case Flag_ApplyForm_Rejected://驳回
                            mPresenter.postApprovalOpinion(this, approvalID, Flag_State_2 + "", content, null, mICustomListener);
                            break;
                        case Flag_ApplyForm_Stop://终止
                            mPresenter.postApprovalOpinion(this, approvalID, Flag_State_3 + "", content, null, mICustomListener);
                            break;
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getMainLayout() {
        return R.layout.activity_opinion;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        //显示加载成功界面
        updateSuccessView();
    }

    @Override
    public void loadData() {

    }

    private void initView() {
        mPresenter = new ApprovalPresenter(this);
        Intent intent = getIntent();
        flag = intent.getIntExtra("flag", -1);
        approvalID = intent.getStringExtra("approvalID");
        isAgree = intent.getBooleanExtra("isAgree", false);
        if (flag == Flag_ApplyForm_Next) {
            getToolbarHelper().setTitle("审批意见-同意转审批");
            opinionLayout.setVisibility(View.VISIBLE);
        } else if (flag == Flag_ApplyForm_End) {
            getToolbarHelper().setTitle("审批意见-同意并结束");
        } else if (flag == Flag_ApplyForm_Rejected) {
            getToolbarHelper().setTitle("审批意见-驳回");
        } else if (flag == Flag_ApplyForm_Stop) {
            getToolbarHelper().setTitle("审批意见-终止");
        }
    }


    @OnClick({R.id.opinion_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.opinion_layout:
                Intent intent = new Intent(this, ContactsChoseActivity.class);
                intent.putExtra("type", 2);//0-查看 1-多选 2-单选
                startActivityForResult(intent, Flag_CallBack_Select);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Flag_CallBack_Select) {
            if (resultCode == RESULT_OK) {
                mApprovalId = data.getIntExtra("id", -1);
                mApprovalName = data.getStringExtra("name");
                opinionName.setText(mApprovalName);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 回调
     */
    private ICustomListener mICustomListener = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            switch (obj0) {
                case 0:
                    if (flag == Flag_ApplyForm_Next) {
                        MyToast.showToast("提交成功");
                    } else {
                        MyToast.showToast("审批完结");
                    }
                    sendMessageDelayed(100, 300);

                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case 1:
                    MyToast.showToast("修改失败");
                    break;
            }
        }
    };

}
