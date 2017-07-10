package widget.indexablelistview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.SectionIndexer;

public class IndexScroller {

    private float mIndexbarWidth;
    private float mIndexbarMargin;
    private float mPreviewPadding;
    private float mDensity;
    private float mScaledDensity;
    private float mAlphaRate;
    private int mState = STATE_HIDDEN;
    private int mListViewWidth;
    private int mListViewHeight;
    private int mCurrentSection = -1;
    private boolean mIsIndexing = false;
    private ListView mListView = null;
    private SectionIndexer mIndexer = null;
    private String[] mSections = null;
    private RectF mIndexbarRect;

    private static final int STATE_HIDDEN = 0;
    private static final int STATE_SHOWING = 1;
    private static final int STATE_SHOWN = 2;
    private static final int STATE_HIDING = 3;
    //索引条被选中时的颜色
    private int mIndexActiveColor;

    public IndexScroller(Context context, ListView lv) {
        mDensity = context.getResources().getDisplayMetrics().density;
        mScaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        mListView = lv;
        setAdapter(mListView.getAdapter());

        mIndexbarWidth = 20 * mDensity;
        mIndexbarMargin = 10 * mDensity;
        mPreviewPadding = 5 * mDensity;

        TypedArray array = context.getTheme().obtainStyledAttributes(new int[]{android.R.attr.colorPrimary});
        mIndexActiveColor = array.getColor(0, Color.WHITE);
        array.recycle();
    }

