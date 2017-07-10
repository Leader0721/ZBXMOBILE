package com.zbxn.pub.bean.dbutils.base;

import java.util.List;

/**
 * 数据库操作类
 *
 * @param <T>
 * @author GISirFive
 * @since 2016-7-18 下午2:41:08
 */
public interface IDBController<T> {

    /**
     * 查询所有的
     *
     * @return
     * @author GISirFive
     */
    List<T> queryAll();

    /**
     * 根据ID查询实体类
     *
     * @param id
     * @return
     * @author GISirFive
     */
    T queryById(String id);

    /**
     * 新增一条数据
     *
     * @param t
     * @return
     * @author GISirFive
     */
    int add(T t);

    /**
     * 新增多条数据
     *
     * @param t
     * @return
     * @author GISirFive
     */
    int add(T... t);

    /**
     * 更新一条数据
     *
     * @param t
     * @param updateColumnNames
     * @author GISirFive
     */
    void update(T t, String... updateColumnNames);

    /**
     * 删除指定ID的数据
     *
     * @param id
     * @author GISirFive
     */
    void deleteById(String id);

    /**
     * 删除所有数据
     *
     * @author GISirFive
     */
    void deleteAll();

    /**
     * 获取实体类对应的Class
     *
     * @return
     * @author GISirFive
     */
    Class getClazz();

}
