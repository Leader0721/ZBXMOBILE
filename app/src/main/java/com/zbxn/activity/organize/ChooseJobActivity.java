package com.zbxn.activity.organize;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zbxn.R;
import com.zbxn.activity.login.LoginActivity;
import com.zbxn.activity.schedule.RemindNewTackPresenter;
import com.zbxn.bean.JobEntity;
import com.zbxn.bean.Member;
import com.zbxn.bean.RepeatNewTaskEntity;
import com.zbxn.bean.adapter.ChooseJobAdapter;
import com.zbxn.bean.adapter.RepeatNewTaskAdapter;
import com.zbxn.http.BaseAsyncTask;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.data.ResultData;
import com.zbxn.pub.data.ResultNetData;
import com.zbxn.pub.data.base.BaseAsyncTaskInterface;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.http.HttpCallBack;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.MyToast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import utils.PreferencesUtils;

/**
 *
 */
public class ChooseJobActivity extends AbsToolbarActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.mListView)
    ListView mListView;

    private List<JobEntity> mList;
    private ChooseJobAdapter mAdapter;

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("选择职位");// 定义上面的名字
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    public int getMainLayout() {
        return R.layout.activity_organize_choose_job;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        getJobData();
        //显示加载成功界面
        updateSuccessView();
    }

    @Override
    public void loadData() {

    }

    private void initView() {
        mList = new ArrayList<>();
        mAdapter = new ChooseJobAdapter(getApplicationContext(), mList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    /**
     * 滑动关闭当前activity
     */
    @Override
    public boolean getSwipeBackEnable() {
        return true;
    }

    //列表的点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        view.findViewById(R.id.mImageview).setVisibility(View.VISIBLE);
        String positionName = mList.get(position).getPositionName();
        String positionId = mList.get(position).getPositionID() + "";
        Intent data = new Intent();
        data.putExtra("positionName", positionName);
        data.putExtra("positionId", positionId);
        setResult(RESULT_OK, data);
        finish();
    }

    /**
     * 获取当前公司职位列表
     */
    public void getJobData() {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID);
        Map<String, String> map = new HashMap<>();
        map.put("tokenid", ssid);
        map.put("CurrentCompanyId", currentCompanyId);
        String netServer = ConfigUtils.getConfig(ConfigUtils.KEY.NetServer_ACTION);
        new BaseAsyncTask(this, false, 0, netServer + "Position/getPositionList", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultData<JobEntity>().parse(json, JobEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultData mResult = (ResultData) result;
                if (mResult.getSuccess().equals("0")) {
                    List<JobEntity> list1 = mResult.getRows();
                    mList.clear();
                    mList.addAll(list1);
                    mAdapter.notifyDataSetChanged();
                } else {
                    MyToast.showToast(mResult.getMsg());
                }
            }

            @Override
            public void dataError(int funId) {
                MyToast.showToast("获取网络数据失败");
            }
        }).execute(map);
    }
}
