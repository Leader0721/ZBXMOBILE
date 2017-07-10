package com.zbxn.activity.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zbxn.R;
import com.zbxn.bean.RepeatNewTaskEntity;
import com.zbxn.bean.adapter.RepeatNewTaskAdapter;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;

/**
 * 项目名称：提醒页面
 * 创建人：LiangHanXin
 * 创建时间：2016/9/21 15:57
 */
public class RemindNewTaskActivity extends AbsToolbarActivity implements AdapterView.OnItemClickListener {
    /**
     * 输出标识java.util.List<{@link Contacts}>
     */
    public static final String Flag_Output_Checked = "ontacts";


    @BindView(R.id.mListView)
    ListView mListView;

    Intent data = new Intent();
    private Calendar now;
    private List<RepeatNewTaskEntity> list;
    private RepeatNewTaskAdapter mAdapter;
    private RemindNewTackPresenter mPresenter;


    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("提醒");// 定义上面的名字
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
        mPresenter = new RemindNewTackPresenter(this);
        list = new ArrayList<>();
        mPresenter.save(this, mICustomListener);
        mListView.setOnItemClickListener(this);
    }

    /**
     * 滑动关闭当前activity
     */
    @Override
    public boolean getSwipeBackEnable() {
//        return super.getSwipeBackEnable();
        return true;
    }


    private ICustomListener mICustomListener = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            switch (obj0) {
                case 0:
                    List<RepeatNewTaskEntity> list1 = (List<RepeatNewTaskEntity>) obj1;
                    list.clear();
                    list.addAll(list1);
                    if (list != null && list.size() != 0) {
                        mAdapter = new RepeatNewTaskAdapter(getApplicationContext(), list);
                        mListView.setAdapter(mAdapter);
                    }
                    break;
            }
        }
    };

    //列表的点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        view.findViewById(R.id.mImageview).setVisibility(View.VISIBLE);
        String typeName = list.get(position).getTypeName();
        String precedeType = list.get(position).getPrecedeType() + "";
        data.putExtra("typeName", typeName);
        data.putExtra("precedeType", precedeType);
        setResult(RESULT_OK, data);
        finish();
    }

}
