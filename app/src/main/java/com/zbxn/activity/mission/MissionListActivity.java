package com.zbxn.activity.mission;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zbxn.R;
import com.zbxn.activity.login.LoginActivity;
import com.zbxn.bean.ApprovalEntity;
import com.zbxn.bean.MissionDetailEntity;
import com.zbxn.bean.MissionEntity;
import com.zbxn.bean.adapter.MissionAdapter;
import com.zbxn.bean.adapter.TaskDetailsAdpter;
import com.zbxn.http.BaseAsyncTask;
import com.zbxn.popupwindow.PopupWindowType;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.data.ResultNetData;
import com.zbxn.pub.data.ResultParse;
import com.zbxn.pub.data.base.BaseAsyncTaskInterface;
import com.zbxn.pub.dialog.InputDialog;
import com.zbxn.pub.dialog.MessageDialog;
import com.zbxn.pub.dialog.ProgressDialog;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.MyToast;
import com.zbxn.widget.SearchEditTextView;
import com.zbxn.widget.pulltorefreshlv.PullRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import utils.PreferencesUtils;
import utils.StringUtils;

/**
 * @author: ysj
 * @date: 2016-11-09 09:58
 */
public class MissionListActivity extends AbsToolbarActivity implements AdapterView.OnItemClickListener, MissionAdapter.ItemCallBack {

    private static final int Misson_CallBack_Creat = 1001;
    private static final int Misson_CallBack_Detail = 1002;
    private static final int Misson_CallBack_Search = 1003;

    @BindView(R.id.search_edit)
    SearchEditTextView searchEdit;
    @BindView(R.id.mLeft)
    TextView mLeft;
    @BindView(R.id.layout_left)
    RelativeLayout layoutLeft;
    @BindView(R.id.mRight)
    TextView mRight;
    @BindView(R.id.layout_right)
    RelativeLayout layoutRight;
    @BindView(R.id.mListView)
    PullRefreshListView mListView;

    private List<ApprovalEntity> listLeft; //任务类型
    private List<ApprovalEntity> listRight; // 任务状态

    private int pageSize = 10; //每页条数

    private MenuItem mCollect;
    private MenuItem mSearch;
    private MissionAdapter mAdapter;
    private List<MissionEntity> mList;
    private int mIndex = 1; //记录页数
    private int leftState = 0; //记录任务类型
    private int rightState = -1; // 记录任务状态
    private int position;
    private String mTaskState;
    private String searchText = ""; // 记录搜索关键词
    private TextView mComment;
    private int mState; // 主任务进度
    private String mTaskId; // 任务id
    private int mStatePerson; // 执行人任务进度
    private ProgressDialog mProgressDialog;
    private MessageDialog messageDialog;
    private InputDialog mDialog;
    private List<MissionDetailEntity> missionList;
    private boolean isChildListNull;

    @Override
    public int getMainLayout() {
        return R.layout.activity_misson_list;
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("任务");
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateSuccessView();
        initListType();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MissionListActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MissionListActivity");
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean getSwipeBackEnable() {
//        return super.getSwipeBackEnable();
        return true;
    }

    private void initListType() {
        listLeft = new ArrayList<>();
        listLeft.add(new ApprovalEntity(0, "全部"));
        listLeft.add(new ApprovalEntity(1, "我创建的"));
        listLeft.add(new ApprovalEntity(2, "我执行的"));
//        listLeft.add(new ApprovalEntity(3, "抄送我的"));
        listLeft.add(new ApprovalEntity(4, "我负责的"));
        listLeft.add(new ApprovalEntity(5, "我审核的"));

        listRight = new ArrayList<>();
        listRight.add(new ApprovalEntity(-1, "全部"));
        listRight.add(new ApprovalEntity(0, "待接受"));
        listRight.add(new ApprovalEntity(1, "进行中"));
        listRight.add(new ApprovalEntity(2, "待审核"));
        listRight.add(new ApprovalEntity(3, "已完成"));
        listRight.add(new ApprovalEntity(4, "已拒绝"));
        listRight.add(new ApprovalEntity(5, "已驳回"));
        listRight.add(new ApprovalEntity(7, "已延期"));
    }

    private void initView() {
        mList = new ArrayList<>();
        mAdapter = new MissionAdapter(this, mList, this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.startFirst();
        setRefresh();
        mListView.setOnPullListener(new PullRefreshListView.OnPullListener() {
            @Override
            public void onRefresh() {
                setRefresh();
            }

            @Override
            public void onLoad() {
                getListData();
            }
        });

        //点击搜索按钮
        searchEdit.setOnSearchClickListener(new SearchEditTextView.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view) {
                searchText = searchEdit.getText().toString();
                if (!StringUtils.isEmpty(searchText)) {
                    mListView.startFirst();
                    setRefresh();
                }
            }
        });

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在提交");
        mProgressDialog.setCancelable(false);
    }

