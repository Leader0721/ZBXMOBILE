package com.zbxn.activity.main.member.memberCenterPager;

import android.content.Context;

import com.zbxn.bean.Member;
import com.zbxn.http.BaseAsyncTaskFile;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.data.Result;
import com.zbxn.pub.data.base.BaseAsyncTaskInterface;
import com.zbxn.pub.frame.mvp.AbsBasePresenterOld;
import com.zbxn.pub.frame.mvp.base.RequestResult;
import com.zbxn.pub.http.RequestUtils;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.MyToast;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import utils.PreferencesUtils;

/**
 * 项目名称：ZBXMobile
 * 创建人：LiangHanXin
 * 创建时间：2016/8/4 9:23
 */
public class MemberCenterPagerPresenter extends AbsBasePresenterOld<IMemberCenterPagerView> {
    public MemberCenterPagerPresenter(IMemberCenterPagerView controller) {
        super(controller);
    }

    @Override
    public void onSuccess(RequestUtils.Code code, JSONObject response, RequestResult result) {

    }

    @Override
    public void onFailure(RequestUtils.Code code, JSONObject error, RequestResult result) {

    }

    /**
     * 清理缓存
     */

    public void logout() {
        Member.clear();
        mController.Jump();
    }

    /**
     * 获取审批主界面的图标等
     *
     * @param context
     */
    public void uploadFile(Context context, File file, final ICustomListener listener) {
        mController.loading().show("加载中...");
        Map<String, String> map = new HashMap<String, String>();

        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);

        Map<String, File> mapFile = new HashMap<>();
        mapFile.put("image", file);

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTaskFile(context, false, 0, server + "/common/doupload.do", new BaseAsyncTaskInterface() {
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
                    listener.onCustomListener(0, mResult.getData(), 0);
                } else {
                    String message = mResult.getMsg();
                    MyToast.showToast( message);
                }
                mController.loading().hide();
            }

            @Override
            public void dataError(int funId) {
                MyToast.showToast( "获取网络数据失败");
                mController.loading().hide();
            }
        }, mapFile).execute(map);
    }

}


