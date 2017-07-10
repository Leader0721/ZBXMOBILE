package com.zbxn.activity.switchcompany;

import android.content.Context;

import com.zbxn.activity.login.LoginActivity;
import com.zbxn.bean.RepeatNewTaskEntity;
import com.zbxn.http.BaseAsyncTask;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.ZmsCompanyListBean;
import com.zbxn.pub.data.ResultData;
import com.zbxn.pub.data.base.BaseAsyncTaskInterface;
import com.zbxn.pub.frame.mvp.AbsBasePresenter;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.frame.mvp.base.ControllerCenter;
import com.zbxn.pub.frame.mvp.base.RequestResult;
import com.zbxn.pub.http.RequestUtils;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.MyToast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.PreferencesUtils;

/**
 * @author GISirFive
 * @time 2016/8/11
 */
public class SwitchCompanyPresenter extends AbsBasePresenter<ControllerCenter> {

    public SwitchCompanyPresenter(AbsToolbarActivity activity) {
        super(activity);
    }

    @Override
    public void onSuccess(RequestUtils.Code code, JSONObject response, RequestResult result) {

    }

    @Override
    public void onFailure(RequestUtils.Code code, JSONObject error, RequestResult result) {

    }

    public void findCompany(Context context, final ICustomListener listener) {
        mController.loading().show("正在加载...");
        //请求网络
        Map<String, String> map = new HashMap<String, String>();
        //将用户输入的账号保存起来，以便下次使用
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTask(context, false, 0, server + "/baseZMSCompany/findCompany.do", new BaseAsyncTaskInterface() {

            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultData<ZmsCompanyListBean>().parse(json, ZmsCompanyListBean.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                mController.loading().hide();
                ResultData mResult = (ResultData) result;
                if ("0".equals(mResult.getSuccess())) {//0成功
                    List<ZmsCompanyListBean> list = mResult.getRows();
                    listener.onCustomListener(0, list, 0);
                } else {
                    MyToast.showToast( mResult.getMsg());
                }
            }

            @Override
            public void dataError(int funId) {
                MyToast.showToast( "获取网络数据失败");
                mController.loading().hide();
            }
        }).execute(map);
    }

    /**
     * 切换公司
     *
     * @param context
     * @param zmsCompanyIdOld
     * @param zmsCompanyIdNow
     * @param listener
     */
    public void updateDefaultCompany(final Context context, String zmsCompanyIdOld, final String zmsCompanyIdNow, final ICustomListener listener) {
        mController.loading().show("正在加载...");
        //请求网络
        Map<String, String> map = new HashMap<String, String>();
        //将用户输入的账号保存起来，以便下次使用
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);
        map.put("zmsCompanyIdOld", zmsCompanyIdOld);//初始化公司id
        map.put("zmsCompanyIdNow", zmsCompanyIdNow);//切换的公司id

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTask(context, false, 0, server + "/baseZMSCompany/updateDefaultCompany.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultData<RepeatNewTaskEntity>().parse(json, RepeatNewTaskEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultData mResult = (ResultData) result;
                if ("0".equals(mResult.getSuccess())) {//0成功
                    PreferencesUtils.putString(BaseApp.getContext(),
                            LoginActivity.FLAG_ZMSCOMPANYID, zmsCompanyIdNow);
                    mController.message().show("切换成功");
                    listener.onCustomListener(1, null, 0);
                    mController.loading().hide();
                } else {
                    String message = mResult.getMsg();
                    MyToast.showToast( message);
                    mController.loading().hide();
                }
            }

            @Override
            public void dataError(int funId) {
                MyToast.showToast( "获取网络数据失败");
                mController.loading().hide();
            }
        }).execute(map);
    }
}