    public void draw(Canvas canvas) {
        if (mState == STATE_HIDDEN)
            return;

        // mAlphaRate determines the rate of opacity
        Paint indexbarPaint = new Paint();
        indexbarPaint.setColor(Color.BLACK);
        indexbarPaint.setAlpha((int) (64 * mAlphaRate));
        indexbarPaint.setAntiAlias(true);
        canvas.drawRoundRect(mIndexbarRect, 5 * mDensity, 5 * mDensity, indexbarPaint);


        try {

            if (mSections != null && mSections.length > 0) {
                //索引提示框
                if (mCurrentSection >= 0) {//字母
                    drawDialogText(canvas, mSections[mCurrentSection] + "");
                } else {//汉字
                    int index = mListView.getFirstVisiblePosition() + 1;
                    if (index >= mListView.getChildCount())
                        index = 0;
                    Object itemObj = mListView.getItemAtPosition(index);
                    String temp = "";
                    if (itemObj instanceof IIndexControl)
                        temp = ((IIndexControl) itemObj).getNameForShort();
                    else
                        temp = temp.toString();
                    if (temp.length() > 0) {
                        temp = temp.substring(0, 1);
                        drawDialogText(canvas, temp);
                    }
                }

                //索引条填充
                Paint indexPaint = new Paint();
                indexPaint.setColor(Color.WHITE);
                indexPaint.setAlpha((int) (255 * mAlphaRate));
                indexPaint.setAntiAlias(true);
                indexPaint.setTextSize(12 * mScaledDensity);

                float sectionHeight = (mIndexbarRect.height() - 2 * mIndexbarMargin) / mSections.length;
                float paddingTop = (sectionHeight - (indexPaint.descent() - indexPaint.ascent())) / 2;
                for (int i = 0; i < mSections.length; i++) {
                    float paddingLeft = (mIndexbarWidth - indexPaint.measureText(mSections[i])) / 2;
                    if (i == mCurrentSection) {
                        indexPaint.setColor(mIndexActiveColor);
                        canvas.drawText(mSections[i], mIndexbarRect.left + paddingLeft
                                , mIndexbarRect.top + mIndexbarMargin + sectionHeight * i + paddingTop - indexPaint.ascent(), indexPaint);
                        indexPaint.setColor(Color.WHITE);
                    } else {
                        canvas.drawText(mSections[i], mIndexbarRect.left + paddingLeft
                                , mIndexbarRect.top + mIndexbarMargin + sectionHeight * i + paddingTop - indexPaint.ascent(), indexPaint);
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * 绘制文字
     *
     * @param canvas
     * @param text
     */
    public void drawDialogText(Canvas canvas, String text) {

        Paint previewPaint = new Paint();
        previewPaint.setColor(Color.BLACK);
        previewPaint.setAlpha(96);
        previewPaint.setAntiAlias(true);
        previewPaint.setShadowLayer(3, 0, 0, Color.argb(64, 0, 0, 0));

        Paint previewTextPaint = new Paint();
        previewTextPaint.setColor(Color.WHITE);
        previewTextPaint.setAntiAlias(true);
        previewTextPaint.setTextSize(50 * mScaledDensity);

        float previewTextWidth = previewTextPaint.measureText(text);
        float previewSize = 2 * mPreviewPadding + previewTextPaint.descent() - previewTextPaint.ascent();
        RectF previewRect = new RectF((mListViewWidth - previewSize) / 2
                , (mListViewHeight - previewSize) / 2
                , (mListViewWidth - previewSize) / 2 + previewSize
                , (mListViewHeight - previewSize) / 2 + previewSize);

        canvas.drawRoundRect(previewRect, 5 * mDensity, 5 * mDensity, previewPaint);
        canvas.drawText(text, previewRect.left + (previewSize - previewTextWidth) / 2 - 1
                , previewRect.top + mPreviewPadding - previewTextPaint.ascent() + 1, previewTextPaint);
    }

    public boolean mIsTouching = false;

    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //触摸发生在索引条上
                if (mState != STATE_HIDDEN && contains(ev.getX(), ev.getY())) {
                    mIsTouching = true;
                    setState(STATE_SHOWN);

                    // It demonstrates that the motion event started from index bar
                    mIsIndexing = true;
                    //当前触摸点所在索引位置
                    mCurrentSection = getSectionByPoint(ev.getY());
                    mListView.setSelection(mIndexer.getPositionForSection(mCurrentSection));
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsIndexing) {
                    mIsTouching = true;
                    //触摸发生在索引条上
                    if (contains(ev.getX(), ev.getY())) {
                        // Determine which section the point is in, and move the list to that section
                        mCurrentSection = getSectionByPoint(ev.getY());
                        mListView.setSelection(mIndexer.getPositionForSection(mCurrentSection));
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsIndexing) {
                    mIsIndexing = false;
                    mCurrentSection = -1;
                }
                if (mState == STATE_SHOWN)
                    setState(STATE_HIDING);
                mIsTouching = false;
                break;
        }
        return false;
    }

    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        mListViewWidth = w;
        mListViewHeight = h;
        mIndexbarRect = new RectF(w - mIndexbarMargin - mIndexbarWidth
                , mIndexbarMargin
                , w - mIndexbarMargin
                , h - mIndexbarMargin);
    }

    public void show() {
        if (mState == STATE_HIDDEN)
            setState(STATE_SHOWING);
        else if (mState == STATE_HIDING)
            setState(STATE_HIDING);
    }

    public void hide() {
        if (mState == STATE_SHOWN)
            setState(STATE_HIDING);
    }

    public void setAdapter(Adapter adapter) {
        if (adapter instanceof SectionIndexer) {
            mIndexer = (SectionIndexer) adapter;
            mSections = (String[]) mIndexer.getSections();
        }
    }

    private void setState(int state) {
        if (state < STATE_HIDDEN || state > STATE_HIDING)
            return;

        mState = state;
        switch (mState) {
            case STATE_HIDDEN:
                // Cancel any fade effect
                mHandler.removeMessages(0);
                break;
            case STATE_SHOWING:
                // Start to fade in
                mAlphaRate = 0;
                fade(0);
                break;
            case STATE_SHOWN:
                // Cancel any fade effect
                mHandler.removeMessages(0);
                break;
            case STATE_HIDING:
                // Start to fade out after three seconds
//                mAlphaRate = 1;
//                fade(3000);
                break;
        }
    }

    public boolean contains(float x, float y) {
        // Determine if the point is in index bar region, which includes the right margin of the bar
        return (x >= mIndexbarRect.left && y >= mIndexbarRect.top && y <= mIndexbarRect.top + mIndexbarRect.height());
    }

    private int getSectionByPoint(float y) {
        if (mSections == null || mSections.length == 0)
            return 0;
        if (y < mIndexbarRect.top + mIndexbarMargin)
            return 0;
        if (y >= mIndexbarRect.top + mIndexbarRect.height() - mIndexbarMargin)
            return mSections.length - 1;
        return (int) ((y - mIndexbarRect.top - mIndexbarMargin) / ((mIndexbarRect.height() - 2 * mIndexbarMargin) / mSections.length));
    }

    private void fade(long delay) {
        mHandler.removeMessages(0);
        mHandler.sendEmptyMessageAtTime(0, SystemClock.uptimeMillis() + delay);
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (mState) {
                case STATE_SHOWING:
                    // Fade in effect
                    mAlphaRate += (1 - mAlphaRate) * 0.2;
                    if (mAlphaRate > 0.9) {
                        mAlphaRate = 1;
                        setState(STATE_SHOWN);
                    }

                    mListView.invalidate();
                    fade(10);
                    break;
                case STATE_SHOWN:
                    // If no action, hide automatically
                    setState(STATE_HIDING);
                    break;
                case STATE_HIDING:
                    // Fade out effect
                    mAlphaRate -= mAlphaRate * 0.2;
                    if (mAlphaRate < 0.1) {
                        mAlphaRate = 1;
                        setState(STATE_HIDDEN);
                    }

                    mListView.invalidate();
                    fade(10);
                    break;
            }
        }

    };
}
