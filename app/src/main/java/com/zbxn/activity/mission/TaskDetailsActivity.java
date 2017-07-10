package com.zbxn.activity.mission;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.activity.login.LoginActivity;
import com.zbxn.bean.CommendListEntity;
import com.zbxn.bean.MissionDetailEntity;
import com.zbxn.bean.MissionEntity;
import com.zbxn.bean.adapter.CommendListViewAdapter;
import com.zbxn.bean.adapter.TaskDetailsAdpter;
import com.zbxn.fragment.CommentFragment;
import com.zbxn.http.BaseAsyncTask;
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
import com.zbxn.pub.widget.NoScrollListview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import utils.PreferencesUtils;
import utils.StringUtils;

/**
 * 项目名称：任务详情
 * 创建人：LiangHanXin
 * 创建时间：2016/11/9 12:01
 */
public class TaskDetailsActivity extends AbsToolbarActivity implements CommentFragment.CallBackComment, TaskDetailsAdpter.DeleteChildCallback {
    private static final int Flag_Callback_ContactsPicker1 = 1002;//进度
    //修改
    private static final int Flag_Callback_MissionUpdateActivity = 1002;
    //打开评论页
    private static final int REQUEST_COMMENT_ACTIVITY = 1003;
    //打开新建子任务
    private static final int REQUEST_MISSION_ACTIVITY = 1004;
    //子任务
    private static final int REQUEST_MISSION_CHILDDETAIL = 1005;

