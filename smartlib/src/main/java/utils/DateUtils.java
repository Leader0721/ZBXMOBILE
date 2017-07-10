package utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	private static final long ONE_MINUTE = 60;
	private static final long ONE_HOUR = 3600;
	private static final long ONE_DAY = 86400;
	private static final long ONE_MONTH = 2592000;
	private static final long ONE_YEAR = 31104000;

	public static Calendar calendar = Calendar.getInstance();

	/**
	 * @return yyyy-mm-dd 2012-12-25
	 */
	public static String getDate() {
		return getYear() + "-" + getMonth() + "-" + getDay();
	}

	/**
	 * @param format
	 * @return yyyy年MM月dd HH:mm MM-dd HH:mm 2012-12-25
	 * 
	 */
	public static String getDate(String format) {
		SimpleDateFormat simple = new SimpleDateFormat(format);
		return simple.format(calendar.getTime());
	}

	/**
	 * @return yyyy-MM-dd 2012-12-25
	 */
	public static String getDate(Date date) {
		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
		return simple.format(date);
	}

	/**
	 * @param date
	 * @param format
	 * @return yyyy年MM月dd HH:mm MM-dd HH:mm 2012-12-25
	 * 
	 */
	public static String getDate(Date date, String format) {
		SimpleDateFormat simple = new SimpleDateFormat(format);
		return simple.format(date);
	}

	/**
	 * 
	 * @return yyyy-MM-dd HH:mm 2012-12-29 23:47
	 */
	public static String getDateAndMinute() {
		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return simple.format(calendar.getTime());
	}

	/**
	 * 
	 * @return yyyy-MM-dd HH:mm:ss 2012-12-29 23:47:36
	 */
	public static String getFullDate() {
		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return simple.format(calendar.getTime());
	}

	/**
	 * 距离今天多久
	 * 
	 * @param date
	 * @return x分钟前、x小时x分钟前、昨天/前天x点x分、x个月x天前x点x分、x年前x月x日
	 */
	public static String fromToday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		long time = date.getTime() / 1000;
		long now = new Date().getTime() / 1000;
		long ago = Math.abs(now - time);
		if (ago <= ONE_HOUR)
			return ago / ONE_MINUTE + "分钟前";
		else if (ago <= ONE_DAY)
			return ago / ONE_HOUR + "小时" + (ago % ONE_HOUR / ONE_MINUTE)
					+ "分钟前";
		else if (ago <= ONE_DAY * 2)
			return "昨天" + calendar.get(Calendar.HOUR_OF_DAY) + "点"
					+ calendar.get(Calendar.MINUTE) + "分";
		else if (ago <= ONE_DAY * 3)
			return "前天" + calendar.get(Calendar.HOUR_OF_DAY) + "点"
					+ calendar.get(Calendar.MINUTE) + "分";
		else if (ago <= ONE_MONTH) {
			long day = ago / ONE_DAY;
			return day + "天前" + calendar.get(Calendar.HOUR_OF_DAY) + "点"
					+ calendar.get(Calendar.MINUTE) + "分";
		} else if (ago <= ONE_YEAR) {
			long month = ago / ONE_MONTH;
			long day = ago % ONE_MONTH / ONE_DAY;
			return month + "个月" + day + "天前"
					+ calendar.get(Calendar.HOUR_OF_DAY) + "点"
					+ calendar.get(Calendar.MINUTE) + "分";
		} else {
			long year = ago / ONE_YEAR;
			int month = calendar.get(Calendar.MONTH) + 1;// JANUARY which is 0
															// so month+1
			return year + "年前" + month + "月" + calendar.get(Calendar.DATE)
					+ "日";
		}

	}

	/**
	 * 距离今天多久
	 * 
	 * @param date
	 * @return 刚刚（小于6分钟）、x分钟前、x小时前、昨天、x月x日、x年x月x日
	 * @author GISirFive
	 */
	public static String fromTodaySimple(Date date) {
		if (date == null)
			return " ";
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		long time = date.getTime() / 1000;
		long now = new Date().getTime() / 1000;
		long ago = Math.abs(now - time);
		if (ago <= (ONE_HOUR * 0.1))
			return "刚刚";
		else if (ago <= ONE_HOUR)
			return ago / ONE_MINUTE + "分钟前";
		else if (ago <= ONE_DAY)
			return ago / ONE_HOUR + "小时前";
		else if (ago <= ONE_DAY * 2) {
			Calendar current = Calendar.getInstance();
			// 今天
			Calendar today = Calendar.getInstance();
			today.set(Calendar.YEAR, current.get(Calendar.YEAR));
			today.set(Calendar.MONTH, current.get(Calendar.MONTH));
			today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
			// Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
			today.set(Calendar.HOUR_OF_DAY, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			// 昨天
			Calendar yesterday = Calendar.getInstance();
			yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
			yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
			yesterday.set(Calendar.DAY_OF_MONTH,
					current.get(Calendar.DAY_OF_MONTH) - 1);
			yesterday.set(Calendar.HOUR_OF_DAY, 0);
			yesterday.set(Calendar.MINUTE, 0);
			yesterday.set(Calendar.SECOND, 0);
			// 现在
			current.setTime(date);
			if (current.before(today) && current.after(yesterday))
				return "昨天";
		}

		if (ago <= ONE_YEAR) {
			SimpleDateFormat simple = new SimpleDateFormat("MM-dd HH:mm");
			return simple.format(date);
		} else {
			SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
			return simple.format(date);
		}
	}

	/**
	 * 距离截止日期还有多长时间
	 * 
	 * @param date
	 * @return 只剩下x分钟、只剩下x天x小时x分钟
	 */
	public static String fromDeadline(Date date) {
		long deadline = date.getTime() / 1000;
		long now = (new Date().getTime()) / 1000;
		long remain = deadline - now;
		if (remain <= ONE_HOUR)
			return "只剩下" + remain / ONE_MINUTE + "分钟";
		else if (remain <= ONE_DAY)
			return "只剩下" + remain / ONE_HOUR + "小时"
					+ (remain % ONE_HOUR / ONE_MINUTE) + "分钟";
		else {
			long day = remain / ONE_DAY;
			long hour = remain % ONE_DAY / ONE_HOUR;
			long minute = remain % ONE_DAY % ONE_HOUR / ONE_MINUTE;
			return "只剩下" + day + "天" + hour + "小时" + minute + "分钟";
		}

	}

	/**
	 * 距离截止日期还有多长时间
	 * 
	 * @param date
	 * @return x分钟、x天x小时x分钟
	 */
	public static String fromDeadlineSmiple(Date date) {
		long deadline = date.getTime() / 1000;
		long now = (new Date().getTime()) / 1000;
		long remain = deadline - now;
		if (remain <= ONE_HOUR)
			return remain / ONE_MINUTE + "分钟";
		else if (remain <= ONE_DAY)
			return remain / ONE_HOUR + "小时" + (remain % ONE_HOUR / ONE_MINUTE)
					+ "分钟";
		else {
			long day = remain / ONE_DAY;
			long hour = remain % ONE_DAY / ONE_HOUR;
			long minute = remain % ONE_DAY % ONE_HOUR / ONE_MINUTE;
			return day + "天" + hour + "小时" + minute + "分钟";
		}

	}

	/**
	 * 距离今天的绝对时间
	 * 
	 * @param date
	 * @return
	 */
	public static String toToday(Date date) {
		long time = date.getTime() / 1000;
		long now = (new Date().getTime()) / 1000;
		long ago = now - time;
		if (ago <= ONE_HOUR)
			return ago / ONE_MINUTE + "分钟";
		else if (ago <= ONE_DAY)
			return ago / ONE_HOUR + "小时" + (ago % ONE_HOUR / ONE_MINUTE) + "分钟";
		else if (ago <= ONE_DAY * 2)
			return "昨天" + (ago - ONE_DAY) / ONE_HOUR + "点" + (ago - ONE_DAY)
					% ONE_HOUR / ONE_MINUTE + "分";
		else if (ago <= ONE_DAY * 3) {
			long hour = ago - ONE_DAY * 2;
			return "前天" + hour / ONE_HOUR + "点" + hour % ONE_HOUR / ONE_MINUTE
					+ "分";
		} else if (ago <= ONE_MONTH) {
			long day = ago / ONE_DAY;
			long hour = ago % ONE_DAY / ONE_HOUR;
			long minute = ago % ONE_DAY % ONE_HOUR / ONE_MINUTE;
			return day + "天前" + hour + "点" + minute + "分";
		} else if (ago <= ONE_YEAR) {
			long month = ago / ONE_MONTH;
			long day = ago % ONE_MONTH / ONE_DAY;
			long hour = ago % ONE_MONTH % ONE_DAY / ONE_HOUR;
			long minute = ago % ONE_MONTH % ONE_DAY % ONE_HOUR / ONE_MINUTE;
			return month + "个月" + day + "天" + hour + "点" + minute + "分前";
		} else {
			long year = ago / ONE_YEAR;
			long month = ago % ONE_YEAR / ONE_MONTH;
			long day = ago % ONE_YEAR % ONE_MONTH / ONE_DAY;
			return year + "年前" + month + "月" + day + "天";
		}

	}

	public static String getYear() {
		return calendar.get(Calendar.YEAR) + "";
	}

	public static String getMonth() {
		int month = calendar.get(Calendar.MONTH) + 1;
		return month + "";
	}

	public static String getDay() {
		return calendar.get(Calendar.DATE) + "";
	}

	public static String get24Hour() {
		return calendar.get(Calendar.HOUR_OF_DAY) + "";
	}

	public static String getMinute() {
		return calendar.get(Calendar.MINUTE) + "";
	}

	public static String getSecond() {
		return calendar.get(Calendar.SECOND) + "";
	}

	public static String longToTime(long time, int level) {
		String format = "yyyy-MM-dd kk:mm:ss";
		switch (level) {
		case 12:
			format = "yyyy-MM-dd kk:mm";
			break;
		case 10:
			format = "yyyy-MM-dd kk";
			break;
		case 5:
			format = "yyyy-MM-dd";
			break;
		case 2:
			format = "yyyy-MM";
			break;
		case 1:
			format = "yyyy";
		case 3:
		case 4:
		case 6:
		case 7:
		case 8:
		case 9:
		case 11:
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(Long.valueOf(time));
	}

	public static int[] covertTimeInYears(long time) {
		long delta = System.currentTimeMillis() - time;
		if (delta <= 0L)
			return new int[] { 0, 0 };
		delta /= 1000L;
		if (delta < 60L)
			return new int[] { (int) delta, 0 };
		delta /= 60L;
		if (delta < 60L)
			return new int[] { (int) delta, 1 };
		delta /= 60L;
		if (delta < 24L)
			return new int[] { (int) delta, 2 };
		delta /= 24L;
		if (delta < 30L)
			return new int[] { (int) delta, 3 };
		delta /= 30L;
		if (delta < 12L)
			return new int[] { (int) delta, 4 };
		delta /= 12L;
		return new int[] { (int) delta, 5 };
	}
}