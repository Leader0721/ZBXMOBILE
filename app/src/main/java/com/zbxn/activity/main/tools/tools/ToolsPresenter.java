package com.zbxn.activity.main.tools.tools;

import com.zbxn.init.http.RequestParams;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.frame.mvp.AbsBasePresenterOld;
import com.zbxn.pub.frame.mvp.base.RequestResult;
import com.zbxn.pub.http.RequestUtils;
import com.zbxn.pub.http.Response;
import com.zbxn.utils.BaseModel;
import com.zbxn.pub.utils.JsonUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import utils.PreferencesUtils;

/**
 * @author LiangHanXin
 * @time 2016/9/12
 */
public class ToolsPresenter extends AbsBasePresenterOld<IToolsView> {
    private BaseModel model = new BaseModel(this);

    public ToolsPresenter(IToolsView controller) {
        super(controller);
    }

    @Override
    public void onSuccess(RequestUtils.Code code, JSONObject response, RequestResult result) {

        RequestResult requestResult = new RequestResult(0);
        try {
            switch (code) {
                case DYNAMIC_APP:
//                    if (response.optBoolean(Response.SUCCESS, true)) {
                    if ("0".equals(response.optString(Response.SUCCESS, "0"))) {
                        // 获取返回的值付给data
                        String rows = response.optString(Response.ROWS, null);

                        // 保存rows到本地
                        PreferencesUtils.putString(BaseApp.CONTEXT,"toolsList", rows);

                        List<RecTool> list = new ArrayList<>();
                        list = JsonUtil.fromJsonList(rows, RecTool.class);
                        mController.resetData(list);

                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {

        } finally {

        }
    }
    @Override
    public void onFailure(RequestUtils.Code code, JSONObject error, RequestResult result) {
        switch (code){
            case DYNAMIC_APP:
                if (error.optBoolean(Response.SUCCESS,false)){
                    mController.message().show("获取权限失败，请重新尝试..");
                }
                break;
        }


    }

    /**
     * 根据用户权限获取插件
     * 动态创建应用
     */
    public void getToolsByAuthor() {
        RequestParams params = new RequestParams();
        //将用户输入的账号保存起来，以便下次使用
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        params.put("tokenid", ssid);
        model.post(RequestUtils.Code.DYNAMIC_APP, params);
    }
}
