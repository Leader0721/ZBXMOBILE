package com.zbxn.widget.calendar.bizs.calendars;

import android.text.TextUtils;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * 月历抽象父类
 * 继承该类可以实现自己的日历对象
 * <p/>
 * Abstract class of Calendar
 *
 * @author ZSS 2015-06-15
 */
public abstract class ZSSCalendar {
    protected final Calendar c = Calendar.getInstance();

    /**
     * 获取某年某月的节日数组
     * <p/>
     * Build the festival date array of given year and month
     *
     * @param year  某年
     * @param month 某月
     * @return 该月节日数组
     */
    public abstract String[][] buildMonthFestival(int year, int month);

    /**
     * 获取某年某月的假期数组
     * <p/>
     * Build the holiday date array of given year and month
     *
     * @param year  某年
     * @param month 某月
     * @return 该月假期数组
     */
    public abstract Set<String> buildMonthHoliday(int year, int month);
    
    /**
     * 获取某年某月的日程数组
     * @param year
     * @param month
     * @return
     */
    public abstract Set<String> buildMonthSchedule(int year, int month);

    /**
     * 判断某年是否为闰年
     *
     * @param year ...
     * @return true表示闰年
     */
    public boolean isLeapYear(int year) {
        return (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0));
    }

    /**
     * 判断给定日期是否为今天
     *
     * @param year  某年
     * @param month 某月
     * @param day   某天
     * @return ...
     */
    public boolean isToday(int year, int month, int day) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.set(year, month - 1, day);
        return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) &&
                (c1.get(Calendar.MONTH) == (c2.get(Calendar.MONTH))) &&
                (c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * 生成某年某月的公历天数数组
     * 数组为6x7的二维数组因为一个月的周数永远不会超过六周
     * 天数填充对应相应的二维数组下标
     * 如果某个数组下标中没有对应天数那么则填充一个空字符串
     *
     * @param year  某年
     * @param month 某月
     * @return 某年某月的公历天数数组
     */
    public String[][] buildDaysOfMonth(int year, int month,boolean isWeek) {
        c.clear();
        String tmp[][] = new String[6][7];
        c.set(year, month - 1, 1);

        int daysInMonth = 0;
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 ||
                month == 12) {
            daysInMonth = 31;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            daysInMonth = 30;
        } else if (month == 2) {
            if (isLeapYear(year)) {
                daysInMonth = 29;
            } else {
                daysInMonth = 28;
            }
        }
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
        //0  星期日 ； 1  星期一  ； 2  星期二
        //如果是0的话  前面应该有6个  当前是星期一的话  前面是0个  星期二的话  前面1   
        int day = 1;
        dayOfWeek=dayOfWeek==0?6:dayOfWeek-1;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
            	if (i==0) {
					if (j<dayOfWeek) {
						 tmp[i][j] = "";
					}else{
						 tmp[i][j] = (day++)+"";
					}
				}else{
					if (day<=daysInMonth) {
						tmp[i][j] = (day++)+"";
					}else{
						tmp[i][j] = "";
					}
				}
            }
        }
        //对week单独的做操作
        //if (isWeek) {
        	//从头开始  第一个不为空的  getHeadNullIndex
        	int i=-1;
        	int j=-1;
        	String headIndex=getHeadNullIndex(tmp);
        	if (!TextUtils.isEmpty(headIndex)&&!"-1".equals(headIndex)) {
				i=Integer.parseInt(headIndex.split("-")[0]);//0
				j=Integer.parseInt(headIndex.split("-")[1]);//1
				Calendar c=Calendar.getInstance();
				c.set(year, month-1, 1);
				for(int k=0;k<j;k++){
					c.add(Calendar.DAY_OF_MONTH, k-j);
					tmp[0][k]=c.get(Calendar.DAY_OF_MONTH)+"";
					c.set(year, month-1, 1);
				}
			}
        	//第一个为空的
        	String footIndex=getFootNullIndex(tmp);
        	if (!TextUtils.isEmpty(footIndex)&&!"-1".equals(footIndex)) {
				i=Integer.parseInt(footIndex.split("-")[0]);//4
				j=Integer.parseInt(footIndex.split("-")[1]);//5
				for(int k=j+1;k<7;k++){
					tmp[i][k]=(k-j)+"";
				}
			}
		//}
        
