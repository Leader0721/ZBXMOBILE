package com.zbxn.activity.mission;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.zbxn.R;
import com.zbxn.activity.login.LoginActivity;
import com.zbxn.bean.MissionEntity;
import com.zbxn.http.BaseAsyncTask;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.data.ResultParse;
import com.zbxn.pub.data.base.BaseAsyncTaskInterface;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.MyToast;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import utils.PreferencesUtils;
import utils.StringUtils;

/**
 * 项目名称：发表评论
 * 创建人：LiangHanXin
 * 创建时间：2016/11/14 13:46
 */
public class CommentActivity extends AbsToolbarActivity {
    @BindView(R.id.mComment)
    EditText mComment;

    @Override
    public int getMainLayout() {
        return R.layout.activity_comment;
    }

    @Override
    public void loadData() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    private String TaskId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //显示加载成功界面
        updateSuccessView();

        TaskId = getIntent().getStringExtra("TaskId");

        initDate();

    }

    private void initDate() {
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("评论");// 定义上面的名字
        return super.onToolbarConfiguration(toolbar, params);
    }

    /**
     * 滑动关闭当前Activitiv
     */
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

        getMenuInflater().inflate(R.menu.menu_progress, menu);

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

        switch (item.getItemId()) {
            case R.id.mProgress:
                if (StringUtils.isEmpty(mComment)) {
                    MyToast.showToast("请输入内容");
                    break;
                } else {
                    comment(this, TaskId, StringUtils.getEditText(mComment));
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 发表评论
     *
     * @param context
     */
    public void comment(Context context, String TaskId, String TaskContent) {
//        progressDialog.show("加载中...");
        Map<String, String> map = new HashMap<String, String>();

        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
//        int CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
        int CurrentCompanyId = Integer.parseInt(PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, ""));
        map.put("tokenid", ssid);
        map.put("CurrentCompanyId", CurrentCompanyId + "");
        map.put("TaskId", TaskId);
        map.put("TaskContent", TaskContent);

        String netServer = ConfigUtils.getConfig(ConfigUtils.KEY.NetServer);
        new BaseAsyncTask(context, false, 0, netServer + "postTaskComment", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultParse<MissionEntity>().parse(json, MissionEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultParse mResult = (ResultParse) result;
                if ("0".equals(mResult.getSuccess())) {//0成功
                    MyToast.showToast("评论成功");
                    setResult(RESULT_OK, getIntent());
                    finish();
                } else {
                    String message = mResult.getMsg();
                    MyToast.showToast(message);
                }
            }

            @Override
            public void dataError(int funId) {
                MyToast.showToast("获取网络数据失败");
            }
        }).execute(map);
    }


}
