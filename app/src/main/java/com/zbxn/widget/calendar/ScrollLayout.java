package com.zbxn.widget.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.zbxn.widget.calendar.views.MonthView;
import com.zbxn.R;
import com.zbxn.widget.MyListView;
import com.zbxn.widget.calendar.views.WeekView;


@SuppressLint("ClickableViewAccessibility")
public class ScrollLayout extends FrameLayout implements MonthView.OnLineCountChangeListener,
        MonthView.OnLineChooseListener, MonthView.OnMonthViewChangeListener, WeekView.OnWeekViewChangeListener,
        WeekView.OnWeekDateClick, MonthView.OnMonthDateClick, WeekView.OnWeekViewLineChangeListener {

    private ViewDragHelper viewDragHelper;
    private MonthView monthView;
    private WeekView weekView;
    private LinearLayout mainLayout;
    private LinearLayout contentLayout;
    private MyListView cotent_listview;

    // 记录month calendar 行数和选择的哪一行的数字的变化
    private int line;// 从0开始
    private int lineCount;// 行数
    // 初始的Y坐标
    private int orignalY;
    // 滑动的过程中记录顶部坐标
    private int layoutTop;
    private OnWeekMonthStyleChangeEventListener onWeekMonthStyleChangeEventListener;

    public ScrollLayout(Context context) {
        this(context, null);
    }

    public ScrollLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private int last_top = 0;

    public ScrollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                // 说明mainLayout可以拖动
                if (child == mainLayout) {
                    last_top = child.getTop();
                    Log.e("Srcoll——————", "last_top:" + last_top);
                }
                return child == mainLayout;
//                return child != cotent_listview;

//                return false;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                if (top >= orignalY) {
                    return orignalY;
                } else {
                    int heigth = ScrollLayout.this.getHeight();
                    int mainHeight = mainLayout.getMeasuredHeight();
                    int maxTop = heigth - mainHeight;
                    if (maxTop <= 0) {
                        if (top <= maxTop) {
                            return maxTop;
                        } else {
                            return top;
                        }
                    } else {
                        if (Math.abs(top) >= monthView.getHeight() * (lineCount - 1) / lineCount) {
                            return -monthView.getHeight() * (lineCount - 1) / lineCount;
                        } else {
                            return top;
                        }
                    }
                }
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                layoutTop = top <= -monthView.getHeight() * (lineCount - 1) / lineCount
                        ? -monthView.getHeight() * (lineCount - 1) / lineCount : top;
                if (top <= -monthView.getHeight() * line / lineCount && dy < 0) {
                    if (weekView.getVisibility() != View.VISIBLE) {
                        weekView.setVisibility(View.VISIBLE);
                        if (onWeekMonthStyleChangeEventListener != null) {
                            onWeekMonthStyleChangeEventListener.onWeekMonthStyleChangeEvent(1);
                        }
                    }
                } else if (top >= -monthView.getHeight() * line / lineCount && dy > 0) {
                    if (weekView.getVisibility() != View.INVISIBLE) {
                        weekView.setVisibility(View.INVISIBLE);
                        if (onWeekMonthStyleChangeEventListener != null) {
                            onWeekMonthStyleChangeEventListener.onWeekMonthStyleChangeEvent(0);
                        }
                    }

                }
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (Math.abs(releasedChild.getTop()) <= monthView.getHeight() * (1) / lineCount / 2) {
                    last_top = orignalY;
                    viewDragHelper.settleCapturedViewAt(0, orignalY);
                    invalidate();
                } else if (Math.abs(releasedChild.getTop()) >= (monthView.getHeight() * (lineCount - 2) / lineCount + monthView.getHeight() * (1) / lineCount / 2)) {
//                    last_top = -monthView.getHeight() * (lineCount - 1) / lineCount;
//                    viewDragHelper.settleCapturedViewAt(0, -monthView.getHeight() * (lineCount - 1) / lineCount);
//                    invalidate();
                } else {
                    int nowTop = releasedChild.getTop();
                    if (Math.abs(nowTop) > Math.abs(last_top)) {
                        viewDragHelper.settleCapturedViewAt(0, -monthView.getHeight() * (lineCount - 1) / lineCount);
                        invalidate();
                    } else {
                        viewDragHelper.settleCapturedViewAt(0, orignalY);
                        invalidate();
                    }
                }
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return monthView.getHeight();
            }
        });
    }

    @Override
    public void onLineChange(int line) {
        this.line = line;
        weekView.setLine(line);
    }

    @Override
    public void onLineCountChange(int lineCount) {
        this.lineCount = lineCount;
        if (lineCount == 6) {
            weekView.setCount(lineCount, true);
        } else {
            weekView.setCount(lineCount, false);
        }
    }

    @Override
    public void onWeekViewLineChange(int line) {
        this.line = line;
    }

    @Override
    public void onMonthViewChange(boolean isforward, int newYear, int newMonth) {
        weekView.setYearMonth(newYear, newMonth);
//		if (isforward) {
//			weekView.moveForwad();
//		} else {
//			weekView.moveBack();
//		}
    }

    @Override
    public void onMonthDateClick(int x, int y) {
        weekView.changeChooseDate(x, y - (monthView.getHeight() * (line) / lineCount));
    }

    @Override
    public void onWeekDateClick(int x, int y) {
        monthView.changeChooseDate(x, y + (monthView.getHeight() * (line) / lineCount));
        weekView.changeChooseDate(x, y - (monthView.getHeight() * (line) / lineCount));
//        monthView.changeChooseDate(x, y );
    }

    @Override
    /**
     *
     * @param isForward
     * @param monthState 0  保持原样  －1 向左  1向右
     */
    public void onWeekViewChange(boolean isForward, int monthState, int line) {

        if (monthState == -1) {
            monthView.moveBack();
        } else if (monthState == 1) {
            monthView.moveForwad();
        }
        this.line = line;
        monthView.setLine(line);
//		if (isForward) {
//			monthView.moveForwad();
//		} else {
//			monthView.moveBack();
//		}
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            postInvalidate();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        monthView = (MonthView) findViewById(R.id.month_calendar);
        contentLayout = (LinearLayout) findViewById(R.id.content_layout);
        cotent_listview = (MyListView) findViewById(R.id.cotent_listview);
        monthView.setOnLineChooseListener(this);
        monthView.setOnLineCountChangeListener(this);
        monthView.setOnMonthDateClickListener(this);
        monthView.setOnMonthViewChangeListener(this);
        weekView = (WeekView) findViewById(R.id.week_calendar);
        weekView.setOnWeekViewChangeListener(this);
        weekView.setOnWeekClickListener(this);
        orignalY = monthView.getTop();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        weekView.layout(0, 0, weekView.getMeasuredWidth(), weekView.getMeasuredHeight());
        mainLayout.layout(0, layoutTop, mainLayout.getMeasuredWidth(), mainLayout.getMeasuredHeight());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    // 重写 onMeasure 支持 wrap_content
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        // 计算所有childview的宽高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        // 计算warp_content的时候的高度
        int wrapHeight = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            int childHeight = child.getMeasuredHeight();
            wrapHeight += childHeight;
        }
        setMeasuredDimension(widthSize, heightMode == MeasureSpec.EXACTLY ? heightSize : wrapHeight);
    }

    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        ViewGroup.LayoutParams lp = child.getLayoutParams();

        int childWidthMeasureSpec;
        int childHeightMeasureSpec;

        childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, getPaddingLeft() + getPaddingRight(),
                lp.width);

        childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed,
                                           int parentHeightMeasureSpec, int heightUsed) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                lp.leftMargin + lp.rightMargin + widthUsed, lp.width);
        final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(lp.topMargin + lp.bottomMargin,
                MeasureSpec.UNSPECIFIED);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    public interface OnWeekMonthStyleChangeEventListener {
        void onWeekMonthStyleChangeEvent(int style);
    }

    public void setOnWeekMonthStyleChangeEventListener(OnWeekMonthStyleChangeEventListener l) {
        onWeekMonthStyleChangeEventListener = l;
    }
}
