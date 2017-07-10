package com.zbxn.fragment.addrbookgroup;

import com.zbxn.fragment.addrbookpy.AddrBookModel;
import com.zbxn.init.http.RequestParams;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.bean.dbutils.DBUtils;
import com.zbxn.pub.frame.mvp.AbsBasePresenterOld;
import com.zbxn.pub.frame.mvp.base.RequestResult;
import com.zbxn.pub.http.RequestUtils;
import com.zbxn.pub.utils.JsonUtil;
import com.zbxn.utils.KEY;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import utils.PreferencesUtils;
import utils.StringUtils;

/**
 * 通讯录按部门分组
 *
 * @author GISirFive
 * @time 2016/8/8
 */
public class AddrBookPresenter extends AbsBasePresenterOld<IAddrBookView> {

    private DBUtils<Contacts> mDBUtils;

    private AddrBookModel mModel;

    /**
     * 选择联系人监听
     */
    private OnContactsPickerListener mPickerListener;

    /**
     * 选择联系人监听
     *
     * @param listener
     */
    public void setOnContactsPickerListener(OnContactsPickerListener listener) {
        mPickerListener = listener;
    }

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

    public void resetData(List<Contacts> list) {
        List<Contacts> listTemp = new ArrayList<>();
        if (StringUtils.isEmpty(list)) {
            list = new ArrayList<>();
        }
        for (int i = 0; i < list.size(); i++) {
            if (StringUtils.isEmpty(list.get(i).getIsDepartment())) {
                listTemp.add(list.get(i));
            }
        }
        mController.getAdapter().resetData(listTemp);
//        saveToLocal(list);
    }

    //通讯录保存至本地
    private void saveToLocal(List<Contacts> list) {
        mDBUtils.deleteAll();
        Contacts[] array = new Contacts[list.size()];
        list.toArray(array);
        mDBUtils.add(array);
    }

    /**
     * 加载数据
     */
    private void init() {
        mDBUtils = new DBUtils<>(Contacts.class);
        List<Contacts> list = mDBUtils.queryAll();

        //缓存数据到本地
        String json = PreferencesUtils.getString(BaseApp.CONTEXT, KEY.CONTACTLIST, "[]");
        list = JsonUtil.fromJsonList(json, Contacts.class);

        if (StringUtils.isEmpty(list)) {
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

//    public boolean getMultiChoiceEnable() {
//        return mAdapter.getMultiChoiceEnable();
//    }
//
//    /**
//     * 设置当前列表是否为多选模式
//     *
//     * @param b
//     */
//    public void setMultiChoiceMode(boolean b) {
//        mAdapter.setMultiChoiceEnable(b);
//        if (b) {
//            if (mSelectedList == null) {
//                mSelectedList = new ArrayList<>();
//            } else {
//                mSelectedList.clear();
//            }
//        }
//    }
//
//    public void selectItem(int position, boolean check) {
//
//    }
//
//    public List<Contacts> getSelectedList() {
//        return mSelectedList;
//    }

}
