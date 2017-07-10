package com.zbxn.activity.okr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zbxn.R;
import com.zbxn.activity.login.LoginActivity;
import com.zbxn.bean.OkrEntity;
import com.zbxn.bean.OkrRankingEntity;
import com.zbxn.http.BaseAsyncTask;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.data.ResultParse;
import com.zbxn.pub.data.base.BaseAsyncTaskInterface;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.MyToast;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.PreferencesUtils;

/**
 * Created by Administrator on 2016/11/16.
 */
public class OkrActivity extends AbsToolbarActivity {

    private static final int REQUEST_COMMENT_ACTIVITY = 1011;
    @BindView(R.id.mCurrency)
    TextView mCurrency;
    @BindView(R.id.mCurrency_Ranking)
    TextView mCurrencyRanking;
    @BindView(R.id.mBusiness)
    TextView mBusiness;
    @BindView(R.id.mBusiness_Ranking)
    TextView mBusinessRanking;
    @BindView(R.id.mColleagues_ranking)
    LinearLayout mColleaguesRanking;
    @BindView(R.id.mDepartment)
    LinearLayout mDepartment;
    @BindView(R.id.mPersonal)
    LinearLayout mPersonal;


    public String CurrencyRanking;
    public String BusinessRanking;

    @Override
    public int getMainLayout() {
        return R.layout.activity_okr;
    }

    @Override
    public void loadData() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("OKR统计");// 定义上面的名字
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        updateSuccessView();
        okrranking(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("OkrActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("OkrActivity");
        MobclickAgent.onPause(this);
    }

    @OnClick({R.id.mColleagues_ranking, R.id.mDepartment, R.id.mPersonal})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mColleagues_ranking:
                Intent intenti = new Intent(OkrActivity.this, OkrStatisticsActivity.class);
                startActivity(intenti);
                break;
            case R.id.mDepartment:
                break;
            case R.id.mPersonal:
                Intent intent = new Intent(OkrActivity.this, OkrMyStatisticsActivity.class);
                startActivity(intent);
                break;
        }
    }


    @Override
    public boolean getSwipeBackEnable() {
        return true;
    }


    /**
     * 创建按钮显示
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_okr, menu);
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
            case R.id.mOkr:

                Intent intents = new Intent(OkrActivity.this, OkrRankingActivity.class);
                intents.putExtra("CurrencyRanking", CurrencyRanking);//通用排名
                intents.putExtra("BusinessRanking", BusinessRanking);//业务排名
                OkrActivity.this.startActivityForResult(intents, REQUEST_COMMENT_ACTIVITY);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 获取OKR统计本月积分数据及排名
     *
     * @param context
     */
    public void okrranking(Context context) {
        Map<String, String> map = new HashMap<String, String>();

        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
        map.put("tokenid", ssid);
        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);
//        String netServer = ConfigUtils.getConfig(ConfigUtils.KEY.NetServer);
        new BaseAsyncTask(context, false, 0, server + "oaOKRCommonStatMonth/findOrkStat.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultParse<OkrEntity>().parse(json, OkrEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {

                ResultParse<OkrEntity> mResult = (ResultParse) result;

                if ("0".equals(mResult.getSuccess())) {//0成功
                    OkrEntity entity = mResult.getData();

                    mCurrency.setText(entity.getScoreAll() + "");//通用积分
                    mCurrencyRanking.setText(entity.getCommonRanKing() + "");//通用积分排名
                    mBusiness.setText(entity.getBizScoreAll() + "");//业务积分
                    mBusinessRanking.setText(entity.getBizRanKing() + "");//业务积分排名
                    CurrencyRanking = entity.getCommonRanKing() + "";
                    BusinessRanking = entity.getBizRanKing() + "";


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


}
