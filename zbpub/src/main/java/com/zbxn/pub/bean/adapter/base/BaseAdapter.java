package com.zbxn.pub.bean.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 重写{@link android.widget.BaseAdapter} 使其更灵活通用
 *
 * @param <T> 适用于此Adapter的Bean
 * @author GISirFive
 * @since 2015-12-14 下午9:36:15
 */
public abstract class BaseAdapter<T> extends android.widget.BaseAdapter
        implements IItemDataControl<T> {

    protected IItemViewControl<T> callback;
    private LayoutInflater inflater;

    /**
     * 请求数据页面
     */
    private int page = 1;

    /**
     * @param context
     * @param list
     */
    public BaseAdapter(Context context, List<T> list) {
        inflater = LayoutInflater.from(context);
        List<T> data = getDataList();
        if (list != null)
            data.addAll(list);
    }

    /**
     * 获取绑定的数据源
     */
    public abstract List<T> getDataList();

    public abstract T getItem(int position);

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (callback != null)
            convertView = callback.initViewItem(inflater, position,
                    convertView, parent);

        return convertView;
    }

    /**
     * 设置监听
     *
     * @param callback
     */
    public void setOnDataItemCallbackListener(IItemViewControl<T> callback) {
        this.callback = callback;
    }

    @Override
    public int resetData(List<? extends T> data) {
        List<T> list = getDataList();
        list.clear();
        resetPage();
        if (data != null) {
            boolean isAdded = list.addAll(data);
            if (isAdded)
                addPage();
        }

        if (callback != null)
            callback.dataSetChangedListener(list);
        notifyDataSetChanged();
        return list.size();
    }

    @Override
    public int addInTop(List<? extends T> data) {
        if (data == null)
            return 0;
        if (getCount() == 0)
            return resetData(data);
        int count = 0;

        List<T> temp = getDataList();
        // 根据绑定数据源的类型不同，选择不同的添加方式
        if (temp instanceof LinkedList) {
            LinkedList<T> list = (LinkedList<T>) temp;
            for (T t : data) {
                if (list.contains(t))
                    continue;
                list.addFirst(t);
                count++;
            }
        } else if (temp instanceof ArrayList) {
            ArrayList<T> list = (ArrayList<T>) temp;
            for (T t : data) {
                if (list.contains(t))
                    continue;
                list.add(0, t);
                count++;
            }
        }

        if (callback != null)
            callback.dataSetChangedListener(temp);
        notifyDataSetChanged();

        return count;
    }

    @Override
    public int addInBottom(List<? extends T> data) {
        if (data == null)
            return 0;
        List<T> list = getDataList();
        int count = 0;
        for (T t : data) {
            if (list.contains(t))
                continue;
            list.add(t);
            count++;
        }
        if (count > 0)
            addPage();
        if (callback != null)
            callback.dataSetChangedListener(list);
        notifyDataSetChanged();
        return count;
    }

    /**
     * <b>请求页面索引</b>
     */
    public final int getPage() {
        return page;
    }

    /**
     * <b>将请求页面的索引重置</b>
     */
    public final void resetPage() {
        this.page = 1;
    }

    /**
     * 请求页面索引自增</br> <b>该方法应该在请求数据成功后调用</b>
     */
    public final void addPage() {
        ++this.page;
    }

}