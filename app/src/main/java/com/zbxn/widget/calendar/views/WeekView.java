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
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import com.zbxn.widget.calendar.bizs.decors.DPDecor;
import com.zbxn.widget.calendar.entities.DateInfo;
import com.zbxn.widget.calendar.bizs.calendars.ZSSCalendarManager;
import com.zbxn.widget.calendar.bizs.themes.DPTManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZSS 2016-04-13
 */
public class WeekView extends View {
    private final Region[] WEEK_REGIONS = new Region[7];// 列数
    private final DateInfo[] INFO = new DateInfo[7];

    private final Map<String, List<Region>> REGION_SELECTED = new HashMap<String, List<Region>>();

    private ZSSCalendarManager mCManager = ZSSCalendarManager.getInstance();
    private DPTManager mTManager = DPTManager.getInstance();

    protected Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
    protected Paint todayPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
    private Scroller mScroller;
    private DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
    private OnWeekViewChangeListener onWeekViewChangeListener;
    private MonthView.OnSelectDayEventListener onSelectDayEventListener;
    private OnWeekViewLineChangeListener onWeekViewLineChangeListener;
    private OnWeekDateClick onWeekClick;
    private ScaleAnimationListener scaleAnimationListener;

    private SlideMode mSlideMode;
    private DPDecor mDPDecor;

    private int circleRadius;
    private int indexYear, indexMonth;
    private int indexWeek;
    private int centerYear, centerMonth;
    private int leftYear, leftMonth;
    private int rightYear, rightMonth;
    private int width, height;
    private int sizeDecor, sizeDecor2x, sizeDecor3x;
    private int lastPointX, lastPointY;
    private int lastMoveX;
    private int criticalWidth;
    private float sizeTextGregorian, sizeTextFestival;
    private float offsetYFestival1, offsetYFestival2;
    // 当前显示的是第几行 从0开始
    private int num = 5;
    // 一共有的行数
    private int count = 5;

    private boolean isNewEvent, isFestivalDisplay = true, isHolidayDisplay = true, isScheduleDisplay = true,
            isTodayDisplay = true, isDeferredDisplay = true;

    private Map<String, BGCircle> cirApr = new HashMap<String, BGCircle>();
    private Map<String, BGCircle> cirDpr = new HashMap<String, BGCircle>();

    private List<String> dateSelected = new ArrayList<String>();

    public WeekView(Context context) {
        this(context, null);
    }

    public WeekView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            scaleAnimationListener = new ScaleAnimationListener();
        }
        mScroller = new Scroller(context);
        mPaint.setTextAlign(Paint.Align.CENTER);
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
        // return false;
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
                            indexWeek++;
                            if ((count - 1) == num) {
                                num = 0;

                                centerMonth++;
                                if (centerMonth == 13) {
                                    centerMonth = 1;
                                    centerYear++;
                                }
                                //通知month更新
                                if (null != onWeekViewChangeListener) {
                                    onWeekViewChangeListener.onWeekViewChange(true, 1, num);
                                }
                            } else {
                                num++;
                                if (null != onWeekViewChangeListener) {
                                    onWeekViewChangeListener.onWeekViewChange(true, 0, num);
                                }
                            }

