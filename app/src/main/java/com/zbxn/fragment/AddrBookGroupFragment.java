package com.zbxn.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.zbxn.R;
import com.zbxn.activity.ContactsDetail;
import com.zbxn.bean.Member;
import com.zbxn.fragment.addrbookgroup.AddrBookPresenter;
import com.zbxn.fragment.addrbookgroup.AnimationExecutor;
import com.zbxn.fragment.addrbookgroup.ContactsAdapter;
import com.zbxn.fragment.addrbookgroup.IAddrBookView;
import com.zbxn.fragment.addrbookgroup.OnContactsPickerListener;
import com.zbxn.init.eventbus.EventRefreshContacts;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.frame.mvp.AbsBaseFragment;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import utils.StringUtils;
import widget.pulltorefresh.PtrDefaultHandler;
import widget.pulltorefresh.PtrFrameLayout;
import widget.pulltorefresh.PtrHandler;
import widget.pulltorefresh.header.MaterialHeader;
import widget.stickylistheaders.ExpandableStickyListHeadersListView;
import widget.stickylistheaders.StickyListHeadersListView;

/**
 * 联系人列表，按部门分组<br/>
 * 若实现了{@link OnContactsPickerListener}接口，点击联系人会回调该接口<br/>
 * 若没实现该接口，点击联系人打开详情
 *
 * @author GISirFive
 * @time 2016/8/10
 */
public class AddrBookGroupFragment extends AbsBaseFragment implements IAddrBookView,
        AdapterView.OnItemClickListener, StickyListHeadersListView.OnHeaderClickListener {

    @BindView(R.id.mContainer)
    PtrFrameLayout mContainer;
    @BindView(R.id.mHeader)
    MaterialHeader mHeader;
    @BindView(R.id.mListView)
    ExpandableStickyListHeadersListView mListView;

    private AddrBookPresenter mPresenter;
    private ContactsAdapter mAdapter;

    /**
     * 选择联系人监听
     */
    private OnContactsPickerListener mPickerListener;

    /**
     * 选择联系人监听
     *
     * @param listener
     */
    public void setOnContactsPickerListener(OnContactsPickerListener listener) {
        mPickerListener = listener;
    }

    public AddrBookGroupFragment() {
        mTitle = "按部门";
    }

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_addrbookgroup, container, false);
        ButterKnife.bind(this, root);
        EventBus.getDefault().register(this);

        return root;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private ICustomListener iCustomListener = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            switch (obj0) {
                case 0:
                    if (mListView.isHeaderCollapsed(position)) {
                        mListView.expand(position);
                    } else {
                        mListView.collapse(position);
                    }
                    /*boolean isChecked = (boolean) obj1;
                    if (mAdapter.getMultiChoiceEnable()) {
                        if (mPickerListener != null) {
                            mAdapter.changeHeaderSelectState(position);
                            mPickerListener.onSelectedContacts(mAdapter.getSelectedList(), false);
                        }
                    } else {
                    }*/
                    break;
            }
        }
    };

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {
        mAdapter = new ContactsAdapter(getActivity(), null, iCustomListener);
        mListView.setAdapter(mAdapter);
        mListView.setAnimExecutor(new AnimationExecutor());
        mListView.setOnItemClickListener(this);
        mListView.setOnHeaderClickListener(this);
        mContainer.addPtrUIHandler(mHeader);
        mContainer.setLoadingMinTime(1500);
        mContainer.setPinContent(true);
        mContainer.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPresenter.refresh();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
            }
        });

        mPresenter = new AddrBookPresenter(this);

        //缓存数据到本地
//        String json = PreferencesUtils.getString(BaseApp.CONTEXT, KEY.CONTACTLIST, "[]");
//        List<Contacts> lists = JsonUtil.fromJsonList(json, Contacts.class);
        List<Contacts> lists = new ArrayList<>();
        try {
            lists = BaseApp.DBLoader.findAll(Selector.from(Contacts.class).where("isDepartment", "=", null).orderBy("captialChar"));//
        } catch (DbException e) {
            e.printStackTrace();
        }

        if (StringUtils.isEmpty(lists)) {
            mContainer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mContainer.autoRefresh();
                }
            }, 0);
        } else {
            mPresenter.resetData(lists);
            if (mAdapter.getMultiChoiceEnable()) {
                mListView.collapseAll();
            } else {
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public ContactsAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void autoRefresh() {
        mContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                mContainer.autoRefresh();
            }
        }, 0);
    }

    @Override
    public void refreshComplete() {
        mContainer.refreshComplete();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int memberId = Member.get().getId();
        if (mPickerListener == null) {
            //判断当前点击的用户是否为登录用户
            Contacts contacts = (Contacts) mListView.getAdapter().getItem(position);
            int itemId = contacts.getId();
            boolean containsLoginUser = (memberId == itemId);
            if (containsLoginUser)
                return;
            Intent intent = new Intent(getActivity(), ContactsDetail.class);
            intent.putExtra(ContactsDetail.Flag_Input_Contactor, contacts);
            startActivity(intent);
        } else {
            if (mAdapter.getMultiChoiceEnable()) {//多选模式
                if (mPickerListener == null)
                    return;
                mAdapter.changeItemSelectState(position);
                List<Contacts> list = mAdapter.getSelectedList();
                //判断当前点击的用户是否为登录用户
                boolean containsLoginUser = false;
                for (Contacts c : list) {
                    int itemId = c.getId();
                    if (memberId == itemId) {
                        containsLoginUser = true;
                        break;
                    }
                }
                mPickerListener.onSelectedContacts(list, containsLoginUser);
            } else {//单选模式
                //判断当前点击的用户是否为登录用户
                Contacts contacts = (Contacts) mListView.getAdapter().getItem(position);
                int itemId = contacts.getId();
                boolean containsLoginUser = (memberId == itemId);
                List<Contacts> list = new ArrayList<>();
                list.add(contacts);
                mPickerListener.onSelectedContacts(list, containsLoginUser);
            }
        }
    }

    @Subscriber
    public void refreshContacts(EventRefreshContacts event) {

    }

    /**
     * 设置当前列表是否为多选模式
     *
     * @param b
     */
    public void setMultiChoiceMode(boolean b) {
        mAdapter.setMultiChoiceEnable(b);
    }

    @Override
    public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
        if (mAdapter.getMultiChoiceEnable()) {
            if (mPickerListener != null) {
                mAdapter.changeHeaderSelectState((int) headerId);
                mPickerListener.onSelectedContacts(mAdapter.getSelectedList(), false);
            }
//            if (mListView.isHeaderCollapsed(headerId)) {
//                mListView.expand(headerId);
//            } else {
//                mListView.collapse(headerId);
//            }
        } else {
            if (mListView.isHeaderCollapsed(headerId)) {
                mListView.expand(headerId);
            } else {
                mListView.collapse(headerId);
            }
        }
    }
}
