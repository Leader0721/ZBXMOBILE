package com.zbxn.widget.calendar.bizs.calendars;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.zbxn.widget.calendar.entities.DateInfo;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 日期管理器 The manager of date picker.
 *
 * @author ZSS 2015-06-12
 */
@SuppressLint("UseSparseArrays")
public final class ZSSCalendarManager {
	private static final HashMap<Integer, HashMap<Integer, DateInfo[][]>> DATE_CACHE = new HashMap<Integer, HashMap<Integer, DateInfo[][]>>();

	private static final HashMap<String, Set<String>> DECOR_CACHE_BG = new HashMap<String, Set<String>>();
	private static final HashMap<String, Set<String>> DECOR_CACHE_TL = new HashMap<String, Set<String>>();
	private static final HashMap<String, Set<String>> DECOR_CACHE_T = new HashMap<String, Set<String>>();
	private static final HashMap<String, Set<String>> DECOR_CACHE_TR = new HashMap<String, Set<String>>();
	private static final HashMap<String, Set<String>> DECOR_CACHE_L = new HashMap<String, Set<String>>();
	private static final HashMap<String, Set<String>> DECOR_CACHE_R = new HashMap<String, Set<String>>();

	private static ZSSCalendarManager sManager;

	private ZSSCalendar c;

	private ZSSCalendarManager() {
		// 默认显示为中文日历
		initCalendar(new ZSSChineseCalendar());
	}

	/**
	 * 获取月历管理器 Get calendar manager
	 *
	 * @return 月历管理器
	 */
	public static ZSSCalendarManager getInstance() {
//		if (null == sManager) {
			sManager = new ZSSCalendarManager();
//		}
		return sManager;
	}

	/**
	 * 初始化日历对象
	 * <p/>
	 * Initialization Calendar
	 *
	 * @param c
	 *            ...
	 */
	public void initCalendar(ZSSCalendar c) {
		this.c = c;
	}
	/**
     * 获取当前的月份   已经加1
     * @return
     */
    public int getCurrentMonth(){
    	Calendar c1 = Calendar.getInstance();
    	return c1.get(Calendar.MONTH)+1;
    }
    /**
     * 获取当前的年份  
     * @return
     */
    public int getCurrentyYear(){
    	Calendar c1 = Calendar.getInstance();
    	return c1.get(Calendar.YEAR);
    }
    /**
     * 获取当前的日  
     * @return
     */
    public int getCurrentDay(){
    	Calendar c1 = Calendar.getInstance();
    	return c1.get(Calendar.DAY_OF_MONTH);
    }
	/**
	 * 设置有背景标识物的日期
	 * <p/>
	 * Set date which has decor of background
	 *
	 * @param date
	 *            日期列表 List of date
	 */
	public void setDecorBG(List<String> date) {
		setDecor(date, DECOR_CACHE_BG);
	}

	/**
	 * 设置左上角有标识物的日期
	 * <p/>
	 * Set date which has decor on Top left
	 *
	 * @param date
	 *            日期列表 List of date
	 */
	public void setDecorTL(List<String> date) {
		setDecor(date, DECOR_CACHE_TL);
	}

	/**
	 * 设置顶部有标识物的日期
	 * <p/>
	 * Set date which has decor on Top
	 *
	 * @param date
	 *            日期列表 List of date
	 */
	public void setDecorT(List<String> date) {
		setDecor(date, DECOR_CACHE_T);
	}

	/**
	 * 设置右上角有标识物的日期
	 * <p/>
	 * Set date which has decor on Top right
	 *
	 * @param date
	 *            日期列表 List of date
	 */
	public void setDecorTR(List<String> date) {
		setDecor(date, DECOR_CACHE_TR);
	}

	/**
	 * 设置左边有标识物的日期
	 * <p/>
	 * Set date which has decor on left
	 *
	 * @param date
	 *            日期列表 List of date
	 */
	public void setDecorL(List<String> date) {
		setDecor(date, DECOR_CACHE_L);
	}

