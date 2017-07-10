package com.zbxn.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zbxn.R;
import com.zbxn.bean.OkrRankingEntity;
import com.zbxn.bean.adapter.OkrRankingAdapter;
import com.zbxn.widget.pulltorefreshlv.PullRefreshListView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewOkr_SortBy_Fragment extends Fragment {
    @BindView(R.id.imageView_arrow_tongyong)
    ImageView new_okr_arrow_tongyong;
    @BindView(R.id.imageView_arrow_yewu)
    ImageView new_okr_arrow_yewu;
    @BindView(R.id.imageView_arrow_okr)
    ImageView new_okr_arrow_okr;
    @BindView(R.id.topLinearLayout_Okr)
    LinearLayout topLinearLayoutOkr;
    @BindView(R.id.listView_okr_sortList)
    PullRefreshListView listViewOkrSortList;

    private OkrRankingAdapter mAdapter;
    private List<OkrRankingEntity> mList;

    private int mIndex = 1;
    private int pageSize = 10;

    private int general_sign = 1;//通用积分标记 0:未选择，1:降序，2:升序
    private int business_sign = 0;//业务
    private int okr_sign = 0;//okr积分标记 1：降序，2：升序  0:通用
    private int orderByType = -1;//积分排序状态 0:通用↑ 1:业务↓ 2:业务↑ 其它:通用↓(默认)
    public NewOkr_SortBy_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_okr_sort_by, container, false);
    }
    @OnClick({R.id.imageView_arrow_tongyong, R.id.imageView_arrow_yewu, R.id.imageView_arrow_okr})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_arrow_tongyong:
                break;
            case R.id.imageView_arrow_yewu:
                break;
            case R.id.imageView_arrow_okr:
                break;
        }
    }

}
