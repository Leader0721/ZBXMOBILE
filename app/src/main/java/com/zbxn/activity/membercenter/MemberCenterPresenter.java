package com.zbxn.activity.membercenter;

import android.content.Context;
import android.widget.BaseAdapter;

import com.zbxn.bean.Member;
import com.zbxn.http.BaseAsyncTaskFile;
import com.zbxn.init.http.RequestParams;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.data.Result;
import com.zbxn.pub.data.base.BaseAsyncTaskInterface;
import com.zbxn.pub.frame.mvp.AbsBasePresenterOld;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.frame.mvp.base.RequestResult;
import com.zbxn.pub.http.RequestUtils;
import com.zbxn.pub.http.Response;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.utils.BaseModel;
import com.zbxn.pub.utils.JsonUtil;
import com.zbxn.pub.utils.MyToast;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.PreferencesUtils;
import utils.StringUtils;

/**
 * 项目名称：ZBXMobile
 * 创建人：LiangHanXin
 * 创建时间：2016/8/8 16:01
 */
public class MemberCenterPresenter extends AbsBasePresenterOld<IMemberCenterView> {

    private MemberInfoAdapter mAdapter;
    //    private Context mContext;
    private BaseModel model = new BaseModel(this);

    public MemberCenterPresenter(IMemberCenterView controller) {
        super(controller);
        AbsToolbarActivity activity = (AbsToolbarActivity) controller;
        mAdapter = new MemberInfoAdapter(activity, getMemberInfoWithList());
    }

    public BaseAdapter getAdapter() {
        return mAdapter;
    }

    public KeyValue getItem(int position) {
        return mAdapter.getItem(position);
    }

    public void resetData() {
        mAdapter.setData(getMemberInfoWithList());
    }



    @Override
    public void onSuccess(RequestUtils.Code code, JSONObject response, RequestResult result) {
        switch (code) {
            case USER_UPDATE:
                //    String data=response.optBoolean(Response.DATA);
                String data = response.optString(Response.DATA);
                Member member = JsonUtil.fromJsonString(data, Member.class);
                Member.save(member);
                mController.loading().hideDelay(2000);
//                mController.message().show("修改成功");
                MyToast.showToast("修改成功");
                break;
            case USER_DETAIL:
                String date = response.optString(Response.DATA);
                Member member1 = JsonUtil
                        .fromJsonString(date, Member.class);
                break;
        }
    }

    @Override
    public void onFailure(RequestUtils.Code code, JSONObject error, RequestResult result) {

        switch (code) {
            case USER_UPDATE:
                mController.loading().hide();
//                    mController.message().show("修改失败");
             //   String data = result.optString(Response.DATA);
                MyToast.showToast("修改失败,"+result.message);
                break;
        }

    }

    /**
     * 提交修改
     *
     * @param key   上传的数据对应的key
     * @param value 数据对应的value
     */
    public void submitModify(String key, String value) {
        RequestParams params = new RequestParams();
        params.put(key, value);
        //将用户输入的账号保存起来，以便下次使用
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        params.put("tokenid", ssid);
        // 参数是地址 与参数
        model.post(RequestUtils.Code.USER_UPDATE, params);

        mController.loading().show("正在提交");
    }

    /**
     * 添加员工的基本信息包括生日的转换
     */
    private List<KeyValue> getMemberInfoWithList() {
        Member member = Member.get();
        List<KeyValue> data = new ArrayList<KeyValue>();
        data.add(new KeyValue("账号", member.getNumber(), null));
        data.add(new KeyValue("姓名", member.getUserName(), "base.userName"));
        data.add(new KeyValue("手机号", member.getLoginname(), "staff.loginName"));
        data.add(new KeyValue("电话", member.getTelephone(), "staff.telephone"));
        data.add(new KeyValue("邮箱", member.getEmail(), "base.email"));
        // 时间转换 Date转换成String类型已 年-月-日的形式输出
        if (member.getBirthday() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String startTime = sdf.format(member.getBirthday());
            data.add(new KeyValue("生日", startTime, null));
        } else {
            data.add(new KeyValue("生日", null, null));
        }
        if (!StringUtils.isEmpty(member.getAddress())){

            data.add(new KeyValue("联系地址", member.getAddress().trim(), "staff.addressNow"));
        }

        data.add(new KeyValue("所在部门", member.getDepartmentName(), null));
        return data;
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
