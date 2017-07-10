package com.zbxn.activity.integral;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbxn.R;
import com.zbxn.bean.IntergralDatailsEntity;
import com.zbxn.bean.adapter.InteragrailsAdapter;
import com.zbxn.http.BaseAsyncTask;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.data.ResultData;
import com.zbxn.pub.data.base.BaseAsyncTaskInterface;
import com.zbxn.pub.dialog.ProgressDialog;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.JsonUtil;
import com.zbxn.pub.utils.MyToast;
import com.zbxn.utils.KEY;
import com.zbxn.widget.pulltorefreshlv.PullRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import utils.PreferencesUtils;

/**
 * 项目名称：我的N币
 * 创建人：LiangHanXin
 * 创建时间：2016/11/1 16:50
 */
public class IntegralDetailsActivity extends AbsToolbarActivity {


    @BindView(R.id.mPortrait)
    CircleImageView mPortrait;
    @BindView(R.id.mName)
    TextView mName;
    @BindView(R.id.mTotalIntegral)
    TextView mTotalIntegral;
    @BindView(R.id.mExchange)
    TextView mExchange;
    @BindView(R.id.mHead)
    LinearLayout mHead;
    @BindView(R.id.all_detail)
    TextView allDetail;
    @BindView(R.id.get_detail)
    TextView getDetail;
    @BindView(R.id.expand_detail)
    TextView expandDetail;
    @BindView(R.id.mListView)
    PullRefreshListView mListView;
    private ProgressDialog progressDialog;
    private InteragrailsAdapter mAdapter;
    public List<IntergralDatailsEntity> mList;
    private int pageSize = 10;
    private int m_index = 1;
    private int type = -1;
    String touxiang;
    String myname;
    String leiji;

    String username;
    String tou;
    String jifen;
    Intent intent;

    @Override
    public int getMainLayout() {
        return R.layout.activity_integraldetails;
    }

    @Override
    public void loadData() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        init();
        String mBaseUrl = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
        if (TextUtils.isEmpty(touxiang)) {
            ImageLoader.getInstance().displayImage(mBaseUrl + touxiang, mPortrait);//头像
        } else {
            ImageLoader.getInstance().displayImage(mBaseUrl + tou, mPortrait);
        }
        if (TextUtils.isEmpty(jifen)) {
            mTotalIntegral.setText(leiji);

        } else {
            mTotalIntegral.setText(jifen);
        }
        if (TextUtils.isEmpty(username)) {
            mName.setText(myname);
        } else {
            mName.setText(username);
        }

        progressDialog = new ProgressDialog(this);
        initView();
        updateSuccessView();
        ButterKnife.bind(this);
    }


    public void init() {
        intent = getIntent();
        touxiang = getIntent().getStringExtra("touxiang");
        myname = getIntent().getStringExtra("name");
        leiji = getIntent().getStringExtra("leiji");


        username = intent.getStringExtra("username");
        tou = intent.getStringExtra("touxiang");
        jifen = intent.getStringExtra("jifen");

    }

    private void initView() {

        mList = new ArrayList<>();
        //缓存数据到本地
        String json = PreferencesUtils.getString(BaseApp.CONTEXT, KEY.DETAILED, "[]");
        List<IntergralDatailsEntity> lis = JsonUtil.fromJsonList(json, IntergralDatailsEntity.class);
        mList.addAll(lis);

        mAdapter = new InteragrailsAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        mListView.setOnPullListener(new PullRefreshListView.OnPullListener() {
            @Override
            public void onRefresh() {
                setRefresh();
            }

            @Override
            public void onLoad() {
                getListData();
            }
        });


        mList = new ArrayList<>();
        mAdapter = new InteragrailsAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        mListView.setOnPullListener(new PullRefreshListView.OnPullListener() {
            @Override
            public void onRefresh() {
                setRefresh();
            }

            @Override
            public void onLoad() {
                getListData();
            }
        });
        allDetail.performClick();
    }


    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        intent = getIntent();
        username = intent.getStringExtra("username");

        if (TextUtils.isEmpty(username)) {
            toolbar.setTitle("我的N币");
        } else {
            toolbar.setTitle(username);
        }

        return super.onToolbarConfiguration(toolbar, params);
    }

    /**
     * 刷新
     */
    public void setRefresh() {
        m_index = 1;
        getListData();
    }

    public void getListData() {
        getIntegralDetail(this, m_index, type);
    }

    /**
     * 显示加载更多
     *
     * @param mResult
     */
    private void setMore(List mResult) {
        if (mResult == null) {
            mListView.setHasMoreData(true);
            return;
        }
        int pageTotal = mResult.size();
        if (pageTotal >= pageSize) {
            mListView.setHasMoreData(true);
            mListView.setPullLoadEnabled(true);
        } else {
            mListView.setHasMoreData(false);
            mListView.setPullLoadEnabled(false);
        }
    }

    /**
     * 滑动关闭当前Activitiv
     */
    @Override
    public boolean getSwipeBackEnable() {
        return true;
    }

    @OnClick({R.id.mExchange, R.id.all_detail, R.id.get_detail, R.id.expand_detail})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mExchange:
                break;
            case R.id.all_detail:
                allDetail.setSelected(true);
                getDetail.setSelected(false);
                expandDetail.setSelected(false);
                type = -1;
                break;
            case R.id.get_detail:
                allDetail.setSelected(false);
                getDetail.setSelected(true);
                expandDetail.setSelected(false);
                type = 1;
                break;
            case R.id.expand_detail:
                allDetail.setSelected(false);
                getDetail.setSelected(false);
                expandDetail.setSelected(true);
                type = 0;
                break;

        }

        mList.clear();
        mAdapter.notifyDataSetChanged();
        //开始加载显示圈圈
        mListView.startFirst();
        setRefresh();

    }


    /**
     * N币明细
     *
     * @param context
     * @param page    分页
     * @param panding 类型（1：收入 0：支出 -1:全部）
     */
    public void getIntegralDetail(Context context, int page, int panding) {
        Map<String, String> map = new HashMap<String, String>();
        String pad = "";
        if (panding != -1) {
            pad = panding + "";
        }
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);
        map.put("page", page + "");
        map.put("panding", pad);
        //获取userid,有则查看其他人N币
        //获取userid,有则查看其他人N币
        Intent intent = getIntent();
        String userid = intent.getStringExtra("userid");
        if (!TextUtils.isEmpty(userid)) {
            map.put("userid", userid);
        }
        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTask(context, false, 0, server + "/baseUserScoreController/selectMyScorels.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultData<IntergralDatailsEntity>().parse(json, IntergralDatailsEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultData mResult = (ResultData) result;
                if ("0".equals(mResult.getSuccess())) {//0成功
                    List<IntergralDatailsEntity> list = mResult.getRows();
                    if (m_index == 1) {

                        String json = JsonUtil.toJsonString(list);
                        //缓存数据到本地
                        PreferencesUtils.putString(BaseApp.CONTEXT, KEY.DETAILED, json);
                        mList.clear();
                    }
                    m_index++;
                    mList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                    mListView.onRefreshFinish();
                    setMore(list);
                } else {
                    String message = mResult.getMsg();
                    MyToast.showToast(message);
                    mListView.onRefreshFinish();
                }

            }

            @Override
            public void dataError(int funId) {
                MyToast.showToast("获取网络数据失败");
                mListView.onRefreshFinish();
            }
        }).execute(map);
    }

}
