package com.zbxn.widget.calendar.bizs.languages;


public abstract class ZSSLManager {
    private static ZSSLManager sLanguage;

    /**
     * 获取日历语言管理器
     *
     * Get DatePicker language manager
     *
     * @return 日历语言管理器 DatePicker language manager
     */
    public static ZSSLManager getInstance() {
        if (null == sLanguage) {
        	sLanguage = new CN();
        }
        return sLanguage;
    }

    /**
     * 月份标题显示
     *
     * Titles of month
     *
     * @return 长度为12的月份标题数组 Array in 12 length of month titles
     */
    public abstract String[] titleMonth();

    /**
     * 确定按钮文本
     *
     * Text of ensure button
     *
     * @return Text of ensure button
     */
    public abstract String titleEnsure();

    /**
     * 公元前文本
     *
     * Text of B.C.
     *
     * @return Text of B.C.
     */
    public abstract String titleBC();

    /**
     * 星期标题显示
     *
     * Titles of week
     *
     * @return 长度为7的星期标题数组 Array in 7 length of week titles
     */
    public abstract String[] titleWeek();
}
