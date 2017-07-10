package com.zbxn.activity.searchcontacts;

import android.app.Activity;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.bean.adapter.ContactsAdapter;
import com.zbxn.pub.bean.adapter.base.IItemViewControl;
import com.zbxn.pub.bean.dbutils.DBUtils;
import com.zbxn.pub.frame.mvp.AbsBasePresenterOld;
import com.zbxn.pub.frame.mvp.base.RequestResult;
import com.zbxn.pub.http.RequestUtils;
import com.zbxn.pub.utils.MyToast;

import org.json.JSONObject;

import java.util.List;

/**
 * @author GISirFive
 * @time 2016/8/11
 */
public class SearchContactsPresenter extends AbsBasePresenterOld<ISearchContactsView> {

    private ContactsAdapter mAdapter;
    private DBUtils<Contacts> mDBUtils;

    public SearchContactsPresenter(ISearchContactsView controller) {
        super(controller);
        init();
    }

    @Override
    public void onSuccess(RequestUtils.Code code, JSONObject response, RequestResult result) {

    }

    @Override
    public void onFailure(RequestUtils.Code code, JSONObject error, RequestResult result) {

    }

    private void init() {
        mAdapter = new ContactsAdapter((Activity) mController, null);
        mAdapter.setOnDataItemCallbackListener((IItemViewControl<Contacts>) mController);
        mController.setAdapter(mAdapter);
        mDBUtils = new DBUtils<>(Contacts.class);
    }

    public void search() {
        String content = mController.getSearchContent();
        try {
            content = "%" + content + "%";
            List<Contacts> list = BaseApp.DBLoader.findAll(
                    Selector.from(Contacts.class)
                            .where("userName", "like", content)
                            .or("telephone", "like", content)
                            .or("loginname", "like", content)
                            .or("departmentName", "like", content)
                            .orderBy("captialChar", true));
            if (list == null || list.isEmpty()) {
                mAdapter.resetData(null);
//                mController.message().show("没找到对应的记录");
                MyToast.showToast("没找到对应的记录");
            } else {
                mAdapter.resetData(list);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
