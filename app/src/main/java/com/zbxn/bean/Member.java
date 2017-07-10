package com.zbxn.bean;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.google.gson.annotations.Expose;
import com.zbxn.activity.login.LoginActivity;
import com.zbxn.http.BaseAsyncTask;
import com.zbxn.init.eventbus.EventMember;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.data.ResultParse;
import com.zbxn.pub.data.base.BaseAsyncTaskInterface;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.ConfigUtils.KEY;
import com.zbxn.pub.utils.JsonUtil;

import org.simple.eventbus.EventBus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import utils.PreferencesUtils;
import utils.StringUtils;

import static utils.PreferencesUtils.getString;

public class Member extends Contacts {

    // 用户基本信息
    public static Member MEMBER = null;

    /**
     * AndroidManifest.xml中的每个Activity的授权信息
     */
    private static Map<String, Bundle> authorMap = null;

    /**
     * 是否正在执行登录操作
     */
    private static boolean isLogining = false;

    @Expose
    private String password;// 密码
    @Expose
    private Date birthday;// 出生日期
    @Expose
    private String address;// 住址

    @Expose
    private String ssid;// ssid

    @Expose
    private String zmsCompanyId;// zmsCompanyId 当前所在公司id

    private int blogStateToday = -1;//当天是否写了日志 -1=未查询 0=未写 1=已写

    @Expose
    private String zmsCompanyName;//公司名称

    public String getZmsCompanyName() {
        return zmsCompanyName;
    }