    /**
     * 创建按钮显示
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_schedule_create, menu);
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
        mCollect = menu.findItem(R.id.schedule_creat);
        mCollect.setTitle("新建任务");
        mSearch = menu.findItem(R.id.mission_search);
        mSearch.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.schedule_creat: // 新建任务
                Intent intent = new Intent(this, MissionActivity.class);
//                startActivity(intent);
                startActivityForResult(intent, Misson_CallBack_Creat);
                break;
            case R.id.mission_search: // 搜索
                Intent searchIntent = new Intent(this, SearchActivity.class);
                startActivityForResult(searchIntent, Misson_CallBack_Search);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.layout_left, R.id.layout_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_left:
                setPopupWindow(mLeft, mLeft.getWidth(), listLeft, "类型", 0, mLeft);
                break;
            case R.id.layout_right:
                setPopupWindow(mRight, mRight.getWidth(), listRight, "状态", 1, mLeft);
                break;
        }
    }


    //类型item的点击事件
    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (id == 0) {
                ApprovalEntity entity = listLeft.get(position);
                mLeft.setText(entity.getName());
                leftState = entity.getTypeid();
                if (leftState == 0) {
                    mLeft.setText("类型");
                }
                mListView.startFirst();
                setRefresh();
            } else if (id == 1) {
                ApprovalEntity entity = listRight.get(position);
                mRight.setText(entity.getName());
                rightState = entity.getTypeid();
                if (rightState == -1) {
                    mRight.setText("状态");
                }
                mListView.startFirst();
                setRefresh();
            }
        }
    };

    @Override
    public void loadData() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mList != null) {
            Intent intent = new Intent(this, TaskDetailsActivity.class);
            String missionID = mList.get(position).getID();
            intent.putExtra("id", missionID);
            startActivityForResult(intent, Misson_CallBack_Detail);
            mComment = ((TextView) view.findViewById(R.id.mComment));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Misson_CallBack_Creat) {//创建回调
            if (resultCode == RESULT_OK) {
                setRefresh();
            }
        }
        if (requestCode == Misson_CallBack_Detail) {//详情回调
            if (resultCode == RESULT_OK) {
                setRefresh();
            }
        }
        if (requestCode == Misson_CallBack_Search) {//搜索回调
            if (resultCode == RESULT_OK) {
                searchText = data.getStringExtra("content");
                mListView.startFirst();
                setRefresh();
                searchText = "";
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                v.clearFocus();
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 刷新
     */
    public void setRefresh() {
        mIndex = 1;
        getListData();
    }

    /**
     * 加载更多
     */
    public void getListData() {
        getTest(this, "0", searchText, leftState + "", rightState);
    }


