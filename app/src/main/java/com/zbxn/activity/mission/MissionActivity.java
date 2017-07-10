package com.zbxn.activity.mission;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.multi_image_selector.MultiImageSelector;
import com.zbxn.R;
import com.zbxn.activity.ContactsPicker;
import com.zbxn.activity.login.LoginActivity;
import com.zbxn.activity.main.contacts.ContactsChoseActivity;
import com.zbxn.activity.main.message.PhotoDetaiActivity;
import com.zbxn.bean.PhotosEntity;
import com.zbxn.bean.adapter.PhotoListAdapter;
import com.zbxn.http.BaseAsyncTask;
import com.zbxn.http.BaseAsyncTaskFile;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.data.Result;
import com.zbxn.pub.data.ResultParse;
import com.zbxn.pub.data.base.BaseAsyncTaskInterface;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.FileAccessor;
import com.zbxn.pub.utils.IsEmptyUtil;
import com.zbxn.pub.utils.MyToast;
import com.zbxn.widget.MyGridView;
import com.zbxn.widget.slidedatetimepicker.NewSlideDateTimeDialogFragment;
import com.zbxn.widget.slidedatetimepicker.SlideDateTimeListener;
import com.zbxn.widget.slidedatetimepicker.SlideDateTimePicker;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import utils.PreferencesUtils;
import utils.StringUtils;

/**
 * Created by wj on 2016/11/8.
 */
