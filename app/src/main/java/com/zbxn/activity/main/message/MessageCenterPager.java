package com.zbxn.activity.main.message;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zbxn.R;
import com.zbxn.activity.MessageDetail;
import com.zbxn.activity.examinationandapproval.ApplyDetailActivity;
import com.zbxn.activity.main.message.messagepager.IMessageView;
import com.zbxn.activity.main.message.messagepager.MessagePresenter;
import com.zbxn.activity.mission.TaskDetailsActivity;
import com.zbxn.activity.schedule.NewTaskActivity;
import com.zbxn.pub.bean.Bulletin;
import com.zbxn.pub.bean.adapter.base.IItemViewControl;
import com.zbxn.pub.frame.mvp.AbsBaseFragment;
import com.zbxn.pub.utils.ListviewUpDownHelper;
import com.zbxn.pub.widget.swipemenulistview.Menu;
import com.zbxn.pub.widget.swipemenulistview.MenuItem;
import com.zbxn.pub.widget.swipemenulistview.SlideAndDragListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import utils.DateUtils;

/**
 * Created by Administrator on 2016/8/3.
 * 消息页面
 */
public class MessageCenterPager extends AbsBaseFragment implements IMessageView,
        IItemViewControl<Bulletin>,
        SlideAndDragListView.OnListItemClickListener,
        SlideAndDragListView.OnMenuItemClickListener {

    @BindView(R.id.listview_content)
    SlideAndDragListView mListView;

    private MessagePresenter messagePresenter;
    private ListviewUpDownHelper mListviewHelper;

    public static String LABELKEY = "";
    public static String KEYWORD = "";

    public MessageCenterPager() {
        mTitle = "消息";
    }

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.main_messagecenter, container, false);
        ButterKnife.bind(this, root);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getContext());
        MobclickAgent.onPageStart("MessageCenterPager");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getContext());
        MobclickAgent.onPageEnd("MessageCenterPager");
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {
        initSlideMenu();

        messagePresenter = new MessagePresenter(this);
        mListView.setAdapter(messagePresenter.getAdapter());
        mListView.setDivider(null);
        mListView.setOnListItemClickListener(this);
        mListView.setOnMenuItemClickListener(this);

        mListviewHelper = new ListviewUpDownHelper(messagePresenter);
        mListviewHelper.bind(root);

        messagePresenter.loadLocalCache();
    }

    // 初始化Item侧滑菜单
    private void initSlideMenu() {
        List<Menu> menuList = new ArrayList<>();
        Menu menu = new Menu(true, false, 0);
        int width = (int) getResources().getDimension(R.dimen.app_main_messagecenter_menu);
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.color.app_listview_slidemenu_1);
        MenuItem.Builder builder = new MenuItem.Builder()
                .setWidth(width)
                .setBackground(drawable)
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setTextColor(Color.WHITE).setText("标为已读");
        menu.addItem(builder.build());
        menuList.add(menu);
        mListView.setMenu(menu);
        mListView.setOnSlideListener(new SlideAndDragListView.OnSlideListener() {

            @Override
            public void onSlideOpen(View view, View parentView, int position, int direction) {
            }

            @Override
            public void onSlideClose(View view, View parentView, int position, int direction) {
            }
        });
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }


    @Override
    public void dataSetChangedListener(List<Bulletin> data) {

    }

    @Override
    public void onListItemClick(View v, int position) {
        Bulletin item = messagePresenter.getItem(position);
        switch (item.getLabel()) {
            case 1://系统消息
                break;
            case 11://公告消息
                Intent intent = new Intent(getActivity(), MessageDetail.class);
                intent.putExtra(MessageDetail.Flag_Input_Bulletin, item);
                startActivity(intent);
                break;
            case 25://日程管理
                Intent schIntent = new Intent(getActivity(), NewTaskActivity.class);
                schIntent.putExtra("id", item.getRelatedid() + "");
                schIntent.putExtra("sign", 1);
                schIntent.putExtra("flag", -1);
                startActivity(schIntent);
                break;
            case 31://审批管理
//                Intent applyIntent = new Intent(getActivity(), ApplyActivity.class);
//                startActivity(applyIntent);
                Intent applyIntent = new Intent(getContext(), ApplyDetailActivity.class);
                applyIntent.putExtra("approvalID", item.getRelatedid() + "");
                startActivity(applyIntent);
                break;
            case 32://任务管理
                Intent missionIntent = new Intent(getContext(), TaskDetailsActivity.class);
                missionIntent.putExtra("id", item.getRelatedid() + "");
                startActivity(missionIntent);
                break;
            case 41://会议通知
                break;
            case 51://跟进评论
                break;
            case 55://评论回复
                break;
        }

        messagePresenter.setRead(position, "1");

    }

    @Override
    public int onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction) {
        messagePresenter.setRead(itemPosition, "1");
        return Menu.ITEM_SCROLL_BACK;
    }

    public ViewHolder holder = null;

    @Override
    public View initViewItem(LayoutInflater inflater, int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_mesagecenter,
                    parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Bulletin item = messagePresenter.getItem(position);

//        String typeName = item.getTypeName();
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
            holder.imgIsread.setVisibility(View.GONE);
            holder.mTitle.setTextColor(getResources().getColor(R.color.tvc9));
        } else {
            holder.imgIsread.setVisibility(View.VISIBLE);
            holder.mTitle.setTextColor(getResources().getColor(R.color.tvc3));
        }

        switch (item.getLabel()) {
            case -1://系统消息
          //      holder.mType.setImageResource(R.mipmap.mholder);
//                holder.mType.setImageResource(R.mipmap.mholder);
//                holder.mTypeTitle.setTextColor(Color.parseColor("#F3725E"));
                break;
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
            case 32://任务管理
                holder.mType.setImageResource(R.mipmap.home_icon_task);
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
                holder.mType.setImageResource(R.mipmap.notice);
                break;
        }

        return convertView;
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

    static class ViewHolder {
        @BindView(R.id.mType)
        ImageView mType;
        @BindView(R.id.mTitle)
        TextView mTitle;
        @BindView(R.id.mCreateTime)
        TextView mCreateTime;
        @BindView(R.id.mTypeTitle)
        TextView mTypeTitle;
        @BindView(R.id.img_isread)
        ImageView imgIsread;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
