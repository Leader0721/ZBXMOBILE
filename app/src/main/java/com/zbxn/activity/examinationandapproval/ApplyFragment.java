package com.zbxn.activity.examinationandapproval;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.bean.ApplyEntity;
import com.zbxn.bean.ApprovalEntity;
import com.zbxn.listener.ICustomListener;
import com.zbxn.widget.pulltorefreshlv.PullRefreshListView;
import com.zbxn.bean.adapter.ApplyAdapter;
import com.zbxn.popupwindow.PopupWindowType;
import com.zbxn.pub.dialog.MessageDialog;
import com.zbxn.pub.frame.mvp.AbsBaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author: ysj
 * @date: 2016-11-04 09:40
 */
public class ApplyFragment extends AbsBaseFragment implements AdapterView.OnItemClickListener {

    private static final int Type_Apply = 101;      // 我的申请
    private static final int Type_Approval = 102;   // 我的审批
    private static final int Type_Inquire = 103;    // 审批查询

    /**
     * 回调处理
     */
    private static final int Flag_Callback_Record = 1001;

    @BindView(R.id.mWhole)
    TextView mWhole;
    @BindView(R.id.layout_whole)
    RelativeLayout layoutWhole;
    @BindView(R.id.mType)
    TextView mType;
    @BindView(R.id.layout_type)
    RelativeLayout layoutType;
    @BindView(R.id.mListView)
    PullRefreshListView mListView;

    private int pageSize = 10;
    private List<ApplyEntity> list;
    private ApplyAdapter mAdapter;
    private ApplyPresenter mPresenter;

    private int m_index = 1;
    private String mStateStr = "0";
    private String mTypeStr = "-1";

    //根据 type 判断当前页面类型
    private int type = 101;
    private boolean isVis = false;

    private MessageDialog messageDialog;

    private List<ApprovalEntity> listLeft = new ArrayList<>();
    private List<ApprovalEntity> listRight = new ArrayList<>();

    public void setFragmentTitle(String title) {
        mTitle = title;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_apply, container, false);
        ButterKnife.bind(this, view);
        type = getArguments().getInt("types");
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVis = true;
        } else {
            isVis = false;
        }
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {
        mListView.startFirst();
        messageDialog = new MessageDialog(getContext());
        mPresenter = new ApplyPresenter(this, mICustomListener);
        list = new ArrayList<>();
        mAdapter = new ApplyAdapter(getContext(), list, mICustomListener);
        mListView.setAdapter(mAdapter);
        mPresenter.selecttype(getContext(), mICustomListener);
        mPresenter.selectFrame(getContext(), mICustomListener);
        mListView.setOnPullListener(new PullRefreshListView.OnPullListener() {
            @Override
            public void onRefresh() {
                setRefresh();
            }

            @Override
            public void onLoad() {
//                mPresenter.dataList(getApplicationContext(), mICustomListener, 1, mStateStr, mTypeStr);
                getListData(type);
            }
        });
        //刷新
        setRefresh();


        mListView.setOnItemClickListener(this);
    }

    //列表的点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        view.findViewById(R.id.mTitle);//当前状态
        Intent intent = new Intent(getContext(), ApplyDetailActivity.class);
        if (type == Type_Apply) {
            intent.putExtra("flag", 1000);
        } else if (type == Type_Approval) {

        } else if (type == Type_Inquire) {
            intent.putExtra("flag", 1001);
        }
        intent.putExtra("approvalID", list.get(position).getID() + "");
        startActivityForResult(intent, Flag_Callback_Record);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Flag_Callback_Record) {
            if (resultCode == getActivity().RESULT_OK) {
                setRefresh();
            } else {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 回调回申请状态
     */
    private ICustomListener mICustomListener = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            try {
                switch (obj0) {
                    case -1://获取列表失败
                        //加载完成
                        mListView.onRefreshFinish();
                        break;
                    case 0:
                        List<ApplyEntity> list1 = (List<ApplyEntity>) obj1;
//                    list.clear();
                        if (m_index == 1) {
                            list.clear();
                        }
                        list.addAll(list1);
                        mAdapter.notifyDataSetChanged();

                        m_index++;
                        setMore(list1);//判断是否还有更多数据
                        //加载完成
                        mListView.onRefreshFinish();
                        break;
                    case 2:
                        listLeft = (List<ApprovalEntity>) obj1;
                        break;
                    case 3:
                        listRight = (List<ApprovalEntity>) obj1;
                        break;
                    case 4:
                        String message = (String) obj1;
                        messageDialog.setTitle("提示");
                        messageDialog.setMessage(message);
                        if (2==ApplyActivity.index) {
                            messageDialog.show();
                        }
                        mListView.onRefreshFinish();
                        break;
                }
            } catch (Exception e) {

            }
        }

    };

    @Override
    public void onResume() {
        super.onResume();
        isVis = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isVis = false;
    }

    /**
     * 刷新
     */
    public void setRefresh() {
        m_index = 1;
//        mPresenter.dataList(this, mICustomListener, 1, mStateStr, mTypeStr);
        getListData(type);
    }

    //请求数据
    public void getListData(int types) {
        if (mPresenter == null) {
            mPresenter = new ApplyPresenter(this, mICustomListener);
        }
        switch (types) {
            case Type_Apply: // 我的申请
                mPresenter.dataList(getContext(), mICustomListener, m_index, mStateStr, mTypeStr);
                break;
            case Type_Approval:// 我的审批
                mPresenter.dataListapproval(getContext(), mICustomListener, m_index, mStateStr, mTypeStr);
                break;
            case Type_Inquire: // 审批查询
                mPresenter.dataListInquire(getContext(), m_index, mStateStr, mTypeStr, mICustomListener);
                break;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @OnClick({R.id.layout_whole, R.id.layout_type})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_whole:
                setPopupWindow(mWhole, mWhole.getWidth(), listLeft, "全部", 0, mWhole);
                break;
            case R.id.layout_type:
                setPopupWindow(mType, mType.getWidth(), listRight, "全部", 1, mWhole);
                break;
        }
    }

    //类型item的点击事件
    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (id == 0) {
                ApprovalEntity entity = listLeft.get(position);
                mWhole.setText(entity.getName());
                if (entity.getTypeid() == -1) {
                    mStateStr = "-1";
                } else {
                    mStateStr = entity.getTypeid() + "";
                }
                mListView.startFirst();
                setRefresh();
            } else if (id == 1) {
                ApprovalEntity entity = listRight.get(position);
                mType.setText(entity.getName());
                if (entity.getTypeid() == -1) {
                    mType.setText("类型");
                }
                mTypeStr = entity.getTypeid() + "";
                mListView.startFirst();
                setRefresh();
            }
        }
    };

    //设置PopupWindow
    public void setPopupWindow(View view, float width, List<ApprovalEntity> list, String type, long flag, View location) {
        PopupWindowType mpopupWindowMeetingType = new PopupWindowType(getActivity(), view, width, listener, list, type, flag);
//                m_popupWindowMeetingType.setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent));
//                m_popupWindowMeetingType.setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent));
        ColorDrawable dw1 = new ColorDrawable(0xffffffff);
        //设置SelectPicPopupWindow弹出窗体的背景
        mpopupWindowMeetingType.setBackgroundDrawable(dw1);
        mpopupWindowMeetingType.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {

            }
        });
        //设置layout在PopupWindow中显示的位置
        mpopupWindowMeetingType.showAsDropDown(location, 1, 1);
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