public class MissionActivity extends AbsToolbarActivity {
    @BindView(R.id.et_mission_name)
    EditText etMissionName;
    @BindView(R.id.ll_mission_name)
    LinearLayout llMissionName;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.ll_time)
    LinearLayout llTime;
    @BindView(R.id.et_work_hours)
    EditText etWorkHours;
    @BindView(R.id.ll_work_hours)
    LinearLayout llWorkHours;
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
    @BindView(R.id.tv_difficulty)
    TextView tvDifficulty;
    @BindView(R.id.ll_difficulty)
    LinearLayout llDifficulty;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.ll_level)
    LinearLayout llLevel;
    @BindView(R.id.et_sub)
    EditText etSub;
    @BindView(R.id.bt_save)
    CircularProgressButton btSave;
    @BindView(R.id.iv_charge)
    ImageView ivCharge;
    @BindView(R.id.iv_checker)
    ImageView ivChecker;

    @BindView(R.id.gridview)
    MyGridView gridview;

    /**
     * 选择接收人回调
     */
    private static final int Flag_Callback_Charge = 1001;//负责人
    private static final int Flag_Callback_Execute = 1002;//执行人
    private static final int Flag_Callback_Check = 1003;//审核人
    private static final int Flag_Callback_Copy = 1004;//抄送人
    private int type;
    //难易程度
    String[] diffItems = new String[]{"普通", "困难"};
    //选中的难易程度
    private String choosedDiff;
    //选中的难易程度(角标)
    private int choosedDiffNum = 0;
    //等级
    String[] levelItems = new String[]{"正常", "紧急"};
    //选中的等级
    private String choosedLevel;
    //选中的等级(角标)
    private int choosedLevelNum = 0;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    //执行人数组
    private String[] mExecuterArray;
    //所有执行人Id
    private String executersId;
    //所有抄送人Id
    private String[] mCopyArray;
    //所有抄送人id字符串
    private String copysId;
    //负责人id
    private String chargeId;
    //负责人姓名
    private String chargeName;
    //审核人id
    private String checkerId;
    //审核人姓名
    private String checkerName;
    //父任务ID
    private String parentId;
    //图片路径的集合
    private ArrayList<String> mSelectPath;
    private String mGuid = "";
    private String endTime;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    private Date endDate;
    private Date childEndDate;

    //空的加号
    private PhotosEntity entityEmpty = null;
    private ArrayList<PhotosEntity> lists = new ArrayList<PhotosEntity>();

    //获取图片
    private static final int REQUEST_IMAGE = 2002;
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;

    //提交数据map
    Map<String, String> mMap = new HashMap<String, String>();
    //是否是子任务
    private boolean mIsChild;
    private String missionName;
    private String ssid;

    @Override
    public int getMainLayout() {
        return R.layout.activity_mission;
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
        mGuid = StringUtils.getGuid();

        //显示加载成功界面
        updateSuccessView();
        //获取传入的parentId值,默认为0
        Intent intent = getIntent();
        parentId = intent.getStringExtra("parentId");
        //获取截止时间
        endTime = intent.getStringExtra("endTime");
        if (TextUtils.isEmpty(parentId)) {
            parentId = "0";
        }
        if ("0".equals(parentId)) {
        } else {
            //子任务的审核人是父任务的负责人,不可修改
            checkerId = intent.getStringExtra("chargeId");
            checkerName = intent.getStringExtra("chargeName");
            //    checkerId = intent.getStringExtra("checkerId");
            //    checkerName = intent.getStringExtra("checkerName");
            //    tvChargePeople.setText(chargeName);
            //    ivCharge.setVisibility(View.INVISIBLE);
            //    llChargePeople.setClickable(false);
            tvCheckerPeople.setText(checkerName);
            ivChecker.setVisibility(View.INVISIBLE);
            llCheckerPeople.setClickable(false);
        }
        String tempDate = sdf.format(new Date());
        tvEndTime.setText(tempDate);
        entityEmpty = new PhotosEntity();
        entityEmpty.setAppname("");
        entityEmpty.setId("");
        entityEmpty.setImgurl("tzs_paizhao");


        lists.clear();
        lists = updatePhotos(lists, null);
        PhotoListAdapter adapterPhotos = new PhotoListAdapter(this, lists, R.layout.list_item_select_photos, listener, true);
        gridview.setAdapter(adapterPhotos);

        //任务名称不能超过200字
        limitMissionName();
    }


    /**
     * 任务名称不能超过200字
     */
    private void limitMissionName() {
        etMissionName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //限制200字
                if (etMissionName.getText().toString().length() >= 200) {
                    MyToast.showToast("任务名称不能超过200字");
                }

            }
        });
    }

    /**
     * 更新照片list
     *
     * @param photoList
     * @param entity
     * @return
     */
    private ArrayList<PhotosEntity> updatePhotos(ArrayList<PhotosEntity> photoList, PhotosEntity entity) {
        if (!StringUtils.isEmpty(photoList)) {
            photoList.remove(entityEmpty);
        } else {
            photoList = new ArrayList<PhotosEntity>();
        }
        List<PhotosEntity> listPhotosTemp = new ArrayList<PhotosEntity>();
        listPhotosTemp.addAll(photoList);
        photoList.clear();
        if (null != entity) {
            listPhotosTemp.add(entity);
        }

        photoList.addAll(listPhotosTemp);
        photoList.add(entityEmpty);
        return photoList;
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
        getMenuInflater().inflate(R.menu.menu_task_create, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 设置标题栏
     *
     * @param toolbar
     * @param params
     * @return
     */
    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        Intent intent = getIntent();
        String parentId = intent.getStringExtra("parentId");
        if (TextUtils.isEmpty(parentId) || "0".equals(parentId)) {
            toolbar.setTitle("新建任务");// 定义上面的名字
        } else {
            toolbar.setTitle("新建子任务");
        }
        return super.onToolbarConfiguration(toolbar, params);
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
            case R.id.schedule_creat:
                /*// 负责人或执行人 有资格新建
                String loginId = PreferencesUtils.getString(this, LoginActivity.FLAG_INPUT_ID);
                if (StringUtils.isEmpty(chargeId)) {
                    break;
                }
                if (loginId.equals(chargeId)) {
                    addNewMission(false);
                    break;
                }
                if (StringUtils.isEmpty(executersId)){
                    break;
                }
                String[] exes = executersId.split(",");
                if (exes.length != 0) {
                    for (String ex : exes) {
                        if (loginId.equals(ex)) {
                            addNewMission(false);
                            break;
                        }
                    }
                }*/
                addNewMission(false);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //选择人员负责人
    private ArrayList<Contacts> mListContactsCharge = new ArrayList<>();
    //选择人员执行人
    private ArrayList<Contacts> mListContactsExecuters = new ArrayList<>();
    //选择人员审核人
    private ArrayList<Contacts> mListContactsChecker = new ArrayList<>();
    /**
     * 回调需要接收的人员
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Flag_Callback_Charge) { //负责人
            if (data != null) {
                if (resultCode == RESULT_OK) {
                    chargeId = data.getIntExtra("id", 0) + "";
                    chargeName = data.getStringExtra("name");
                    Contacts  entity = new Contacts();
                    entity.setId(data.getIntExtra("id", 0));
                    entity.setUserName(checkerName);
                    mListContactsCharge.clear();
                    mListContactsCharge.add(entity);
                    tvChargePeople.setText(chargeName);
                }

            } else {
                return;
            }
        } else if (requestCode == Flag_Callback_Execute) { //执行人
            if (data != null) {
                if (resultCode == RESULT_OK) {
                    mListContactsExecuters = (ArrayList<Contacts>) data.getExtras().getSerializable(ContactsPicker.Flag_Output_Checked);
                    mExecuterArray = new String[mListContactsExecuters.size()];
                    String content = "";
                    //所有执行人id字符串
                    executersId = "";
                    for (int i = 0; i < mListContactsExecuters.size(); i++) {
                        mExecuterArray[i] = mListContactsExecuters.get(i).getId() + "";
                        content += mListContactsExecuters.get(i).getUserName() + "、";
                        executersId += mListContactsExecuters.get(i).getId() + ",";
                    }
                    if (!StringUtils.isEmpty(executersId)) {
                        content = content.substring(0, content.length() - 1);
                        executersId = executersId.substring(0, executersId.length() - 1);
                    }
                    if (mListContactsExecuters.size() >= 2) {
                        content = mListContactsExecuters.get(0).getUserName() + "、" + mListContactsExecuters.get(1).getUserName() + "等" + mListContactsExecuters.size() + "人";
                    }
                    tvExecutorPeople.setText(content);
                }

            } else {
                return;
            }
        } else if (requestCode == Flag_Callback_Check) { //审核人
            if (data != null) {
                if (resultCode == RESULT_OK) {
                    checkerId = data.getIntExtra("id", 0) + "";
                    checkerName = data.getStringExtra("name");
                    Contacts  entity = new Contacts();
                    entity.setId(data.getIntExtra("id", 0));
                    entity.setUserName(checkerName);
                    mListContactsChecker.clear();
                    mListContactsChecker.add(entity);
                    tvCheckerPeople.setText(checkerName);
                }

            } else {
                return;
            }
        } else if (requestCode == Flag_Callback_Copy) { //抄送人
            if (data != null) {
                if (resultCode == RESULT_OK) {
                    List<Contacts> list = (ArrayList<Contacts>) data.getExtras().getSerializable(ContactsPicker.Flag_Output_Checked);
                    //所有抄送人Id
                    mCopyArray = new String[list.size()];
                    String content = "";
                    //所有抄送人id字符串
                    copysId = "";
                    for (int i = 0; i < list.size(); i++) {
                        mCopyArray[i] = list.get(i).getId() + "";
                        copysId += list.get(i).getId() + ",";
                        content += list.get(i).getUserName() + "、";
                    }
                    if (!StringUtils.isEmpty(copysId)) {
                        content = content.substring(0, content.length() - 1);
                        copysId = copysId.substring(0, copysId.length() - 1);
                    }
                    if (list.size() >= 2) {
                        content = list.get(0).getUserName() + "、" + list.get(1).getUserName() + "等" + list.size() + "人";
                    }
                    tvCopyPeople.setText(content);
                }

            }
        } else if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                StringBuilder sb = new StringBuilder();
                PhotosEntity entity = null;
                lists.clear();
                for (String p : mSelectPath) {
                    sb.append(p);
                    sb.append("\n");

                    entity = new PhotosEntity();
                    entity.setAppname("");
                    entity.setId("");
                    entity.setImgurl(p);
                    entity.setImgurlNet(p);
                    lists = updatePhotos(lists, entity);
                }
                PhotoListAdapter photoListAdapter = new PhotoListAdapter(this, lists, R.layout.list_item_select_photos, listener, true);
                gridview.setAdapter(photoListAdapter);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 上传图片
     */
    public void uploadFile(Context context, ArrayList<PhotosEntity> list) {
        Map<String, String> map = new HashMap<String, String>();
        //附件集合
        Map<String, File> mapFile = new HashMap<>();

        ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);
        map.put("guId", mGuid);
        for (int i = 0; i < list.size() - 1; i++) {
            File file = new File(list.get(i).getImgurl());
            if (file == null || !file.exists()) {
                break;
            }
            File fileSmall = null;
            try {
                String filePathStr = FileAccessor.getSmallPicture(file.getPath()); // 压缩文件
                fileSmall = new File(filePathStr);
            } catch (Exception e) {
                System.out.println("上传图片错误：" + e.toString());
                e.printStackTrace();
                fileSmall = file;
            }
            mapFile.put("image" + i, fileSmall);
        }

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTaskFile(context, false, 0, server + "/common/doUpLoads.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return Result.parse(json);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                Result mResult = (Result) result;
                if ("0".equals(mResult.getSuccess())) {//0成功
                    submitData(mMap);
                } else {
                    String message = mResult.getMsg();
                    MyToast.showToast(message);
                }
            }

            @Override
            public void dataError(int funId) {
            }
        }, mapFile).execute(map);
    }

    /**
     * 点击事件
     */
    @OnClick({R.id.ll_time,
            R.id.ll_charge_people, R.id.ll_executor_people, R.id.ll_checker_people,
            R.id.ll_copy_people, R.id.ll_difficulty, R.id.ll_level, R.id.bt_save})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_time:
                //截止时间
                String time = StringUtils.getEditText(tvEndTime);
                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(new SlideDateTimeListener() {
                            @Override
                            public void onDateTimeSet(Date date) {
                                if (TextUtils.isEmpty(parentId) || "0".equals(parentId)) {
                                    //主任务直接赋值
                                    tvEndTime.setText(format.format(date));
                                } else {
                                    //子任务判断,不能超过主任务
                                    String childEndTime = format.format(date);
                                    try {
                                        endDate = sdf.parse(endTime);
                                        childEndDate = sdf.parse(childEndTime);
                                        if (endDate.before(childEndDate)) {
                                            MyToast.showToast("不能超过主任务截止时间");
                                        } else {
                                            tvEndTime.setText(format.format(date));
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        })
                        .setInitialDate(StringUtils.convertToDate(format, time))
                        .setIs24HourTime(true)
                        .setIsHaveTime(NewSlideDateTimeDialogFragment.Have_Date_Time)
                        .build()
                        .show();
                break;
            case R.id.ll_charge_people:
                //负责人
                intent = new Intent(this, ContactsChoseActivity.class);
                intent.putExtra("list",mListContactsCharge);
                intent.putExtra("type", 2);//0-查看 1-多选 2-单选
                startActivityForResult(intent, Flag_Callback_Charge);
                break;
            case R.id.ll_executor_people:
                //执行人
//                startActivityForResult(new Intent(this, ContactsPicker.class), Flag_Callback_Execute);
                intent = new Intent(this, ContactsChoseActivity.class);
                intent.putExtra("list",mListContactsExecuters);
                intent.putExtra("type", 1);//0-查看 1-多选 2-单选
                startActivityForResult(intent, Flag_Callback_Execute);
                break;
            case R.id.ll_checker_people:
                //审核人
//                startActivityForResult(new Intent(this, SelectOnePeopleActivity.class), Flag_Callback_Check);
                intent = new Intent(this, ContactsChoseActivity.class);
                intent.putExtra("list",mListContactsChecker);
                intent.putExtra("type", 2);//0-查看 1-多选 2-单选
                startActivityForResult(intent, Flag_Callback_Check);
                break;
            case R.id.ll_copy_people:
                //抄送人
//                startActivityForResult(new Intent(this, ContactsPicker.class), Flag_Callback_Copy);
                intent = new Intent(this, ContactsChoseActivity.class);
                intent.putExtra("type", 1);//0-查看 1-多选 2-单选
                startActivityForResult(intent, Flag_Callback_Copy);
                break;
            case R.id.ll_difficulty:
                //难易程度
                openDifficulityDialog();
                break;
            case R.id.ll_level:
                //等级
                openLevelDialog();
                break;
            case R.id.bt_save:
                //保存并新建子任务
                addNewMission(true);
                break;
        }
    }

    /**
     * 打开选择难易程度的单选框
     */
    private void openDifficulityDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MissionActivity.this);
        builder.setTitle("请选择难易程度");
        builder.setSingleChoiceItems(diffItems, choosedDiffNum, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                choosedDiff = diffItems[which];
                choosedDiffNum = which;
                tvDifficulty.setText(choosedDiff);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 打开选择等级的单选框
     */
    private void openLevelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MissionActivity.this);
        builder.setTitle("请选择等级");
        builder.setSingleChoiceItems(levelItems, choosedLevelNum, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                choosedLevel = levelItems[which];
                choosedLevelNum = which;
                tvLevel.setText(choosedLevel);
                dialog.dismiss();
            }
        });
        builder.show();
    }


    /**
     * 新建任务
     */
    public void addNewMission(final boolean isChild) {
        mIsChild = isChild;
        mMap.clear();
        ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        mMap.put("TokenId", ssid);
        mMap.put("ParentID", parentId);
        //任务名
        if (!IsEmptyUtil.isEmpty(etMissionName, "请输入任务名称")) {
            return;
        }
        mMap.put("TaskTitle", StringUtils.getEditText(etMissionName));
        //截止时间
        if (!IsEmptyUtil.isEmpty(tvEndTime, "请输入截止时间")) {
            return;
        }
        mMap.put("EndTime", StringUtils.getEditText(tvEndTime) + ":00");
        //工作量
        if (!IsEmptyUtil.isEmpty(etWorkHours.getText().toString(), "请输入工作量")) {
            return;
        }
        //创建任务时间和截止任务时间，任务所用时间的一个判断
        try {
            if (etWorkHours.getText().toString().length() < 5) {
                String newDate = sdf.format(new Date());
                String endDate = StringUtils.getEditText(tvEndTime);
                String wh = StringUtils.getEditText(etWorkHours);
                long newTime = sdf.parse(newDate).getTime() + Integer.parseInt(wh) * 3600000;
                long localTime = sdf.parse(endDate).getTime();
                if (newTime > localTime) {
                    MyToast.showToast("任务时长不能超过截止时间");
                    return;
                }
            } else {
                MyToast.showToast("你输入的时间过长");
                return;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        mMap.put("WorkHours", etWorkHours.getText().toString());
        //负责人
        if (!IsEmptyUtil.isEmpty(tvChargePeople.getText().toString(), "请选择负责人")) {
            return;
        }
        mMap.put("PersonLeadingId", chargeId + "");
        if (!IsEmptyUtil.isEmpty(tvExecutorPeople.getText().toString(), "请选择执行人")) {
            return;
        }
        //执行人
        mMap.put("PersonExecuteIds", executersId + "");
        //审查人
        if (!IsEmptyUtil.isEmpty(tvCheckerPeople.getText().toString(), "请选择审查人")) {
            return;
        }
        mMap.put("PersonCheckId", checkerId + "");
        //抄送人,已经取消
        //    mMap.put("PersonSendIds", copysId + "");
        mMap.put("PersonSendIds", "");
        //难易程度
        mMap.put("DifficultyLevel", choosedDiffNum + "");
        //等级
        mMap.put("UrgentLevel", choosedLevelNum + "");
        //任务内容描述
        /*if (!IsEmptyUtil.isEmpty(etSub, "请输入任务内容描述")) {
            return;
        }*/

        if (lists.size() > 1) {
            uploadFile(MissionActivity.this, lists);
        } else {
            submitData(mMap);
        }
    }

    /**
     * 提交数据
     *
     * @param map
     */
    private void submitData(Map<String, String> map) {
        map.put("TaskContent", etSub.getText().toString());
        map.put("AttachmentId", mGuid);
        String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
        map.put("CurrentCompanyId", CurrentCompanyId);

        String netServer = ConfigUtils.getConfig(ConfigUtils.KEY.NetServer);
        new BaseAsyncTask(MissionActivity.this, false, 0, netServer + "creatTask", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultParse<MissionResultEntity>().parse(json, MissionResultEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultParse mResult = (ResultParse) result;
                if ("0".equals(mResult.getSuccess())) {
                    MyToast.showToast("创建成功");
                    if (mIsChild) {
                        MissionResultEntity entity = (MissionResultEntity) mResult.getData();
                        Intent intent = new Intent(MissionActivity.this, MissionActivity.class);
                        parentId = entity.getId() + "";
                        intent.putExtra("parentId", parentId);
                        intent.putExtra("chargeId", chargeId);
                        intent.putExtra("chargeName", chargeName);
                        //    intent.putExtra("checkerId", checkerId);
                        //    intent.putExtra("checkerName", checkerName);
                        intent.putExtra("endTime", tvEndTime.getText().toString());
                        MissionActivity.this.startActivity(intent);
                    } else {
                    }
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

    private ICustomListener listener = new ICustomListener() {

        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            switch (obj0) {
                case 0://刪除
                    lists.remove(position);
                    mSelectPath.remove(position);
                    lists = updatePhotos(lists, null);
                    PhotoListAdapter photoListAdapter = new PhotoListAdapter(MissionActivity.this, lists, R.layout.list_item_select_photos, listener, true);
                    gridview.setAdapter(photoListAdapter);
                    break;
                case 1://添加
                    pickImage();
                    break;
                case 3://显示大图
                    ArrayList<String> list_Ads = new ArrayList<>();
                    for (int j = 0; j < lists.size() - 1; j++) {
                        list_Ads.add(lists.get(j).getImgurl());
                    }
                    Intent intent = new Intent(MissionActivity.this, PhotoDetaiActivity.class);
                    intent.putExtra("list", list_Ads);
                    intent.putExtra("position", position);
                    startActivity(intent);
                    break;

                default:
                    break;
            }
        }
    };

    /**
     * 选择图片
     */
    private void pickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.mis_permission_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            boolean showCamera = true;//是否显示相机
            int maxNum = 9;//最大选择9张
            boolean isMulti = true;//是否多选

            MultiImageSelector selector = MultiImageSelector.create(MissionActivity.this);
            selector.showCamera(showCamera);
            selector.count(maxNum);
            if (isMulti) {
                selector.multi();
            } else {
                selector.single();
            }
            selector.origin(mSelectPath);
            selector.start(MissionActivity.this, REQUEST_IMAGE);
        }
    }

    private void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.mis_permission_dialog_title)
                    .setMessage(rationale)
                    .setPositiveButton(R.string.mis_permission_dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MissionActivity.this, new String[]{permission}, requestCode);
                        }
                    })
                    .setNegativeButton(R.string.mis_permission_dialog_cancel, null)
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE_READ_ACCESS_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onDestroy() {
        Intent intent = getIntent();
        intent.putExtra("refresh", true);
        super.onDestroy();
    }
}
