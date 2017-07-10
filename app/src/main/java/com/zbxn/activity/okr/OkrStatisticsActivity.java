package com.zbxn.activity.okr;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.activity.ContactsPicker;
import com.zbxn.activity.main.contacts.ContactsChoseActivity;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.MyToast;
import com.zbxn.pub.widget.ProgressWebView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import utils.PreferencesUtils;
import utils.StringUtils;


/**
 * 项目名称：公司同事积分统计
 * 创建人：wj
 * 创建时间：2016/11/16 9:46
 */
public class OkrStatisticsActivity extends AbsToolbarActivity {


    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.rl_search_people)
    LinearLayout rlSearchPeople;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.tv_workers)
    TextView tvWorkers;
    @BindView(R.id.webview)
    ProgressWebView webview;
    //打开通讯录,返回多选
    private static final int Flag_Callback_Worker = 1002;
    //模式默认为第一条
    private int temp = 0;
    private DatePickerDialog dpDialog;
    private String workers;

    //选择日期
    private String mDate = "";
    private int tempYear;
    private int tempMonth;

    //选择人员
    private ArrayList<Contacts> mListContacts = new ArrayList<>();
    @Override
    public int getMainLayout() {
        return R.layout.activity_okrstatistics;
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDate = new SimpleDateFormat("yyyy-M").format(new Date());
        //    String timeStr = mDate.toString().replace("-", "年").concat("月");
        tempYear = Calendar.getInstance().get(Calendar.YEAR);
        tempMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        String timeStr = "" + tempYear + "年" + tempMonth + "月";
        tvTime.setText(timeStr);
        updateSuccessView();

    }


    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("公司同事积分统计");
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }


    @Override
    public boolean getSwipeBackEnable() {
        return true;
    }


    @OnClick({R.id.tv_time, R.id.tv_type, R.id.rl_search_people, R.id.iv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_time:  //选择时间
                showListLeft();
                break;
            case R.id.tv_type:  //选择模式
                showListRight();

                break;
            case R.id.rl_search_people:
//                startActivityForResult(new Intent(this, ContactsPicker.class), Flag_Callback_Worker);
                Intent intent = new Intent(this, ContactsChoseActivity.class);
                intent.putExtra("list",mListContacts);
                intent.putExtra("type", 1);//0-查看 1-多选 2-单选
                startActivityForResult(intent, Flag_Callback_Worker);
                break;
            case R.id.iv_search:
                openWebView();
                break;
        }
    }

    /**
     * 打开网页
     */
    private void openWebView() {
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);
    //    int userId = Integer.parseInt(PreferencesUtils.getString(this, LoginActivity.FLAG_INPUT_ID));
        if (StringUtils.isEmpty(workers)) {
            MyToast.showToast("请选择查询人员");
            return;
        }
        String url = server + "oaOKRCommonStatMonth/findOKRScore.do?tokenid=" + ssid +
                "&scoreDate=" + mDate + "&userId=" + workers + "&scoreType=" + temp;
        Log.i("", url);
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.setWebViewClient(new MyWebViewClient());
        webview.setVerticalScrollBarEnabled(true);
        webview.loadUrl(url);
    }

    // 监听 所有点击的链接，如果拦截到我们需要的，就跳转到相对应的页面。
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageFinished(view, url);

        }
    }

    private void showListLeft() {
        //    final Calendar cal = Calendar.getInstance();
        dpDialog = new CustomerDatePickerDialog(OkrStatisticsActivity.this, setDateCallBack,
                tempYear, (tempMonth - 1),
                1);
        dpDialog.setTitle("" + tempYear + "年"
                + tempMonth + "月");
        dpDialog.show();
        DatePicker dp = findDatePicker((ViewGroup) dpDialog.getWindow()
                .getDecorView());
        if (dp != null) {
            ((ViewGroup) dp.getChildAt(0)).getChildAt(1).setVisibility(
                    View.GONE);
            ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0))
                    .getChildAt(2).setVisibility(View.GONE);
        }

    }


    /**
     * 右边的选择框
     */
    private void showListRight() {
        final String[] listRight = new String[]{"综合显示", "通用积分", "业务积分"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(listRight, temp, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvType.setText(listRight[which]);
                temp = which;
                dialog.dismiss();
            }
        });
        
        builder.show();
    }

    //从当前Dialog中查找DatePicker子控件

    private DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;
    }

    //自定义年月控件
    class CustomerDatePickerDialog extends DatePickerDialog {

        public CustomerDatePickerDialog(Context context,
                                        OnDateSetListener callBack, int year, int monthOfYear,
                                        int dayOfMonth) {
            super(context, R.style.MyDateDialog, callBack, year, monthOfYear, dayOfMonth);
        }

        @Override
        public void onDateChanged(DatePicker view, int year, int month, int day) {
            //滚动时修改标题时间
            dpDialog.setTitle("" + year + "年" + (month + 1) + "月");
        }
    }

    //日期控件回调函数
    private DatePickerDialog.OnDateSetListener setDateCallBack = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            tempYear = year;
            tempMonth =monthOfYear+1;
            //将选择的时间返回给textview
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            mDate = new SimpleDateFormat("yyyy-M").format(calendar
                    .getTime());
            tvTime.setText(new SimpleDateFormat("yyyy年MM月").format(calendar
                    .getTime()));

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Flag_Callback_Worker) { //执行人
            if (data != null) {
                if (resultCode == RESULT_OK) {
                    mListContacts = (ArrayList<Contacts>) data.getExtras().getSerializable(ContactsPicker.Flag_Output_Checked);
                    String content = "";
                    //所有执行人id字符串
                    workers = new String();
                    for (int i = 0; i < mListContacts.size(); i++) {
                        workers += mListContacts.get(i).getId() + ",";
                        content += mListContacts.get(i).getUserName() + " ";
                    }
                    workers = workers.substring(0,workers.length()-1);
                    if (mListContacts.size() >= 2) {
                        content = mListContacts.get(0).getUserName() + " " + mListContacts.get(1).getUserName() + "等" + mListContacts.size() + "人";
                    }
                    tvWorkers.setText(content.trim());
                }

            } else {
                return;
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}
