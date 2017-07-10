package com.zbxn.activity.main.member;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbxn.R;
import com.zbxn.activity.AboutActivity;
import com.zbxn.activity.CollectCenter;
import com.zbxn.activity.Feedback;
import com.zbxn.activity.Main;
import com.zbxn.activity.MemberCenter;
import com.zbxn.activity.Settings;
import com.zbxn.activity.Share;
import com.zbxn.activity.integral.IntegralActivity;
import com.zbxn.activity.integral.IntegralDetailsActivity;
import com.zbxn.activity.login.LoginActivity;
import com.zbxn.activity.main.member.memberCenterPager.IMemberCenterPagerView;
import com.zbxn.activity.main.member.memberCenterPager.MemberCenterPagerPresenter;
import com.zbxn.activity.organize.OrganizeActivity;
import com.zbxn.activity.switchcompany.SwitchCompanyActivity;
import com.zbxn.bean.IntegralEntity;
import com.zbxn.bean.Member;
import com.zbxn.http.BaseAsyncTask;
import com.zbxn.init.eventbus.EventCustom;
import com.zbxn.init.eventbus.EventMember;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.bean.dbutils.DBUtils;
import com.zbxn.pub.data.ResultParse;
import com.zbxn.pub.data.base.BaseAsyncTaskInterface;
import com.zbxn.pub.dialog.MessageDialog;
import com.zbxn.pub.frame.mvp.AbsBaseFragment;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.DemoUtils;
import com.zbxn.pub.utils.FileAccessor;
import com.zbxn.pub.utils.MyToast;
import com.zbxn.utils.KEY;
import com.zbxn.utils.KeyEvent;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import de.hdodenhof.circleimageview.CircleImageView;
import utils.PreferencesUtils;
import utils.StringUtils;
import widget.pulltozoomview.PullToZoomScrollViewEx;

/**
 * 我的  主页面
 *
 * @author LiangHanXin
 */
public class MemberCenterPager extends AbsBaseFragment implements IMemberCenterPagerView {


    @BindView(R.id.mLuckDraw)
    LinearLayout mLuckDraw;
    @BindView(R.id.mOrganizeFrame)
    LinearLayout mOrganizeFrame;
    @BindView(R.id.mOrganizeLine)
    View mOrganizeLine;
    private MemberCenterPagerPresenter memberCenterPagerPresenter = new MemberCenterPagerPresenter(this);

    /**
     * 登录回调
     */
    private static final int Flag_Callback_Login = 1000;
    private static final int Flag_Callback_Switch = 1001;

    PullToZoomScrollViewEx mScrollView;
    @BindView(R.id.mId)
    TextView mId;
    @BindView(R.id.mPortrait)
    CircleImageView mPortrait;// 头像
    @BindView(R.id.mRemarkName)
    TextView mRemarkName;// 昵称
    @BindView(R.id.mDepartment)
    TextView mDepartment;// 部门
    @BindView(R.id.mServicePhone)
    LinearLayout mServicePhone;//客服
    @BindView(R.id.mAboutZBX)
    LinearLayout mAboutZBX;//关于淄博星
    @BindView(R.id.mMoreSettings)
    LinearLayout mMoreSettings;//设置
    @BindView(R.id.mLogout)
    CircularProgressButton mLogout;//退出
    @BindView(R.id.MyHeadPortrait)
    LinearLayout MyHeadPortrait;//头部
    @BindView(R.id.mMoreRecommend)
    LinearLayout mMoreRecommend;//推荐基友
    @BindView(R.id.MyCollection)
    LinearLayout MyCollection;//收藏
    @BindView(R.id.MyIntegral)
    LinearLayout MyIntegral;//N币
    @BindView(R.id.mForAdvice)
    LinearLayout mForAdvice;//意见反馈
    @BindView(R.id.mSwitchCompany)
    LinearLayout mSwitchCompany;//切换公司

    @BindView(R.id.mIntegral1)
    LinearLayout mIntegral1;//N币
    @BindView(R.id.mRanking)
    LinearLayout mRanking;//N币排名
    @BindView(R.id.myRanking)
    TextView myRanking;//排名数

    String touxiang;
    String name;
    String paiming;
    /**
     * Header头部显示的比例
     */
    private float mHeaderShowRadio = 0f;
    private String leiji = "";

    public MemberCenterPager() {
    }

