package com.zbxn.bean.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbxn.R;
import com.zbxn.activity.ContactsDetail;
import com.zbxn.activity.main.contacts.ContactsChoseActivity;
import com.zbxn.init.eventbus.EventCustom;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.bean.ContactsDepartment;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.utils.KeyEvent;

import org.simple.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名称：申请列表的adapter
 * 创建人：Wuzy
 * 创建时间：2016/10/10 10:05
 */
public class ContactsDepartmentAdapter extends BaseExpandableListAdapter {
    private Context mContext;//
    private final List<ContactsDepartment> listGroup;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    private ICustomListener mListener;// 视图容器
    private int mType;//

    private HashMap<String, Integer> mAlphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
    private String[] mSections;// 存放存在的汉语拼音首字母
//        private CustomListenerExpand listenerExpand;// 视图容器

//        public interface CustomListenerExpand {
//            public void onCustomListener(int obj0, Object obj1, int groupPosition, int childPosition);
//        }

    public ContactsDepartmentAdapter(Activity context, final List<ContactsDepartment> data, ICustomListener listener
            , final HashMap<String, Integer> alphaIndexer, final String[] sections, int type) {
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.mContext = context;
        this.listGroup = data;
        this.mListener = listener;
        this.mType = type;

        mAlphaIndexer = alphaIndexer;
        mSections = sections;

        for (int i = 0; i < listGroup.size(); i++) {
            // 当前汉语拼音首字母
            // getAlpha(list.get(i));
            String currentStr = listGroup.get(i).getCaptialChar();
            // 上一个汉语拼音首字母，如果不存在为“ ”
            String previewStr = (i - 1) >= 0 ? listGroup.get(i - 1).getCaptialChar() : " ";
            if (!previewStr.equals(currentStr)) {
                String name = listGroup.get(i).getCaptialChar();
                mAlphaIndexer.put(name, i);
                mSections[i] = name;
            }
        }
    }