//						indexMonth++;
//						centerMonth = (centerMonth + 1) % 13;
//						if (centerMonth == 0) {
//							centerMonth = 1;
//							centerYear++;
//						}


                        } else if (lastPointX < event.getX() && Math.abs(lastPointX - event.getX()) >= criticalWidth) {
                            indexWeek--;
                            if (num == 0) {
                                //更新下一个月
                                centerMonth--;
                                if (centerMonth == 0) {
                                    centerMonth = 12;
                                    centerYear--;
                                }

                                DateInfo[][] info_left = mCManager.getDateInfo(centerYear, centerMonth, true);//左边
                                if (TextUtils.isEmpty(info_left[4][0].strG)) {
                                    num = 3;
                                } else if (TextUtils.isEmpty(info_left[5][0].strG)) {
                                    num = 4;
                                } else {
                                    num = 5;
                                }

                                if (null != onWeekViewChangeListener) {
                                    onWeekViewChangeListener.onWeekViewChange(false, -1, num);
                                }
                            } else {
                                num--;
                                if (null != onWeekViewChangeListener) {
                                    onWeekViewChangeListener.onWeekViewChange(false, 0, num);
                                }
                            }
//						indexMonth--;
//						centerMonth = (centerMonth - 1) % 12;
//						if (centerMonth == 0) {
//							centerMonth = 12;
//							centerYear--;
//						}
                        }
                        buildRegion();
                        computeDate();
                        smoothScrollTo(width * indexWeek, 0);
                        lastMoveX = width * indexWeek;
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
        setMeasuredDimension(measureWidth, (int) (measureWidth * 6F / 7F) / count);
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
        requestLayout();
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
        requestLayout();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        width = w;
        height = h;

        criticalWidth = (int) (1F / 5F * width);
        int cellW = (int) (w / 7F);

        circleRadius = cellW-20;

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

        for (int j = 0; j < WEEK_REGIONS.length; j++) {
            Region region = new Region();
            region.set((j * cellW), 0, cellW + (j * cellW), cellW);
            WEEK_REGIONS[j] = region;
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(mTManager.colorBG());
        // int numb=num;
        if (num == 0) {
            //需要调用这月和上月
            int tempYear = centerYear;
            int tempMonth = centerMonth;
            tempMonth = tempMonth - 1;
            if (tempMonth == 0) {
                tempMonth = 12;
                tempYear = tempYear - 1;
            }
            DateInfo[] result = null;
            DateInfo[][] info_left = mCManager.getDateInfo(tempYear, tempMonth, true);//左边
            if (TextUtils.isEmpty(info_left[4][0].strG)) {
                result = info_left[3];
            } else if (TextUtils.isEmpty(info_left[5][0].strG)) {
                result = info_left[4];
            } else {
                result = info_left[5];
            }
            drawWeek(canvas, width * (indexWeek - 1), 0, result);

            tempYear = centerYear;
            tempMonth = centerMonth;
            DateInfo[][] info_center = mCManager.getDateInfo(tempYear, tempMonth, true);//中间
            result = info_center[0];
            drawWeek(canvas, width * indexWeek, 0, result);

            result = info_center[1];
            drawWeek(canvas, width * (indexWeek + 1), 0, result);

            //draw(canvas, width * (indexWeek - 1), 0, centerYear, centerMonth, -1);
//			draw(canvas, width * indexWeek,0, centerYear, centerMonth, 0);
//			draw(canvas, width * (indexWeek + 1), 0, centerYear, centerMonth, 0);
        } else if (num == (count - 1)) {
            //需要调用这月和下月
            int tempYear = centerYear;
            int tempMonth = centerMonth;
            DateInfo[] result = null;
            DateInfo[][] info_center = mCManager.getDateInfo(tempYear, tempMonth, true);//中间
            result = info_center[num - 1];
            drawWeek(canvas, width * (indexWeek - 1), 0, result);

            result = info_center[num];
            drawWeek(canvas, width * indexWeek, 0, result);

            tempMonth++;
            if (tempMonth == 13) {
                tempMonth = 1;
                tempYear++;
            }
            DateInfo[][] info_right = mCManager.getDateInfo(tempYear, tempMonth, true);//
            result = info_right[0];
            drawWeek(canvas, width * (indexWeek + 1), 0, result);
//			draw(canvas, width * (indexWeek-1),0, centerYear, centerMonth, 0);
//			draw(canvas, width * indexWeek,0, centerYear, centerMonth, 0);
//			draw(canvas, width * (indexWeek + 1), 0, centerYear, centerMonth, 1);
        } else {
            DateInfo[] result = null;
            DateInfo[][] info_center = mCManager.getDateInfo(centerYear, centerMonth, true);//
            result = info_center[num - 1];
            drawWeek(canvas, width * (indexWeek - 1), 0, result);
            result = info_center[num];
            drawWeek(canvas, width * indexWeek, 0, result);
            result = info_center[num + 1];
            drawWeek(canvas, width * (indexWeek + 1), 0, result);
//			draw(canvas, width * (indexWeek-1), 0, centerYear, centerMonth, 0);
//			draw(canvas, width * indexWeek, 0, centerYear, centerMonth, 0);
//			draw(canvas, width * (indexWeek+1), 0, centerYear, centerMonth, 0);
        }
//		draw(canvas, width * (indexWeek - 1), 0, centerYear, centerMonth, -1);
//		draw(canvas, width * indexWeek, indexYear * height, centerYear, centerMonth, 0);
//		draw(canvas, width * (indexWeek + 1), height * indexYear, centerYear, centerMonth, 1);

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

    /**
     * @param canvas
     * @param x
     * @param y
     */
    private void drawWeek(Canvas canvas, int x, int y, DateInfo[] result) {
        canvas.save();
        canvas.translate(x, 0);
        Region[] tmp = WEEK_REGIONS;
        for (int j = 0; j < result.length; j++) {
            draw(canvas, tmp[j].getBounds(), result[j]);
        }
        canvas.restore();
    }

    /**
     * @param canvas
     * @param x
     * @param y
     * @param year
     * @param month
     * @param location -1 left 0 middle 1 right
     */
    private void draw(Canvas canvas, int x, int y, int year, int month, int location) {
        canvas.save();
        canvas.translate(x, y);
        DateInfo[][] info = null;
        if (location == -1) {
            //调用上个月的
            int tempMonth = month - 1;
            int tempYear = year;
            if (tempMonth == 0) {
                tempMonth = 12;
                tempYear--;
            }
            info = mCManager.getDateInfo(tempYear, tempMonth, true);//上个月
        } else if (location == 0) {
            //这个月
            info = mCManager.getDateInfo(year, month, true);//上个月
        } else {
            //调用下个月
            int tempMonth = month + 1;
            int tempYear = year;
            if (tempMonth == 13) {
                tempMonth = 1;
                tempYear++;
            }
            info = mCManager.getDateInfo(tempYear, tempMonth, true);//下个月
        }
        DateInfo[] result;
        if (location == -1) {
            if (TextUtils.isEmpty(info[4][0].strG)) {
                result = info[3];
            } else if (TextUtils.isEmpty(info[5][0].strG)) {
                result = info[4];
            } else {
                result = info[5];
            }
        } else if (location == 0) {
            result = info[num];
        } else {
            result = info[0];
        }
        Region[] tmp = WEEK_REGIONS;
        for (int j = 0; j < result.length; j++) {
            draw(canvas, tmp[j].getBounds(), result[j]);
        }
        // if (TextUtils.isEmpty(info[4][0].strG)) {
        // tmp = MONTH_REGIONS_4;
        // arrayClear(INFO_4);
        // result = arrayCopy(info, INFO_4);
        // } else if (TextUtils.isEmpty(info[5][0].strG)) {
        // tmp = MONTH_REGIONS_5;
        // arrayClear(INFO_5);
        // result = arrayCopy(info, INFO_5);
        // } else {
        // tmp = MONTH_REGIONS_6;
        // arrayClear(INFO_6);
        // result = arrayCopy(info, INFO_6);
        // }
        // for (int i = 0; i < result.length; i++) {
        // if (num >= result.length) {
        // // 5行与6行之间切换导致数组下标越界
        // for (int j = 0; j < result[result.length - 1].length; j++) {
        // draw(canvas, tmp[0][j].getBounds(), info[result.length - 1][j]);
        // }
        // } else {
        // for (int j = 0; j < result[num].length; j++) {
        // draw(canvas, tmp[0][j].getBounds(), info[num][j]);
        // }
        // }

        // }
        canvas.restore();
    }

    public void setLine(int num) {
        this.num = num;
    }

    public void setCount(int count, boolean requestLayout) {
        this.count = count;
        if (requestLayout) {
            requestLayout();
        }
    }

    private void draw(Canvas canvas, Rect rect, DateInfo info) {
        drawBG(canvas, rect, info);
        drawGregorian(canvas, rect, info.strG, info.isWeekend, info.isToday);
        if (isFestivalDisplay)
            drawFestival(canvas, rect, info.strF, info.isFestival);
        drawDecor(canvas, rect, info);
    }

    private void drawBG(Canvas canvas, Rect rect, DateInfo info) {
        if (null != mDPDecor && info.isDecorBG) {
            mDPDecor.drawDecorBG(canvas, rect, mPaint, centerYear + "-" + centerMonth + "-" + info.strG);
        }
        if (info.isToday && isTodayDisplay) {
            drawBGToday(canvas, rect);
        } else {
            if (isHolidayDisplay)
                drawBGHoliday(canvas, rect, info.isHoliday);
            if (isScheduleDisplay)
                drawBGSchedule(canvas, rect, info.isScheduled);
            if (isDeferredDisplay)
                drawBGDeferred(canvas, rect, info.isDeferred);
        }
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
            mPaint.setColor(Color.BLUE);
            float dx = (float) (Math.sqrt(2.0) / 4.0 * circleRadius);
            canvas.drawText("假", rect.centerX() + dx, rect.centerY() - dx, mPaint);
        }*/
        // canvas.drawCircle(rect.centerX(), rect.centerY(), circleRadius / 2F,
        // mPaint);
    }

    private void drawBGSchedule(Canvas canvas, Rect rect, boolean isSchedule) {
        mPaint.setColor(mTManager.colorHoliday());
        if (isSchedule) {
//            mPaint.setColor(Color.GREEN);
            mPaint.setColor(0xff1EB391);
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

    // public void setOnDateChangeListener(
    // OnDateChangeListener onDateChangeListener) {
    // this.onDateChangeListener = onDateChangeListener;
    // }

    public void setOnWeekViewChangeListener(OnWeekViewChangeListener onWeekViewChangeListener) {
        this.onWeekViewChangeListener = onWeekViewChangeListener;
    }

    public void setOnWeekViewLineChangeListener(OnWeekViewLineChangeListener onWeekViewLineChangeListener) {
        this.onWeekViewLineChangeListener = onWeekViewLineChangeListener;
    }

    public void setOnWeekClickListener(OnWeekDateClick onWeekClick) {
        this.onWeekClick = onWeekClick;
    }

    public void setOnSelectDayEventListener(MonthView.OnSelectDayEventListener onSelectDayEventListener) {
        this.onSelectDayEventListener = onSelectDayEventListener;
    }

    public void setDPDecor(DPDecor decor) {
        this.mDPDecor = decor;
    }

    public void setYearMonth(int year, int month) {
        centerYear = year;
        centerMonth = month;
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
        return dst;
    }

    @SuppressLint("NewApi")
    public void defineRegion(final int x, final int y) {
        DateInfo[][] info = mCManager.getDateInfo(centerYear, centerMonth, true);
        Region[][] tmp;
        if (TextUtils.isEmpty(info[4][0].strG)) {
            tmp = MonthView.MONTH_REGIONS_4;
        } else if (TextUtils.isEmpty(info[5][0].strG)) {
            tmp = MonthView.MONTH_REGIONS_5;
        } else {
            tmp = MonthView.MONTH_REGIONS_6;
        }
        for (int i = 0; i < tmp.length; i++) {
            //5，6行切换导致的下标越界
            if (num >= tmp.length) {
                num = tmp.length - 1;
            }

            for (int j = 0; j < tmp[num].length; j++) {
                Region region = tmp[0][j];
                String strG = mCManager.getDateInfo(centerYear, centerMonth, true)[num][j].strG;
                if (TextUtils.isEmpty(strG)) {
                    continue;
                }
                if (region.contains(x, y)) {
                    List<Region> regions = REGION_SELECTED.get(indexYear + ":" + indexMonth);
                    cirApr.clear();
                    regions.add(region);
                    int tempCenterYear = centerYear;
                    int tempCenterMonth = centerMonth;

                    String tempDate = "";
                    if (num == 0 && j < 7 && Integer.parseInt(strG) > 10) {
                        tempCenterMonth = tempCenterMonth - 1;
                        if (tempCenterMonth == 0) {
                            tempCenterMonth = 12;
                            tempCenterYear--;
                        }
                    } else if (num > 2 && Integer.parseInt(strG) < 10) {
                        tempCenterMonth = tempCenterMonth + 1;
                        if (tempCenterMonth == 13) {
                            tempCenterMonth = 1;
                            tempCenterYear++;
                        }
                    }
                    tempDate = tempCenterYear + "." + tempCenterMonth + "."
                            + mCManager.getDateInfo(centerYear, centerMonth, true)[num][j].strG;

                    final String date = tempDate;
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
                                    onSelectDayEventListener.onSelectDayEvent(date);
                                }
                                if (null != onWeekClick) {
                                    onWeekClick.onWeekDateClick(x, y);
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
                        if (null != onWeekClick) {
                            onWeekClick.onWeekDateClick(x, y);
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("NewApi")
    public void changeChooseDate(int x, int y) {
        DateInfo[][] info = mCManager.getDateInfo(centerYear, centerMonth, true);
        Region[][] tmp;
        if (TextUtils.isEmpty(info[4][0].strG)) {
            tmp = MonthView.MONTH_REGIONS_4;
        } else if (TextUtils.isEmpty(info[5][0].strG)) {
            tmp = MonthView.MONTH_REGIONS_5;
        } else {
            tmp = MonthView.MONTH_REGIONS_6;
        }
        for (int j = 0; j < tmp[num].length; j++) {
            Region region = tmp[0][j];
            if (TextUtils.isEmpty(mCManager.getDateInfo(centerYear, centerMonth, true)[num][j].strG)) {
                continue;
            }
            if (region.contains(x, y)) {
                List<Region> regions = REGION_SELECTED.get(indexYear + ":" + indexMonth);
                cirApr.clear();
                regions.add(region);
                final String date = centerYear + "." + centerMonth + "."
                        + mCManager.getDateInfo(centerYear, centerMonth, true)[num][j].strG;
                BGCircle circle = createCircle(region.getBounds().centerX() + indexMonth * width,
                        region.getBounds().centerY() + indexYear * height);
                WeekView.this.invalidate();
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
    }

    public interface OnWeekViewChangeListener {
        /**
         * @param isForward
         * @param monthState 0  保持原样  －1 向左  1向右
         */
        void onWeekViewChange(boolean isForward, int monthState, int line);
    }

    public interface OnWeekViewLineChangeListener {
        void onWeekViewLineChange(int line);
    }

    public interface OnWeekDateClick {
        void onWeekDateClick(int x, int y);
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

        public void setShape(ShapeDrawable shape) {
            this.shape = shape;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private class ScaleAnimationListener implements ValueAnimator.AnimatorUpdateListener {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            WeekView.this.invalidate();
        }
    }

    /**
     * @return
     */
    public DateInfo[][] completebuildDaysOfMonth(DateInfo[][] tmp, int year, int month) {
        if (tmp == null) {
            return null;
        }
        // 从头开始 第一个不为空的 getHeadNullIndex
        int i = -1;
        int j = -1;
        String headIndex = getHeadNullIndex(tmp);
        if (!TextUtils.isEmpty(headIndex) && !"-1".equals(headIndex)) {
            i = Integer.parseInt(headIndex.split("-")[0]);
            j = Integer.parseInt(headIndex.split("-")[1]);
            Calendar c = Calendar.getInstance();
            c.set(year, month - 1, 1);
            for (int k = 0; k < i; k++) {
                c.add(Calendar.DAY_OF_MONTH, i - k);
                tmp[0][k].strG = c.get(Calendar.DAY_OF_MONTH) + "";
                c.set(year, month - 1, 1);
            }
        }
        // 第一个为空的
        String footIndex = getFootNullIndex(tmp);
        if (!TextUtils.isEmpty(footIndex) && !"-1".equals(footIndex)) {
            i = Integer.parseInt(footIndex.split("-")[0]);
            j = Integer.parseInt(footIndex.split("-")[1]);
            for (int k = i + 1; k < 7; k++) {
                tmp[k][j].strG = (k - i) + "";
            }
        }
        return tmp;
    }

    /**
     * 返回第一个头上不为空的 坐标 i j
     *
     * @param tmp
     * @return
     */
    private String getHeadNullIndex(DateInfo tmp[][]) {
        String result = "-1";
        if (tmp == null) {
            return result;
        }
        boolean breakCan = false;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                String str = tmp[i][j].strG;
                if (!TextUtils.isEmpty(str)) {
                    result = i + "-" + j;
                    breakCan = true;
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
     * 返回尾部得 最后一个不为空的坐标 i j
     *
     * @param tmp
     * @return
     */
    private String getFootNullIndex(DateInfo tmp[][]) {
        String result = "-1";
        if (tmp == null) {
            return result;
        }
        boolean breakCan = false;
        for (int i = 5; i > 1; i--) {
            for (int j = 6; j >= 0; j--) {
                String str = tmp[i][j].strG;
                if (!TextUtils.isEmpty(str)) {
                    result = i + "-" + j;
                    breakCan = true;
                    break;
                }
            }
            if (breakCan) {
                break;
            }
        }
        return result;
    }
}
