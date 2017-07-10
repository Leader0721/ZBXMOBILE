package com.zbxn.widget.calendar.entities;

/**
 * 日历数据实体
 * 封装日历绘制时需要的数据
 * 
 * Entity of calendar
 *
 * @author ZSS 2016-04-13
 */
public class DateInfo {
    public String strG;//3号
    public String strF;//清明
    public boolean isHoliday;//是否是节假日
    public boolean isChoosed;//是否正在被选中
    public boolean isToday;//是否是今天  
    public boolean isScheduled;//是否有日程
    public boolean isWeekend;
    public boolean isSolarTerms, isFestival, isDeferred;
    public boolean isDecorBG;
    public boolean isDecorTL, isDecorT, isDecorTR, isDecorL, isDecorR;
    
    public void Reset(){
    	strG="";
    	strF="";
    	isHoliday=false;
    	isChoosed=false;
    	isToday=false;
    	isScheduled=false;
    	isWeekend=false;
    	isSolarTerms=false;
    	isFestival=false;
    	isDeferred=false;
    	isDecorBG=false;
    	isDecorTL=false;
    	isDecorT=false;
    	isDecorTR=false;
    	isDecorL=false;
    	isDecorR=false;
    }
}