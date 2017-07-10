package com.zbxn.pub.bean.adapter.base;

import java.util.List;

/**
 * 数据管理、分页
 *
 * @param <T>
 * @author GISirFive
 * @since 2015-12-14 下午9:31:50
 */
public interface IItemDataControl<T> {

    /**
     * 重新绑定数据
     *
     * @param data
     * @return 加入的数量
     */
    int resetData(List<? extends T> data);

    /**
     * 从列表顶部加入数据
     *
     * @param data
     * @return 加入的数量
     */
    int addInTop(List<? extends T> data);

    /**
     * 从列表底部加入数据
     *
     * @param data
     * @return 加入的数量
     */
    int addInBottom(List<? extends T> data);

    /**
     * <b>请求页面索引</b>
     */
    int getPage();

    /**
     * <b>将请求页面的索引重置为0</b>
     */
    void resetPage();

    /**
     * 请求页面索引自增</br> <b>该方法应该在请求数据成功后调用</b>
     */
    void addPage();

}