    //    @BindView(R.id.mPortrait)
//    CircleImageView mPortrait;
//    @BindView(R.id.mGrade)
//    TextView mGrade;
    @BindView(R.id.mTime)
    TextView mTime;
    @BindView(R.id.firstBar)
    ProgressBar firstBar;
    @BindView(R.id.mPercentage)
    TextView mPercentage;
    @BindView(R.id.mState)
    TextView mState;
    //    @BindView(R.id.mHead)
//    LinearLayout mHead;
    @BindView(R.id.mTitle)
    TextView mTitle;
    @BindView(R.id.mContent)
    TextView mContent;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.ll_time)
    LinearLayout llTime;
    @BindView(R.id.et_work_hours)
    TextView etWorkHours;
    @BindView(R.id.ll_work_hours)
    LinearLayout llWorkHours;
    @BindView(R.id.mEject)
    LinearLayout mEject;
    @BindView(R.id.tv_create_people)
    TextView tvCreatePeople;
    @BindView(R.id.tv_charge_people)
    TextView tvChargePeople;
    @BindView(R.id.ll_charge_people)
    LinearLayout llChargePeople;
    @BindView(R.id.tv_executor_people)
    TextView tvExecutorPeople;
    @BindView(R.id.ll_executor_people)
    LinearLayout llExecutorPeople;
    @BindView(R.id.tv_checker_people)
    TextView tvCheckerPeople;
    @BindView(R.id.ll_checker_people)
    LinearLayout llCheckerPeople;
    @BindView(R.id.tv_copy_people)
    TextView tvCopyPeople;
    @BindView(R.id.ll_copy_people)
    LinearLayout llCopyPeople;
    @BindView(R.id.mDisplay)
    LinearLayout mDisplay;
    @BindView(R.id.tv_difficulty)
    TextView tvDifficulty;
    @BindView(R.id.ll_difficulty)
    LinearLayout llDifficulty;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.ll_level)
    LinearLayout llLevel;
    @BindView(R.id.mission_listview)
    NoScrollListview mission_listview;
    @BindView(R.id.mComment)
    TextView mComment;
    @BindView(R.id.mListViewP)
    NoScrollListview mListViewP;
    @BindView(R.id.mProgress)
    LinearLayout mProgress;
    @BindView(R.id.mSubTask)
    LinearLayout mSubTask;
    @BindView(R.id.mAccept)
    TextView mAccept;
    @BindView(R.id.mComplete)
    TextView mComplete;
    @BindView(R.id.mRefuse)
    TextView mRefuse;
    @BindView(R.id.mission_scrollView)
    ScrollView missionScrollView;
    @BindView(R.id.ll_attachment)
    RelativeLayout attachment;
    private int minPaddingTop;
    private boolean isExtend = false;//是否是伸展 ，默认是收缩的

    private String ID;//任务ID
    private String leadId;//负责人id
    private String executeIds;//执行人id（多个）
    private String checkId;//审核人id
    private String sendIds;//抄送人id(多个)
    private String creatId;//创建人id
    private String mTaskId;//当前任务ID
    private int mPlan;//记录任务进度
    private int taskState;//记录主任务状态
    private int taskPersonState;//记录个人任务状态

    //按钮标识
    private boolean isAgree;//true:同意 false:通过
    private boolean isStop;//true:拒绝 false:驳回
    private boolean isCommit;//true:提交审核 false:已完成
    private boolean isTrunDownPerson;//是否为监督人
    private boolean isChildTask;//是否是子任务
    private int types;

    private InputDialog mDialog;
    private MessageDialog messageDialog;
    private ProgressDialog progressDialog;

    //任务详情
    private MissionDetailEntity entity;

    //子任务列表
    private List<MissionDetailEntity> missionList;
    private TaskDetailsAdpter taskDetailsAdpter;
    //评论列表
    private List<CommendListEntity> commendList;
    private CommendListViewAdapter commendListViewAdapter;

    private String loginId;
    private String chargePeopleName;
    private String checkerPeopleName;
    private String chargePeopleIds;
    private String checkerPeopleId;
    private String currentCompanyId;

    private MenuItem mUpdate;
    private MenuItem mDelete;
    private MenuItem menuItem;
    private MenuItem mCreatChild;

    @Override
    public int getMainLayout() {
        return R.layout.activity_taskdetails;
    }

    @Override
    public void loadData() {
        //空实现
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        //获取任务详情
        taskdetails(this);
    }

    private void initData() {
        loginId = PreferencesUtils.getString(this, LoginActivity.FLAG_INPUT_ID);
        Intent intent = getIntent();
        ID = intent.getStringExtra("id");
        types = intent.getIntExtra("types", 0);
        if (types == 1) {
            isChildTask = true;
        } else {
            isChildTask = false;
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在提交");
        progressDialog.setCancelable(false);
        mListViewP.setFocusable(false);
        missionScrollView.smoothScrollTo(0, 20);
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("任务详情");// 定义上面的名字
        return super.onToolbarConfiguration(toolbar, params);
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
        mUpdate = menu.findItem(R.id.mission_update);
        mDelete = menu.findItem(R.id.mission_delete);
        menuItem = menu.findItem(R.id.menu_mission);
        mCreatChild = menu.findItem(R.id.schedule_creat);

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
            case R.id.schedule_creat://新建子任务
                addNewMission();
                break;
            case R.id.mission_update://编辑
                Intent intent = new Intent(this, MissionUpdateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("mission", entity);
                intent.putExtra("type", 1);
                intent.putExtra("detail", bundle);
                startActivityForResult(intent, Flag_Callback_MissionUpdateActivity);
                break;
            case R.id.mission_delete://删除

                MessageDialog dialog = new MessageDialog(this);
                dialog.setTitle("提示");
                dialog.setMessage("确认删除?");
                dialog.setNegativeButton("取消");
                dialog.setPositiveButton("删除",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (isChildTask) {
                                    deleteTask(TaskDetailsActivity.this, mTaskId, currentCompanyId, 3);
                                } else {
                                    deleteTask(TaskDetailsActivity.this, mTaskId, currentCompanyId, 1);
                                }
                            }

                        });
                dialog.show();


                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 滑动关闭当前Activitiv
     */
    @Override
    public boolean getSwipeBackEnable() {
        return true;
    }


    @Override
    public void sendComment(String name, int commentId, int replyToId) {
    }

    /**
     * 回调需要接收的人员
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Flag_Callback_ContactsPicker1) {//
            if (resultCode == RESULT_OK) {
                taskdetails(this);
            }
        } else if (requestCode == REQUEST_COMMENT_ACTIVITY) { //评论
            if (resultCode == RESULT_OK) {
                MyToast.showToast("评论成功");
                getCommentList();
            }
        } else if (requestCode == REQUEST_MISSION_ACTIVITY) { //新建子任务
            if (resultCode == RESULT_OK) {
//                MyToast.showToast("新建子任务成功");
                getChildMissionList();
            }
        } else if (requestCode == REQUEST_MISSION_CHILDDETAIL) { // 子任务
            if (resultCode == RESULT_OK) {
                taskdetails(this);
            }
        } else if (requestCode == Flag_Callback_MissionUpdateActivity) { // 修改任务
            if (resultCode == RESULT_OK) {
                taskdetails(this);
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }


    @OnClick({R.id.mEject, R.id.mProgress, R.id.mAccept, R.id.mComplete, R.id.mRefuse, R.id.mComment,
            R.id.ll_attachment})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mEject:
                PaddingTopAnim anim = null;
                if (isExtend) {
                    //执行收缩动画
                    anim = new PaddingTopAnim(mDisplay, 0, minPaddingTop);
                } else {
                    //执行伸展动画
                    anim = new PaddingTopAnim(mDisplay, minPaddingTop, 0);
                }
                anim.start(350);

                //将标记置为反值
                isExtend = !isExtend;
                break;
            case R.id.mProgress://更新进度
                Intent intent = new Intent(TaskDetailsActivity.this, ProgressActivity.class);
                intent.putExtra("id", ID);
                intent.putExtra("progress", mPlan);
                startActivityForResult(intent, Flag_Callback_ContactsPicker1);
                break;
            case R.id.mAccept:
                progressDialog.show();
                if (isAgree) { //同意(审核通过)
                    postTaskState("", 3 + "");
                } else { //接受
                    postTaskState("", 11 + "");
                }
                break;
            case R.id.mComplete:
                if (isCommit) { //提交审核
                    if (isLeadPerson() && isExecutePerson() && isCheckPerson() && isExecuteOne()) {
                        progressDialog.show();
                        postTaskState("", 2 + "");
                    } else {
                        if (mPlan < 100) {
                            MyToast.showToast("进度还未到达100%");
                        } else {
                            progressDialog.show();
                            postTaskState("", 2 + "");
                        }
                    }
                } else {//已完成
                    messageDialog = new MessageDialog(this);
                    messageDialog.setTitle("任务提示");
                    messageDialog.setMessage("确认任务是否已完成？");
                    messageDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (isChildListNull()) {
                                progressDialog.show();
                                postTaskState("", 13 + "");
                            } else {
                                if (mPlan < 100) {
                                    MyToast.showToast("进度还未到达100%");
                                } else {
                                    progressDialog.show();
                                    postTaskState("", 13 + "");
                                }
                            }
                        }
                    });
                    messageDialog.setNegativeButton("取消");
                    messageDialog.show();
                }
                break;
            case R.id.mRefuse:
                if (isStop) {//拒绝
                    mDialog = new InputDialog(this);
                    mDialog.setTitle("拒绝理由");
                    mDialog.setContentHint("请输入拒绝理由");
                    mDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String content = mDialog.getEditText().getText().toString();
                            if (!StringUtils.isEmpty(content.trim())) {
                                progressDialog.show();
                                if (taskState == 0) {
                                    if (taskPersonState == 10) {
                                        postTaskState(content, 14 + "");
                                    }
                                } else {
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
                                progressDialog.show();
                                if (taskState == 3) {
                                    if (isTrunDownPerson) {
                                        postTaskState(content, 5 + "");
                                    }
                                } else {
                                    postTaskState(content, 1 + "");
                                }
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
            case R.id.mComment://评论
                Intent intents = new Intent(TaskDetailsActivity.this, CommentActivity.class);
                intents.putExtra("TaskId", ID);
                TaskDetailsActivity.this.startActivityForResult(intents, REQUEST_COMMENT_ACTIVITY);
                break;
            case R.id.ll_attachment: //查看附件
                if (null == entity && StringUtils.isEmpty(entity.getAttachmentList())) {
                    MyToast.showToast("暂无附件");
                    break;
                }
                Intent intent2 = new Intent(TaskDetailsActivity.this, AttachmentActivity.class);
                intent2.putExtra("list", entity.getAttachmentList());
                TaskDetailsActivity.this.startActivity(intent2);
                break;
        }


    }

    private void anim() {
        mDisplay.measure(0, 0);
        mDisplay.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mDisplay.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                //最小值
                minPaddingTop = -1 * mDisplay.getMeasuredHeight();
                mDisplay.setPadding(mDisplay.getPaddingLeft(), minPaddingTop, mDisplay.getPaddingRight()
                        , mDisplay.getPaddingBottom());
            }
        });
    }

    /**
     * 任务详情
     *
     * @param context
     */
    public void taskdetails(Context context) {
        Map<String, String> map = new HashMap<String, String>();

        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
        map.put("tokenid", ssid);
        map.put("CurrentCompanyId", CurrentCompanyId);
        map.put("ID", ID);

        String netServer = ConfigUtils.getConfig(ConfigUtils.KEY.NetServer);
        new BaseAsyncTask(context, false, 0, netServer + "getTaskDetail", new BaseAsyncTaskInterface() {
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

                    mCreatChild.setVisible(true);//加载成功显示创建子任务加号
                    entity = (MissionDetailEntity) mResult.getData();

                    if (entity == null) {
                        return;
                    }

                    mTime.setText(StringUtils.convertDateWithMin(entity.getCreateTime()));//开始时间
                    tvEndTime.setText(StringUtils.convertDateWithMin(entity.getEndTime()));//截止时间
                    mPercentage.setText(entity.getDoneProgress() + "" + "%");//进度百分比
                    firstBar.setProgress(entity.getDoneProgress());//进度条
                    mTitle.setText(entity.getTaskTitle());//标题
                    mContent.setText(entity.getTaskContent());//内容
                    if (StringUtils.isEmpty(entity.getTaskContent())) {//内容
                        mContent.setVisibility(View.GONE);
                    } else {
                        mContent.setVisibility(View.VISIBLE);
                    }
                    if (StringUtils.isEmpty(entity.getAttachmentList())) {//附件
                        attachment.setVisibility(View.GONE);
                    }
                    etWorkHours.setText(entity.getWorkHours() + "");//工作时间

                    if (StringUtils.isEmpty(entity.getPersonCreateName())) {
                        tvCreatePeople.setHint(null);
                    } else {
                        tvCreatePeople.setText(entity.getPersonCreateName());//负责人
                    }

                    chargePeopleName = entity.getPersonLeadingName();

                    chargePeopleIds = entity.getPersonLeadingId() + "";//负责人id
                    if (StringUtils.isEmpty(entity.getPersonLeadingName())) {
                        tvChargePeople.setHint(null);
                    } else {
                        tvChargePeople.setText(chargePeopleName);//负责人
                    }
                    if (StringUtils.isEmpty(entity.getPersonExecuteNames())) {
                        tvExecutorPeople.setHint(null);
                    } else {
                        String personExecuteNames = entity.getPersonExecuteNames();
                        personExecuteNames = personExecuteNames.substring(0, personExecuteNames.length());
                        tvExecutorPeople.setText(personExecuteNames);//执行人
                    }
                    checkerPeopleName = entity.getPersonCheckNames();
                    checkerPeopleId = entity.getPersonCheckId() + "";
                    if (StringUtils.isEmpty(entity.getPersonCheckNames())) {
                        tvCheckerPeople.setHint(null);
                    } else {
                        tvCheckerPeople.setText(checkerPeopleName);//审核人
                    }
                    currentCompanyId = entity.getCurrentCompanyId() + "";
                    /*if (StringUtils.isEmpty(entity.getPersonSendNames())) {
                        tvCopyPeople.setText("");
                    }else {
                        tvCopyPeople.setText(entity.getPersonSendNames());//抄送人,已被砍
                    }*/
                    mPlan = entity.getDoneProgress();
                    leadId = entity.getPersonLeadingId() + "";//负责人id
                    executeIds = entity.getPersonExecuteIds();//执行人id（多个）
                    checkId = entity.getPersonCheckId() + "";//审核人id
                    sendIds = entity.getPersonSendIds();//抄送人id(多个)
                    creatId = entity.getPersonCreateId() + "";//创建人id
                    isTrunDownPerson = entity.isTaskTrunDownPerson();//是否为监督人
                    mTaskId = entity.getID() + "";

                    executeIds = entity.getPersonExecuteIds();
                    //获取子任务列表
                    getChildMissionList();
                    //获取评论列表
                    getCommentList();
                    taskState = entity.getTaskState();
                    taskPersonState = entity.getTaskPersonState();
                    mProgress.setVisibility(View.GONE);
                    if (isCanDelete()) {
                        menuItem.setVisible(true);
                        mDelete.setVisible(true);
                        mUpdate.setVisible(true);
                    }
                    if (!isCanCreatChildTask()) {
                        mCreatChild.setVisible(false);
                    }
                    initView();
                    anim();
                    if (entity.getDifficultyLevel() == 0) {//难易程度
                        tvDifficulty.setText("普通");
                    } else if (entity.getDifficultyLevel() == 1) {
                        tvDifficulty.setText("困难");
                    }
                    if (entity.getUrgentLevel() == 0) {//级别
                        tvLevel.setText("正常");
                    } else if (entity.getUrgentLevel() == 1) {
                        tvLevel.setText("紧急");
                    }

                    if (isCanDelete()) {
                    }

                    switch (entity.getTaskState()) {//状态
                        case 0:
                            mState.setText("待接受");
                            break;
                        case 1:
                            mState.setText("进行中");
                            break;
                        case 2:
                            mState.setText("待审核");
                            break;
                        case 3:
                            mState.setText("已完成");
                            break;
                        case 4:
                            mState.setText("已拒绝");
                            break;
                        case 5:
                            mState.setText("已驳回");
                            break;
                        case 6:
                            mState.setText("已延期");
                            break;
                        case 7:
                            mState.setText("已超期");
                            break;
                        case 8:
                            mState.setText("进行中");
                            break;
                    }
                    //显示加载成功界面
                    updateSuccessView();
                } else {
                    mCreatChild.setVisible(false);//加载成功显示创建子任务加号
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
     * 获取评论列表
     */
    private void getCommentList() {
        //评论列表
        commendList = new ArrayList<>();
        //先设置适配器
        commendListViewAdapter = new CommendListViewAdapter(TaskDetailsActivity.this, commendList);
        mListViewP.setAdapter(commendListViewAdapter);
        //获取网络数据,子任务列表信息
        Map<String, String> map = new HashMap<String, String>();
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("TokenId", ssid);
        String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
        map.put("CurrentCompanyId", CurrentCompanyId);

        map.put("TaskID", ID);
        map.put("PageIndex", 1 + "");
        map.put("PageSize", 10 + "");

        String netServer = ConfigUtils.getConfig(ConfigUtils.KEY.NetServer);
        new BaseAsyncTask(TaskDetailsActivity.this, false, 0, netServer + "getTaskCommentList", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultNetData<CommendListEntity>().parse(json, CommendListEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultNetData mResult = (ResultNetData) result;
                if ("0".equals(mResult.getSuccess())) {//0成功
                    List<CommendListEntity> tempList = mResult.getData();
                    commendList.clear();
                    commendList.addAll(tempList);
                    commendListViewAdapter.notifyDataSetChanged();
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

    //初始化下方的按钮
    public void initView() {
        hideView();

        //待接受的任务
        if (taskState == 0) {

//            //如果是审核人
//            if (isCheckPerson()) {
//                hideView();
//                mAccept.setVisibility(View.VISIBLE);
//                mAccept.setText("同意");
//                mRefuse.setVisibility(View.VISIBLE);
//                mRefuse.setText("驳回");
//                isAgree = true;
//                isStop = false;
//            }

            //如果是负责人
            if (isLeadPerson()) {
                hideView();
                mComplete.setVisibility(View.VISIBLE);
                mComplete.setText("提交审核");
                isCommit = true;
            }

        }

        //进行中的任务
        if (taskState == 1) {

//            //如果是审核人
//            if (isCheckPerson()) {
//                hideView();
//                mAccept.setVisibility(View.VISIBLE);
//                mAccept.setText("同意");
//                mRefuse.setVisibility(View.VISIBLE);
//                mRefuse.setText("驳回");
//                isAgree = true;
//                isStop = false;
//            }

            //如果是负责人
            if (isLeadPerson()) {
                hideView();
                mComplete.setVisibility(View.VISIBLE);
                mComplete.setText("提交审核");
                isCommit = true;
            }

        }

        //待审核的任务
        if (taskState == 2) {
            //如果是审核人
            if (isCheckPerson()) {
                mAccept.setVisibility(View.VISIBLE);
                mAccept.setText("同意");
                mRefuse.setVisibility(View.VISIBLE);
                mRefuse.setText("驳回");
                isAgree = true;
                isStop = false;
            }
        }

        //已完成的任务
        if (taskState == 3) {
            mProgress.setVisibility(View.GONE);
        }

        //执行人已完成
        if (taskState == 8) {

//            //如果是审核人
//            if (isCheckPerson()) {
//                hideView();
//                mAccept.setVisibility(View.VISIBLE);
//                mAccept.setText("同意");
//                mRefuse.setVisibility(View.VISIBLE);
//                mRefuse.setText("驳回");
//                isAgree = true;
//                isStop = false;
//            }

            //如果是负责人
            if (isLeadPerson()) {
                hideView();
                mComplete.setVisibility(View.VISIBLE);
                mComplete.setText("提交审核");
                isCommit = true;
            }
        }

        //如果主任务是待接收或进行中
        if (taskState == 0 || taskState == 1) {
            if (isExecutePerson()) {
                //个人待接受
                if (taskPersonState == 10) {
                    hideView();
                    mAccept.setVisibility(View.VISIBLE);
                    mAccept.setText("接受");
                    mRefuse.setVisibility(View.VISIBLE);
                    mRefuse.setText("拒绝");
                    isAgree = false;
                    isStop = true;
                }

                //个人已接受
                if (taskPersonState == 11) {
                    hideView();
                    if (taskState == 1) {
                        mComplete.setVisibility(View.VISIBLE);
                        mComplete.setText("已完成");
                        isCommit = false;
                    }
                }

                //个人进行中
                if (taskPersonState == 12) {
                    //只有是执行人的时候，显示更改进度
                    if (isExecutePerson() && isChildListNull()) {
                        mProgress.setVisibility(View.VISIBLE);
                    }
                    hideView();
                    mComplete.setVisibility(View.VISIBLE);
                    mComplete.setText("已完成");
                    isCommit = false;
                }

                //个人已完成
                if (taskPersonState == 13) {
                    hideView();
                }

                //个人已拒绝
                if (taskPersonState == 14) {
                    hideView();
                    mAccept.setVisibility(View.VISIBLE);
                    mAccept.setText("接受");
                    isAgree = false;
                }
            }
        }

        if (taskState == 3) {
            if (isTrunDownPerson) {
                hideView();
                mRefuse.setVisibility(View.VISIBLE);
                mRefuse.setText("驳回");
                isStop = false;
            }
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
        map.put("TaskId", ID);
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
                    switch (taskState) {//状态
                        case 0://待接受
                            break;
                        case 1://进行中
                            break;
                        case 2://待审核
                            setResult(RESULT_OK);
                            finish();
                            break;
                        case 3://已完成
                            setResult(RESULT_OK);
                            finish();
                            break;
                        case 4://已拒绝
                            break;
                        case 5://已驳回
                            break;
                        case 6://已作废
                            break;
                    }
                    taskdetails(getApplicationContext());
                    MyToast.showToast("提交成功");
                } else {
                    String message = mResult.getMsg();
                    MyToast.showToast(message);
                }
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void dataError(int funId) {
                MyToast.showToast("获取网络数据失败");
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
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
        map.put("ParentID", ID);
        map.put("Keyword", "");
        map.put("StateType", "0");
        map.put("TaskState", "");
        map.put("PageIndex", 1 + "");
        map.put("PageSize", 100 + "");
        String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
        map.put("CurrentCompanyId", CurrentCompanyId);

        String netServer = ConfigUtils.getConfig(ConfigUtils.KEY.NetServer);
        new BaseAsyncTask(TaskDetailsActivity.this, false, 0, netServer + "gettaskList", new BaseAsyncTaskInterface() {
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

                    missionList.clear();
                    missionList.addAll(list);
                    taskDetailsAdpter = new TaskDetailsAdpter(TaskDetailsActivity.this, missionList, TaskDetailsActivity.this, isCanDelete());
                    //先设置适配器
                    mission_listview.setAdapter(taskDetailsAdpter);
                    taskDetailsAdpter.notifyDataSetChanged();
                    if (isChildListNull()) {//有子任务就显示 没有就隐藏
                        mSubTask.setVisibility(View.GONE);
                    } else {
                        mSubTask.setVisibility(View.VISIBLE);
                    }
                    mission_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            openChildDetail(position);
                        }
                    });
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
     * 打开子任务详情
     *
     * @param position
     */
    private void openChildDetail(int position) {
        Intent intent = new Intent(TaskDetailsActivity.this, TaskDetailsActivity.class);
        String missionID = missionList.get(position).getID() + "";
        intent.putExtra("id", missionID);
        intent.putExtra("types", 1);
        TaskDetailsActivity.this.startActivity(intent);
    }

    /**
     * 隐藏按钮
     */
    public void hideView() {
        mAccept.setVisibility(View.GONE);
        mComplete.setVisibility(View.GONE);
        mRefuse.setVisibility(View.GONE);
    }

    /**
     * 是否为创建人
     *
     * @return
     */
    public boolean isCreatPerson() {
        if (!StringUtils.isEmpty(creatId)) {
            if (creatId.equals(loginId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否为负责人
     *
     * @return
     */
    public boolean isLeadPerson() {
        if (!StringUtils.isEmpty(leadId)) {
            if (leadId.equals(loginId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否为执行人
     *
     * @return
     */
    public boolean isExecutePerson() {
        if (!StringUtils.isEmpty(executeIds)) {
            String[] array = executeIds.split(",");
            for (int i = 0; i < array.length; i++) {
                if (array[i].equals(loginId)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 执行人是否只有一个
     *
     * @return
     */
    public boolean isExecuteOne() {
        if (!StringUtils.isEmpty(executeIds)) {
            String[] array = executeIds.split(",");
            List<String> list = new ArrayList<>();
            for (int i = 0; i < array.length; i++) {
                if (!StringUtils.isEmpty(array[i])) {
                    list.add(array[i]);
                }
            }
            if (list.size() == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否为审核人
     *
     * @return
     */
    public boolean isCheckPerson() {
        if (!StringUtils.isEmpty(checkId)) {
            if (checkId.equals(loginId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否为抄送人
     *
     * @return
     */
    public boolean isSendPerson() {
        if (!StringUtils.isEmpty(sendIds)) {
            String[] array = sendIds.split(",");
            for (int i = 0; i < array.length; i++) {
                if (array[i].equals(loginId)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 负责人与执行人是否相同
     *
     * @return
     */
    public boolean isAlike() {
        if (!StringUtils.isEmpty(leadId) && !StringUtils.isEmpty(executeIds)) {
            if (leadId.equals(executeIds)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 子任务列表是否为空
     * true null
     * false 不为空
     *
     * @return
     */
    public boolean isChildListNull() {
        if (StringUtils.isEmpty(missionList)) {
            return true;
        }
        return false;
    }

    /**
     * 新建子任务
     */
    public void addNewMission() {
        Intent intent =
                new Intent(TaskDetailsActivity.this, MissionActivity.class);
        intent.putExtra("parentId", ID);
        intent.putExtra("chargeId", chargePeopleIds);
        intent.putExtra("chargeName", chargePeopleName);
        intent.putExtra("checkerId", checkerPeopleId);
        intent.putExtra("checkerName", checkerPeopleName);
        intent.putExtra("endTime", tvEndTime.getText().toString());
        TaskDetailsActivity.this.startActivityForResult(intent, REQUEST_MISSION_ACTIVITY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
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
     * 删除任务
     *
     * @param context
     * @param TaskId           任务id
     * @param currentCompanyId 公司id
     */
    public void deleteTask(final Context context, String TaskId, String currentCompanyId, final int type) {
        Map<String, String> map = new HashMap<>();
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);
        map.put("CurrentCompanyId", currentCompanyId);
        map.put("ID", TaskId);

        String netServer = ConfigUtils.getConfig(ConfigUtils.KEY.NetServer);
        new BaseAsyncTask(context, false, 0, netServer + "DeleteTask", new BaseAsyncTaskInterface() {
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
                    MyToast.showToast("删除成功");
                    switch (type) {
                        case 1://删除主任务
                            setResult(RESULT_OK);
                            finish();
                            break;
                        case 2://子任务列表删除
                            taskdetails(context);
                            break;
                        case 3://删除子任务
                            setResult(RESULT_OK);
                            finish();
                            break;
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

    //子任务列表的删除
    @Override
    public void deleteChildTask(final View view, final String childId, final String childCurrentCompanyId) {

        if (isCanDelete()) {
            MessageDialog dialog = new MessageDialog(this);
            dialog.setTitle("提示");
            dialog.setMessage("确认删除?");
            dialog.setNegativeButton("取消");
            dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteTask(getApplicationContext(), childId, childCurrentCompanyId, 2);
                }
            });
            dialog.show();
        } else {
            view.setVisibility(View.GONE);
        }


    }

    /**
     * 是否可以删除
     *
     * @return
     */
    public boolean isCanDelete() {
        if (taskState != 0 && taskState != 6) {//任务状态是已完成或已驳回
            return false;
        }
        if (isExecutePerson() && !isLeadPerson() && !isCreatPerson()) {//如果只是执行人
            return false;
        }
        if (isCheckPerson() && !isLeadPerson() && !isCreatPerson()) {//如果只是审核人
            return false;
        }
        if (!isLeadPerson() && !isCreatPerson()) {
            return false;
        }
        return true;
    }

    /**
     * 是否可以创建子任务
     *
     * @return
     */
    public boolean isCanCreatChildTask() {
        if (taskState == 5 || taskState == 3) {
            return false;
        }
        if (isExecutePerson() && !isLeadPerson() && !isCreatPerson()) {//如果只是执行人
            return false;
        }
        if (isCheckPerson() && !isLeadPerson() && !isCreatPerson()) {//如果只是审核人
            return false;
        }
        if (!isLeadPerson() && !isCreatPerson()) {
            return false;
        }
        return true;
    }

}
