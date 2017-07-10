package com.zbxn.fragment.addrbookpy;

import android.content.Context;
import android.widget.SectionIndexer;

import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.bean.adapter.base.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GISirFive
 * @time 2016/8/8
 */
public class ContactAdapter extends BaseAdapter<Contacts> implements SectionIndexer {

    private List<Contacts> mList;

    /**
     * @param context
     * @param list
     */
    public ContactAdapter(Context context, List<Contacts> list) {
        super(context, list);
    }


    @Override
    public List<Contacts> getDataList() {
        if(mList == null)
            mList = new ArrayList<>();
        return mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }


    @Override
    public Contacts getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private String[] mSections = new String[] { "#", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

    @Override
    public Object[] getSections() {
        return mSections;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        String section = mSections[sectionIndex];
        for (int i = 0; i < mList.size(); i++) {
            String l = mList.get(i).getCaptialChar();
            if(l != null && l.equals(section))
                return i;
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }


}
