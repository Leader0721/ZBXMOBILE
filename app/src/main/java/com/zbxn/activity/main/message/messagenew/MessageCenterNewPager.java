package com.zbxn.activity.main.message.messagenew;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zbxn.R;
import com.zbxn.activity.main.message.MessageCenterPager;
import com.zbxn.bean.OaAlertList;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.data.ResultData;
import com.zbxn.pub.frame.base.BaseFragment;
import com.zbxn.pub.http.HttpCallBack;
import com.zbxn.pub.utils.JsonUtil;
import com.zbxn.pub.utils.MyToast;
import com.zbxn.utils.KEY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import utils.PreferencesUtils;
import utils.StringUtils;

/**
 * Created by Administrator on 2016/12/19.
 */
public class MessageCenterNewPager extends BaseFragment {
    public static String TITLE_MESSAGECENTER;
    @BindView(R.id.listView_MessageCenter)
    ListView listViewMessageCenter;
    private List<OaAlertList> mLists;
    private MessageCenterAdapter mMessageCenterAdapter;

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.main_messagecenternew, container, false);
        ButterKnife.bind(this, root);
        try {
            //获取本地缓存数据
            String messageList = PreferencesUtils.getString(BaseApp.CONTEXT, KEY.MESSAGELISTUNREAD, "[]");
            mLists = JsonUtil.fromJsonList(messageList, OaAlertList.class);
        } catch (Exception e) {
            mLists = new ArrayList<>();
        }

        mMessageCenterAdapter = new MessageCenterAdapter(mLists, getContext());
        listViewMessageCenter.setAdapter(mMessageCenterAdapter);
        listViewMessageCenter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageCenterPager.LABELKEY = mLists.get(position).getLabel() + "";
                MessageCenterPager.KEYWORD = "";
                MessageCenterNewPager.TITLE_MESSAGECENTER = mLists.get(position).getTitle();
                Intent intent = new Intent(getActivity(), MessageCenterDetailActivity.class);
                startActivity(intent);
            }
        });
        findBaseUserOaAlertUnRead();
        return root;
    }

    @Override
    public void onResume() {
        findBaseUserOaAlertUnRead();
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume
            findBaseUserOaAlertUnRead();
        } else {
            //相当于Fragment的onPause
        }
    }

    /**
     * 获取未读消息列表
     */
    public void findBaseUserOaAlertUnRead() {
        //请求网络
        Map<String, String> map = new HashMap<String, String>();

        callRequest(map, "oaAlert/findBaseUserOaAlertUnRead.do", new HttpCallBack(OaAlertList.class, getActivity()) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    if (!StringUtils.isEmpty(mResult.getRows())) {
                        mLists.clear();
                        mLists.addAll(mResult.getRows());
                        String json = JsonUtil.toJsonString(mLists);
                        //缓存数据到本地
                        PreferencesUtils.putString(BaseApp.CONTEXT, KEY.MESSAGELISTUNREAD, json);

                        mMessageCenterAdapter = new MessageCenterAdapter(mLists, getContext());
                        listViewMessageCenter.setAdapter(mMessageCenterAdapter);
                    }
                } else {
                    MyToast.showToast(mResult.getMsg());
                }
            }

            @Override
            public void onFailure(String string) {
            }
        });
    }


    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
