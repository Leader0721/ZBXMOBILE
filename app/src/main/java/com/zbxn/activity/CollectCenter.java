package com.zbxn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.umeng.analytics.MobclickAgent;
import com.zbxn.R;
import com.zbxn.activity.collectcenter.CollectCenterPresenter;
import com.zbxn.activity.collectcenter.ICollectCenterView;
import com.zbxn.pub.bean.Bulletin;
import com.zbxn.pub.bean.adapter.base.IItemViewControl;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.ListviewUpDownHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import utils.DateUtils;

/**
 * 收藏列表
 *
 * @author GISirFive
 * @time 2016/8/18
 */
public class CollectCenter extends AbsToolbarActivity implements ICollectCenterView,
        IItemViewControl<Bulletin>, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    @BindView(R.id.listview_content)
    ListView mListview;
    private ListviewUpDownHelper mListviewHelper;
    private CollectCenterPresenter mPresenter;

    @Override
    public int getMainLayout() {
        return R.layout.activitiy_collect;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        updateSuccessView();
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("CollectCenter");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("CollectCenter");
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("我的收藏");
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    public boolean getSwipeBackEnable() {
        return true;
    }

    private void init() {
        mPresenter = new CollectCenterPresenter(this);
        mListviewHelper = new ListviewUpDownHelper(mPresenter);
        mListviewHelper.bind(this);
        mListview.setOnItemClickListener(this);
        mListview.setOnItemLongClickListener(this);

        mListviewHelper.autoRefresh(1000);
    }

    /*@Override
    public View initViewItem(LayoutInflater inflater, int position, View convertView, ViewGroup parent) {
        ViewHolder mholder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_mesagecenter, parent, false);
            mholder = new ViewHolder(convertView);
            convertView.setTag(mholder);
        } else {
            mholder = (ViewHolder) convertView.getTag();
        }
        Bulletin item = (Bulletin) mListview.getAdapter().getItem(position);

        String type = item.getTypeName();
//        if (TextUtils.isEmpty(type)) {
//            mholder.mType.setText("公告");
//        } else {
//            mholder.mType.setText(item.getTypeName());
//        }
        mholder.mType.setVisibility(View.GONE);
        mholder.mTitle.setText(item.getTitle());
        mholder.mCreateTime.setText(DateUtils.fromTodaySimple(item
                .getCreateTime()));
        mholder.mCreateUser.setText(item.getCreateUserName());

        if (item.isRead() == 1) {
            mholder.mTitle.setTextColor(Color.BLACK);
        } else {
            mholder.mTitle.setTextColor(ContextCompat.getColor(this, R.color.app_colorPrimary));
        }

        return convertView;
    }*/
    @Override
    public View initViewItem(LayoutInflater inflater, int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_mesagecenter,
                    parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Bulletin item = (Bulletin) mListview.getAdapter().getItem(position);

        String typeName = item.getTypeName();
   /*     if (typeName == null | TextUtils.isEmpty(typeName)) {
//            mholder.mType.setText("公告");
        } else {
//            mholder.mType.setText(item.getTypeName());
        }*/
        holder.mTypeTitle.setText(item.getTitle());
        holder.mTitle.setText(item.getContent());
        holder.mCreateTime.setText(DateUtils.fromTodaySimple(item
                .getCreatetime()));
//        mholder.mCreateUser.setText(item.getCreateUserName());

        if (item.isRead() == 1) {
            holder.mTitle.setTextColor(getResources().getColor(R.color.tvc6));
        } else {
            holder.mTitle.setTextColor(getResources().getColor(R.color.app_theme));
        }

        switch (item.getLabel()) {
            case 1://系统消息
                holder.mType.setImageResource(R.mipmap.notice);
                break;
            case 11://公告消息
                holder.mType.setImageResource(R.mipmap.notice);
                break;
            case 25://日程管理
                holder.mType.setImageResource(R.mipmap.schedule);
                break;
            case 31://审批管理
                holder.mType.setImageResource(R.mipmap.approval);
                break;
            case 41://会议通知
                holder.mType.setImageResource(R.mipmap.notice);
                break;
            case 51://跟进评论
                holder.mType.setImageResource(R.mipmap.notice);
                break;
            case 55://评论回复
                holder.mType.setImageResource(R.mipmap.notice);
                break;

            default:
                holder.mType.setImageResource(R.mipmap.mholder);
                break;
        }

        return convertView;
    }

    @Override
    public void dataSetChangedListener(List<Bulletin> data) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, MessageDetail.class);
        intent.putExtra(MessageDetail.Flag_Input_Bulletin, (Bulletin) mListview.getAdapter().getItem(position));
        intent.putExtra("position", position);
//        startActivity(intent);
        startActivityForResult(intent, 1001);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        new MaterialDialog.Builder(this).title("提示").theme(Theme.LIGHT)
                .content("确认删除所选的收藏项?")
                .positiveText("确定").positiveColorRes(R.color.app_colorPrimary)
                .negativeText("取消").positiveColorRes(R.color.app_secondary_text)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mPresenter.cancelCollect(position);
                    }
                }).show();
        return true;
    }

    @Override
    public void setAdapter(BaseAdapter adapter) {
        mListview.setAdapter(adapter);
    }

    @Override
    public void showRefresh(int delay) {
        if (delay <= 0)
            mListviewHelper.autoRefresh();
        else
            mListviewHelper.autoRefresh(delay);
    }

    @Override
    public void refreshComplete() {
        mListviewHelper.refreshComplete();
    }

    @Override
    public void loadMoreComplete(boolean emptyResult, boolean hasMore) {
        mListviewHelper.loadMoreComplete(emptyResult, hasMore);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1001:
                if (data != null) {
                    mListviewHelper.refreshComplete();
                    Bundle bundle = data.getExtras();
                    int num = bundle.getInt("position");
                    if (num != -1) {
                        mPresenter.cancelCollect(num);
                    }
                }
                return;
            default:
                break;
        }
    }

    static class ViewHolder {
        @BindView(R.id.mType)
        ImageView mType;
        @BindView(R.id.mTitle)
        TextView mTitle;
        /* @BindView(R.id.mCreateUser)
         TextView mCreateUser;*/
        @BindView(R.id.mCreateTime)
        TextView mCreateTime;
        @BindView(R.id.mTypeTitle)
        TextView mTypeTitle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