	/**
	 * 设置右上角有标识物的日期
	 * <p/>
	 * Set date which has decor on right
	 *
	 * @param date
	 *            日期列表 List of date
	 */
	public void setDecorR(List<String> date) {
		setDecor(date, DECOR_CACHE_R);
	}
	private void checkSchedule(DateInfo[][] dataOfMonth,int month){
		List<String> list=ZSSChineseCalendar.SCHEDULE.get((month-1)+"");
		for (int i = 0; i < dataOfMonth.length; i++) {
			for (int j = 0; j < dataOfMonth[0].length; j++) {
				if (isExist(dataOfMonth[i][j].strG, list)) {
					dataOfMonth[i][j].isScheduled=true;
				}else{
					dataOfMonth[i][j].isScheduled=false;
				}
			}
		}
	}
	private boolean isExist(String str,List<String> list){
		try {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).equals(str)) {
					return true;
				}
			}
		}catch (Exception e){

		}
		return false;
	}
	/**
	 * 获取指定年月的日历对象数组
	 *
	 * @param year
	 *            公历年
	 * @param month
	 *            公历月
	 * @return 日历对象数组 该数组长度恒为6x7 如果某个下标对应无数据则填充为null
	 */
	public DateInfo[][] getDateInfo(int year, int month,boolean isWeek) {
		HashMap<Integer, DateInfo[][]> dataOfYear = DATE_CACHE.get(year);
		if (null != dataOfYear && dataOfYear.size() != 0) {
			DateInfo[][] dataOfMonth = dataOfYear.get(month);
			if (dataOfMonth != null) {
				checkSchedule(dataOfMonth,month);
				return dataOfMonth;
			}
			dataOfMonth = buildDateInfo(year, month,isWeek);
			dataOfYear.put(month, dataOfMonth);
			return dataOfMonth;
		}
		if (null == dataOfYear)
			dataOfYear = new HashMap<Integer, DateInfo[][]>();
		DateInfo[][] dataOfMonth = buildDateInfo(year, month,isWeek);
		dataOfYear.put((month), dataOfMonth);
		DATE_CACHE.put(year, dataOfYear);
		return dataOfMonth;
	}

	private void setDecor(List<String> date, HashMap<String, Set<String>> cache) {
		for (String str : date) {
			int index = str.lastIndexOf("-");
			String key = str.substring(0, index).replace("-", ":");
			Set<String> days = cache.get(key);
			if (null == days) {
				days = new HashSet<String>();
			}
			days.add(str.substring(index + 1, str.length()));
			cache.put(key, days);
		}
	}

	/**
	 * 根据年月 获取当月的所有日期
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	private DateInfo[][] buildDateInfo(int year, int month,boolean isWeek) {
		// 该数组长度恒为6x7 如果某个下标对应无数据则填充为null
		DateInfo[][] info = new DateInfo[6][7];

		String[][] strG = c.buildDaysOfMonth(year, month,isWeek);
		String[][] strF = c.buildMonthFestival(year, month);

		Set<String> strHoliday = c.buildMonthHoliday(year, month);
		Set<String> strWeekend = c.buildMonthWeekend(year, month);
		Set<String> strSchedule = c.buildMonthSchedule(year, month);

		Set<String> decorBG = DECOR_CACHE_BG.get(year + ":" + month);
		Set<String> decorTL = DECOR_CACHE_TL.get(year + ":" + month);
		Set<String> decorT = DECOR_CACHE_T.get(year + ":" + month);
		Set<String> decorTR = DECOR_CACHE_TR.get(year + ":" + month);
		Set<String> decorL = DECOR_CACHE_L.get(year + ":" + month);
		Set<String> decorR = DECOR_CACHE_R.get(year + ":" + month);
		for (int i = 0; i < info.length; i++) {
			for (int j = 0; j < info[i].length; j++) {
				DateInfo tmp = new DateInfo();
				tmp.strG = strG[i][j];
				tmp.strF = strF[i][j].replace("F", "");
				if (!TextUtils.isEmpty(tmp.strG) && strHoliday.contains(tmp.strG))
					tmp.isHoliday = true;
				if (!TextUtils.isEmpty(tmp.strG) && strSchedule.contains(tmp.strG))
					tmp.isScheduled = true;
				if (!TextUtils.isEmpty(tmp.strG))
					tmp.isToday = c.isToday(year, month, Integer.valueOf(tmp.strG));
				if (strWeekend.contains(tmp.strG))
					tmp.isWeekend = true;
				if (!TextUtils.isEmpty(tmp.strG))
					tmp.isSolarTerms = ((ZSSChineseCalendar) c).isSolarTerm(year, month, Integer.valueOf(tmp.strG));
				if (!TextUtils.isEmpty(strF[i][j]) && strF[i][j].endsWith("F"))
					tmp.isFestival = true;
				if (!TextUtils.isEmpty(tmp.strG))
					tmp.isDeferred = ((ZSSChineseCalendar) c).isDeferred(year, month, Integer.valueOf(tmp.strG));
				if (null != decorBG && decorBG.contains(tmp.strG))
					tmp.isDecorBG = true;
				if (null != decorTL && decorTL.contains(tmp.strG))
					tmp.isDecorTL = true;
				if (null != decorT && decorT.contains(tmp.strG))
					tmp.isDecorT = true;
				if (null != decorTR && decorTR.contains(tmp.strG))
					tmp.isDecorTR = true;
				if (null != decorL && decorL.contains(tmp.strG))
					tmp.isDecorL = true;
				if (null != decorR && decorR.contains(tmp.strG))
					tmp.isDecorR = true;
				info[i][j] = tmp;
			}
		}
		return info;
	}
}
