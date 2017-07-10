package com.zbxn.activity.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zbxn.bean.GrievanceTypeEntity;
import com.zbxn.listener.ICustomListener;
import com.zbxn.R;
import com.zbxn.bean.adapter.GrievanceAdapter;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;

import java.util.List;

import butterknife.BindView;

/**
 * 考勤申诉
 * Created by Administrator on 2016/9/30.
 */
public class GrievanceTypeActivity extends AbsToolbarActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.mListView)
    ListView mListView;

    private GrievanceTypePresenter mPresenter;
    private GrievanceAdapter mAdapter;
    private List<GrievanceTypeEntity> mList;

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("申诉类型");// 定义上面的名字
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    public int getMainLayout() {
        return R.layout.activity_schedule_repeal;
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
        mPresenter = new GrievanceTypePresenter(this);
        mPresenter.getAppealType(this, mICustomListener);
        mListView.setOnItemClickListener(this);
    }

    private ICustomListener mICustomListener = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            switch (obj0) {
                case 0:
                    mList = (List<GrievanceTypeEntity>) obj1;
                    if (mList != null) {
                        mAdapter = new GrievanceAdapter(getApplicationContext(), mList);
                        mListView.setAdapter(mAdapter);
                    }
                    break;
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent data = new Intent();
        view.findViewById(R.id.mImageview).setVisibility(View.VISIBLE);
        String typeName = mList.get(position).getAppealTypeName();
        String appealType = mList.get(position).getAppealtype() + "";
        data.putExtra("typeName", typeName);
        data.putExtra("appealType", appealType);
        setResult(RESULT_OK, data);
        finish();
    }
}