    public void setZmsCompanyName(String zmsCompanyName) {
        this.zmsCompanyName = zmsCompanyName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getBlogStateToday() {
        return blogStateToday;
    }

    public static Member getMEMBER() {
        return MEMBER;
    }

    public static void setMEMBER(Member MEMBER) {
        Member.MEMBER = MEMBER;
    }

    /**
     * 当天是否写了日志
     *
     * @param state -1=未查询 0=未写 1=已写
     */
    public void setBlogStateToday(int state) {
        this.blogStateToday = state;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getZmsCompanyId() {
        return zmsCompanyId;
    }

    public void setZmsCompanyId(String zmsCompanyId) {
        this.zmsCompanyId = zmsCompanyId;
    }

    /**
     * 读取用户信息
     *
     * @return
     * @author GISirFive
     */
    public static Member get() {
        if (null==MEMBER) {
           String json= PreferencesUtils.getString(BaseApp.CONTEXT, ConfigUtils.getConfig(KEY.userInfo), "{}");
            MEMBER = JsonUtil.fromJsonString(json,Member.class);
        }
        return MEMBER;
    }

    /**
     * 保存用户信息至本地缓存中
     *
     * @param member
     * @author GISirFive
     */
    public static void save(Member member) {
        MEMBER = member;

        // 保存至本地
        String json = JsonUtil.gson.toJson(member);
        PreferencesUtils.putString(BaseApp.CONTEXT,
                ConfigUtils.getConfig(KEY.userInfo), json);

        if (member != null) {
            EventMember event = new EventMember();
            EventBus.getDefault().post(event);
        }
    }

    /**
     * 清除Member，包括本地缓存
     *
     * @author GISirFive
     */
    public static void clear() {
        MEMBER = null;
        PreferencesUtils.putString(BaseApp.CONTEXT,
                ConfigUtils.getConfig(KEY.userInfo), null);
        PreferencesUtils.putString(BaseApp.CONTEXT, "password", null);
        EventBus.getDefault().post(new EventMember());
    }

    /**
     * 检查要访问的页面是否有权访问<br>
     * 若用户未登录，自动打开登录页面
     *
     * @param clazz
     * @return
     * @author GISirFive
     */
    public static boolean checkAuthorize(Class<?> clazz) {
        /*if (clazz == null)
            return false;
        if (authorMap == null)
            authorMap = AuthorUtils.getAuthorMap();
        if (!authorMap.containsKey(clazz.getName()))
            return false;
        Bundle bundle = authorMap.get(clazz.getName());
        // 神马都不验证
        if (bundle == null)
            return true;
        // 验证登录，默认不验证
        boolean login = bundle.getBoolean(Author.verifyLogin.toString(), false);
        if (!login)// 不验证登录
            return true;
        if (!isLogin())// 未登录，打开登录页面
            loginWithActivity(null, 0);

        Toast.makeText(BaseApp.CONTEXT, "您无权访问该页面，请验证身份", Toast.LENGTH_LONG)
                .show();*/
        return false;
    }

    /**
     * 检查用户是否已登录
     *
     * @return
     * @author GISirFive
     */
    public static boolean isLogin() {
        if (MEMBER != null)
            return true;
        /**
         * 如果当前正在登录，算未登录
         */
        return isLogining;
    }

    /**
     * 检查是否有本地缓存的用户信息
     *
     * @return
     * @author GISirFive
     */
    public static boolean localCacheExist() {
        String data = getString(BaseApp.CONTEXT,
                ConfigUtils.getConfig(KEY.userInfo));
        if (data == null || data.length() == 0)
            return false;
        Member member = JsonUtil.fromJsonString(data, Member.class);
        return member != null;
    }

    /**
     * 获取本地缓存
     *
     * @return
     * @author GISirFive
     */
    public static Member getLocalCache() {
        if (!localCacheExist())
            return null;
        String data = getString(BaseApp.CONTEXT,
                ConfigUtils.getConfig(KEY.userInfo));
        return JsonUtil.fromJsonString(data, Member.class);
    }

    /**
     * 打开登录界面
     *
     * @param callbackActivity 接收登录回调的Activity
     * @author GISirFive
     */
    public static void loginWithActivity(Activity callbackActivity, int requestCode) {
        /*Intent intent = new Intent(BaseApp.CONTEXT, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (callbackActivity == null) {
            BaseApp.CONTEXT.startActivity(intent);
        } else {
            callbackActivity.startActivityForResult(intent, requestCode);
        }*/
    }

    /**
     * 登录
     */
    public void login(Context context, final String userName, final String password, final ICustomListener listener) {
        //请求网络
        Map<String, String> map = new HashMap<String, String>();
        map.put("loginName", userName);
        map.put("password", password);
        map.put("ip", "android");
        map.put("uuid", StringUtils.getPushGuid(context));
        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTask(context, false, 0, server + "/baseUser/doLogin.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {
                isLogining = true;
            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultParse<Member>().parse(json, Member.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultParse mResult = (ResultParse) result;
                if ("0".equals(mResult.getSuccess()) || "100".equals(mResult.getSuccess())) {//0成功   100未完善信息
                    Member entity = (Member) mResult.getData();

                    //将用户输入的密码保存起来，以便下次使用
                    PreferencesUtils.putString(BaseApp.getContext(), LoginActivity.FLAG_INPUT_PASSWORD, password);
                    //将用户输入的账号保存起来，以便下次使用
                    PreferencesUtils.putString(BaseApp.getContext(),
                            LoginActivity.FLAG_INPUT_USERNAME, userName);
                    //登录用户头像
                    PreferencesUtils.putString(BaseApp.getContext(),
                            LoginActivity.FLAG_INPUT_HEADURL, entity.getPortrait());
                    //登录用户id
                    PreferencesUtils.putString(BaseApp.getContext(),
                            LoginActivity.FLAG_INPUT_ID, entity.getId() + "");
                    //用户默认职位权限
                    PreferencesUtils.putString(BaseApp.getContext(),
                            LoginActivity.FLAG_PERMISSIONIDS, entity.getPermissionIds() + "");

                    //将用户输入的账号保存起来，以便下次使用
                    PreferencesUtils.putString(BaseApp.getContext(),
                            LoginActivity.FLAG_SSID, entity.getSsid());

                    PreferencesUtils.putString(BaseApp.getContext(),
                            LoginActivity.FLAG_ZMSCOMPANYID, entity.getZmsCompanyId());

                    if ("100".equals(mResult.getSuccess())) {
                        PreferencesUtils.putString(BaseApp.getContext(),
                                LoginActivity.FLAG_INFO_MSG, mResult.getMsg());
                    }else{
                        PreferencesUtils.putString(BaseApp.getContext(),
                                LoginActivity.FLAG_INFO_MSG, "");
                    }

                    MEMBER = entity;

                    listener.onCustomListener(0, null, 0);
                } else {
                    clear();
                    listener.onCustomListener(1, null, 0);
                }
            }

            @Override
            public void dataError(int funId) {
                listener.onCustomListener(1, null, 0);
                clear();
            }
        }).execute(map);
    }
}
