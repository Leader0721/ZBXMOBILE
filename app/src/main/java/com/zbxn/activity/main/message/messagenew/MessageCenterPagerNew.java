package com.zbxn.activity.main.message.messagenew;


import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.activity.main.message.MessageCenterPager;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.Bulletin;
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
import butterknife.OnClick;
import utils.PreferencesUtils;
import utils.StringUtils;

/**
 * Created by Administrator on 2016/12/15.
 */
public class MessageCenterPagerNew extends BaseFragment {
    @BindView(R.id.imageView_gonggao)
    ImageView imageViewGonggao;
    @BindView(R.id.imageView1_gonggao)
    ImageView imageView1Gonggao;
    @BindView(R.id.title_gonggao)
    TextView titleGonggao;
    @BindView(R.id.content_gonggao)
    TextView contentGonggao;
    @BindView(R.id.imageView_richeng)
    ImageView imageViewRicheng;
    @BindView(R.id.imageView1_richeng)
    ImageView imageView1Richeng;
    @BindView(R.id.title_richeng)
    TextView titleRicheng;
    @BindView(R.id.content_richeng)
    TextView contentRicheng;
    @BindView(R.id.imageView_renwu)
    ImageView imageViewRenwu;
    @BindView(R.id.imageView1_renwu)
    ImageView imageView1Renwu;
    @BindView(R.id.title_renwu)
    TextView titleRenwu;
    @BindView(R.id.content_renwu)
    TextView contentRenwu;
    @BindView(R.id.imageView_shenpi)
    ImageView imageViewShenpi;
    @BindView(R.id.imageView1_shenpi)
    ImageView imageView1Shenpi;
    @BindView(R.id.title_shenpi)
    TextView titleShenpi;
    @BindView(R.id.content_shenpi)
    TextView contentShenpi;
    @BindView(R.id.imageView_rizhi)
    ImageView imageViewRizhi;
    @BindView(R.id.imageView1_rizhi)
    ImageView imageView1Rizhi;
    @BindView(R.id.title_rizhi)
    TextView titleRizhi;
    @BindView(R.id.content_rizhi)
    TextView contentRizhi;
    @BindView(R.id.imageView_xitong)
    ImageView imageViewXitong;
    @BindView(R.id.imageView1_xitong)
    ImageView imageView1Xitong;
    @BindView(R.id.title_xitong)
    TextView titleXitong;
    @BindView(R.id.content_xitong)
    TextView contentXitong;

    public static String TOP_TITLE;

    private List<Bulletin> mLists;

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.main_newmessagecenter, container, false);
        ButterKnife.bind(this, root);

        try {
            //获取本地缓存数据
            String messageList = PreferencesUtils.getString(BaseApp.CONTEXT, KEY.MESSAGELISTUNREAD, "[]");
            mLists = JsonUtil.fromJsonList(messageList, Bulletin.class);
        } catch (Exception e) {
            mLists = new ArrayList<>();
        }
        setData();

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

        callRequest(map, "oaAlert/findBaseUserOaAlertUnRead.do", new HttpCallBack(Bulletin.class, getActivity()) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    mLists = mResult.getRows();
                    String json = JsonUtil.toJsonString(mLists);
                    //缓存数据到本地
                    PreferencesUtils.putString(BaseApp.CONTEXT, KEY.MESSAGELISTUNREAD, json);

                    setData();
                } else {
                    MyToast.showToast(mResult.getMsg());
                }
            }

            @Override
            public void onFailure(String string) {
            }
        });
    }

    //设置显示的数据
    private void setData() {
        imageView1Gonggao.setVisibility(View.GONE);
        imageView1Richeng.setVisibility(View.GONE);
        imageView1Renwu.setVisibility(View.GONE);
        imageView1Shenpi.setVisibility(View.GONE);
        imageView1Rizhi.setVisibility(View.GONE);
        imageView1Xitong.setVisibility(View.GONE);
        contentGonggao.setText("暂无");
        contentRicheng.setText("暂无");
        contentRenwu.setText("暂无");
        contentShenpi.setText("暂无");
        contentRizhi.setText("暂无");
        contentXitong.setText("暂无");
        for (int i = 0; i < mLists.size(); i++) {
            switch (mLists.get(i).getLabel()) {
                case 11:
                    imageView1Gonggao.setVisibility(View.VISIBLE);
                    contentGonggao.setText(mLists.get(i).getContent());
                    break;
                case 25:
                    imageView1Richeng.setVisibility(View.VISIBLE);
                    contentRicheng.setText(mLists.get(i).getContent());
                    break;
                case 32:
                    imageView1Renwu.setVisibility(View.VISIBLE);
                    contentRenwu.setText(mLists.get(i).getContent());
                    break;
                case 31:
                    imageView1Shenpi.setVisibility(View.VISIBLE);
                    contentShenpi.setText(mLists.get(i).getContent());
                    break;
                case 23:
                    imageView1Rizhi.setVisibility(View.VISIBLE);
                    contentRizhi.setText(mLists.get(i).getContent());
                    break;
                case 1:
                    imageView1Xitong.setVisibility(View.VISIBLE);
                    contentXitong.setText(mLists.get(i).getContent());
                    break;
            }
        }
    }

    public MessageCenterPagerNew() {
        mTitle = "消息";
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @OnClick({R.id.linearlayout_gonggao, R.id.linearlayout_richeng, R.id.linearlayout_renwu, R.id.linearlayout_shenpi, R.id.linearlayout_rizhi, R.id.linearlayout_xitong})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linearlayout_gonggao:
                MessageCenterPager.LABELKEY = "11";
                MessageCenterPager.KEYWORD = "";
                TOP_TITLE = StringUtils.getEditText(titleGonggao);
                Intent intentGonggao = new Intent(getContext(), MessageCenterDetailActivity.class);
                startActivity(intentGonggao);
                break;
            case R.id.linearlayout_richeng:
                MessageCenterPager.LABELKEY = "25";
                MessageCenterPager.KEYWORD = "";
                TOP_TITLE = StringUtils.getEditText(titleRicheng);
                Intent intentRicheng = new Intent(getContext(), MessageCenterDetailActivity.class);
                startActivity(intentRicheng);
                break;
            case R.id.linearlayout_renwu:
                MessageCenterPager.LABELKEY = "32";
                MessageCenterPager.KEYWORD = "";
                TOP_TITLE = StringUtils.getEditText(titleRenwu);
                Intent intentRenwu = new Intent(getContext(), MessageCenterDetailActivity.class);
                startActivity(intentRenwu);
                break;
            case R.id.linearlayout_shenpi:
                MessageCenterPager.LABELKEY = "31";
                MessageCenterPager.KEYWORD = "";
                TOP_TITLE = StringUtils.getEditText(titleShenpi);
                Intent intentShenpi = new Intent(getContext(), MessageCenterDetailActivity.class);
                startActivity(intentShenpi);
                break;
            case R.id.linearlayout_rizhi:
                MessageCenterPager.LABELKEY = "23";
                MessageCenterPager.KEYWORD = "";
                TOP_TITLE = StringUtils.getEditText(titleRizhi);
                Intent intentRizhi = new Intent(getContext(), MessageCenterDetailActivity.class);
                startActivity(intentRizhi);
                break;
            case R.id.linearlayout_xitong:
                MessageCenterPager.LABELKEY = "1";
                MessageCenterPager.KEYWORD = "";
                TOP_TITLE = StringUtils.getEditText(titleXitong);
                Intent intentXitong = new Intent(getContext(), MessageCenterDetailActivity.class);
                startActivity(intentXitong);
                break;
        }
    }
}
