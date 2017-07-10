package com.zbxn.activity.main.tools.tools;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.umeng.analytics.MobclickAgent;
import com.zbxn.R;
import com.zbxn.activity.examinationandapproval.ApprovalPresenter;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.JsonUtil;
import com.zbxn.pub.utils.MyToast;
import com.zbxn.widget.MyGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import utils.ScreenUtils;
import utils.StringUtils;

/**
 * 项目名称：预告功能的展示
 * 创建人：LiangHanXin
 * 创建时间：2016/10/17 14:12
 */
public class TrailerActivity extends AbsToolbarActivity implements AdapterView.OnItemClickListener {

    private ToolsAdapter mToolsAdapter;
    private ApprovalPresenter mToolsPresenter;

    private List<RecTool> listTemp = new ArrayList<>();
    @BindView(R.id.mGridView)
    MyGridView mGridView;
    //    private String toolsList = "[{\"id\":3,\"title\":\"N币\",\"menuid\":113,\"img\":\"temp113\",\"isvisible\":true},{\"id\":5,\"title\":\"会议\",\"menuid\":114,\"img\":\"temp114\",\"isvisible\":true},{\"id\":8,\"title\":\"文件\",\"menuid\":118,\"img\":\"temp118\",\"isvisible\":true},{\"id\":9,\"title\":\"任务\",\"menuid\":119,\"img\":\"temp119\",\"isvisible\":true},{\"id\":11,\"title\":\"客户\",\"menuid\":121,\"img\":\"temp121\",\"isvisible\":true},{\"id\":12,\"title\":\"车辆\",\"menuid\":122,\"img\":\"temp122\",\"isvisible\":true},{\"id\":13,\"title\":\"物资\",\"menuid\":123,\"img\":\"temp123\",\"isvisible\":true},{\"id\":14,\"title\":\"公司制度\",\"menuid\":124,\"img\":\"temp124\",\"isvisible\":true},{\"id\":15,\"title\":\"反馈\",\"menuid\":125,\"img\":\"temp125\",\"isvisible\":true}]";
    private String toolsList = "[{\"id\":5,\"title\":\"会议\",\"\":114,\"img\":\"temp114\",\"isvisible\":true},{\"id\":8,\"title\":\"文件\",\"menuid\":118,\"img\":\"temp118\",\"isvisible\":true},{\"id\":11,\"title\":\"客户\",\"menuid\":121,\"img\":\"temp121\",\"isvisible\":true},{\"id\":12,\"title\":\"车辆\",\"menuid\":122,\"img\":\"temp122\",\"isvisible\":true},{\"id\":13,\"title\":\"物资\",\"menuid\":123,\"img\":\"temp123\",\"isvisible\":true},{\"id\":14,\"title\":\"公司制度\",\"menuid\":124,\"img\":\"temp124\",\"isvisible\":true}]";

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("预告");
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("TrailerActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("TrailerActivity");
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean getSwipeBackEnable() {
//        return super.getSwipeBackEnable();
        return true;
    }

    @Override
    public int getMainLayout() {
        return R.layout.activity_trailer;
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

    public void setData(List<RecTool> list) {
        int size = list.size() + 1;
        int line = size / 3;//一共有几行
        if (size % 3 > 0)
            line += 1;

        int height = ScreenUtils.dipToPx(getApplicationContext(), line * 96);
        ViewGroup.LayoutParams params = mGridView.getLayoutParams();
        params.height = height;
        mGridView.setLayoutParams(params);

        mToolsAdapter.resetData(list);

    }

    private void initView() {
        mToolsAdapter = new ToolsAdapter(this, null);
        mGridView.setAdapter(mToolsAdapter);
        mGridView.setOnItemClickListener(this);
        mGridView.setFocusable(false);
        mToolsPresenter = new ApprovalPresenter(this);
//        mToolsPresenter.getApprovalByAuthor(this, mICustomListener);

        try {
            listTemp = JsonUtil.fromJsonList(toolsList, RecTool.class);
            if (!StringUtils.isEmpty(listTemp)) {
                setData(listTemp);
            }
        } catch (Exception e) {

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MyToast.showToast("敬请期待...");
    }

}
