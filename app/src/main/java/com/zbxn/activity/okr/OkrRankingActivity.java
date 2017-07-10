package com.zbxn.activity.okr;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.activity.login.LoginActivity;
import com.zbxn.bean.OkrDepartEntity;
import com.zbxn.bean.OkrRankingEntity;
import com.zbxn.bean.adapter.OkrRankingAdapter;
import com.zbxn.fragment.NewOkr_SelectBy_Fragment;
import com.zbxn.http.BaseAsyncTask;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.data.ResultNetData;
import com.zbxn.pub.data.base.BaseAsyncTaskInterface;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.MyToast;
import com.zbxn.widget.pulltorefreshlv.PullRefreshListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import utils.PreferencesUtils;
import utils.StringUtils;

/**
 * @author: ysj
 * @date: 2016-11-16 09:34
 */
public class OkrRankingActivity extends AbsToolbarActivity implements NewOkr_SelectBy_Fragment.CallBackValue {

    public static final int ORK_NEWSEARCH_CALLBACK = 1000;

    @BindView(R.id.general_ranking)
    TextView generalRanking;
    @BindView(R.id.business_ranking)
    TextView businessRanking;
    @BindView(R.id.general_integral)
    LinearLayout generalIntegral;
    @BindView(R.id.business_integral)
    LinearLayout businessIntegral;
    @BindView(R.id.okr_listView)
    PullRefreshListView mListView;
    @BindView(R.id.general_tv)
    TextView generalTv;
    @BindView(R.id.general_img)
    ImageView generalImg;
    @BindView(R.id.business_tv)
    TextView businessTv;
    @BindView(R.id.business_img)
    ImageView businessImg;
    @BindView(R.id.okr_tv)
    TextView okrTv;
    @BindView(R.id.okr_img)
    ImageView okrImg;
    @BindView(R.id.okr_integral)
    LinearLayout okrIntegral;
    @BindView(R.id.mDrawerLayout_newOkr)
    DrawerLayout mDrawerLayout;

    private OkrRankingAdapter mAdapter;
    private List<OkrRankingEntity> mList;

    private ActionBarDrawerToggle mDrawerToggle;
    private NewOkr_SelectBy_Fragment mNewOkr_SelectBy_Fragment;

    private int mIndex = 1;
    private int pageSize = 10;

    private int general_sign = 1;//通用积分标记 0:未选择，1:升序，2:降序
    private int business_sign = 1;//业务 0:未选择,1:升序，2:降序

    private int orderByType = 6;//积分排序状态 1~6
    private int okr_sign = 1;//okr排名 0:未选择, 1:升序,2:降序
    private int mOrderCustom = 0;//0—无,1—本部门，2—本职位
    private int mDepartmentId = 0;//部门id
    private int mYear = 2016;//年
    private int mMonth = 10;//月
    private String mKeyword = "";//关键词 100字符以内

    public String CurrencyRanking;
    public String BusinessRanking;

    @Override
    public int getMainLayout() {
        return R.layout.activity_okr_ranking;
    }

    @Override
    public boolean getSwipeBackEnable() {
        return super.getSwipeBackEnable();
//        return true;
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("OKR排名");
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View();
        init();
        getDepartmentList();
        updateSuccessView();
        initView();
    }

