package com.zbxn.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbxn.bean.Member;
import com.zbxn.R;
import com.zbxn.activity.ContactsDetail;
import com.zbxn.fragment.addrbookpy.AddrBookPresenter;
import com.zbxn.fragment.addrbookpy.IAddrBookView;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.bean.adapter.base.IItemViewControl;
import com.zbxn.pub.frame.mvp.AbsBaseFragment;
import com.zbxn.pub.utils.ConfigUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import widget.indexablelistview.IndexableListView;
import widget.pulltorefresh.PtrDefaultHandler;
import widget.pulltorefresh.PtrFrameLayout;
import widget.pulltorefresh.PtrHandler;
import widget.pulltorefresh.header.MaterialHeader;

/**
 * 通讯录按拼音排序
 *
 * @author GISirFive
 * @time 2016/8/8
 */
public class AddrBookPyFragment extends AbsBaseFragment implements IAddrBookView,
        IItemViewControl<Contacts>, AdapterView.OnItemClickListener {

    @BindView(R.id.mContainer)
    PtrFrameLayout mContainer;
    @BindView(R.id.mHeader)
    MaterialHeader mHeader;
    @BindView(R.id.mListView)
    IndexableListView mListView;

    private AddrBookPresenter mPresenter;

    public AddrBookPyFragment() {
        mTitle = "所有同事";
    }

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_addrbookpy, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {
        mPresenter = new AddrBookPresenter(this);
        mListView.setFastScrollEnabled(true);
        mListView.setOnItemClickListener(this);

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
                if (mListView.isScrollOnTouched())
                    return false;
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
            }
        });
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public void autoRefresh() {
        mContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                mContainer.autoRefresh();
            }
        }, 300);
    }

    @Override
    public void setAdapter(BaseAdapter adapter) {
        mListView.setAdapter(adapter);
    }

    @Override
    public void refreshComplete() {
        mContainer.refreshComplete();
    }

    @Override
    public View initViewItem(LayoutInflater inflater, int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_addrbookpy, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Contacts item = (Contacts) mListView.getAdapter().getItem(position);

        //首字母索引
        String captial = item.getCaptialChar();
        if (position == 0) {
            holder.mCaptialChar.setVisibility(View.VISIBLE);
            holder.mCaptialChar.setText(captial);
        } else {
            Contacts temp = (Contacts) mListView.getAdapter().getItem(position - 1);
            String preCaptial = temp.getCaptialChar();
            if (captial != null && captial.equals(preCaptial)) {
                holder.mCaptialChar.setVisibility(View.GONE);
            } else {
                holder.mCaptialChar.setVisibility(View.VISIBLE);
                holder.mCaptialChar.setText(captial);
            }
        }
        //姓名
        holder.mRemarkName.setText(item.getUserName() + "");
        String mBaseUrl = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.userhead_img)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.userhead_img)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.userhead_img)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                //.displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                .build();                                   // 创建配置过得DisplayImageOption对象
        ImageLoader.getInstance().displayImage(mBaseUrl + item.getPortrait(), holder.mPortrait, options);

        return convertView;
    }

    @Override
    public void dataSetChangedListener(List<Contacts> data) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //判断当前点击的用户是否为登录用户
        Contacts contacts = (Contacts) mListView.getAdapter().getItem(position);
        int itemId = contacts.getId();
        int memberId = Member.get().getId();
        if (memberId == itemId)
            return;
        Intent intent = new Intent(getActivity(), ContactsDetail.class);
        intent.putExtra(ContactsDetail.Flag_Input_Contactor, contacts);
        startActivity(intent);
    }

    static class ViewHolder {
        @BindView(R.id.mCaptialChar)
        TextView mCaptialChar;
        @BindView(R.id.mPortrait)
        CircleImageView mPortrait;
        @BindView(R.id.mRemarkName)
        TextView mRemarkName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
