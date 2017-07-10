package com.zbxn.activity.integral;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.zbxn.R;
import com.zbxn.bean.IntegralEntity;
import com.zbxn.bean.adapter.IntegralAdapter;
import com.zbxn.http.BaseAsyncTask;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.data.ResultData;
import com.zbxn.pub.data.base.BaseAsyncTaskInterface;
import com.zbxn.pub.frame.mvp.AbsBaseFragment;
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
import utils.PreferencesUtils;

/**
 * Created by wj on 2016/11/23.
 */
public class IntegralFragment extends AbsBaseFragment {

    private static final int TYPE_COMPANY = 101;      // 公司
    private static final int TYPE_FRIEND = 102;   // 好友
    private static final int TYPE_ALL = 103;    // 全部
    @BindView(R.id.mListView)
    PullRefreshListView mListView;
    public List<IntegralEntity> mList;
    private IntegralAdapter mAdapter;
    private int m_index = 1;
    private int pageSize = 10;
    private int pagerNum = TYPE_COMPANY;

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_integral, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {

        mListView.startFirst();
        mList = new ArrayList<>();
        //    getListData();
        //缓存数据到本地
        String json = PreferencesUtils.getString(BaseApp.CONTEXT, KEY.INTEGRAL, "[]");
        List<IntegralEntity> list = JsonUtil.fromJsonList(json, IntegralEntity.class);
        mList.addAll(list);

        mAdapter = new IntegralAdapter(getContext(), mList);
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
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), IntegralDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //    try{
                intent.putExtra("userid", mList.get(position).getUserID() + "");       //用户id
                intent.putExtra("username", mList.get(position).getUsername());   //用户姓名
                intent.putExtra("touxiang", mList.get(position).getTouxiang());   //用户头像
                intent.putExtra("jifen", mList.get(position).getUserScore());   //用户积分
            /*    }catch (Exception e){
                    MyToast.showToast("请求异常,将访问自己的信息");
                }*/
                getContext().startActivity(intent);
            }
        });

        //开始加载显示圈圈
        mListView.startFirst();
        //刷新
        setRefresh();
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }


    /**
     * 刷新
     */
    public void setRefresh() {
        m_index = 1;
        getListData();
    }

    public void getListData() {
        TotalRanking(getContext(), m_index, pagerNum);
    }


    /**
     * 总排名
     *
     * @param context
     */
    public void TotalRanking(Context context, int page, int pagerNum) {
        Map<String, String> map = new HashMap<String, String>();

        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);
        map.put("page", page + "");
        Bundle arguments = getArguments();
        int types = arguments.getInt("types");
        switch (types) {
            case TYPE_COMPANY:
                map.put("scoreType", 1 + "");
                break;
            case TYPE_FRIEND:
                //TODO 等接口
                break;
            case TYPE_ALL:
                //空实现 默认搜索全部
                break;
        }

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTask(context, false, 0, server + "/baseUserScoreController/baseUserScorephb.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultData<IntegralEntity>().parse(json, IntegralEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultData mResult = (ResultData) result;
                if ("0".equals(mResult.getSuccess())) {//0成功
                    List<IntegralEntity> list = mResult.getRows();
                    //防止崩掉
                    if (m_index == 1) {
                        String json = JsonUtil.toJsonString(list);
                        //缓存数据到本地
                        PreferencesUtils.putString(BaseApp.CONTEXT, KEY.INTEGRAL, json);
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

    public void setFragmentTitle(String title) {
        mTitle = title;
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


}