    private void init() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
//        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mNewOkr_SelectBy_Fragment = (NewOkr_SelectBy_Fragment) getSupportFragmentManager()
                .findFragmentById(R.id.mNewOkr_SelectBy_Fragment);
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        PreferencesUtils.putBoolean(this, "okrdrawlayout", false);

    }

    /**
     * 创建按钮显示
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_okr, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 创建按钮监听
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    /**
     * 创建按钮的点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_okr_search:
                Intent intent = new Intent(this, OkrNewSearchActivity.class);
                intent.putExtra("keyword", mKeyword);
                startActivityForResult(intent, ORK_NEWSEARCH_CALLBACK);
                break;
            case R.id.new_okr_screening:
                if (mDrawerLayout.isDrawerOpen(mNewOkr_SelectBy_Fragment.getView())) {
                    mDrawerLayout.closeDrawer(mNewOkr_SelectBy_Fragment.getView());
                } else {
                    mDrawerLayout.openDrawer(mNewOkr_SelectBy_Fragment.getView());
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * 通用业务排名
     */
    private void View() {
        CurrencyRanking = getIntent().getStringExtra("CurrencyRanking");//通用排名
        BusinessRanking = getIntent().getStringExtra("BusinessRanking");//业务排名
        generalRanking.setText(CurrencyRanking);
        businessRanking.setText(BusinessRanking);

    }

    public void initView() {
        mListView.startFirst();
        mList = new ArrayList<>();
        mAdapter = new OkrRankingAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        setRefresh();
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
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mDrawerLayout.isShown()) {

        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 刷新
     */
    public void setRefresh() {
        mIndex = 1;
        getListData();
    }

    public void getListData() {
        getRanking(mIndex);
    }

    @Override
    public void loadData() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @OnClick({R.id.general_integral, R.id.business_integral, R.id.okr_integral})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.general_integral:
                setImageNull();
                business_sign = 1;
                okr_sign = 1;
                if (general_sign == 1) {
                    orderByType = 1;
                    generalImg.setImageResource(R.mipmap.rank_up);
                    general_sign = 2;
                } else if (general_sign == 2) {
                    orderByType = 2;
                    generalImg.setImageResource(R.mipmap.rank_down);
                    general_sign = 1;
                }
                mListView.startFirst();
                setRefresh();
                break;
            case R.id.business_integral:
                setImageNull();
                general_sign = 1;
                okr_sign = 1;
                if (business_sign == 1) {
                    orderByType = 3;
                    businessImg.setImageResource(R.mipmap.rank_up);
                    business_sign = 2;
                } else if (business_sign == 2) {
                    orderByType = 4;
                    businessImg.setImageResource(R.mipmap.rank_down);
                    business_sign = 1;
                }
                mListView.startFirst();
                setRefresh();
                break;
            case R.id.okr_integral:
                setImageNull();
                general_sign = 1;
                business_sign = 1;
                if (okr_sign == 1) {
                    orderByType = 5;
                    okrImg.setImageResource(R.mipmap.rank_up);
                    okr_sign = 2;
                } else if (okr_sign == 2) {
                    orderByType = 6;
                    okrImg.setImageResource(R.mipmap.rank_down);
                    okr_sign = 1;
                }
                mListView.startFirst();
                setRefresh();
                break;
        }
    }

    public void setImageNull() {
        generalImg.setImageResource(R.mipmap.rank_null);
        businessImg.setImageResource(R.mipmap.rank_null);
        okrImg.setImageResource(R.mipmap.rank_null);
    }

    public void getRanking(int page) {
        Map<String, String> map = new HashMap<>();
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
        map.put("tokenid", ssid);
        map.put("CurrentCompanyId", CurrentCompanyId);
        map.put("Order", orderByType + "");
        map.put("OrderCustom", mOrderCustom + "");
        map.put("DepartmentId", mDepartmentId + "");
        map.put("Year", mYear + "");
        map.put("Month", mMonth + "");
        map.put("Keyword", mKeyword);
        map.put("PageIndex", page + "");
        map.put("PageSize", pageSize + "");

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.NetServer);
        new BaseAsyncTask(this, false, 0, server + "okr/getOKRList", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultNetData<OkrRankingEntity>().parse(json, OkrRankingEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultNetData mResult = (ResultNetData) result;
                if ("0".equals(mResult.getSuccess())) {
                    mListView.onRefreshFinish();
                    List<OkrRankingEntity> list = mResult.getData();
                    if (mIndex == 1) {
                        mList.clear();
                    }
                    if (!StringUtils.isEmpty(list)) {
                        mIndex++;
                    }
                    setMore(list);
                    mList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                } else {
                    mListView.onRefreshFinish();
                    String message = mResult.getMsg();
                    MyToast.showToast(message);
                }
            }

            @Override
            public void dataError(int funId) {
                mListView.onRefreshFinish();
                MyToast.showToast("获取网络数据失败");
            }
        }).execute(map);
    }

    public void getDepartmentList() {
        Map<String, String> map = new HashMap<>();
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
        map.put("tokenid", ssid);
        map.put("CurrentCompanyId", CurrentCompanyId);

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.NetServer_ACTION);
        new BaseAsyncTask(this, false, 0, server + "Base/GetDepartmentList", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultNetData<OkrDepartEntity>().parse(json, OkrDepartEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultNetData mResult = (ResultNetData) result;
                if ("0".equals(mResult.getSuccess())) {
                    List<OkrDepartEntity> list = mResult.getData();
                    if (!StringUtils.isEmpty(list)) {
                        mNewOkr_SelectBy_Fragment.setDepartList(list);
                    }
                } else {
                    String message = mResult.getMsg();
                    MyToast.showToast(message);
                }
            }

            @Override
            public void dataError(int funId) {
                MyToast.showToast("获取网络数据失败");
            }
        }).execute(map);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //搜索
        if (requestCode == ORK_NEWSEARCH_CALLBACK) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    if (mDrawerLayout.isDrawerOpen(mNewOkr_SelectBy_Fragment.getView())) {
                        mDrawerLayout.closeDrawer(mNewOkr_SelectBy_Fragment.getView());
                    }
                    mKeyword = data.getStringExtra("keyword");
                    mListView.startFirst();
                    setRefresh();
                }
            }
        }
    }

    @Override
    public void SendMessageValue(String OrderCustom, String Order, String DepartmentId, String Year, String Month) {
        mKeyword = "";
        mDepartmentId = Integer.decode(DepartmentId);
        mOrderCustom = Integer.decode(OrderCustom);
        int order = Integer.decode(Order);
        if (order != 0) {
            orderByType = order;
        }
        if (orderByType == 7 || orderByType == 8) {
            setImageNull();
        } else {
            setImageNull();
            okrImg.setImageResource(R.mipmap.rank_down);
            okr_sign = 1;
        }
        mYear = Integer.decode(Year);
        mMonth = Integer.decode(Month);
        if (mDrawerLayout.isDrawerOpen(mNewOkr_SelectBy_Fragment.getView())) {
            mDrawerLayout.closeDrawer(mNewOkr_SelectBy_Fragment.getView());
        }
        mListView.startFirst();
        setRefresh();
    }

//    @Override
//    public void DefaultSendMessage() {
//        orderByType = 6;
//        okrImg.setImageResource(R.mipmap.rank_down);
//        okr_sign = 1;
//    }
}