//        for (int i = 0; i < 6; i++) {
//            for (int j = 0; j < 7; j++) {
//                tmp[i][j] = "";
//                if (i == 0 && j >= dayOfWeek) {
//                    tmp[i][j] = "" + day;
//                    day++;
//                } else if (i > 0 && day <= daysInMonth) {
//                    tmp[i][j] = "" + day;
//                    day++;
//                }
//            }
//        }
        return tmp;
    }
    /**
     * 返回第一个头上不为空的 坐标    i  j
     * @param tmp
     * @return
     */
    private String getHeadNullIndex(String tmp[][]){
    	String result="-1";
    	if (tmp==null) {
    		return result;
		}
    	boolean breakCan=false;
    	for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
            	String str=tmp[i][j];
            	if (!TextUtils.isEmpty(str)) {
            		result=i+"-"+j;
            		breakCan=true;
            		break;
				}
            }
            if (breakCan) {
            	break;
			}
        }
    	return result;	
    }
    /**
     * 返回尾部得  最后一个不为空的坐标  i j
     * @param tmp
     * @return
     */
    private String getFootNullIndex(String tmp[][]){
    	String result="-1";
    	if (tmp==null) {
    		return result;
		}
    	boolean breakCan=false;
    	for (int i = 5; i>1; i--) {
            for (int j = 6; j >=0; j--) {
            	String str=tmp[i][j];
            	if (!TextUtils.isEmpty(str)) {
            		result=i+"-"+j;
            		breakCan=true;
            		break;
				}
            }
            if (breakCan) {
            	break;
			}
        }
    	return result;	
    }
    /**
     * 
     * @param dateInfo
     * @return
     */
    public String[][] completebuildDaysOfMonth(String[][] tmp,int year,int month){
    	if (tmp==null) {
			return null;
		}
    	//从头开始  第一个不为空的  getHeadNullIndex
    	int i=-1;
    	int j=-1;
    	String headIndex=getHeadNullIndex(tmp);
    	if (!TextUtils.isEmpty(headIndex)&&!"-1".equals(headIndex)) {
			i=Integer.parseInt(headIndex.split("-")[0]);
			j=Integer.parseInt(headIndex.split("-")[1]);
			Calendar c=Calendar.getInstance();
			c.set(year, month-1, 1);
			for(int k=0;k<i;k++){
				c.add(Calendar.DAY_OF_MONTH, i-k);
				tmp[0][k]=c.get(Calendar.DAY_OF_MONTH)+"";
				c.set(year, month-1, 1);
			}
		}
    	//第一个为空的
    	String footIndex=getFootNullIndex(tmp);
    	if (!TextUtils.isEmpty(footIndex)&&!"-1".equals(footIndex)) {
			i=Integer.parseInt(footIndex.split("-")[0]);
			j=Integer.parseInt(footIndex.split("-")[1]);
			for(int k=i+1;k<7;k++){
				tmp[k][j]=(k-i)+"";
			}
		}
    	return tmp;
    }
    /**
     * 生成某年某月的周末日期集合
     *
     * @param year  某年
     * @param month 某月
     * @return 某年某月的周末日期集合
     */
    public Set<String> buildMonthWeekend(int year, int month) {
        Set<String> set = new HashSet<String>();
        c.clear();
        c.set(year, month - 1, 1);
        do {
            int day = c.get(Calendar.DAY_OF_WEEK);
            if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) {
                set.add(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
            }
            c.add(Calendar.DAY_OF_YEAR, 1);
        } while (c.get(Calendar.MONTH) == month - 1);
        return set;
    }

    protected long GToNum(int year, int month, int day) {
        month = (month + 9) % 12;
        year = year - month / 10;
        return 365 * year + year / 4 - year / 100 + year / 400 + (month * 306 + 5) / 10 + (day - 1);
    }

    protected int getBitInt(int data, int length, int shift) {
        return (data & (((1 << length) - 1) << shift)) >> shift;
    }
}
