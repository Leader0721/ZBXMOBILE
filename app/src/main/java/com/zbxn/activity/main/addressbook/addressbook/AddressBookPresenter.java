package com.zbxn.activity.main.addressbook.addressbook;

import com.zbxn.init.http.RequestParams;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.bean.dbutils.DBUtils;
import com.zbxn.pub.frame.mvp.AbsBasePresenterOld;
import com.zbxn.pub.frame.mvp.base.RequestResult;
import com.zbxn.pub.http.RequestUtils;
import com.zbxn.pub.utils.JsonUtil;
import com.zbxn.utils.BaseModel;
import com.zbxn.utils.KEY;

import org.json.JSONObject;

import java.util.List;

import utils.PreferencesUtils;

/**
 * @author GISirFive
 * @time 2016/8/10
 */
public class AddressBookPresenter extends AbsBasePresenterOld<IAddressBookView> {

    private DBUtils<Contacts> mDBUtils;
    private BaseModel mBaseModel;

    public AddressBookPresenter(IAddressBookView controller) {
        super(controller);
        mBaseModel = new BaseModel(this);
    }

    @Override
    public void onSuccess(RequestUtils.Code code, JSONObject response, RequestResult result) {
        List<Contacts> list = (List<Contacts>) result.obj1;
        saveToLocal(list);
        mController.loading().hideDelay(0);
        mController.initViewPager();
    }

    @Override
    public void onFailure(RequestUtils.Code code, JSONObject error, RequestResult result) {
        mController.loading().hideDelay(0);
        mController.initViewPager();
    }

    /**
     * 重新加载数据
     */
    public void loadData() {
        if (mDBUtils == null)
            mDBUtils = new DBUtils<>(Contacts.class);
        List<Contacts> list = mDBUtils.queryAll();

        //缓存数据到本地
        String json = PreferencesUtils.getString(BaseApp.CONTEXT, KEY.CONTACTLIST, "[]");
        list = JsonUtil.fromJsonList(json, Contacts.class);

        if (list == null || list.isEmpty()) {//本地无缓存
            mController.loading().show();
            RequestParams params = new RequestParams();
            //将用户输入的账号保存起来，以便下次使用
            String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
            params.put("tokenid", ssid);
            mBaseModel.post(RequestUtils.Code.ADDRESSBOOK_LIST, params);
        } else {//加载缓存
            mController.loading().hideDelay(0);
            mController.initViewPager();
        }
    }

    //通讯录保存至本地
    private void saveToLocal(List<Contacts> list) {
        mDBUtils.deleteAll();
        Contacts[] array = new Contacts[list.size()];
        list.toArray(array);
        mDBUtils.add(array);
    }

}