    @Override
    public boolean handleMessage(Message msg) {

        return false;
    }


    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup group) {
        View root = inflater.inflate(R.layout.main_membercenter, group, false);
        ButterKnife.bind(this, root);
        System.getProperty("java.classpath");
        EventBus.getDefault().register(this);
        Passwor(getContext());

        mOrganizeLine.setVisibility(View.GONE);
        mOrganizeFrame.setVisibility(View.GONE);
        String permissionIds = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_PERMISSIONIDS, "");
        String[] arr = permissionIds.split(",");
        for (int i = 0; i < arr.length; i++) {
            try {
                if ("4".equals(arr[i])) {
                    mOrganizeLine.setVisibility(View.VISIBLE);
                    mOrganizeFrame.setVisibility(View.VISIBLE);
                    break;
                }
            } catch (Exception e) {

            }
        }

        return root;
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {
        refreshUI(null);
    }


    @OnClick({R.id.mPortrait, R.id.mServicePhone, R.id.mAboutZBX, R.id.mForAdvice,
            R.id.mMoreSettings, R.id.mLogout, R.id.MyHeadPortrait, R.id.mMoreRecommend,
            R.id.MyCollection, R.id.MyIntegral, R.id.mSwitchCompany, R.id.mIntegral1,
            R.id.mRanking, R.id.mLuckDraw, R.id.mOrganizeFrame})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.MyHeadPortrait:
                startActivity(new Intent(getActivity(), MemberCenter.class));
                break;
            case R.id.mPortrait:
//                //实例化SelectPicPopupWindow
//                PopupWindowSelectPic menuWindow = new PopupWindowSelectPic(getActivity(), listenerPic);
//                //显示窗口
//                menuWindow.showAtLocation(getActivity().findViewById(R.id.mPortrait), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                break;
            case R.id.mServicePhone:
                showCallServiceDialog();
                break;
            case R.id.mForAdvice:
                startActivity(new Intent(getActivity(), Feedback.class));
                break;
            case R.id.mSwitchCompany:
                startActivityForResult(new Intent(getActivity(), SwitchCompanyActivity.class), Flag_Callback_Switch);
                break;
            case R.id.MyIntegral:
//                startActivity(new Intent(getActivity(), IntegralDetailsActivity.class));
                Intent inten = new Intent(getActivity(), IntegralDetailsActivity.class);
                inten.putExtra("touxiang", touxiang);
                inten.putExtra("name", name);
                inten.putExtra("jifen", leiji);   //用户积分
                startActivity(inten);
                break;
            case R.id.MyCollection:
                startActivity(new Intent(getActivity(), CollectCenter.class));
                break;
            case R.id.mAboutZBX:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.mMoreSettings:
                startActivity(new Intent(getActivity(), Settings.class));
                break;
            case R.id.mLogout:
                MessageDialog dialog = new MessageDialog(getActivity());
                dialog.setTitle("提示");
                dialog.setMessage("确定要退出登录吗");
                dialog.setPositiveButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        // 调用 Handler 来异步设置别名
                        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, ""));
                        JPushInterface.stopPush(getActivity());

                        memberCenterPagerPresenter.logout();
                    }
                });
                dialog.setNegativeButton("取消");
                dialog.show();
                break;
            case R.id.mMoreRecommend://推荐好友
                startActivity(new Intent(getActivity(), Share.class));
                break;
            case R.id.mIntegral1://我的N币MyIntegral
               /* Intent inten = new Intent(getActivity(), IntegralDetailsActivity.class);
                inten.putExtra("touxiang", touxiang);
                inten.putExtra("name", name);
                inten.putExtra("leiji", leiji);
                startActivity(inten);*/
                break;
            case R.id.mRanking://N币排名
                Intent intent = new Intent(getActivity(), IntegralActivity.class);
                intent.putExtra("touxiang", touxiang);
                intent.putExtra("name", name);
                intent.putExtra("leiji", leiji);
                //    intent.putExtra("paiming", paiming);
                startActivity(intent);
                break;
            case R.id.mLuckDraw://抽奖
