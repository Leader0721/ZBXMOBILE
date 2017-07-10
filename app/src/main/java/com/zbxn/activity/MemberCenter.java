package com.zbxn.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbxn.R;
import com.zbxn.activity.membercenter.IMemberCenterView;
import com.zbxn.activity.membercenter.KeyValue;
import com.zbxn.activity.membercenter.MemberCenterPresenter;
import com.zbxn.bean.Member;
import com.zbxn.init.eventbus.EventMember;
import com.zbxn.init.eventbus.EventCustom;
import com.zbxn.listener.ICustomListener;
import com.zbxn.popupwindow.PopupWindowSelectPic;
import com.zbxn.pub.dialog.InputDialog;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.DemoUtils;
import com.zbxn.pub.utils.FileAccessor;
import com.zbxn.pub.utils.MyToast;
import com.zbxn.pub.widget.NoScrollListview;
import com.zbxn.utils.KeyEvent;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import utils.PreferencesUtils;
import utils.StringUtils;
import utils.ValidationUtil;

/**
 * 个人中心
 *
 * @author GISirFive
 * @since 2016-7-6 上午10:11:35
 */
public class MemberCenter extends AbsToolbarActivity implements OnItemClickListener, IMemberCenterView {

    MemberCenterPresenter mPresenter;
    /**
     * 选择头像回调
     */
    private static final int Flag_Callback_SelectPortrait = 1001;
    Member member = Member.get();
    @BindView(R.id.mSelectPortrait)
    LinearLayout mSelectPortrait;
    @BindView(R.id.mPortrait)
    CircleImageView mPortrait;
    @BindView(R.id.mNoScrollListview)
    NoScrollListview mNoScrollListview;

    private InputDialog mModifyDialog;

    @Override
    public int getMainLayout() {
        return R.layout.activity_membercenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        init();
        refreshUI(null);
        updateSuccessView();
    }

    @Override
    public void loadData() {

    }

    private void init() {
        mPresenter = new MemberCenterPresenter(this);
        mNoScrollListview.setAdapter(mPresenter.getAdapter());
        mNoScrollListview.setOnItemClickListener(this);

    }

    /**
     * 滑动关闭当前Activitiv
     */
    @Override
    public boolean getSwipeBackEnable() {
//        return super.getSwipeBackEnable();
        return true;
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("个人信息");// 定义上面的名字
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @OnClick({R.id.mSelectPortrait})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mSelectPortrait:
                //实例化SelectPicPopupWindow
                PopupWindowSelectPic menuWindow = new PopupWindowSelectPic(MemberCenter.this, listenerPic);
                //显示窗口
                menuWindow.showAtLocation(MemberCenter.this.findViewById(R.id.mPortrait), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                break;
        }
    }

    /**
     * 选择图片拍照路径
     */
    private String mFilePath;
    /**
     * request code for tack pic
     */
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x3;
    public static final int REQUEST_CODE_LOAD_IMAGE = 0x4;
    private ICustomListener listenerPic = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            switch (obj0) {
                case 0:
                    //调用照相机
                    if (!FileAccessor.isExistExternalStore()) {
                        return;
                    }
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = FileAccessor.getTackPicFilePath();
                    if (file != null) {
                        Uri uri = Uri.fromFile(file);
                        if (uri != null) {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        }
                        mFilePath = file.getAbsolutePath();
                    }
                    startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
                    break;
                case 1:
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, REQUEST_CODE_LOAD_IMAGE);
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         /*回调内容*/
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == Flag_Callback_SelectPortrait) {
            if (resultCode == RESULT_OK) {
                boolean b = data.getBooleanExtra(
                        SelectPortrait.Flag_Output_Portrait, true);
                PreferencesUtils.putBoolean(this,
                        SelectPortrait.Flag_Output_Portrait, b);
                // 保存到本地
                Member.save(Member.get());


            }
        }
        if (requestCode == REQUEST_CODE_TAKE_PICTURE
                || requestCode == REQUEST_CODE_LOAD_IMAGE) {
            if (requestCode == REQUEST_CODE_LOAD_IMAGE) {
                mFilePath = DemoUtils.resolvePhotoFromIntent(MemberCenter.this, data, FileAccessor.IMESSAGE_IMAGE);
            }
            if (TextUtils.isEmpty(mFilePath)) {
                return;
            }
            File file = new File(mFilePath);
            if (file == null || !file.exists()) {
                return;
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
            mPresenter.uploadFile(MemberCenter.this, fileSmall, mICustomListener);
            return;
        }
    }

    /**
     * 回调
     */
    private ICustomListener mICustomListener = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            switch (obj0) {
                case 0:
                    MyToast.showToast("修改成功");

                    String mBaseUrl = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
                    ImageLoader.getInstance().displayImage(mBaseUrl + obj1, mPortrait);

//                    mBaseUrl + obj1
                    EventCustom eventCustom = new EventCustom();
                    eventCustom.setTag(KeyEvent.UPDATEPHOTO);
                    eventCustom.setObj(mBaseUrl + obj1);
                    EventBus.getDefault().post( eventCustom);
                    break;
                case 1:
                    MyToast.showToast("修改失败");
                    break;
            }
        }
    };
    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    // 刷新界面
    @Subscriber
    private void refreshUI(EventMember event) {
        Member member = Member.get();
        if (member == null) {
            mPortrait.setImageResource(R.mipmap.temp100);
        } else {
            /*boolean b = PreferencesUtils.getBoolean(this,
                    SelectPortrait.Flag_Output_Portrait, true);
            Glide.with(this).load(b ? R.mipmap.temp110 : R.mipmap.temp111)
                    .error(R.mipmap.temp100).into(mPortrait);*/

            String mBaseUrl = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showStubImage(R.mipmap.userhead_img)          // 设置图片下载期间显示的图片
                    .showImageForEmptyUri(R.mipmap.userhead_img)  // 设置图片Uri为空或是错误的时候显示的图片
                    .showImageOnFail(R.mipmap.userhead_img)       // 设置图片加载或解码过程中发生错误显示的图片
                    .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                    .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                    //.displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                    .build();                                   // 创建配置过得DisplayImageOption对象
            ImageLoader.getInstance().displayImage(mBaseUrl + member.getPortrait(), mPortrait, options);
            mPresenter.resetData();

        }
    }

    /**
     * ListView的点击事件
     */

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 1:
            case 3:
            case 4:
//            case 6://部门不能修改
//                mPresenter.OpenModifyDialog(position);
                OpenModifyDialog(position);
                break;
            default:
//                message().show("暂时无法修改");
//                MyToast.showToast("暂时无法修改");
                break;
        }

    }

    @Override
    public String getLoginName() {
        return null;
    }

    @Override
    public String getRemarkName() {
        return null;
    }

    @Override
    public String getTelephone() {
        return null;
    }

    @Override
    public String getOfficeTelOut() {
        return null;
    }


    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getBirthday() {
        return null;
    }

    @Override
    public String getAddress() {
        return null;
    }

    @Override
    public String getDepartmentName() {
        return null;
    }

    /**
     * 打开修改对话框，弹窗方法。<Br />
     * 数据库里的值为空的时候没法修改，等待进一步的完善！
     *
     * @param position 要修改的Item在列表中的位置
     */
    public void OpenModifyDialog(final int position) {
        // 根据position获取对应的数据
        final KeyValue keyValue = mPresenter.getItem(position);

        mModifyDialog = new InputDialog(this);
        // 更改选项的按钮点击事件
        mModifyDialog.setNegativeButton("取消", null);
        mModifyDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        if (keyValue.getValue() != null) {
                        // 用户输入的内容
                        String content = mModifyDialog.getEditText().getText().toString();
                        // 若无改动，直接返回

                        if (content.length() > 0) {

                            if (content.equals(keyValue.getValue())) {
                                MyToast.showToast("你输入的已存在");
                                return;
                            }
                        } else {
                            MyToast.showToast("修改内容不能为空");
                        }
                        // 输入是否合法
                        boolean isValidate = false;
                        switch (position) {
                            case 1:// 判断姓名
                                isValidate = true;
                                break;
                            case 2:// 判断手机号
                                isValidate = true;
                                break;
                            case 3:// 判断电话
                                isValidate = true;
                                break;
                            case 4:// 判断邮箱
                                isValidate = ValidationUtil.isMailbox(content);
                                break;
                            case 6:// 判断联系地址
                                isValidate = true;
                                break;

                            default:
                                break;
                        }

                        if (StringUtils.isEmpty(content)) {
                            MyToast.showToast("修改内容不能为空");
                        } else if (isValidate) {// 输入合法
                            mPresenter.submitModify(keyValue.getParamKey(), content);
                        } else {
                            MyToast.showToast("你输入的" + keyValue.getKey() + "格式有误");
                        }

//                        }

                    }
                }

        );
        // 设置标题
        mModifyDialog.setTitle("修改" + keyValue.getKey());

        // 设置全部选中里面的内容
        if (keyValue.getValue() != null) {
            mModifyDialog.getEditText().setSelectAllOnFocus(true);
            // 显示弹出要改的内容
            mModifyDialog.show(keyValue.getValue());
        } else if (keyValue.getValue() == null) {
            // 显示弹出要改的内容
            mModifyDialog.show();
            mModifyDialog.getEditText().setHint("请输入" + keyValue.getKey());
        }
    }

}
