package com.zbxn.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.github.yoojia.anyversion.AnyVersion;
import com.github.yoojia.anyversion.NotifyStyle;
import com.igexin.sdk.PushManager;
import com.lidroid.xutils.exception.DbException;
import com.umeng.analytics.MobclickAgent;
import com.zbxn.R;
import com.zbxn.activity.examinationandapproval.ApplyDetailActivity;
import com.zbxn.activity.login.LoginActivity;
import com.zbxn.activity.main.PagerFragmentManager;
import com.zbxn.activity.main.contacts.ContactsSearchActivity;
import com.zbxn.activity.main.message.messagenew.MessageCenterSearchActivity;
import com.zbxn.activity.mission.TaskDetailsActivity;
import com.zbxn.activity.schedule.NewTaskActivity;
import com.zbxn.bean.Member;
import com.zbxn.fragment.ShortCutFragment;
import com.zbxn.fragment.shortcut.OnItemSelectListener;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.bean.dbutils.DBUtils;
import com.zbxn.pub.data.ResultData;
import com.zbxn.pub.dialog.MessageDialog;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvc.AbsToolbarActivity;
import com.zbxn.pub.http.HttpCallBack;
import com.zbxn.pub.http.RequestUtils;
import com.zbxn.pub.http.RequestUtils.Code;
import com.zbxn.pub.utils.JsonUtil;
import com.zbxn.pub.utils.MyToast;
import com.zbxn.service.DemoPushService;
import com.zbxn.utils.KEY;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import utils.PreferencesUtils;
import utils.StringUtils;

public class Main extends AbsToolbarActivity {
    /**
     * 消息_头部Toolbar的透明度
     */
    public static final int Flag_Message_ToolbarAlpha = 1001;

    /**
     * 两次按返回键的间隔
     */
    public static final long BACK_DURATION = 3000;

    @BindView(R.id.mShortCut)
    ImageView mShortCut;

    private PagerFragmentManager mPagerManager;
    private FragmentManager mFragmentManager;
    private ShortCutFragment mShortCutFragment;

    /**
     * 第一次按返回键的时间
     */
    private long mFistPressBackTime = System.currentTimeMillis() - BACK_DURATION;

    // DemoPushService.class 自定义服务名称, 核心服务
    private Class userPushService = DemoPushService.class;

    private DBUtils<Contacts> mDBUtils;
    @Override
    public int getMainLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobclickAgent.openActivityDurationTrack(false);
        updateSuccessView();

        mPagerManager = new PagerFragmentManager(this);

        init();

        String id = PreferencesUtils.getString(this, LoginActivity.FLAG_INPUT_ID);
      // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, id));
        //如果停止了resume
        if (JPushInterface.isPushStopped(this)) {
            JPushInterface.resumePush(this);
        }

        /*PushManager.getInstance().initialize(this.getApplicationContext(), userPushService);

        // 注册 intentService 后 PushDemoReceiver 无效, sdk 会使用 DemoIntentService 传递数据,
        // AndroidManifest 对应保留一个即可(如果注册 DemoIntentService, 可以去掉 PushDemoReceiver, 如果注册了
        // IntentService, 必须在 AndroidManifest 中声明)
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), DemoIntentService.class);

        setGetui(id);*/

