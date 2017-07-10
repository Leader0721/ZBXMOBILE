package com.zbxn.pub.bean.adapter.base;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * <b>隶属于{@link Adapter}的数据装饰器回调接口</b>
 * 用于操作ItemView的布局参数、数据重置等<br>
 * 所有使用该装饰器的ListView建议实现该接口
 * @author GISirFive
 * @param <T>
 */
public interface IItemViewControl<T> {
	/**
	 * <b>用于初始化List中列表项的布局、参数、数据等</b>
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
	 */
	public View initViewItem(LayoutInflater inflater, int position,
                             View convertView, ViewGroup parent);
	
	/**
	 * <b>装饰器中的数据集合发生改变</b>
	 * @param data 改变后的数据集合
	 */
	public void dataSetChangedListener(List<T> data);
}
