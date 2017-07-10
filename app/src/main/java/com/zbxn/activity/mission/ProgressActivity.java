package com.zbxn.activity.mission;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.zbxn.R;
import com.zbxn.activity.login.LoginActivity;
import com.zbxn.bean.MissionDetailEntity;
import com.zbxn.bean.MissionEntity;
import com.zbxn.http.BaseAsyncTask;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.data.ResultParse;
import com.zbxn.pub.data.base.BaseAsyncTaskInterface;
import com.zbxn.pub.dialog.MessageDialog;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.IsEmptyUtil;
import com.zbxn.pub.utils.MyToast;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import utils.PreferencesUtils;
import utils.StringUtils;
import utils.ValidationUtil;

/**
 * 项目名称：更新进度条
 * 创建人：LiangHanXin
 * 创建时间：2016/11/14 13:40
 */
public class ProgressActivity extends AbsToolbarActivity {
    /**
     * 输出标识java.util.List<{@link Contacts}>
     */
    public static final String Flag_Callback_ContactsPicker1 = "ontacts";
    @BindView(R.id.mProgress)
    EditText mProgresss;
    Intent data = new Intent();
    private MessageDialog mMessageDialog;

    private String progress;
    private int Progress;
    private String id;
    private int nowProgress;

    @Override
    public int getMainLayout() {
        return R.layout.activity_progress;
    }

    @Override
    public void loadData() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //显示加载成功界面
        initView();
        updateSuccessView();
    }

    /**
     * 接收任務id
     */
    private void initView() {
        id = getIntent().getStringExtra("id");
        nowProgress = getIntent().getIntExtra("progress", -1);
    }


    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("完成进度");// 定义上面的名字
        return super.onToolbarConfiguration(toolbar, params);
    }

    /**
     * 滑动关闭当前Activitiv
     */
    @Override
    public boolean getSwipeBackEnable() {
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
        getMenuInflater().inflate(R.menu.menu_establish, menu);
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
            case R.id.mEstablish:

                progress = StringUtils.getEditText(mProgresss);
                if (progress.length() > 4) {
                    MyToast.showToast("你输入的有误，请重新输入");
                    break;
                }

                if (ValidationUtil.isNumeric(progress)) {

                    if (!StringUtils.isEmpty(progress)) {
                        Progress = Integer.decode(progress);

                        if (IsEmptyUtil.isEmpty(mProgresss, "输入进度不能为空")) {

                            if (nowProgress != -1 && Progress > nowProgress) {
                                if (Progress == 100) {
                                    mMessageDialog = new MessageDialog(this);
                                    mMessageDialog.setTitle("任务提示");
                                    mMessageDialog.setMessage(getResources().getString(R.string.app_mission_tishi));
                                    mMessageDialog.setNegativeButton("取消");
                                    mMessageDialog.setPositiveButton("确认",
                                            new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
//                                                progress(ProgressActivity.this, id + "", 100 + "");
                                                    postTaskState("", 13 + "");
                                                }

                                            });
                                    mMessageDialog.show();
                                } else if (Progress < 100) {//进度小于100时
                                    progress(this, id, Progress + "");
                                } else if (Progress > 100) {
                                    MyToast.showToast("你输入的有误，请重新输入");
                                    break;
                                }
                            } else {
                                MyToast.showToast("不能小于现有进度");
                            }
                        } else {
                            MyToast.showToast("输入不能为空");
                        }
                    } else {
                        MyToast.showToast("输入不能为空");
                    }

                } else {
                    MyToast.showToast("请输入进度");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 提交进度
     *
     * @param context
     */
    public void progress(Context context, String TaskId, String TaskProgress) {
        Map<String, String> map = new HashMap<String, String>();

        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
        map.put("tokenid", ssid);
        map.put("CurrentCompanyId", CurrentCompanyId + "");
        map.put("TaskId", TaskId);
        map.put("TaskProgress", TaskProgress + "");//进度

        String netServer = ConfigUtils.getConfig(ConfigUtils.KEY.NetServer);
        new BaseAsyncTask(context, false, 0, netServer + "postTaskProgress", new BaseAsyncTaskInterface() {
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
                    MyToast.showToast("修改成功");
                    setResult(RESULT_OK);
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

    /**
     * 修改状态
     *
     * @param context
     * @param TaskState 状态
     */
    public void postTaskState(String context, String TaskState) {
        Map<String, String> map = new HashMap<String, String>();

        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
        map.put("tokenid", ssid);
        map.put("CurrentCompanyId", CurrentCompanyId);
        map.put("TaskId", id);
        map.put("Content", context);
        map.put("TaskState", TaskState);

        String netServer = ConfigUtils.getConfig(ConfigUtils.KEY.NetServer);
        new BaseAsyncTask(this, false, 0, netServer + "postTaskState", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultParse<MissionDetailEntity>().parse(json, MissionDetailEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultParse mResult = (ResultParse) result;
                if ("0".equals(mResult.getSuccess())) {//0成功
                    Intent intent = new Intent(getApplicationContext(), MissionListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
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
