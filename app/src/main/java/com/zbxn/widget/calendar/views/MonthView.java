package com.zbxn.widget.calendar.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import com.zbxn.widget.calendar.bizs.calendars.ZSSCalendarManager;
import com.zbxn.widget.calendar.bizs.decors.DPDecor;
import com.zbxn.widget.calendar.bizs.themes.DPTManager;
import com.zbxn.widget.calendar.entities.DateInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MonthView
 * 
 * @author ZSS 2016-04-13
 */
public class MonthView extends View {
	protected static final Region[][] MONTH_REGIONS_4 = new Region[4][7];
	protected static final Region[][] MONTH_REGIONS_5 = new Region[5][7];
	protected static final Region[][] MONTH_REGIONS_6 = new Region[6][7];

	private final DateInfo[][] INFO_4 = new DateInfo[4][7];
	private final DateInfo[][] INFO_5 = new DateInfo[5][7];
	private final DateInfo[][] INFO_6 = new DateInfo[6][7];

	private final Map<String, List<Region>> REGION_SELECTED = new HashMap<String, List<Region>>();

	private ZSSCalendarManager mCManager = ZSSCalendarManager.getInstance();// 日期管理器
	private DPTManager mTManager = DPTManager.getInstance();

	protected Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
	protected Paint todayPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
	private Scroller mScroller;
	private DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
	private AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();
	private OnLineCountChangeListener onLineCountChangeListener;
	private OnMonthChangeEventListener onDateChangeListener;
	private OnLineChooseListener onLineChooseListener;
	private OnMonthViewChangeListener onMonthViewChangeListener;
	private OnMonthDateClick onMonthClick;
	private OnSelectDayEventListener onSelectDayEventListener;
	private ScaleAnimationListener scaleAnimationListener;

	private SlideMode mSlideMode;
	private DPDecor mDPDecor;

	private int circleRadius;
	private int indexYear, indexMonth;
	private int centerYear, centerMonth;
	private int leftYear, leftMonth;
	private int rightYear, rightMonth;
	// private int topYear, topMonth;
	// private int bottomYear, bottomMonth;
	private int width, height;
	private int sizeDecor, sizeDecor2x, sizeDecor3x;
	private int lastPointX, lastPointY;
	private int lastMoveX, lastMoveY;
	private int criticalWidth, criticalHeight;
	private int animZoomOut1, animZoomIn1, animZoomOut2;

	private float sizeTextGregorian, sizeTextFestival;
	private float offsetYFestival1, offsetYFestival2;
	private int num = -1;
	// 记录日历的总行数： 5行，6行
	private int lineCount;
	// 点击选中的day
	private String chooseDay;
	// 为了实现点击改变文本颜色
	private int currentDrawMonth;
	// 自定义一个文本size
	private int textSize;
	private int recordLine;

	private boolean isNewEvent, isFestivalDisplay = true, isHolidayDisplay = true, isScheduleDisplay = true,
			isTodayDisplay = true, isDeferredDisplay = true;

	private Map<String, BGCircle> cirApr = new HashMap<String, BGCircle>();
	private Map<String, BGCircle> cirDpr = new HashMap<String, BGCircle>();

	private List<String> dateSelected = new ArrayList<String>();

	public MonthView(Context context) {
		this(context, null);
	}