    @Override
    public int getGroupCount() {
        return listGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listGroup.get(groupPosition).getListContacts().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listGroup.get(groupPosition).getListContacts().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        // 这个的意思就是你的每个item中元素的id是否稳定，如果你用position当id ，那就是不稳定的，如果有自己特定的id那就是稳定的
        // 就是干这个用的
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolderHeader holder = null;
        if (convertView == null) {
            convertView = listContainer.inflate(R.layout.list_item_contacts_department_group, parent, false);
            holder = new ViewHolderHeader(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderHeader) convertView.getTag();
        }

        final ContactsDepartment entity = listGroup.get(groupPosition);

        String currentStr = entity.getCaptialChar();
        String previewStr = (groupPosition - 1) >= 0 ? listGroup.get(groupPosition - 1).getCaptialChar() : " ";

        if (!previewStr.equals(currentStr)) {
            holder.mCaptialChar.setVisibility(View.VISIBLE);
            holder.mCaptialChar.setText(currentStr);
        } else {
            holder.mCaptialChar.setVisibility(View.GONE);
        }

        holder.mDepartmentName.setText(entity.getDepartmentName());
        /*holder.mDepartmentImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCustomListener(0, null, groupPosition);
            }
        });*/
        if (isExpanded) {
            holder.mDepartmentImage.setImageResource(R.mipmap.bg_banner_up);
        } else {
            holder.mDepartmentImage.setImageResource(R.mipmap.bg_banner_down);
        }

        if (entity.isSelected()) {
            holder.mCheck.setSelected(true);
        } else {
            holder.mCheck.setSelected(false);
        }
        //可选人
        if (mType == 1) {
            holder.mCheck.setVisibility(View.VISIBLE);

            holder.mCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.isSelected()) {
                        //v.setSelected(false);
                        entity.setSelected(false);
                        for (int i = 0; i < entity.getListContacts().size(); i++) {
                            entity.getListContacts().get(i).setSelected(false);
                            ContactsChoseActivity.mHashMap.remove(entity.getListContacts().get(i).getId() + "");
                        }
                    } else {
                        //v.setSelected(true);
                        entity.setSelected(true);
                        for (int i = 0; i < entity.getListContacts().size(); i++) {
                            entity.getListContacts().get(i).setSelected(true);
                            ContactsChoseActivity.mHashMap.put(entity.getListContacts().get(i).getId() + "", entity.getListContacts().get(i).getUserName());
                        }
                    }
                    notifyDataSetChanged();
                    //通知更新选择人
                    EventCustom eventCustom = new EventCustom();
                    eventCustom.setTag(KeyEvent.UPDATECONTACTSSELECT);
                    eventCustom.setObj("");
                    EventBus.getDefault().post(eventCustom);
                }
            });
        }

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = listContainer.inflate(R.layout.list_item_contacts_people, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ContactsDepartment entity = listGroup.get(groupPosition);
        final Contacts entitySub = entity.getListContacts().get(childPosition);

        holder.mCaptialChar.setVisibility(View.GONE);

        //姓名
        holder.mRemarkName.setText(entitySub.getUserName() + "");
        String mBaseUrl = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
        ImageLoader.getInstance().displayImage(mBaseUrl + entitySub.getPortrait(), holder.mPortrait);
        holder.mMobileNumber.setText("ID:" + entitySub.getNumber());

        if (entitySub.isSelected()) {
            holder.mCheck.setSelected(true);
        } else {
            holder.mCheck.setSelected(false);
        }
//        holder.mCheck.setEnabled(false);
        //可选人
        if (mType == 1 || mType == 2) {
            holder.mCheck.setVisibility(View.VISIBLE);
            int count = 0;
            for (int i = 0; i < entity.getListContacts().size(); i++) {
                if (entity.getListContacts().get(i).isSelected()) {
                    count++;
                }
            }
            if (count >= entity.getListContacts().size()) {
                entity.setSelected(true);
            } else {
                entity.setSelected(false);
            }
            notifyDataSetChanged();

            //final ViewHolderPeople finalHolder = holder;
            holder.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    changeState(entitySub);

                    if (mType == 2) {
                        //通知更新选择人
                        EventCustom eventCustom = new EventCustom();
                        eventCustom.setTag(KeyEvent.UPDATECONTACTSSELECTSINGLE);
                        eventCustom.setObj("");
                        EventBus.getDefault().post(eventCustom);
                    }
                }
            });
        } else {
            holder.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mType == 0) {
                        Intent intent = new Intent(mContext, ContactsDetail.class);
                        intent.putExtra(ContactsDetail.Flag_Input_Contactor, entitySub);
                        mContext.startActivity(intent);
                        return;
                    }
                }
            });
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class ViewHolderHeader {
        @BindView(R.id.mCaptialChar)
        TextView mCaptialChar;
        @BindView(R.id.mCheck)
        ImageView mCheck;
        @BindView(R.id.mDepartmentName)
        TextView mDepartmentName;
        @BindView(R.id.mDepartmentImage)
        ImageView mDepartmentImage;
        @BindView(R.id.mDepartmentImageLayout)
        LinearLayout mDepartmentImageLayout;

        ViewHolderHeader(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolder {
        @BindView(R.id.mLayout)
        LinearLayout mLayout;
        @BindView(R.id.mCaptialChar)
        TextView mCaptialChar;
        @BindView(R.id.mCheck)
        ImageView mCheck;
        @BindView(R.id.mPortrait)
        CircleImageView mPortrait;
        @BindView(R.id.mRemarkName)
        TextView mRemarkName;
        @BindView(R.id.mMobileNumber)
        TextView mMobileNumber;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
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

    private static ContactsDepartment lastItemInfoDepartment = null;

    // 修改选中的状态
    public void changeState(ContactsDepartment itemInfo) { // 多选时
        // 单选时
        /*if (lastItemInfoDepartment != null) {
            lastItemInfoDepartment.setSelected(false);// 取消上一次的选中状态
        }*/
        itemInfo.setSelected(!itemInfo.isSelected());// 直接取反即可
        //lastItemInfoDepartment = itemInfo; // 记录本次选中的位置
        notifyDataSetChanged(); // 通知适配器进行更新
//        if (lastItemInfo.isSelected()){
//            ContactsChoseActivity.mHashMap.put(itemInfo.getId()+"",itemInfo.getUserName());
//        }else{
//            ContactsChoseActivity.mHashMap.remove(itemInfo.getId()+"");
//        }

        //通知更新选择人
//        EventCustom eventCustom = new EventCustom();
//        eventCustom.setTag(KeyEvent.UPDATECONTACTSSELECT);
//        eventCustom.setObj("");
//        EventBus.getDefault().post( eventCustom);
    }
}