//                startActivity(new Intent(getActivity(), ShopLuckActivity.class));
                break;

            case R.id.mOrganizeFrame://组织架构
                startActivity(new Intent(getActivity(), OrganizeActivity.class));
                break;


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Passwor(getContext());
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    // 刷新界面
    @Subscriber
    private void refreshUI(EventMember event) {
        Member member = Member.get();
        if (member == null) {
            //登录用户头像
            mPortrait.setImageResource(R.mipmap.userhead_img);
            mRemarkName.setText("");
            mDepartment.setText("");
            mId.setText("");
        } else {
            //登录用户头像
            String headUrl = PreferencesUtils.getString(getActivity(), LoginActivity.FLAG_INPUT_HEADURL, "");
            String mBaseUrl = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showStubImage(R.mipmap.userhead_img)          // 设置图片下载期间显示的图片
                    .showImageForEmptyUri(R.mipmap.userhead_img)  // 设置图片Uri为空或是错误的时候显示的图片
                    .showImageOnFail(R.mipmap.userhead_img)       // 设置图片加载或解码过程中发生错误显示的图片
                    .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                    .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                    //.displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                    .build();                                   // 创建配置过得DisplayImageOption对象
            ImageLoader.getInstance().displayImage(mBaseUrl + headUrl, mPortrait, options);
            mRemarkName.setText(member.getUserName());
            mDepartment.setText(member.getDepartmentName());
            if (!StringUtils.isEmpty(member.getNumber())) {
                mId.setText("ID:" + member.getNumber());
            } else {
                mId.setText("ID:");
            }
        }
    }

    /**
     * 修改密码
     */
    private void modifyPassword() {

    }

    /**
     * 退出登录
     */
    /*private void logout() {
        Member.clear();
        post(Code.USER_LOGOUT, null);
        Intent intent = new Intent(getActivity(), Login.class);
        startActivityForResult(intent, Flag_Callback_Login);
    }*/
    private void showCallServiceDialog() {
        MessageDialog dialog = new MessageDialog(getActivity());
        dialog.setMessage("确定拨打客服热线?");
        dialog.setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + "05336078222");
                intent.setData(data);
                startActivity(intent);
            }
        });
        dialog.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        dialog.show();
    }

    /**
     * 获取头部显示的比例
     */
    public float getHeaderShowRadio() {
        return mHeaderShowRadio;
    }

    /**
     * 退出登录
     */
    @Override
    public void Jump() {

        DBUtils<Contacts> mDBUtils = new DBUtils<>(Contacts.class);
        mDBUtils.deleteAll();

        Intent intent = new Intent(BaseApp.getContext(), Main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("isExit", true);
        BaseApp.getContext().startActivity(intent);
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
        switch (requestCode) {
            case Flag_Callback_Login:
                if (resultCode == Activity.RESULT_OK) {
                    Intent intent = new Intent(getContext(), Main.class);
                    startActivity(intent);
//                    getActivity().finish();
                } else {// 没有登录，关闭应用
                    getActivity().finish();
                }
                break;
            case Flag_Callback_Switch:
                if (resultCode == Activity.RESULT_OK) {
                    getActivity().finish();
                }
                break;

            default:
                break;
        }
         /*回调内容*/
        if (resultCode != getActivity().RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_TAKE_PICTURE
                || requestCode == REQUEST_CODE_LOAD_IMAGE) {
            if (requestCode == REQUEST_CODE_LOAD_IMAGE) {
                mFilePath = DemoUtils.resolvePhotoFromIntent(getActivity(), data, FileAccessor.IMESSAGE_IMAGE);
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
            memberCenterPagerPresenter.uploadFile(getActivity(), fileSmall, mICustomListener);
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
                    break;
                case 1:
                    MyToast.showToast("修改失败");
                    break;
            }
        }
    };

    /**
     * ************* 设置别名 *************************
     */
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    System.out.println("Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getActivity(),
                            (String) msg.obj, null, mAliasCallback);
                    break;
                default:
                    System.out.println("Unhandled msg - " + msg.what);
            }
        }
    };
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    System.out.println("alia退出:" + alias + "|");
                    System.out.println(logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。

                    // 保存是否设置别名状态
                    PreferencesUtils.putBoolean(getActivity(), KEY.ISSETALIAS, false);
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    System.out.println(logs);
                    // 保存是否设置别名状态
                    // 保存是否设置别名状态
                    PreferencesUtils.putBoolean(getActivity(), KEY.ISSETALIAS, false);

                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(
                            mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    System.out.println(logs);
            }
        }
    };


    /**
     * 自己的排名
     *
     * @param context
     */
    public void Passwor(Context context) {
//        progressDialog.show("加载中...");
        Map<String, String> map = new HashMap<String, String>();

        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTask(context, false, 0, server + "/baseUserScoreController/selectMyph.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultParse<IntegralEntity>().parse(json, IntegralEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultParse mResult = (ResultParse) result;
                if ("0".equals(mResult.getSuccess())) {//0成功
                    IntegralEntity entity = (IntegralEntity) mResult.getData();
                    touxiang = entity.getTouxiang();
                    name = entity.getUsername();
                    mRemarkName.setText(name);
                    paiming = entity.getPaiming() + "";
                    myRanking.setText(paiming + "名");//名次
                    leiji = entity.getLeiji();
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

    @Subscriber
    public void onEventMainThread(EventCustom eventCustom) {
        if (KeyEvent.UPDATEPHOTO.equals(eventCustom.getTag())) {
            ImageLoader.getInstance().displayImage(eventCustom.getObj() + "", mPortrait);
        }
    }


}