	public MonthView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MonthView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			scaleAnimationListener = new ScaleAnimationListener();
		}
		mScroller = new Scroller(context);
		mPaint.setTextAlign(Paint.Align.CENTER);
		textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, getResources().getDisplayMetrics());
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		return super.onSaveInstanceState();
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		super.onRestoreInstanceState(state);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			invalidate();
		}
	}

	private int last_x = 0;
	private int last_y = 0;

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			getParent().requestDisallowInterceptTouchEvent(true);
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaX = x - last_x;
			int deltaY = y - last_y;
			if (Math.abs(deltaX) < Math.abs(deltaY)) {
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			break;
		default:
			break;
		}
		last_x = x;
		last_y = y;
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mScroller.forceFinished(true);
			mSlideMode = null;
			isNewEvent = true;
			lastPointX = (int) event.getX();
			lastPointY = (int) event.getY();
			// break;
			return true;
		case MotionEvent.ACTION_MOVE:
			if (isNewEvent) {
				if (Math.abs(lastPointX - event.getX()) > 100) {
					mSlideMode = SlideMode.HOR;
					isNewEvent = false;
				} else if (Math.abs(lastPointY - event.getY()) > 50) {
					// mSlideMode = SlideMode.VER;
					// isNewEvent = false;
				}
			}
			if (mSlideMode == SlideMode.HOR) {
				int totalMoveX = (int) (lastPointX - event.getX()) + lastMoveX;
				smoothScrollTo(totalMoveX, indexYear * height);
				// return true;
			} else if (mSlideMode == SlideMode.VER) {
				// int totalMoveY = (int) (lastPointY - event.getY()) +
				// lastMoveY;
				// smoothScrollTo(width * indexMonth, totalMoveY);
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mSlideMode == SlideMode.VER) {
				// if (Math.abs(lastPointY - event.getY()) > 25) {
				// if (lastPointY < event.getY()) {
				// if (Math.abs(lastPointY - event.getY()) >= criticalHeight) {
				// indexYear--;
				// centerYear = centerYear - 1;
				// }
				// } else if (lastPointY > event.getY()) {
				// if (Math.abs(lastPointY - event.getY()) >= criticalHeight) {
				// indexYear++;
				// centerYear = centerYear + 1;
				// }
				// }
				// buildRegion();
				// computeDate();
				// smoothScrollTo(width * indexMonth, height * indexYear);
				// lastMoveY = height * indexYear;
				// } else {
				// defineRegion((int) event.getX(), (int) event.getY());
				// }
			} else if (mSlideMode == SlideMode.HOR) {
				if (Math.abs(lastPointX - event.getX()) > 25) {
					if (lastPointX > event.getX() && Math.abs(lastPointX - event.getX()) >= criticalWidth) {
						indexMonth++;
						centerMonth = (centerMonth + 1) % 13;
						if (centerMonth == 0) {
							centerMonth = 1;
							centerYear++;
						}

						if (null != onMonthViewChangeListener) {
							onMonthViewChangeListener.onMonthViewChange(true,centerYear,centerMonth);
						}

					} else if (lastPointX < event.getX() && Math.abs(lastPointX - event.getX()) >= criticalWidth) {
						indexMonth--;
						centerMonth = (centerMonth - 1) % 12;
						if (centerMonth == 0) {
							centerMonth = 12;
							centerYear--;
						}
						if (null != onMonthViewChangeListener) {
							onMonthViewChangeListener.onMonthViewChange(false,centerYear,centerMonth);
						}
					}
					buildRegion();
					computeDate();
					smoothScrollTo(width * indexMonth, indexYear * height);
					lastMoveX = width * indexMonth;
				} else {
					defineRegion((int) event.getX(), (int) event.getY());
				}
			} else {
				defineRegion((int) event.getX(), (int) event.getY());
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
		setMeasuredDimension(measureWidth, (int) (measureWidth * 6F / 7F));
	}
	public void setLine(int line){
		this.recordLine=line;
	}
	public void gotoToday() {
		if (indexMonth == 0 && indexYear == 0) {
			return;
		}
		indexMonth = 0;
		indexYear = 0;
		centerMonth = mCManager.getCurrentMonth();
		centerYear = mCManager.getCurrentyYear();
		buildRegion();
		computeDate();
		smoothScrollTo(width * indexMonth, indexYear * height);
		lastMoveX = width * indexMonth;
	}

	public void comeToSpecifyDate(int year, int month, int day) {
		indexYear = year - mCManager.getCurrentyYear();
		if (indexYear >= 0) {
			// 说明日期在后面的
			indexMonth = 12 - mCManager.getCurrentMonth() + month + (indexYear - 1) * 12;
		} else {
			indexMonth = -(mCManager.getCurrentMonth() + (indexYear - 1) * 12 + 12 - month);
		}
		centerMonth = (month) % 13;
		if (centerMonth == 0) {
			centerMonth = 1;
		}
		centerYear = mCManager.getCurrentyYear() + indexYear;
		indexYear = 0;
		buildRegion();
		computeDate();
		smoothScrollTo(width * indexMonth, indexYear * height);
		lastMoveX = width * indexMonth;
	}

	public void moveForwad() {
		indexMonth++;
		centerMonth = (centerMonth + 1) % 13;
		if (centerMonth == 0) {
			centerMonth = 1;
			centerYear++;
		}
		buildRegion();
		computeDate();
		smoothScrollTo(width * indexMonth, indexYear * height);
		lastMoveX = width * indexMonth;
	}

	// 滑动back
	public void moveBack() {
		indexMonth--;
		centerMonth = (centerMonth - 1) % 12;
		if (centerMonth == 0) {
			centerMonth = 12;
			centerYear--;
		}
		buildRegion();
		computeDate();
		smoothScrollTo(width * indexMonth, indexYear * height);
		lastMoveX = width * indexMonth;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldW, int oldH) {
		width = w;
		height = h;

		criticalWidth = (int) (1F / 5F * width);
		criticalHeight = (int) (1F / 5F * height);

		int cellW = (int) (w / 7F);
		int cellH4 = (int) (h / 4F);
		int cellH5 = (int) (h / 5F);
		int cellH6 = (int) (h / 6F);

		circleRadius = cellW-20;

		animZoomOut1 = (int) (cellW * 1.2F);
		animZoomIn1 = (int) (cellW * 0.8F);
		animZoomOut2 = (int) (cellW * 1.1F);

		sizeDecor = (int) (cellW / 3F);
		sizeDecor2x = sizeDecor * 2;
		sizeDecor3x = sizeDecor * 3;

		sizeTextGregorian = width / 23F;
		mPaint.setTextSize(sizeTextGregorian);

		float heightGregorian = mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top;
		sizeTextFestival = width / 40F;
		mPaint.setTextSize(sizeTextFestival);

		float heightFestival = mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top;
		offsetYFestival1 = (((Math.abs(mPaint.ascent() + mPaint.descent())) / 2F) + heightFestival / 2F
				+ heightGregorian / 2F) / 2F;
		offsetYFestival2 = offsetYFestival1 * 2F;

		for (int i = 0; i < MONTH_REGIONS_4.length; i++) {
			for (int j = 0; j < MONTH_REGIONS_4[i].length; j++) {
				Region region = new Region();
				region.set((j * cellW), (i * cellH4), cellW + (j * cellW), cellW + (i * cellH4));
				MONTH_REGIONS_4[i][j] = region;
			}
		}
		for (int i = 0; i < MONTH_REGIONS_5.length; i++) {
			for (int j = 0; j < MONTH_REGIONS_5[i].length; j++) {
				Region region = new Region();
				region.set((j * cellW), (i * cellH5), cellW + (j * cellW), cellW + (i * cellH5));
				MONTH_REGIONS_5[i][j] = region;
			}
		}
		for (int i = 0; i < MONTH_REGIONS_6.length; i++) {
			for (int j = 0; j < MONTH_REGIONS_6[i].length; j++) {
				Region region = new Region();
				region.set((j * cellW), (i * cellH6), cellW + (j * cellW), cellW + (i * cellH6));
				MONTH_REGIONS_6[i][j] = region;
			}
		}
	}

	@SuppressLint("NewApi")
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(mTManager.colorBG());
		// draw(canvas, width * indexMonth, (indexYear - 1) * height, topYear,
		// topMonth);
		
		draw(canvas, width * (indexMonth - 1), height * indexYear, leftYear, leftMonth);
		draw(canvas, width * indexMonth, indexYear * height, centerYear, centerMonth);
		draw(canvas, width * (indexMonth + 1), height * indexYear, rightYear, rightMonth);
		// draw(canvas, width * indexMonth, (indexYear + 1) * height,
		// bottomYear, bottomMonth);

		drawBGCircle(canvas);
	}

	private void drawBGCircle(Canvas canvas) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			for (String s : cirDpr.keySet()) {
				BGCircle circle = cirDpr.get(s);
				drawBGCircle(canvas, circle);
			}
		}
		for (String s : cirApr.keySet()) {
			BGCircle circle = cirApr.get(s);
			drawBGCircle(canvas, circle);
		}
	}

	private void drawBGCircle(Canvas canvas, BGCircle circle) {
		canvas.save();
		canvas.translate(circle.getX() - circle.getRadius() / 2, circle.getY() - circle.getRadius() / 2);
		circle.getShape().getShape().resize(circle.getRadius(), circle.getRadius());
		circle.getShape().draw(canvas);
		canvas.restore();
	}

	@SuppressLint("NewApi")
	private void draw(Canvas canvas, int x, int y, int year, int month) {
		canvas.save();
		canvas.translate(x, 0);
		currentDrawMonth = month;
		DateInfo[][] info = mCManager.getDateInfo(year, month, false);
		DateInfo[][] result;
		Region[][] tmp;
		if (TextUtils.isEmpty(info[4][0].strG)) {
			tmp = MONTH_REGIONS_4;
			arrayClear(INFO_4);
			result = arrayCopy(info, INFO_4);
		} else if (TextUtils.isEmpty(info[5][0].strG)) {
			tmp = MONTH_REGIONS_5;
			arrayClear(INFO_5);
			result = arrayCopy(info, INFO_5);
		} else {
			tmp = MONTH_REGIONS_6;
			arrayClear(INFO_6);
			result = arrayCopy(info, INFO_6);
		}
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[i].length; j++) {
				recordLine = i;
				if (i == 0 && j < 7 && Integer.parseInt(info[i][j].strG) > 10) {
					
				}else if (i >2&& Integer.parseInt(info[i][j].strG) < 10) {
					
				}else{
					draw(canvas, tmp[i][j].getBounds(), info[i][j]);
				}
			}
		}
		if (month == centerMonth && year == centerYear) {
			lineCount = result.length;
			changDateListener();
		}
		canvas.restore();
	}

	private void draw(Canvas canvas, Rect rect, DateInfo info) {
		drawBG(canvas, rect, info);
		drawGregorian(canvas, rect, info.strG, info.isWeekend, info.isToday);
		if (isFestivalDisplay)
			drawFestival(canvas, rect, info.strF, info.isFestival);
		// drawDecor(canvas, rect, info);
	}

	private void drawBG(Canvas canvas, Rect rect, DateInfo info) {
		if (null != mDPDecor && info.isDecorBG) {
			mDPDecor.drawDecorBG(canvas, rect, mPaint, centerYear + "-" + centerMonth + "-" + info.strG);
		}
		if (info.isToday && isTodayDisplay) {
			drawBGToday(canvas, rect);
			// 只在 第一次初始化时 更新week的line 来悬挂当前日期
			if (null != onLineChooseListener && num == -1) {
				onLineChooseListener.onLineChange(recordLine);
			}
		} else {
			if (isHolidayDisplay)
				drawBGHoliday(canvas, rect, info.isHoliday);
			
			if (isDeferredDisplay)
				drawBGDeferred(canvas, rect, info.isDeferred);
		}
		if (isScheduleDisplay)
			drawBGSchedule(canvas, rect, info.isScheduled,info.isChoosed);
	}

	// 因为今天的样式需要自定义所以 重新换了Paint
	private void drawBGToday(Canvas canvas, Rect rect) {
		// mPaint.setColor(mTManager.colorToday());
		todayPaint.setColor(mTManager.colorToday());
		todayPaint.setStyle(Paint.Style.STROKE);
		canvas.drawCircle(rect.centerX(), rect.centerY()+10, circleRadius / 2F, todayPaint);
	}

	private void drawBGHoliday(Canvas canvas, Rect rect, boolean isHoliday) {
		/*mPaint.setColor(mTManager.colorHoliday());
		if (isHoliday) {
			mPaint.setColor(Color.YELLOW);
			float dx = (float) (Math.sqrt(2.0) / 4.0 * circleRadius);
			canvas.drawText("假", rect.centerX() + dx, rect.centerY() - dx, mPaint);
		}*/
		// canvas.drawCircle(rect.centerX(), rect.centerY(), circleRadius / 2F,
		// mPaint);
	}

	private void drawBGSchedule(Canvas canvas, Rect rect, boolean isSchedule,boolean isChoosed) {
		mPaint.setColor(mTManager.colorHoliday());
		if (isSchedule) {
			if (isChoosed) {
//				mPaint.setColor(Color.GREEN);
				mPaint.setColor(0xff1EB391);
			}else{
//				mPaint.setColor(Color.GREEN);
				mPaint.setColor(0xff1EB391);
			}
			
			canvas.drawCircle(rect.centerX(), rect.centerY() + circleRadius / 3, 8, mPaint);
		}
	}

	private void drawBGDeferred(Canvas canvas, Rect rect, boolean isDeferred) {
		mPaint.setColor(mTManager.colorDeferred());
		if (isDeferred)
			canvas.drawCircle(rect.centerX(), rect.centerY(), circleRadius / 2F, mPaint);
	}

	private void drawGregorian(Canvas canvas, Rect rect, String str, boolean isWeekend, boolean isToday) {
		mPaint.setTextSize(sizeTextGregorian);
		// 自定义一个文本大小
		// mPaint.setTextSize(textSize);

		if (isWeekend) {
			mPaint.setColor(mTManager.colorWeekend());
		} else if (isToday && isTodayDisplay) {
			mPaint.setColor(mTManager.colorTodayText());
		} else {
			mPaint.setColor(mTManager.colorG());
		}
		float y = rect.centerY();
		if (!isFestivalDisplay)
			y = rect.centerY() + Math.abs(mPaint.ascent()) - (mPaint.descent() - mPaint.ascent()) / 2F;
		canvas.drawText(str, rect.centerX(), y, mPaint);
	}

	private void drawFestival(Canvas canvas, Rect rect, String str, boolean isFestival) {
		mPaint.setTextSize(sizeTextFestival);
		if (isFestival) {
			mPaint.setColor(mTManager.colorF());
		} else {
			mPaint.setColor(mTManager.colorL());
		}
		if (str.contains("&")) {
			String[] s = str.split("&");
			String str1 = s[0];
			if (mPaint.measureText(str1) > rect.width()) {
				float ch = mPaint.measureText(str1, 0, 1);
				int length = (int) (rect.width() / ch);
				canvas.drawText(str1.substring(0, length), rect.centerX(), rect.centerY() + offsetYFestival1, mPaint);
				canvas.drawText(str1.substring(length), rect.centerX(), rect.centerY() + offsetYFestival2, mPaint);
			} else {
				canvas.drawText(str1, rect.centerX(), rect.centerY() + offsetYFestival1, mPaint);
				String str2 = s[1];
				if (mPaint.measureText(str2) < rect.width()) {
					canvas.drawText(str2, rect.centerX(), rect.centerY() + offsetYFestival2, mPaint);
				}
			}
		} else {
			if (mPaint.measureText(str) > rect.width()) {
				float ch = 0.0F;
				for (char c : str.toCharArray()) {
					float tmp = mPaint.measureText(String.valueOf(c));
					if (tmp > ch) {
						ch = tmp;
					}
				}
				int length = (int) (rect.width() / ch);
				canvas.drawText(str.substring(0, length), rect.centerX(), rect.centerY() + offsetYFestival1, mPaint);
				canvas.drawText(str.substring(length), rect.centerX(), rect.centerY() + offsetYFestival2, mPaint);
			} else {
				canvas.drawText(str, rect.centerX(), rect.centerY() + offsetYFestival1, mPaint);
			}
		}
	}

	private void drawDecor(Canvas canvas, Rect rect, DateInfo info) {
		if (!TextUtils.isEmpty(info.strG)) {
			String data = centerYear + "-" + centerMonth + "-" + info.strG;
			if (null != mDPDecor && info.isDecorTL) {
				canvas.save();
				canvas.clipRect(rect.left, rect.top, rect.left + sizeDecor, rect.top + sizeDecor);
				mDPDecor.drawDecorTL(canvas, canvas.getClipBounds(), mPaint, data);
				canvas.restore();
			}
			if (null != mDPDecor && info.isDecorT) {
				canvas.save();
				canvas.clipRect(rect.left + sizeDecor, rect.top, rect.left + sizeDecor2x, rect.top + sizeDecor);
				mDPDecor.drawDecorT(canvas, canvas.getClipBounds(), mPaint, data);
				canvas.restore();
			}
			if (null != mDPDecor && info.isDecorTR) {
				canvas.save();
				canvas.clipRect(rect.left + sizeDecor2x, rect.top, rect.left + sizeDecor3x, rect.top + sizeDecor);
				mDPDecor.drawDecorTR(canvas, canvas.getClipBounds(), mPaint, data);
				canvas.restore();
			}
			if (null != mDPDecor && info.isDecorL) {
				canvas.save();
				canvas.clipRect(rect.left, rect.top + sizeDecor, rect.left + sizeDecor, rect.top + sizeDecor2x);
				mDPDecor.drawDecorL(canvas, canvas.getClipBounds(), mPaint, data);
				canvas.restore();
			}
			if (null != mDPDecor && info.isDecorR) {
				canvas.save();
				canvas.clipRect(rect.left + sizeDecor2x, rect.top + sizeDecor, rect.left + sizeDecor3x,
						rect.top + sizeDecor2x);
				mDPDecor.drawDecorR(canvas, canvas.getClipBounds(), mPaint, data);
				canvas.restore();
			}
		}
	}

	List<String> getDateSelected() {
		return dateSelected;
	}

	// 月份左右滑动切换
	public void setOnLineCountChangeListener(OnLineCountChangeListener onLineCountChangeListener) {
		this.onLineCountChangeListener = onLineCountChangeListener;
	}

	// 月份点击
	public void setOnMonthDateClickListener(OnMonthDateClick onMonthClick) {
		this.onMonthClick = onMonthClick;
	}

	// 通过MonthView的变化来判断如何滑动weekview
	public void setOnMonthViewChangeListener(OnMonthViewChangeListener onWeekViewChangeListener) {
		this.onMonthViewChangeListener = onWeekViewChangeListener;
	}

	// 日期选择监听
	public void setOnMonthChangeEventListener(OnMonthChangeEventListener onDateChangeListener) {
		this.onDateChangeListener = onDateChangeListener;
	}

	public void setOnLineChooseListener(OnLineChooseListener onLineChooseListener) {
		this.onLineChooseListener = onLineChooseListener;
	}

	public void setOnDatePickedListener(OnSelectDayEventListener onDatePickedListener) {
		this.onSelectDayEventListener = onDatePickedListener;
	}

	public void setDPDecor(DPDecor decor) {
		this.mDPDecor = decor;
	}
	
	public void setDate(int year, int month) {
		centerYear = year;
		centerMonth = month;
		indexYear = 0;
		indexMonth = 0;
		buildRegion();
		computeDate();
		requestLayout();
		invalidate();
	}

	public void setInvalidate() {
		invalidate();
	}

	public void setFestivalDisplay(boolean isFestivalDisplay) {
		this.isFestivalDisplay = isFestivalDisplay;
	}

	public void setTodayDisplay(boolean isTodayDisplay) {
		this.isTodayDisplay = isTodayDisplay;
	}

	public void setHolidayDisplay(boolean isHolidayDisplay) {
		this.isHolidayDisplay = isHolidayDisplay;
	}

	public void setDeferredDisplay(boolean isDeferredDisplay) {
		this.isDeferredDisplay = isDeferredDisplay;
	}

	private void smoothScrollTo(int fx, int fy) {
		int dx = fx - mScroller.getFinalX();
		int dy = fy - mScroller.getFinalY();
		smoothScrollBy(dx, dy);
	}

	private void smoothScrollBy(int dx, int dy) {
		mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy, 500);
		invalidate();
	}

	private BGCircle createCircle(float x, float y) {
		OvalShape circle = new OvalShape();
		circle.resize(0, 0);
		ShapeDrawable drawable = new ShapeDrawable(circle);
		BGCircle circle1 = new BGCircle(drawable);
		circle1.setX(x);
		circle1.setY(y+10);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			circle1.setRadius(circleRadius);
		}
		drawable.getPaint().setColor(mTManager.colorBGCircle());
		return circle1;
	}

	private void buildRegion() {
		String key = indexYear + ":" + indexMonth;
		if (!REGION_SELECTED.containsKey(key)) {
			REGION_SELECTED.put(key, new ArrayList<Region>());
		}
	}

	private void arrayClear(DateInfo[][] info) {
		for (DateInfo[] anInfo : info) {
			Arrays.fill(anInfo, null);
		}
	}

	private DateInfo[][] arrayCopy(DateInfo[][] src, DateInfo[][] dst) {
		for (int i = 0; i < dst.length; i++) {
			System.arraycopy(src[i], 0, dst[i], 0, dst[i].length);
		}
//		// 去掉前面的部分
//		boolean canBreak = false;
//		for (int i = 0; i < dst.length; i++) {
//			for (int j = 0; j < dst[0].length; j++) {
//				if (dst[i][j].strG.equals("1")) {
//					canBreak = true;
//					break;
//				} else {
//					dst[i][j].Reset();
//				}
//			}
//			if (canBreak) {
//				break;
//			}
//		}
//		// 去掉后面的部分
//		for (int i = 2; i < dst.length; i++) {
//			for (int j = 0; j <dst[0].length ; j++) {
//				if (dst[i][j].strG.equals("1") || dst[i][j].strG.equals("2") || dst[i][j].strG.equals("3")
//						|| dst[i][j].strG.equals("4") || dst[i][j].strG.equals("5") || dst[i][j].strG.equals("6")|| dst[i][j].strG.equals("7")) {
//					dst[i][j].Reset();
//				}
//			}
//		}

		return dst;
	}

	@SuppressLint("NewApi")
	public void defineRegion(final int x, final int y) {
		DateInfo[][] info = mCManager.getDateInfo(centerYear, centerMonth, false);
		Region[][] tmp;
		if (TextUtils.isEmpty(info[4][0].strG)) {
			tmp = MONTH_REGIONS_4;
		} else if (TextUtils.isEmpty(info[5][0].strG)) {
			tmp = MONTH_REGIONS_5;
		} else {
			tmp = MONTH_REGIONS_6;
		}
		for (int i = 0; i < tmp.length; i++) {
			for (int j = 0; j < tmp[i].length; j++) {
				Region region = tmp[i][j];
				//现在这个条件失效了
				String strG=mCManager.getDateInfo(centerYear, centerMonth, false)[i][j].strG;
				if (TextUtils.isEmpty(strG))
				{
					continue;
				}
				if (i == 0 && j < 7 && Integer.parseInt(strG) > 10) {
					continue;
				}
				if (i >2&& Integer.parseInt(strG) < 10) {
					continue;
				}
				if (region.contains(x, y)) {
					List<Region> regions = REGION_SELECTED.get(indexYear + ":" + indexMonth);
					cirApr.clear();
					regions.add(region);
					num = i;
					final String date = centerYear + "." + centerMonth + "."
							+ mCManager.getDateInfo(centerYear, centerMonth, false)[i][j].strG;
					chooseDay = mCManager.getDateInfo(centerYear, centerMonth, false)[i][j].strG;
					BGCircle circle = createCircle(region.getBounds().centerX() + indexMonth * width,
							region.getBounds().centerY() + indexYear * height);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
						ValueAnimator animScale1 = ObjectAnimator.ofInt(circle, "radius", 0, circleRadius);
						animScale1.setDuration(10);
						animScale1.setInterpolator(decelerateInterpolator);
						animScale1.addUpdateListener(scaleAnimationListener);

						AnimatorSet animSet = new AnimatorSet();
						animSet.playSequentially(animScale1);
						animSet.addListener(new AnimatorListenerAdapter() {
							@Override
							public void onAnimationEnd(Animator animation) {
								if (null != onSelectDayEventListener) {
									//new Date(milliseconds)
									onSelectDayEventListener.onSelectDayEvent(date);
								}
								if (null != onLineChooseListener) {
									onLineChooseListener.onLineChange(num);
								}
								if (null != onMonthClick) {
									onMonthClick.onMonthDateClick(x, y);
								}
							}
						});
						animSet.start();
					}
					cirApr.put(date, circle);
					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
						invalidate();
						if (null != onSelectDayEventListener) {
							onSelectDayEventListener.onSelectDayEvent(date);
						}
						if (null != onLineChooseListener) {
							onLineChooseListener.onLineChange(num);
						}
						if (null != onMonthClick) {
							onMonthClick.onMonthDateClick(x, y);
						}
					}
				}
			}
		}
	}

	@SuppressLint("NewApi")
	public void changeChooseDate(int x, int y) {
		DateInfo[][] info = mCManager.getDateInfo(centerYear, centerMonth, false);
		Region[][] tmp;
		if (TextUtils.isEmpty(info[4][0].strG)) {
			tmp = MONTH_REGIONS_4;
		} else if (TextUtils.isEmpty(info[5][0].strG)) {
			tmp = MONTH_REGIONS_5;
		} else {
			tmp = MONTH_REGIONS_6;
		}
		for (int i = 0; i < tmp.length; i++) {
			for (int j = 0; j < tmp[i].length; j++) {
				Region region = tmp[i][j];
				if (TextUtils.isEmpty(mCManager.getDateInfo(centerYear, centerMonth, false)[i][j].strG)) {
					continue;
				}
				if (region.contains(x, y)) {
					List<Region> regions = REGION_SELECTED.get(indexYear + ":" + indexMonth);
					cirApr.clear();
					regions.add(region);
					num = i;
					final String date = centerYear + "." + centerMonth + "."
							+ mCManager.getDateInfo(centerYear, centerMonth, false)[i][j].strG;
					BGCircle circle = createCircle(region.getBounds().centerX() + indexMonth * width,
							region.getBounds().centerY() + indexYear * height);
					MonthView.this.invalidate();
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
						ValueAnimator animScale1 = ObjectAnimator.ofInt(circle, "radius", 0, circleRadius);
						animScale1.setDuration(10);
						animScale1.setInterpolator(decelerateInterpolator);
						animScale1.addUpdateListener(scaleAnimationListener);
						animScale1.start();
					}
					cirApr.put(date, circle);
					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
						invalidate();
					}

				}
			}
		}
	}

	private void computeDate() {
		rightYear = leftYear = centerYear;
		rightMonth = centerMonth + 1;
		leftMonth = centerMonth - 1;

		if (centerMonth == 12) {
			rightYear++;
			rightMonth = 1;
		}
		if (centerMonth == 1) {
			leftYear--;
			leftMonth = 12;
		}

		if (null != onDateChangeListener) {
			onDateChangeListener.onMonthChangeEvent(centerYear, centerMonth);
		}

	}

	public void changDateListener() {
		if (null != onLineCountChangeListener) {
			onLineCountChangeListener.onLineCountChange(lineCount);
		}
	}

	public interface OnLineCountChangeListener {
		void onLineCountChange(int lineCount);
	}

	public interface OnMonthDateClick {
		void onMonthDateClick(int x, int y);
	}

	public interface OnMonthViewChangeListener {
		void onMonthViewChange(boolean isforward, int newYear, int newMonth);
	}

	public interface OnMonthChangeEventListener {
		void onMonthChangeEvent(int year, int month);
	}

	public interface OnLineChooseListener {
		void onLineChange(int line);
	}

	public interface OnSelectDayEventListener {
		void onSelectDayEvent(String date);
	}

	private enum SlideMode {
		VER, HOR
	}

	private class BGCircle {
		private float x, y;
		private int radius;

		private ShapeDrawable shape;

		public BGCircle(ShapeDrawable shape) {
			this.shape = shape;
		}

		public float getX() {
			return x;
		}

		public void setX(float x) {
			this.x = x;
		}

		public float getY() {
			return y;
		}

		public void setY(float y) {
			this.y = y;
		}

		public int getRadius() {
			return radius;
		}

		public void setRadius(int radius) {
			this.radius = radius;
		}

		public ShapeDrawable getShape() {
			return shape;
		}

		// public void setShape(ShapeDrawable shape) {
		// this.shape = shape;
		// }
	}

	// 获取日历总行数
	public int getLineCount() {
		return lineCount;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private class ScaleAnimationListener implements ValueAnimator.AnimatorUpdateListener {
		@Override
		public void onAnimationUpdate(ValueAnimator animation) {
			MonthView.this.invalidate();
		}
	}
}