        String info_msg = PreferencesUtils.getString(BaseApp.getContext(),
                LoginActivity.FLAG_INFO_MSG, "");
        int info_msg_count = PreferencesUtils.getInt(BaseApp.getContext(),
                LoginActivity.FLAG_INFO_MSG_COUNT, 0);
        if (!StringUtils.isEmpty(info_msg) && info_msg_count < 3) {
            info_msg_count++;
            PreferencesUtils.putInt(BaseApp.getContext(), LoginActivity.FLAG_INFO_MSG_COUNT, info_msg_count);
            MessageDialog dialog = new MessageDialog(this);
            dialog.setTitle("提示");
            dialog.setMessage(info_msg);
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {

                }
            });
            //dialog.setNegativeButton("取消");
            dialog.show();
        }

        findDepartmentContacts(this);

        if (mDBUtils == null)
            mDBUtils = new DBUtils<>(Contacts.class);

        setData(getIntent());
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //刷新intent内容
        setIntent(intent);
        setData(intent);

        boolean isSwitchCompany = intent.getBooleanExtra("isSwitchCompany", false);
        if (isSwitchCompany) {
//            finish();
//            Intent intent1 = new Intent(getApplicationContext(), Main.class);
//            startActivity(intent1);
            Member member = Member.getLocalCache();
            if (member != null) {// 本地无缓存，直接登录
                String username = PreferencesUtils.getString(this, LoginActivity.FLAG_INPUT_USERNAME, null);
                String password = PreferencesUtils.getString(this, LoginActivity.FLAG_INPUT_PASSWORD, null);
                member.setPassword(password);
                member.login(this, username, password, new ICustomListener() {
                    @Override
                    public void onCustomListener(int obj0, Object obj1, int position) {
                        switch (obj0) {
                            case 0:
                                finish();
                                Intent intent1 = new Intent(getApplicationContext(), Main.class);
                                startActivity(intent1);
                                break;
                        }
                    }
                });
            }

        }

        //此处专供刷新推送消息使用
        int index = intent.getIntExtra("index", -1);
        if (index == 0) {
            mPagerManager.setSelection(0);
        }
    }

    private void setData(Intent intent1) {
        boolean isExit = intent1.getBooleanExtra("isExit", false);
        if (isExit) {
            finish();
            Intent intent2 = new Intent(this, LoginActivity.class);
            startActivity(intent2);
            return;
        }
        String extra = intent1.getStringExtra("extra");
        String type = "";
        String id = "";
        String msgId = "";
        try {
            JSONObject json = new JSONObject(extra);
            Iterator<String> it = json.keys();

            while (it.hasNext()) {
                String myKey = it.next().toString();
                Log.d("Main", "结果：[" + myKey + "：" + json.optString(myKey) + "]");
                if ("type".equals(myKey)) {
                    type = json.optString(myKey);
                } else if ("id".equals(myKey)) {
                    id = json.optString(myKey);
                } else if ("msgId".equals(myKey)) {
                    msgId = json.optString(myKey);
                }
            }
        } catch (Exception e) {
            Log.e("Main", "Get message extra JSON error!");
            return;
        }
        try {
            Intent intent;
            switch (type) {
                case "1"://系统消息
                    break;
                case "11"://公告消息
                    intent = new Intent(this, MessageDetail.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                    break;
                case "23"://日志消息
                    break;
                case "25"://日程管理
                    intent = new Intent(this, NewTaskActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("sign", 1);
                    intent.putExtra("flag", -1);
                    startActivity(intent);
                    break;
                case "31"://审批管理
//                Intent applyIntent = new Intent(getActivity(), ApplyActivity.class);
//                startActivity(applyIntent);
                    intent = new Intent(this, ApplyDetailActivity.class);
                    intent.putExtra("approvalID", id);
                    startActivity(intent);
                    break;
                case "32"://任务
                    intent = new Intent(this, TaskDetailsActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                    break;
                case "41"://会议通知
                    break;
                case "51"://跟进评论
                    break;
                case "55"://评论回复
                    break;
            }
        } catch (Exception e) {

        }
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setNavigationIcon(null);
        getLayoutInflater().inflate(R.layout.main_topbar, toolbar);
        return super.onToolbarConfiguration(toolbar, params);
    }


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Flag_Message_ToolbarAlpha:
                mPagerManager.changeToolbarStatus(2);
                break;

            default:
                break;
        }
        return false;
    }

    @Override
    public void onSuccess(Code code, JSONObject response) {
    }

    @Override
    public void onFailure(Code code, JSONObject errorResponse) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //   getMenuInflater().inflate(R.menu.menu_main_switch, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (mPagerManager.getCurrentPageIndex()) {
            case 0:
                menu.findItem(R.id.mSearch).setVisible(true);
               // menu.findItem(R.id.mSearch).setVisible(false);
                break;
            case 2:
                menu.findItem(R.id.mSearch).setVisible(true);
                // menu.findItem(R.id.mSearch1).setVisible(false);
                break;
            default:
                menu.findItem(R.id.mSearch).setVisible(false);
                //  menu.findItem(R.id.mSearch1).setVisible(false);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mSearch: {
                switch (mPagerManager.getCurrentPageIndex()) {
                    case 0:
                        Intent intent1 = new Intent(this,MessageCenterSearchActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent = new Intent(this,ContactsSearchActivity.class);
                        intent.putExtra("type", 0);
                        startActivity(intent);
                        break;
                }

            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mShortCutFragment == null || mShortCutFragment.isHidden()) {
                long duration = System.currentTimeMillis() - mFistPressBackTime;
                if (duration < BACK_DURATION) {
                    return super.onKeyDown(keyCode, event);
                } else {
                    mFistPressBackTime = System.currentTimeMillis();
//                    showToast("再按一次退出应用", Toast.LENGTH_SHORT);
                    MyToast.showToast("再按一次退出应用");
                    return false;
                }
            } else {
                hideShortCutView();
                return false;
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @OnClick(R.id.mShortCut)
    public void onClick() {
        if (mShortCutFragment == null || mShortCutFragment.isHidden()) {
            showShortCutView();
        } else {
            hideShortCutView();
        }
    }

    private void init() {
        AnyVersion version = AnyVersion.getInstance(this);
        version.setURL(RequestUtils.getUrlWithFlag(Code.APP_UPDATE));
        version.check(NotifyStyle.Dialog);

        mFragmentManager = getSupportFragmentManager();
    }

    /**
     * 显示快捷入口
     */
    private void showShortCutView() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (mShortCutFragment == null) {
            mShortCutFragment = new ShortCutFragment();
            transaction.add(R.id.mShortCutContainer, mShortCutFragment, mShortCutFragment.mTitle);
            mShortCutFragment.setOnItemSelectListener(new OnItemSelectListener() {
                @Override
                public void OnItemSelected() {
                    hideShortCutView();
                }
            });
        } else {
            transaction.show(mShortCutFragment);
            mShortCutFragment.show();
        }
        transaction.commit();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(mShortCut, "rotation", 0, 135));
        animatorSet.setInterpolator(new OvershootInterpolator());
        animatorSet.setDuration(300);
        animatorSet.start();
    }

    /**
     * 隐藏快捷入口
     */
    private void hideShortCutView() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.hide(mShortCutFragment);
        transaction.commit();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(mShortCut, "rotation", 45, 0));
        animatorSet.setInterpolator(new OvershootInterpolator());
        animatorSet.setDuration(400);
        animatorSet.start();
    }

    /**
     * ************* 设置别名 *************************
     */
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    System.out.println("Set alias in handler.");

                    Set<String> tag = new HashSet<String>();
                    tag.add("EnterpriseApp");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj, tag, mAliasCallback);

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
                    System.out.println(logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。

                    // 保存是否设置别名状态
                    PreferencesUtils.putBoolean(Main.this, KEY.ISSETALIAS, true);
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    System.out.println(logs);

                    // 保存是否设置别名状态
                    PreferencesUtils.putBoolean(Main.this, KEY.ISSETALIAS, false);

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
     * 设置个推推送
     *
     * @param alias
     */
    private void setGetui(String alias) {
        boolean isSuccess = PushManager.getInstance().bindAlias(Main.this, alias);
//        MyToast.showToast("设置alias：" + alias + "|   |" + isSuccess);

        /*String[] tags = alias.split(",");
        Tag[] tagParam = new Tag[tags.length];
        for (int i = 0; i < tags.length; i++) {
            Tag t = new Tag();
            t.setName(tags[i]);
            tagParam[i] = t;
        }
        int i = PushManager.getInstance().setTag(Main.this, tagParam, "" + System.currentTimeMillis());
        String text = "设置标签失败, 未知异常";

        // 这里的返回结果仅仅是接口调用是否成功, 不是真正成功, 真正结果见{
        // com.getui.demo.DemoIntentService.setTagResult 方法}
        switch (i) {
            case PushConsts.SETTAG_SUCCESS:
                text = "接口调用成功";
                break;
            case PushConsts.SETTAG_ERROR_COUNT:
                text = "接口调用失败, tag数量过大, 最大不能超过200个";
                break;
            case PushConsts.SETTAG_ERROR_FREQUENCY:
                text = "接口调用失败, 频率过快, 两次间隔应大于1s";
                break;
            case PushConsts.SETTAG_ERROR_NULL:
                text = "接口调用失败, tag 为空";
                break;
            default:
                break;
        }
        MyToast.showToast("设置tag："+text);*/
    }

    /**
     * 获取通讯录
     */
    public void findDepartmentContacts(Context context) {
        //请求网络
        Map<String, String> map = new HashMap<String, String>();

        callRequest(map, "baseUser/findDepartmentContacts.do", new HttpCallBack(Contacts.class, context) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    List<Contacts> list = mResult.getRows();
                    String json = JsonUtil.toJsonString(list);
                    //缓存数据到本地
                    PreferencesUtils.putString(BaseApp.CONTEXT, KEY.CONTACTLIST, json);

                    saveToLocal(list);
                } else {
                    //MyToast.showToast(mResult.getMsg());
                }
            }

            @Override
            public void onFailure(String string) {
            }
        });
    }

    //通讯录保存至本地
    private void saveToLocal(List<Contacts> list) {
        try {
            BaseApp.DBLoader.dropTable(Contacts.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        mDBUtils.deleteAll();
        Contacts[] array = new Contacts[list.size()];
        list.toArray(array);
        mDBUtils.add(array);
    }

}
