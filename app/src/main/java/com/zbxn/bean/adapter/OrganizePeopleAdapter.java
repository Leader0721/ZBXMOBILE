package com.zbxn.bean.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbxn.R;
import com.zbxn.activity.main.contacts.ContactsChoseActivity;
import com.zbxn.init.eventbus.EventCustom;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.utils.KeyEvent;

import org.simple.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名称：申请列表的adapter
 * 创建人：Wuzy
 * 创建时间：2016/10/10 10:05
 */
public class OrganizePeopleAdapter extends BaseAdapter {

    private Activity mContext;
    private List<Contacts> mList;
    private ICustomListener mListener;

    private boolean mIsVisWord = true;

    public OrganizePeopleAdapter(Activity mContext, List<Contacts> mList, ICustomListener listener
            , boolean isVisWord) {
        this.mContext = mContext;
        this.mList = mList;
        this.mListener = listener;
        this.mIsVisWord = isVisWord;

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_item_organize_people, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Contacts entity = mList.get(position);

        //姓名
        holder.mRemarkName.setText(entity.getUserName() + "");
        String mBaseUrl = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
        ImageLoader.getInstance().displayImage(mBaseUrl + entity.getPortrait(), holder.mPortrait);
        holder.mNumber.setText("(" + entity.getNumber() + ")");
        if (entity.getGender() == 0) {
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.my_woman);
            // 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.mRemarkName.setCompoundDrawables(drawable, null, null, null);
        } else {
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.my_man);
            // 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.mRemarkName.setCompoundDrawables(drawable, null, null, null);
        }
        holder.tvDepartment.setText(entity.getDepartmentName());
        holder.tvPosition.setText(entity.getPositionName());

        return convertView;
    }

    private static Contacts lastItemInfo = null;

    // 修改选中的状态
    public void changeState(Contacts itemInfo) { // 多选时
        // 单选时
        /*if (lastItemInfo != null) {
            lastItemInfo.setSelected(false);// 取消上一次的选中状态
        }*/
        itemInfo.setSelected(!itemInfo.isSelected());// 直接取反即可
        //lastItemInfo = itemInfo; // 记录本次选中的位置
        notifyDataSetChanged(); // 通知适配器进行更新
        if (itemInfo.isSelected()) {
            ContactsChoseActivity.mHashMap.put(itemInfo.getId() + "", itemInfo.getUserName());
        } else {
            ContactsChoseActivity.mHashMap.remove(itemInfo.getId() + "");
        }
        //通知更新选择人
        EventCustom eventCustom = new EventCustom();
        eventCustom.setTag(KeyEvent.UPDATECONTACTSSELECT);
        eventCustom.setObj("");
        EventBus.getDefault().post(eventCustom);
    }

    static class ViewHolder {
        @BindView(R.id.mCaptialChar)
        TextView mCaptialChar;
        @BindView(R.id.mPortrait)
        CircleImageView mPortrait;
        @BindView(R.id.mRemarkName)
        TextView mRemarkName;
        @BindView(R.id.mNumber)
        TextView mNumber;
        @BindView(R.id.tv_department)
        TextView tvDepartment;
        @BindView(R.id.tv_position)
        TextView tvPosition;
        @BindView(R.id.mLayout)
        LinearLayout mLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
