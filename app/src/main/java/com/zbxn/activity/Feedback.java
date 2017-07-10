package com.zbxn.activity;

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
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.multi_image_selector.MultiImageSelector;
import com.zbxn.R;
import com.zbxn.activity.main.message.PhotoDetaiActivity;
import com.zbxn.bean.PhotosEntity;
import com.zbxn.bean.adapter.PhotoListAdapter;
import com.zbxn.createworkblog.BlogHintView;
import com.zbxn.http.BaseAsyncTaskFile;
import com.zbxn.init.http.RequestParams;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.data.Result;
import com.zbxn.pub.data.base.BaseAsyncTaskInterface;
import com.zbxn.pub.dialog.MessageDialog;
import com.zbxn.pub.dialog.ProgressDialog;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvc.AbsToolbarActivity;
import com.zbxn.pub.http.RequestUtils;
import com.zbxn.pub.http.Response;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.FileAccessor;
import com.zbxn.pub.utils.IsEmptyUtil;
import com.zbxn.pub.utils.MyToast;
import com.zbxn.widget.MyGridView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import utils.PreferencesUtils;
import utils.StringUtils;


/**
 * 意见反馈
 */
public class Feedback extends AbsToolbarActivity {
    /**
     * 反馈的长度范围
     */
    private static final int[] Flag_RangeLengthOfBlog = {20, 1000};

    private BlogHintView mBlogHintView;

    @BindView(R.id.feed_edit)
    EditText feedEdit;
    @BindView(R.id.anonymity_check)
    CheckBox mCheckBox;
    @BindView(R.id.mContentLength)
    TextView mContentLength;
    @BindView(R.id.mCreateBlog)
    TextView mCreateBlog;
    @BindView(R.id.mGridView)
    MyGridView mGridView;
    // 已完成反馈提交，清空缓存
    private boolean mSubmitFinish = false;
    private ProgressDialog mProgressDialog;

    //空的加号
    private PhotosEntity entityEmpty = null;
    private ArrayList<PhotosEntity> lists = new ArrayList<PhotosEntity>();

    //获取图片
    private static final int REQUEST_IMAGE = 2002;
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;

    //图片路径的集合
    private ArrayList<String> mSelectPath;
    private String ssid;
    private String mGuid = "";

    @Override
    public int getMainLayout() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGuid = StringUtils.getGuid();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        init();

        showAddButton();

        entityEmpty = new PhotosEntity();
        entityEmpty.setAppname("");
        entityEmpty.setId("");
        entityEmpty.setImgurl("tzs_paizhao");

        lists.clear();
        lists = updatePhotos(lists, null);
        PhotoListAdapter adapterPhotos = new PhotoListAdapter(this, lists, R.layout.list_item_select_photos, listener, true);
        mGridView.setAdapter(adapterPhotos);

        updateSuccessView();
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

    private ICustomListener listener = new ICustomListener() {

        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            switch (obj0) {
                case 0://刪除
                    lists.remove(position);
                    mSelectPath.remove(position);
                    lists = updatePhotos(lists, null);
                    PhotoListAdapter photoListAdapter = new PhotoListAdapter(Feedback.this, lists, R.layout.list_item_select_photos, listener, true);
                    mGridView.setAdapter(photoListAdapter);
                    break;
                case 1://添加
                    pickImage();
                    break;
                case 3://显示大图
                    ArrayList<String> list_Ads = new ArrayList<>();
                    for (int j = 0; j < lists.size() - 1; j++) {
                        list_Ads.add(lists.get(j).getImgurl());
                    }
                    Intent intent = new Intent(Feedback.this, PhotoDetaiActivity.class);
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

            MultiImageSelector selector = MultiImageSelector.create(Feedback.this);
            selector.showCamera(showCamera);
            selector.count(maxNum);
            if (isMulti) {
                selector.multi();
            } else {
                selector.single();
            }
            selector.origin(mSelectPath);
            selector.start(Feedback.this, REQUEST_IMAGE);
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
                            ActivityCompat.requestPermissions(Feedback.this, new String[]{permission}, requestCode);
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

    // 显示创建评论按钮
    private void showAddButton() {
        mCreateBlog.setVisibility(View.VISIBLE);
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.playTogether(
//                ObjectAnimator.ofFloat(mCreateBlog, "rotation", 360, 0),
//                ObjectAnimator.ofFloat(mCreateBlog, "scaleX", 0, 1),
//                ObjectAnimator.ofFloat(mCreateBlog, "scaleY", 0, 1));
//        animatorSet.setInterpolator(new DecelerateInterpolator());
//        animatorSet.setDuration(200);
//        animatorSet.start();
    }


    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("意见反馈");
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    public boolean handleMessage(Message message) {
        return false;
    }

    @Override
    public void onSuccess(RequestUtils.Code code, JSONObject response) {
        try {
            switch (code) {
                case SUBMIT_FEEDBACK://意见反馈
                    mSubmitFinish = true;
                    String data = response.optString(Response.MSG, "提交成功");
                    showToast(data, Toast.LENGTH_LONG);
                    setResult(RESULT_OK);
                    finish();

                    break;
                default:
                    break;
            }
        } catch (Exception e) {


        } finally {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }

    @Override
    public void onFailure(RequestUtils.Code code, JSONObject error) {

        try {
            switch (code) {
                case SUBMIT_FEEDBACK://反馈意见保存
                    String message = error.optString(Response.MSG, "提交失败,请重试");
                    MessageDialog dialog = new MessageDialog(this);
                    dialog.setTitle("提示");
                    dialog.setMessage(message);
                    dialog.setPositiveButton("确定");
                    dialog.show();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {

        } finally {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }
    }

    @OnClick({R.id.mContentLength, R.id.mCreateBlog})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mContentLength://字数
                break;
            case R.id.mCreateBlog://确认键
                if (!IsEmptyUtil.isEmpty(feedEdit, "请输入反馈意见")) {
                    return;
                }

                if (lists.size() > 1) {
                    uploadFile(Feedback.this, lists);
                } else {
                    submitData();
                }
                break;
        }
    }

    private void submitData() {
        int isanonymit;
        if (mCheckBox.isChecked()) {
            isanonymit = 1;
        } else {
            isanonymit = 0;
        }
        String feed = feedEdit.getText().toString();
        RequestParams params = new RequestParams();
        params.put("content", feed);
//                    params.put("source",1);
        params.put("isanonymity", isanonymit);
        //将用户输入的账号保存起来，以便下次使用
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        params.put("tokenid", ssid);
        post(RequestUtils.Code.SUBMIT_FEEDBACK, params);
    }

    private void init() {
        mBlogHintView = new BlogHintView(this,
                findViewById(R.id.mFeedb), getHandler());

        mProgressDialog = new ProgressDialog(this);

        // 创建PerformEdit，一定要传入不为空的EditText
       /* mPerformEdit = new PerformEdit(mContent);
        mPerformEdit.setDefaultText("");*/
        // 最大输入长度
        feedEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                Flag_RangeLengthOfBlog[1])});
        feedEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = 0;
                if (s == null)
                    length = 0;
                String content = s.toString().trim();
                length = content.length();
                mContentLength.setText(String.valueOf(length));
            }
        });

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

        new BaseAsyncTaskFile(context, true, 0, server + "/common/doUpLoads.do", new BaseAsyncTaskInterface() {
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
                    submitData();
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
     * 回调需要接收的人员
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE) {
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
                mGridView.setAdapter(photoListAdapter);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
