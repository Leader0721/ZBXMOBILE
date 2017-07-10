package com.zbxn.pub.bean.dbutils.base;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.zbxn.pub.application.BaseApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbsBaseDbUtils<T> implements IDBController<T> {

    public AbsBaseDbUtils() {

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> queryAll() {
        List<T> list = new ArrayList<T>();
        try {
            list = (List<T>) BaseApp.DBLoader.findAll(Selector.from(getClazz())
                    .orderBy("createTime", true));
            if (list == null || list.isEmpty())
                list = (List<T>) BaseApp.DBLoader.findAll(getClazz());
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public T queryById(String id) {
        try {
            T t = (T) BaseApp.DBLoader.findById(getClazz(), id);
            return t;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int add(T t) {
        try {
            BaseApp.DBLoader.save(t);
        } catch (DbException e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    @Override
    public int add(T... t) {
        List<T> list = Arrays.asList(t);
        try {
            BaseApp.DBLoader.saveAll(list);
        } catch (DbException e) {
            e.printStackTrace();
            return 0;
        }
        return list.size();
    }

    @Override
    public void update(T t, String... updateColumnNames) {
        try {
            BaseApp.DBLoader.update(t, updateColumnNames);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            BaseApp.DBLoader.deleteById(getClazz(), id);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAll() {
        try {
            BaseApp.DBLoader.deleteAll(getClazz());
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
