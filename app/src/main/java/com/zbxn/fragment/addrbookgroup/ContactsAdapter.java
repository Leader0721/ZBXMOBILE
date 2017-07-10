package com.zbxn.fragment.addrbookgroup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbxn.R;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.utils.ConfigUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import widget.stickylistheaders.StickyListHeadersAdapter;

/**
 * @author GISirFive
 * @time 2016/8/10
 */
public class ContactsAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private LayoutInflater mInflater;
    private List<Contacts> mList;
    private Map<Integer, String> mHDMap;
    private List<Contacts> mSelectedList;
    private ICustomListener mListener;

    /**
     * 当前是否为多选模式
     */
    private boolean multiChoiceEnable = false;

    /**
     * @param context
     * @param list
     */
    public ContactsAdapter(Context context, List<Contacts> list, ICustomListener listener) {
        mInflater = LayoutInflater.from(context);
        mListener = listener;
        if (list != null)
            mList = list;
        else
            mList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Contacts getItem(int position) {
        try {
            return mList.get(position);
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderChild holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_addrbookpy, parent, false);
            holder = new ViewHolderChild(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderChild) convertView.getTag();
        }
        Contacts item = getItem(position);
        //姓名
        holder.mRemarkName.setText(item.getUserName() + "");
        String mBaseUrl = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);

        DisplayImageOptions   options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.userhead_img)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.userhead_img)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.userhead_img)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                //.displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                .build();                                   // 创建配置过得DisplayImageOption对象
        ImageLoader.getInstance().displayImage(mBaseUrl + item.getPortrait(), holder.mPortrait,options);

        holder.mCheckBox.setVisibility(multiChoiceEnable ? View.VISIBLE : View.GONE);
        if (multiChoiceEnable) {
            boolean selected = mSelectedList != null && mSelectedList.contains(item);
            holder.mCheckBox.setChecked(selected);
        }

        return convertView;
    }

    @Override
    public View getHeaderView(final int position, View convertView, ViewGroup parent) {
        ViewHolderHeader holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_addrbookpy_group, parent, false);
            holder = new ViewHolderHeader(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderHeader) convertView.getTag();
        }
        Contacts item = getItem(position);
        holder.mDepartmentName.setText(item.getDepartmentName());
        //holder.mDepartmentImage.setVisibility(multiChoiceEnable ? View.GONE : View.VISIBLE);
//        holder.mCheckBoxHeader.setVisibility(multiChoiceEnable ? View.VISIBLE : View.GONE);
        /*holder.mCheckBoxHeader.setVisibility(View.GONE);
        holder.mCheckBoxHeader.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mListener.onCustomListener(0,isChecked,position);
            }
        });*/
        holder.mDepartmentImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCustomListener(0, null, position);
            }
        });
        return convertView;
    }

    public long getHeaderId(int position) {
        try {
            if (position == 0) {
                if (!mHDMap.containsKey(position))
                    mHDMap.put(0, getItem(position).getDepartmentId());
                return 0;
            }
            String id = getItem(position).getDepartmentId();
            String idPre = getItem(position - 1).getDepartmentId();
            if (id.equals(idPre))
                return getHeaderId(position - 1);
            if (!mHDMap.containsKey(position))
                mHDMap.put(position, id);

        } catch (Exception e) {

        }
        return position;
    }

    /***
     * 为列表传入数据
     *
     * @param list
     */
    public void resetData(List<Contacts> list) {
        mList.clear();
        if (list == null)
            list = new ArrayList<>();
        mList = list;
        //JDK7中的Collections.Sort方法实现中，如果两个值是相等的，那么compare方法需要返回0，否则可能会在排序时抛错，而JDK6是没有这个限制的
        Collections.sort(mList, new Comparator<Contacts>() {
            @Override
            public int compare(Contacts a, Contacts b) {
                String idA = a.getDepartmentId();
                String idB = b.getDepartmentId();
                if (idA.compareTo(idB) == 0) {
                    return 0;
                } else if (idA.compareTo(idB) > 0)
                    return 1;
                else
                    return -1;
            }
        });

        mHDMap = new HashMap<>();

        notifyDataSetChanged();
    }

    public boolean getMultiChoiceEnable() {
        return this.multiChoiceEnable;
    }

    /**
     * 设置当前是否为多选模式
     *
     * @param b
     */
    public void setMultiChoiceEnable(boolean b) {
        if (multiChoiceEnable == b)
            return;
        multiChoiceEnable = b;
        if (multiChoiceEnable)
            mSelectedList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void changeItemSelectState(int position) {
        Contacts contacts = getItem(position);
        if (mSelectedList.contains(contacts))
            mSelectedList.remove(contacts);
        else
            mSelectedList.add(contacts);
        notifyDataSetChanged();
    }

    public void changeHeaderSelectState(int headerId) {
        String departmentId = mHDMap.get(headerId);
        List<Contacts> tempList = new ArrayList<>();
        for (Contacts c : mList) {
            if (c.getDepartmentId().equals(departmentId))
                tempList.add(c);
        }
        if (mSelectedList.containsAll(tempList)) {
            mSelectedList.removeAll(tempList);
        } else {
            mSelectedList.removeAll(tempList);
            mSelectedList.addAll(tempList);
        }
        tempList = null;
        notifyDataSetChanged();
    }

    public List<Contacts> getSelectedList() {
        return mSelectedList;
    }

    static class ViewHolderChild {
        @BindView(R.id.mCheckBox)
        CheckBox mCheckBox;
        @BindView(R.id.mPortrait)
        CircleImageView mPortrait;
        @BindView(R.id.mRemarkName)
        TextView mRemarkName;

        ViewHolderChild(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolderHeader {
        @BindView(R.id.mCheckBoxHeader)
        CheckBox mCheckBoxHeader;
        @BindView(R.id.mDepartmentName)
        TextView mDepartmentName;
        @BindView(R.id.mDepartmentImage)
        ImageView mDepartmentImage;
        @BindView(R.id.mDepartmentImageLayout)
        LinearLayout mDepartmentImageLayout;


        public ViewHolderHeader(View root) {
            ButterKnife.bind(this, root);
        }

    }
}
