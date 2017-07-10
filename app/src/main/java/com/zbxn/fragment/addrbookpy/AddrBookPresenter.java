package com.zbxn.fragment.addrbookpy;

import android.support.v4.app.Fragment;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.zbxn.init.eventbus.EventRefreshContacts;
import com.zbxn.init.http.RequestParams;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.bean.adapter.base.IItemViewControl;
import com.zbxn.pub.bean.dbutils.DBUtils;
import com.zbxn.pub.frame.mvp.AbsBasePresenterOld;
import com.zbxn.pub.frame.mvp.base.RequestResult;
import com.zbxn.pub.http.RequestUtils;
import com.zbxn.pub.utils.JsonUtil;
import com.zbxn.utils.KEY;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.util.List;

import utils.PreferencesUtils;

/**
 * @author GISirFive
 * @time 2016/8/8
 */
public class AddrBookPresenter extends AbsBasePresenterOld<IAddrBookView> {

    private DBUtils<Contacts> mDBUtils;
    private AddrBookModel mModel;
    private ContactAdapter mAdapter;

    public AddrBookPresenter(IAddrBookView controller) {
        super(controller);
        mModel = new AddrBookModel(this);
        init();
    }

    @Override
    public void onSuccess(RequestUtils.Code code, JSONObject response, RequestResult result) {
        try {
            switch (code) {
                case ADDRESSBOOK_LIST:
                    List<Contacts> list = (List<Contacts>) result.obj1;
                    resetData(list);
                    mController.refreshComplete();
                    EventRefreshContacts event = new EventRefreshContacts();
                    EventBus.getDefault().post(event);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(RequestUtils.Code code, JSONObject error, RequestResult result) {
        try {
            switch (code) {
                case ADDRESSBOOK_LIST:
                    mController.message().show(result.message);
                    mController.refreshComplete();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void resetData(List<Contacts> list) {
        if (mAdapter == null) {
            mAdapter = new ContactAdapter(((Fragment) mController).getContext(), null);
            mAdapter.setOnDataItemCallbackListener((IItemViewControl<Contacts>) mController);
            mAdapter.resetData(list);
            mController.setAdapter(mAdapter);
        } else {
            mAdapter.resetData(list);
        }
    }

    /**
     * 加载数据
     */
    private void init() {
        mDBUtils = new DBUtils<>(Contacts.class);
        List<Contacts> list = null;
        try {
            list = BaseApp.DBLoader.findAll(Selector.from(Contacts.class).orderBy("captialChar"));

            //缓存数据到本地
            String json = PreferencesUtils.getString(BaseApp.CONTEXT, KEY.CONTACTLIST, "[]");
            list = JsonUtil.fromJsonList(json, Contacts.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (list == null || list.isEmpty()) {
            mController.autoRefresh();
        } else {
            resetData(list);
        }
    }

    public void refresh() {
        RequestParams params = new RequestParams();
        //将用户输入的账号保存起来，以便下次使用
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        params.put("tokenid", ssid);
        mModel.post(RequestUtils.Code.ADDRESSBOOK_LIST, params);
    }
}
