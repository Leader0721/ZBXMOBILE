package com.zbxn.widget.slidedatetimepicker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zbxn.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by wj on 2016/11/21.
 */
public class NewSlideDateTimeDialogFragment extends DialogFragment implements DateFragment.DateChangedListener,
        TimeFragment.TimeChangedListener {
    public static final String TAG_SLIDE_DATE_TIME_DIALOG_FRAGMENT = "tagSlideDateTimeDialogFragment";

    private static SlideDateTimeListener mListener;

    private Context mContext;
    private View mButtonHorizontalDivider;
    private View mButtonVerticalDivider;
    private Button mOkButton;
    private Button mCancelButton;
    private Date mInitialDate;
    private int mTheme;
    private int mIndicatorColor;
    private Date mMinDate;
    private Date mMaxDate;
    private boolean mIsClientSpecified24HourTime;
    private boolean mIs24HourTime;
    private Calendar mCalendar;
    private int mDateFlags =
            DateUtils.FORMAT_SHOW_YEAR |
            DateUtils.FORMAT_SHOW_WEEKDAY |
                    DateUtils.FORMAT_SHOW_DATE |
                    DateUtils.FORMAT_ABBREV_ALL;
    private int mTimeFlags =
            DateUtils.FORMAT_SHOW_YEAR |
                    DateUtils.FORMAT_SHOW_WEEKDAY |
                    DateUtils.FORMAT_SHOW_DATE |
                    DateUtils.FORMAT_SHOW_TIME|
                    DateUtils.FORMAT_ABBREV_ALL
                    ;

    private static int mIsHaveTime;
    public final static int Have_Date_Time = 1001;//日期加时间
    public final static int Have_Date = 1002;//只有日期
    public final static int Have_Time = 1003;//只有时间
    private DateFragment fgDate;
    private TimeFragment fgTime;
    private TextView tv_time;
    private FrameLayout flDate;
    private FrameLayout flTime;
    private FragmentTransaction transaction;
    private FragmentManager childFragmentManager;

    public NewSlideDateTimeDialogFragment() {
        // Required empty public constructor
    }

    /**
     * <p>Return a new instance of {@code NewSlideDateTimeDialogFragment} with its bundle
     * filled with the incoming arguments.</p>
     * <p/>
     * <p>Called by {@link SlideDateTimePicker#show()}.</p>
     *
     * @param listener
     * @param initialDate
     * @param minDate
     * @param maxDate
     * @param isClientSpecified24HourTime
     * @param is24HourTime
     * @param theme
     * @param indicatorColor
     * @return
     */
    public static NewSlideDateTimeDialogFragment newInstance(SlideDateTimeListener listener,
                                                             Date initialDate, Date minDate, Date maxDate, boolean isClientSpecified24HourTime,
                                                             boolean is24HourTime, int theme, int indicatorColor, int isHaveTime) {
        mListener = listener;
        mIsHaveTime = isHaveTime;

        // Create a new instance of SlideDateTimeDialogFragment
        NewSlideDateTimeDialogFragment dialogFragment = new NewSlideDateTimeDialogFragment();

        // Store the arguments and attach the bundle to the fragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("initialDate", initialDate);
        bundle.putSerializable("minDate", minDate);
        bundle.putSerializable("maxDate", maxDate);
        bundle.putBoolean("isClientSpecified24HourTime", isClientSpecified24HourTime);
        bundle.putBoolean("is24HourTime", is24HourTime);
        bundle.putInt("theme", theme);
        bundle.putInt("indicatorColor", indicatorColor);
        dialogFragment.setArguments(bundle);
        // Return the fragment with its bundle
        return dialogFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        unpackBundle();

        mCalendar = Calendar.getInstance();
        mCalendar.setTime(mInitialDate);

        switch (mTheme) {
            case SlideDateTimePicker.HOLO_DARK:
                setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_NoActionBar);
                break;
            case SlideDateTimePicker.HOLO_LIGHT:
                setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
                break;
            default:  // if no theme was specified, default to holo light
                setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_slide_date_time_picker, container);
        //初始化控件
        setupViews(view);
        //主题
        customizeViews();
        //初始化fragment
        initFragment();
        //按钮
        initButtons();
        //初始化标题


        if (mIsHaveTime == Have_Date || mIsHaveTime == Have_Date_Time){
            tv_time.setText(DateUtils.formatDateTime(
                    mContext, mCalendar.getTimeInMillis(), mDateFlags));
        } else {
            tv_time.setText(DateUtils.formatDateTime(
                    mContext, mCalendar.getTimeInMillis(), mTimeFlags));
        }

        return view;
    }

    /**
     * 初始化fragement
     */
    private void initFragment() {

        //    manager = getActivity().getSupportFragmentManager();
        childFragmentManager = getChildFragmentManager();
        transaction = childFragmentManager.beginTransaction();
        //日期
        fgDate = DateFragment.newInstance(
                mTheme,
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH),
                mMinDate,
                mMaxDate);
        fgDate.setTargetFragment(NewSlideDateTimeDialogFragment.this, 100);
        //时间
        fgTime = TimeFragment.newInstance(
                mTheme,
                mCalendar.get(Calendar.HOUR_OF_DAY),
                mCalendar.get(Calendar.MINUTE),
                mIsClientSpecified24HourTime,
                mIs24HourTime);
        fgTime.setTargetFragment(NewSlideDateTimeDialogFragment.this, 200);
        //替换
        //隐藏与显示
        if (mIsHaveTime == Have_Date_Time) {
            flTime.setVisibility(View.VISIBLE);
            flDate.setVisibility(View.VISIBLE);
            transaction.replace(R.id.fl_date, fgDate);
            transaction.replace(R.id.fl_time, fgTime);
        } else if (mIsHaveTime == Have_Date) {
            flDate.setVisibility(View.VISIBLE);
            flTime.setVisibility(View.GONE);
            transaction.replace(R.id.fl_date, fgDate);
        } else if (mIsHaveTime == Have_Time) {
            flTime.setVisibility(View.VISIBLE);
            flDate.setVisibility(View.GONE);
            transaction.replace(R.id.fl_time, fgTime);
        }

        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }

        super.onDestroyView();
    }

    private void unpackBundle() {
        Bundle args = getArguments();

        mInitialDate = (Date) args.getSerializable("initialDate");
        mMinDate = (Date) args.getSerializable("minDate");
        mMaxDate = (Date) args.getSerializable("maxDate");
        mIsClientSpecified24HourTime = args.getBoolean("isClientSpecified24HourTime");
        mIs24HourTime = args.getBoolean("is24HourTime");
        mTheme = args.getInt("theme");
        mIndicatorColor = args.getInt("indicatorColor");
    }

    private void setupViews(View v) {
        flDate = (FrameLayout) v.findViewById(R.id.fl_date);
        flTime = (FrameLayout) v.findViewById(R.id.fl_time);
        tv_time = (TextView) v.findViewById(R.id.tv_time);
        mButtonHorizontalDivider = v.findViewById(R.id.buttonHorizontalDivider);
        mButtonVerticalDivider = v.findViewById(R.id.buttonVerticalDivider);
        mOkButton = (Button) v.findViewById(R.id.okButton);
        mCancelButton = (Button) v.findViewById(R.id.cancelButton);

    }

    private void customizeViews() {
        int lineColor = mTheme == SlideDateTimePicker.HOLO_DARK ?
                getResources().getColor(R.color.gray_holo_dark) :
                getResources().getColor(R.color.gray_holo_light);

        // Set the colors of the horizontal and vertical lines for the
        // bottom buttons depending on the theme.
        switch (mTheme) {
            case SlideDateTimePicker.HOLO_LIGHT:
            case SlideDateTimePicker.HOLO_DARK:
                mButtonHorizontalDivider.setBackgroundColor(lineColor);
                mButtonVerticalDivider.setBackgroundColor(lineColor);
                break;

            default:  // if no theme was specified, default to holo light
                mButtonHorizontalDivider.setBackgroundColor(getResources().getColor(R.color.gray_holo_light));
                mButtonVerticalDivider.setBackgroundColor(getResources().getColor(R.color.gray_holo_light));
        }

        // Set the color of the selected tab underline if one was specified.
    }

    private void initButtons() {
        mOkButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mListener == null) {
                    throw new NullPointerException(
                            "Listener no longer exists for mOkButton");
                }

                mListener.onDateTimeSet(new Date(mCalendar.getTimeInMillis()));

                dismiss();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mListener == null) {
                    throw new NullPointerException(
                            "Listener no longer exists for mCancelButton");
                }

                mListener.onDateTimeCancel();

                dismiss();
            }
        });
    }

    /**
     * <p>The callback used by the DatePicker to update {@code mCalendar} as
     * the user changes the date. Each time this is called, we also update
     * the text on the date tab to reflect the date the user has currenly
     * selected.</p>
     * <p/>
     * <p>Implements the {@link DateFragment.DateChangedListener}
     * interface.</p>
     */
    @Override
    public void onDateChanged(int year, int month, int day) {
        mCalendar.set(year, month, day);
        updateDateTab();
    }

    /**
     * <p>The callback used by the TimePicker to update {@code mCalendar} as
     * the user changes the time. Each time this is called, we also update
     * the text on the time tab to reflect the time the user has currenly
     * selected.</p>
     * <p/>
     * <p>Implements the {@link TimeFragment.TimeChangedListener}
     * interface.</p>
     */
    @Override
    public void onTimeChanged(int hour, int minute) {
        mCalendar.set(Calendar.HOUR_OF_DAY, hour);
        mCalendar.set(Calendar.MINUTE, minute);

        // Set initial time on time tab
        /*if (mIsHaveTime == Have_Time) {
            updateTimeOnlyTab();
        } else {
            updateTimeTab();
        }*/
        if (mIsHaveTime == Have_Time) {
            updateTimeOnlyTab();
        }
        updateTimeTab();
    }

    private void updateDateTab() {
        tv_time.setText(DateUtils.formatDateTime(
                mContext, mCalendar.getTimeInMillis(), mDateFlags));
    }

    @SuppressLint("SimpleDateFormat")
    private void updateTimeTab() {
        if (mIsClientSpecified24HourTime) {
            SimpleDateFormat formatter;

            if (mIs24HourTime) {
                formatter = new SimpleDateFormat("HH:mm");
                tv_time.setText(formatter.format(mCalendar.getTime()));
            } else {
                formatter = new SimpleDateFormat("h:mm aa");
                tv_time.setText(formatter.format(mCalendar.getTime()));
            }
        } else { // display time using the device's default 12/24 hour format preference
            tv_time.setText(DateFormat.getTimeFormat(
                    mContext).format(mCalendar.getTimeInMillis()));
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void updateTimeOnlyTab() {
        if (mIsClientSpecified24HourTime) {
            SimpleDateFormat formatter;

            if (mIs24HourTime) {
                formatter = new SimpleDateFormat("HH:mm");
                tv_time.setText(formatter.format(mCalendar.getTime()));
            } else {
                formatter = new SimpleDateFormat("h:mm aa");
                tv_time.setText(formatter.format(mCalendar.getTime()));
            }
        } else { // display time using the device's default 12/24 hour format preference
            tv_time.setText(DateFormat.getTimeFormat(
                    mContext).format(mCalendar.getTimeInMillis()));
        }
    }

    /**
     * <p>Called when the user clicks outside the dialog or presses the <b>Back</b>
     * button.</p>
     * <p/>
     * <p><b>Note:</b> Actual <b>Cancel</b> button clicks are handled by {@code mCancelButton}'s
     * event handler.</p>
     */
    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);

        if (mListener == null) {
            throw new NullPointerException(
                    "Listener no longer exists in onCancel()");
        }

        mListener.onDateTimeCancel();
    }


}
