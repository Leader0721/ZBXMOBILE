package com.zbxn.createworkblog;

import java.util.ArrayList;
import java.util.List;

import com.zbxn.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HintAdapter extends BaseAdapter {

	private LayoutInflater mInflater;

	private List<String> mList = new ArrayList<String>();

	public HintAdapter(Context context) {
		mInflater = LayoutInflater.from(context);

		mList.add("今天的主要工作内容如下：");
		mList.add("• 上班前打扫了一下卫生；");
		mList.add("• 今天又是忙碌的一天；");
		mList.add("• 每天为了挣N币而努力工作；");
		mList.add("• 努力学习新知识，为3.02的开发做准备；");
		mList.add("• 拜访了几个意向比较大的客户，询问了一下大概的需求；");
		mList.add("• 闲暇之余看了点书，为自己充电；");
		mList.add("• 最近的会议特别多，占用了很多时间；");
		mList.add("• 完成日常工装卫生检查，日常审批业务处理；");
		mList.add("• 今天搜索了一些简历，预约了几场面试；");
		mList.add("• 管理后勤物资，包括易耗办公用品、固定物资、烟酒及宣传页等；");
		mList.add("• 行政部没有男同事，什么事都得姑娘自己上，工作中不分男女");
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public String getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = mInflater.inflate(
					R.layout.createworkblog_bloghint_item, parent, false);
		TextView content = (TextView) convertView.findViewById(R.id.mContent);
		content.setText(getItem(position));
		return convertView;
	}

}
