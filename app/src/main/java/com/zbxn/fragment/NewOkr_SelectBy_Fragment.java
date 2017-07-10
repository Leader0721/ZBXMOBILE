package com.zbxn.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.bean.OkrDepartEntity;
import com.zbxn.bean.adapter.FlowAdapter;
import com.zbxn.widget.flowlayout.FlowLayout;
import com.zbxn.widget.flowlayout.OnTagSelectListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.StringUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewOkr_SelectBy_Fragment extends Fragment {
    @BindView(R.id.gridView_selectByCondition)
    FlowLayout gridViewSelectByCondition;
    @BindView(R.id.gridView_selectByDepartment)
    FlowLayout gridViewSelectByDepartment;
    @BindView(R.id.gridView_selectByYear)
    FlowLayout gridViewSelectByYear;
    @BindView(R.id.gridView_selectByMonth)
    FlowLayout gridViewSelectByMonth;
    @BindView(R.id.textView_default_newokr)
    TextView textViewDefaultNewokr;
    @BindView(R.id.textView_certain_newokr)
    TextView textViewCertainNewokr;
    @BindView(R.id.imageView_selectByDepartment_newOkr)
    ImageView imageViewSelectByDepartmentNewOkr;
    @BindView(R.id.imageView_selectByMonth_newOkr)
    ImageView imageViewSelectByMonthNewOkr;
    @BindView(R.id.all_department)
    LinearLayout allDepartment;
    @BindView(R.id.all_month)
    LinearLayout allMonth;
    @BindView(R.id.scrollView_okr)
    ScrollView scrollViewOkr;
    @BindView(R.id.gridView_selectBy)
    FlowLayout gridViewSelectBy;
    @BindView(R.id.textView_selectByDepartment_expand)
    TextView textViewByDepartment;
    @BindView(R.id.textView_selectByMonth_expand)
    TextView textViewByMonth;
    private List<String> byConditionList, byDepartmentList, byYearList, byMonthList, selectByList;
    private boolean isDepartmentExpand = true; // 部门是否展开
    private boolean isMonthExpand = true; // 月是否展开
    private List<OkrDepartEntity> mList;
    private int year;
    private int month;
    private int OrderCustom;//0—无,1—本部门，2—本职位
    private int Order;//7—进步率，8—退步率
    private int DepartmentId;//部门Id，0--全部
    private int Year;
    private int Month;

    private CallBackValue callBackValue;

    private FlowAdapter<String> byConditionAdapter, byDepartmentAdapter, byYearAdapter, byMonthAdapter, selectByAdapter;

    public NewOkr_SelectBy_Fragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callBackValue = (CallBackValue) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_okr_select_by_, container, false);
        ButterKnife.bind(this, view);

        mList = new ArrayList<>();

        //四种不同的排序方式
        byConditionList = new ArrayList<>();
        byDepartmentList = new ArrayList<>();
        byYearList = new ArrayList<>();
        byMonthList = new ArrayList<>();
        selectByList = new ArrayList<>();
        //条件筛选
        byConditionList.add(0, "本部门排名");
        byConditionList.add(1, "本职位排名");
        selectByList.add(0, "进步率排名");
        selectByList.add(1, "退步率排名");

        //按年进行筛选
        Calendar ca = Calendar.getInstance();
        year = ca.get(Calendar.YEAR);
        month = ca.get(Calendar.MONTH);
        Year = year;
        Month = month;
        byYearList.add(0, year - 3 + "");
        byYearList.add(1, year - 2 + "");
        byYearList.add(2, year - 1 + "");
        byYearList.add(3, year + "");
        textViewByMonth.setText((Month + 1) + " 月 ");
        //按月进行筛选
        for (int i = 1; i < 13; i++) {
            byMonthList.add(i - 1, i + " 月 ");
        }
        initByConditionAdapter();
        initByYearAdapter();
        initByMonthAdapter();
        initSelectByAdapter();
        return view;
    }

    //进步，退步
    private void initSelectByAdapter() {
        selectByAdapter = new FlowAdapter<>(getContext(), R.layout.grid_item_newokr_selectbycondition, R.id.button_newOkr_selectBy);
        gridViewSelectBy.setTagCheckedMode(FlowLayout.FLOW_TAG_CHECKED_SINGLE);
        gridViewSelectBy.setAdapter(selectByAdapter);
        gridViewSelectBy.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    for (int i : selectedList) {
                        if (i == 0) {
                            Order = 7;
                        } else if (i == 1) {
                            Order = 8;
                        }
                    }
                } else {
                    Order = 6;
                }
            }
        });
        selectByAdapter.onlyAddAll(selectByList);
    }

    //条件筛选
    private void initByConditionAdapter() {
        byConditionAdapter = new FlowAdapter<>(getContext(), R.layout.grid_item_newokr_selectbycondition, R.id.button_newOkr_selectBy);
        gridViewSelectByCondition.setTagCheckedMode(FlowLayout.FLOW_TAG_CHECKED_SINGLE);
        gridViewSelectByCondition.setAdapter(byConditionAdapter);
        gridViewSelectByCondition.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    for (int i : selectedList) {
                        if (i == 0) {
                            OrderCustom = 1;
                        } else if (i == 1) {
                            OrderCustom = 2;
                        }
                    }
                } else {
                    OrderCustom = 0;
                }
            }
        });
        byConditionAdapter.onlyAddAll(byConditionList);
    }

    //部门
    private void initByDepartmentAdapter() {
        byDepartmentAdapter = new FlowAdapter<>(getContext(), R.layout.grid_item_newokr_selectby, R.id.button_newOkr_selectBy);
        gridViewSelectByDepartment.setTagCheckedMode(FlowLayout.FLOW_TAG_CHECKED_SINGLE);
        gridViewSelectByDepartment.setAdapter(byDepartmentAdapter);
        gridViewSelectByDepartment.setExpand(false);
        gridViewSelectByDepartment.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i : selectedList) {
                        DepartmentId = mList.get(i).getDepartmentId();
                        textViewByDepartment.setText(byDepartmentList.get(i));
                    }
                } else {
                    DepartmentId = 0;
                    textViewByDepartment.setText("全部");
                }
            }
        });
        byDepartmentAdapter.onlyAddAll(byDepartmentList);
    }

    //按年
    private void initByYearAdapter() {
        byYearAdapter = new FlowAdapter<>(getContext(), R.layout.grid_item_newokr_selectby, R.id.button_newOkr_selectBy);
        gridViewSelectByYear.setTagCheckedMode(FlowLayout.FLOW_TAG_CHECKED_SINGLE);
        gridViewSelectByYear.setAdapter(byYearAdapter);
        gridViewSelectByYear.setOnlyOneNull(false);
        gridViewSelectByYear.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    for (int i : selectedList) {
                        Year = Integer.decode(byYearList.get(i));
                    }
                } else {
                }
            }
        });
        byYearAdapter.onlyAddAll(byYearList);
        gridViewSelectByYear.setSelect(byYearList.size() - 1);
    }

    //按月
    private void initByMonthAdapter() {
        byMonthAdapter = new FlowAdapter<>(getContext(), R.layout.grid_item_newokr_selectby, R.id.button_newOkr_selectBy);
        gridViewSelectByMonth.setTagCheckedMode(FlowLayout.FLOW_TAG_CHECKED_SINGLE);
        gridViewSelectByMonth.setAdapter(byMonthAdapter);
        gridViewSelectByMonth.setOnlyOneNull(false);
        gridViewSelectByMonth.setExpand(false);
        gridViewSelectByMonth.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    for (int i : selectedList) {
                        Month = i + 1;
                        textViewByMonth.setText(byMonthList.get(i));
                    }
                } else {
                    textViewByMonth.setText("全部");
                }
            }
        });
        byMonthAdapter.onlyAddAll(byMonthList);
        gridViewSelectByMonth.setSelect(month);
    }

    @OnClick({R.id.textView_default_newokr, R.id.textView_certain_newokr, R.id.all_department, R.id.all_month})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.all_department:
                if (StringUtils.isEmpty(mList)) {
                    return;
                }
                if (isDepartmentExpand) {
                    imageViewSelectByDepartmentNewOkr.setImageResource(R.mipmap.home_arrows_up);
                } else {
                    imageViewSelectByDepartmentNewOkr.setImageResource(R.mipmap.home_arrows_down);
                }
                gridViewSelectByDepartment.setExpand(isDepartmentExpand);
                isDepartmentExpand = !isDepartmentExpand;
                break;
            case R.id.all_month:
                if (isMonthExpand) {
                    imageViewSelectByMonthNewOkr.setImageResource(R.mipmap.home_arrows_up);
                } else {
                    imageViewSelectByMonthNewOkr.setImageResource(R.mipmap.home_arrows_down);
                }
                gridViewSelectByMonth.setExpand(isMonthExpand);
                isMonthExpand = !isMonthExpand;
                break;
            case R.id.textView_default_newokr:
                byConditionAdapter.clearAndAddAll(byConditionList);
                if (!StringUtils.isEmpty(byDepartmentList)) {
                    byDepartmentAdapter.clearAndAddAll(byDepartmentList);
                }
                byYearAdapter.clearAndAddAll(byYearList);
                byMonthAdapter.clearAndAddAll(byMonthList);
                selectByAdapter.clearAndAddAll(selectByList);
                OrderCustom = 0;
                Order = 6;
                DepartmentId = 0;
                gridViewSelectByYear.setSelect(3);
                Year = year;
                Month = month + 1;
                textViewByMonth.setText(Month + " 月 ");
                textViewByDepartment.setText("全部");
                gridViewSelectByMonth.setSelect(month);
                break;
            case R.id.textView_certain_newokr:
                callBackValue.SendMessageValue(OrderCustom + "", Order + "", DepartmentId + "", Year + "", Month + "");
                break;
        }
    }

    public void setDepartList(List<OkrDepartEntity> list) {
        mList = list;
        byDepartmentList.clear();
        for (int i = 0; i < list.size(); i++) {
            byDepartmentList.add(list.get(i).getDepartmentName());
        }
        initByDepartmentAdapter();
    }

    //确定回调
    public interface CallBackValue {
        void SendMessageValue(String OrderCustom, String Order, String DepartmentId, String Year, String Month);
    }

}
