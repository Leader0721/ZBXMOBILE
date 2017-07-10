package com.zbxn.activity.schedule;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.zbxn.R;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;

/**
 * 项目名称：是否重复界面
 * 创建人：LiangHanXin
 * 创建时间：2016/9/21 15:28
 */
public class RepeatNewTaskActivity extends AbsToolbarActivity {
    /**
     * 输出标识java.util.List<{@link Contacts}>
     */
    public static final String Flag_Output_Checked = "ontacts1";


    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public int getMainLayout() {
        return R.layout.activity_schedule_repeal;
    }
    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("重复");// 定义上面的名字
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //显示加载成功界面
        updateSuccessView();
    }

    @Override
    public void loadData() {

    }

    /**
     * 滑动关闭当前Activitiv
     */
    @Override
    public boolean getSwipeBackEnable() {
//        return super.getSwipeBackEnable();
        return true;
    }

}
