package com.zbxn.activity.main.message.messagepager;

import android.widget.BaseAdapter;

import com.zbxn.activity.main.message.MessageCenterPager;
import com.zbxn.utils.BaseModel;
import com.zbxn.init.http.RequestParams;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.Bulletin;
import com.zbxn.pub.bean.adapter.BulletinAdapter;
import com.zbxn.pub.bean.adapter.base.IItemViewControl;
import com.zbxn.pub.bean.dbutils.BulletinDBUtils;
import com.zbxn.pub.frame.base.BaseFragment;
import com.zbxn.pub.frame.mvp.AbsBasePresenterOld;
import com.zbxn.pub.frame.mvp.base.RequestResult;
import com.zbxn.pub.http.RequestUtils;
import com.zbxn.pub.http.Response;
import com.zbxn.pub.utils.JsonUtil;
import com.zbxn.pub.utils.ListviewUpDownHelper;
import com.zbxn.utils.KEY;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import utils.PreferencesUtils;
import utils.StringUtils;

/**
 * Created by Administrator on 2016/8/3.
 */
public class MessagePresenter extends AbsBasePresenterOld<IMessageView> implements ListviewUpDownHelper.OnRequestDataListener {

    private BaseModel mBaseModel;

    private BulletinAdapter mAdapter;
    private BulletinDBUtils mDbUtils;


    public MessagePresenter(IMessageView controller) {
        super(controller);

        BaseFragment fragment = (BaseFragment) controller;
        mAdapter = new BulletinAdapter(fragment.getContext(), null);
        mAdapter.setOnDataItemCallbackListener((IItemViewControl) controller);

        mDbUtils = BulletinDBUtils.getInstance();
        mBaseModel = new BaseModel(this);
    }

    @Override
    public void onSuccess(RequestUtils.Code code, JSONObject response, RequestResult result) {
        String rows;
        List<Bulletin> list;
        Bulletin mBulletin = new Bulletin();
        mBulletin.setLabel(-1);
        mBulletin.setTitle("系统消息");
        mBulletin.setContent("敬请期待新功能上线");
        switch (code) {
            case BULLETIN_LIST_TOP:
                //List<Bulletin> list = (List<Bulletin>) result.obj1;
                rows = response.optString(Response.ROWS, null);
                //缓存数据到本地
                PreferencesUtils.putString(BaseApp.CONTEXT, MessageCenterPager.LABELKEY + KEY.MESSAGELIST, rows);
                list = JsonUtil.fromJsonList(rows, Bulletin.class);
                // list.add(0, mBulletin);
                mAdapter.resetData(list);
                mController.refreshComplete();
                break;
            case BULLETIN_LIST_BOTTOM:
                //list = (List<Bulletin>) result.obj1;
                rows = response.optString(Response.ROWS, null);
                list = JsonUtil.fromJsonList(rows, Bulletin.class);
                //  list.add(0, mBulletin);
                int count = mAdapter.addInBottom(list);
                mController.loadMoreComplete(false, count != 0);
                break;
        }
    }

    @Override
    public void onFailure(RequestUtils.Code code, JSONObject error, RequestResult result) {
        String message = result.message;
        mController.message().show(message);
        mController.refreshComplete();
    }

    @Override
    public void onRefresh() {
        RequestParams params = new RequestParams();
        params.put("page", "1");
        params.put("rows", 8);
        //将用户输入的账号保存起来，以便下次使用
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        params.put("tokenid", ssid);
        params.put("label", MessageCenterPager.LABELKEY);
        params.put("keyword", MessageCenterPager.KEYWORD);
        mBaseModel.post(RequestUtils.Code.BULLETIN_LIST_TOP, params);
    }

    @Override
    public void onLoadMore() {
        RequestParams params = new RequestParams();
        params.put("page", mAdapter.getPage());
        params.put("rows", 8);
        //将用户输入的账号保存起来，以便下次使用
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        params.put("tokenid", ssid);
        params.put("label", MessageCenterPager.LABELKEY);
        params.put("keyword", MessageCenterPager.KEYWORD);
        mBaseModel.post(RequestUtils.Code.BULLETIN_LIST_BOTTOM, params);
    }

    // 将通知公告标记为已读或未读  state  标志位 (0:未读  1:已读)
    public void setRead(int position, String state) {
        Bulletin bulletin = mAdapter.getItem(position);
        if (bulletin.isRead() == 1)
            return;
        bulletin.setRead(1);
        mAdapter.notifyDataSetChanged();
        // 更新本地数据库
        mDbUtils.update(bulletin, "isRead");
        // 提交服务器
        RequestParams params = new RequestParams();
        params.put("id", String.valueOf(bulletin.getId()));
        //将用户输入的账号保存起来，以便下次使用
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        params.put("tokenid", ssid);
        params.put("state", state);
        mBaseModel.post(RequestUtils.Code.BULLETIN_SETREAD, params);
    }

    public BaseAdapter getAdapter() {
        return mAdapter;
    }

    public Bulletin getItem(int position) {
        return mAdapter.getItem(position);
    }

    /**
     * 加载本地缓存
     *
     * @return
     */
    public void loadLocalCache() {
        List<Bulletin> list = null;
        Bulletin mBulletin = new Bulletin();
        mBulletin.setLabel(-1);
        mBulletin.setTitle("系统消息");
        mBulletin.setContent("敬请期待新功能上线");
        try {
            //获取本地缓存数据
            String messageList = PreferencesUtils.getString(BaseApp.CONTEXT, MessageCenterPager.LABELKEY + KEY.MESSAGELIST, "[]");
            list = JsonUtil.fromJsonList(messageList, Bulletin.class);
        } catch (Exception e) {
            list = new ArrayList<>();
        }
        //      list.add(0, mBulletin);
        if (!StringUtils.isEmpty(list)) {
            mAdapter.resetData(list);
            mController.showRefresh(1000);
        } else {
            mController.showRefresh(0);
        }
        return;
    }

    // 保存到本地
    private void saveToLocal(List<Bulletin> list) {
        List<Bulletin> temp = mDbUtils.queryAll();
        if (temp == null || temp.isEmpty()) {
            Bulletin[] arrays = new Bulletin[list.size()];
            list.toArray(arrays);
            mDbUtils.add(arrays);
        } else {
            for (Bulletin bulletin : list) {
                Bulletin b = mDbUtils.queryById(bulletin.getId() + "");
                if (b == null)
                    mDbUtils.add(b);
            }
        }
    }
}
