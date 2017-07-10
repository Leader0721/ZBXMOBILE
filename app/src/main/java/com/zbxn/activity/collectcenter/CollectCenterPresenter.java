package com.zbxn.activity.collectcenter;

import android.content.Context;
import android.util.Log;

import com.zbxn.init.http.RequestParams;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.Bulletin;
import com.zbxn.pub.bean.adapter.BulletinAdapter;
import com.zbxn.pub.bean.adapter.base.IItemViewControl;
import com.zbxn.pub.frame.mvp.AbsBasePresenterOld;
import com.zbxn.pub.frame.mvp.base.RequestResult;
import com.zbxn.pub.http.RequestUtils;
import com.zbxn.utils.BaseModel;
import com.zbxn.pub.utils.ListviewUpDownHelper;
import com.zbxn.pub.utils.MyToast;

import org.json.JSONObject;

import java.util.List;

import utils.PreferencesUtils;

/**
 * @author GISirFive
 * @time 2016/8/18
 */
public class CollectCenterPresenter extends AbsBasePresenterOld<ICollectCenterView> implements
        ListviewUpDownHelper.OnRequestDataListener {

    private BaseModel mBaseModel;
    private BulletinAdapter mAdapter;

    public CollectCenterPresenter(ICollectCenterView controller) {
        super(controller);
        init();
        mAdapter.setOnDataItemCallbackListener((IItemViewControl) controller);
    }

    @Override
    public void onSuccess(RequestUtils.Code code, JSONObject response, RequestResult result) {
        switch (code) {
            case COLLECT_LIST_TOP:
                List<Bulletin> list = (List<Bulletin>) result.obj1;
                if (list == null || list.isEmpty()) {
//                    mController.message().show("收藏列表为空");
                    MyToast.showToast("收藏列表为空");
                } else {
                    mAdapter.resetData(list);
                }
                mController.refreshComplete();
                break;
            case COLLECT_LIST_BOTTOM:
                list = (List<Bulletin>) result.obj1;
                int count = mAdapter.addInBottom(list);
                mController.loadMoreComplete(false, count != 0);
                break;
        }
    }

    @Override
    public void onFailure(RequestUtils.Code code, JSONObject error, RequestResult result) {
        String message = result.message;
//        mController.message().show(message);
        MyToast.showToast(message);
        mController.loadMoreComplete(false, false);
        mController.refreshComplete();
    }

    private void init() {
        mBaseModel = new BaseModel(this);
        mAdapter = new BulletinAdapter((Context) mController, null);

        mController.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        RequestParams params = new RequestParams();
        params.put("page", "1");
        params.put("rows", 6);
        //将用户输入的账号保存起来，以便下次使用
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        params.put("tokenid", ssid);
        Log.e("tokenid", ssid);
        mBaseModel.post(RequestUtils.Code.COLLECT_LIST_TOP, params);
    }

    @Override
    public void onLoadMore() {
        RequestParams params = new RequestParams();
        params.put("page", mAdapter.getPage());
        params.put("rows", 6);
        //将用户输入的账号保存起来，以便下次使用
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        params.put("tokenid", ssid);
        mBaseModel.post(RequestUtils.Code.COLLECT_LIST_BOTTOM, params);
    }

    /**
     * 取消收藏
     *
     * @param position
     */
    public void cancelCollect(int position) {
        Bulletin bulletin = mAdapter.getItem(position);
        RequestParams params = new RequestParams();
        params.put("bulletinId", bulletin.getId());
        params.put("isCollect", "false");
        //将用户输入的账号保存起来，以便下次使用
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        params.put("tokenid", ssid);
        mBaseModel.post(RequestUtils.Code.ALERT_COLLECT, params);
        mAdapter.getDataList().remove(position);
        mAdapter.notifyDataSetChanged();
    }
}
