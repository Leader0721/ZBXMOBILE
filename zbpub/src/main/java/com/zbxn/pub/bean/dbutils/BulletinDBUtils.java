package com.zbxn.pub.bean.dbutils;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.Bulletin;

import java.util.ArrayList;
import java.util.List;

public class BulletinDBUtils extends DBUtils<Bulletin> {

    private static BulletinDBUtils Instance = null;

    public static BulletinDBUtils getInstance() {
        if (Instance == null)
            Instance = new BulletinDBUtils();
        return Instance;
    }

    public BulletinDBUtils() {
        super(Bulletin.class);
    }

    /**
     * 获取最新的一条数据
     *
     * @return
     * @author GISirFive
     */
    public Bulletin getLastBulletin() {
        try {
            Bulletin bulletin = BaseApp.DBLoader.findFirst(Selector.from(
                    getClazz()).orderBy("createTime", true));
            return bulletin;
        } catch (DbException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取所有可访问的数据
     *
     * @return
     * @author GISirFive
     */
    public List<Bulletin> findEnable() {
        List<Bulletin> list = new ArrayList<Bulletin>();
        try {
            Selector selector = Selector.from(getClazz())
                    .where("isDelete", "=", false).orderBy("createTime", true);
            list = BaseApp.DBLoader.findAll(selector);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }
}