    //设置PopupWindow
    public void setPopupWindow(View view, float width, List<ApprovalEntity> list, String type, long flag, View location) {
        PopupWindowType mpopupWindowMeetingType = new PopupWindowType(this, view, width, listener, list, type, flag);
        ColorDrawable dw1 = new ColorDrawable(0xffffffff);
        //设置SelectPicPopupWindow弹出窗体的背景
        mpopupWindowMeetingType.setBackgroundDrawable(dw1);
        mpopupWindowMeetingType.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {

            }
        });
        //设置layout在PopupWindow中显示的位置
        mpopupWindowMeetingType.showAsDropDown(location, 1, 1);
    }

    public void getTest(Context context, String ParentID, String Keyword, String StateType, int TaskState) {
        //请求网络
        Map<String, String> map = new HashMap<String, String>();
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        String task;
        if (TaskState == -1) {
            task = "";
        } else {
            task = TaskState + "";
        }
        map.put("TokenId", ssid);
        map.put("ParentID", "");
        map.put("Keyword", Keyword);
        map.put("StateType", StateType);
        map.put("TaskState", task);
        map.put("PageIndex", mIndex + "");
        map.put("PageSize", pageSize + "");
        String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
        map.put("CurrentCompanyId", CurrentCompanyId);

        String netServer = ConfigUtils.getConfig(ConfigUtils.KEY.NetServer);
        new BaseAsyncTask(context, false, 0, netServer + "gettaskList", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultNetData<MissionEntity>().parse(json, MissionEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultNetData mResult = (ResultNetData) result;
                if ("0".equals(mResult.getSuccess())) {//0成功
                    mCollect.setVisible(true);
                    List<MissionEntity> list = mResult.getData();
                    if (mIndex == 1) {
                        mList.clear();
                    }
                    mIndex++;
                    mList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                    mListView.onRefreshFinish();
                    setMore(list);
                } else {
                    mCollect.setVisible(false);
                    String message = mResult.getMsg();
                    MyToast.showToast(message);
                    mListView.onRefreshFinish();
                }
            }

            @Override
            public void dataError(int funId) {
                MyToast.showToast("获取网络数据失败");
                mListView.onRefreshFinish();
            }
        }).execute(map);
    }

    /**
     * 显示加载更多
     *
     * @param mResult
     */
    private void setMore(List mResult) {
        if (mResult == null) {
            mListView.setHasMoreData(true);
            return;
        }
        int pageTotal = mResult.size();
        if (pageTotal >= pageSize) {
            mListView.setHasMoreData(true);
            mListView.setPullLoadEnabled(true);
        } else {
            mListView.setHasMoreData(false);
            mListView.setPullLoadEnabled(false);
        }
    }

    @Override
    public void onTextViewClick(View view, final int position, int taskState, int taskPersonState, boolean isType) {
        mState = taskState;
        mTaskId = mList.get(position).getID();
        mStatePerson = taskPersonState;
        this.position = position;

        switch (view.getId()) {
            case R.id.mComment://评论
                Intent intent = new Intent(this, CommentActivity.class);
                intent.putExtra("TaskId", mList.get(position).getID());
                startActivity(intent);
                break;
            case R.id.mAccept://接收 or 同意
                mProgressDialog.show();
                if (isType) {//同意
                    postTaskState("", 3 + "");
                } else {//接受
                    postTaskState("", 11 + "");
                }
                break;
            case R.id.mComplete://已完成 or 提交审核

                if (isType) {//提交审核
                    if (mList.get(position).getDoneProgress() < 100) {
                        MyToast.showToast("进度还未到达100%");
                    } else {
                        mProgressDialog.show();
                        postTaskState("", 2 + "");
                    }
                } else {//已完成
                    messageDialog = new MessageDialog(this);
                    messageDialog.setTitle("任务提示");
                    messageDialog.setMessage("确认任务是否已完成？");
                    messageDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            getChildMissionList();
                            
                        }
                    });
                    messageDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    messageDialog.show();
                }
                break;
            case R.id.mRefuse://拒绝 or 驳回
                if (isType) {//拒绝
                    mDialog = new InputDialog(this);
                    mDialog.setTitle("拒绝理由");
                    mDialog.setContentHint("请输入拒绝理由");
                    mDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String content = mDialog.getEditText().getText().toString();
                            if (!StringUtils.isEmpty(content.trim())) {
                                if (mState == 0) {
                                    if (mStatePerson == 10) {
                                        mProgressDialog.show();
                                        postTaskState(content, 14 + "");
                                    }
                                } else {
                                    mProgressDialog.show();
                                    postTaskState(content, 4 + "");
                                }
                            } else {
                                MyToast.showToast("拒绝理由不能为空");
                            }
                        }
                    });
                    mDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    mDialog.show();
                } else {//驳回
                    mDialog = new InputDialog(this);
                    mDialog.setTitle("驳回意见");
                    mDialog.setContentHint("请输入驳回意见");
                    mDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String content = mDialog.getEditText().getText().toString();
                            if (!StringUtils.isEmpty(content.trim())) {
                                mProgressDialog.show();
                                postTaskState(content, 1 + "");
                            } else {
                                MyToast.showToast("驳回意见不能为空");
                            }
                        }
                    });
                    mDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    mDialog.show();
                }
                break;
        }
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
        map.put("TaskId", mTaskId);
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
                    MyToast.showToast("提交成功");
                    mListView.startFirst();
                    setRefresh();
                } else {
                    String message = mResult.getMsg();
                    MyToast.showToast(message);
                }
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }

            @Override
            public void dataError(int funId) {
                MyToast.showToast("获取网络数据失败");
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
        }).execute(map);
    }

    /**
     * 提交进度
     *
     * @param context
     */
    public void postProgress(Context context, String TaskId, String TaskProgress) {
        Map<String, String> map = new HashMap<String, String>();

        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
        map.put("tokenid", ssid);
        map.put("CurrentCompanyId", CurrentCompanyId + "");
        map.put("TaskId", TaskId);
        map.put("TaskProgress", TaskProgress);//进度

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
                    postTaskState("", 13 + "");
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
     * 获取子任务列表
     */
    private void getChildMissionList() {
        //子任务列表
        missionList = new ArrayList<>();
        //获取网络数据,子任务列表信息
        Map<String, String> map = new HashMap<String, String>();
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("TokenId", ssid);
        map.put("ParentID", mTaskId);
        map.put("Keyword", "");
        map.put("StateType", "0");
        map.put("TaskState", "");
        map.put("PageIndex", 1 + "");
        map.put("PageSize", 100 + "");
        String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
        map.put("CurrentCompanyId", CurrentCompanyId);

        String netServer = ConfigUtils.getConfig(ConfigUtils.KEY.NetServer);
        new BaseAsyncTask(this, false, 0, netServer + "gettaskList", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultNetData<MissionDetailEntity>().parse(json, MissionDetailEntity.class);
            }

            /**
             * @param funId
             * @param result
             */
            @Override
            public void dataSuccess(int funId, Object result) {
                ResultNetData mResult = (ResultNetData) result;
                if ("0".equals(mResult.getSuccess())) {//0成功

                    List<MissionDetailEntity> list = mResult.getData();
                    if (list.size() == 0) {
                        mProgressDialog.show();
                        postTaskState("", 13 + "");
                    } else {
                        if (mList.get(position).getDoneProgress() < 100) {
                            MyToast.showToast("进度还未到达100%");
                        } else {
                            mProgressDialog.show();
                            postTaskState("", 13 + "");
                        }
                    }
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
