package com.zbxn.bean.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.bean.ContactsDepartment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 项目名称：申请列表的adapter
 * 创建人：Wuzy
 * 创建时间：2016/10/10 10:05
 */
public class ContactsDepartmentOnlyAdapter extends BaseAdapter {

    private Context mCxontext;
    private List<ContactsDepartment> mList;
    private ICustomListener listener;

    public ContactsDepartmentOnlyAdapter(Context mContext, List<ContactsDepartment> mList, ICustomListener listener) {
        this.mCxontext = mContext;
        this.mList = mList;
        this.listener = listener;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mCxontext, R.layout.list_item_contacts_department_only, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ContactsDepartment entity = mList.get(position);


        //姓名
        holder.mRemarkName.setText(entity.getDepartmentName() + "");
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.mRemarkName)
        TextView mRemarkName;
//        @BindView(R.id.mMobileNumber)
//        TextView mMobileNumber;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
