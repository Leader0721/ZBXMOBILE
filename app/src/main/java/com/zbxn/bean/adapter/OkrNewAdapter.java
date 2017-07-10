package com.zbxn.bean.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbxn.R;
import com.zbxn.bean.InviteEmplEntity;
import com.zbxn.bean.OkrRankingEntity;
import com.zbxn.pub.bean.adapter.base.BaseAdapter;
import com.zbxn.pub.utils.ConfigUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/11/29.
 */
public class OkrNewAdapter extends BaseAdapter {


    /**
     * @param context
     * @param list
     */
    private Context mContext;
    private List<InviteEmplEntity> mList;

    public OkrNewAdapter(Context context, List list, Context mContext, List<InviteEmplEntity> mList) {
        super(context, list);
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public List getDataList() {
        return null;
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
            convertView = View.inflate(mContext, R.layout.list_item_inviteemployee, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        InviteEmplEntity entity = mList.get(position);
        String name = holder.inviteName.getText().toString();
        String phoneNum = holder.invitePhoneNum.getText().toString();

        holder.inviteName.setText(name);
        holder.invitePhoneNum.setText(phoneNum);




        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.invite_name)
        EditText inviteName;
        @BindView(R.id.invite_phoneNum)
        EditText invitePhoneNum;
        @BindView(R.id.delete_item)
        ImageView deleteItem;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
