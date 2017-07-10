package com.zbxn.pub.data.base;

public interface BaseAsyncTaskInterface {
    /**
     * 提交数据中
     *
     * @param funId
     */
    void dataSubmit(int funId);

    /**
     * 解析数据
     *
     * @param funId
     * @param json
     * @return
     * @throws Exception
     */
    Object dataParse(int funId, String json) throws Exception;

    /**
     * 获取数据成功
     *
     * @param funId
     * @param result
     */
    void dataSuccess(int funId, Object result);

    /**
     * 获取数据失败
     *
     * @param funId
     */
    void dataError(int funId);
}