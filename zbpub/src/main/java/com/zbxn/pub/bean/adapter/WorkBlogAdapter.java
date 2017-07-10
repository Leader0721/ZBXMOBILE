package com.zbxn.pub.bean.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.zbxn.pub.bean.WorkBlog;
import com.zbxn.pub.bean.adapter.base.BaseAdapter;

public class WorkBlogAdapter extends BaseAdapter<WorkBlog>{

	private List<WorkBlog> mList;
	
	public WorkBlogAdapter(Context context, List<WorkBlog> list) {
		super(context, list);
	}

	@Override
	public int getCount() {
		return getDataList().size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public List<WorkBlog> getDataList() {
		if(mList == null)
			mList = new ArrayList<>();
		return mList;
	}

	@Override
	public WorkBlog getItem(int position) {
		return getDataList().get(position);
	}

}
