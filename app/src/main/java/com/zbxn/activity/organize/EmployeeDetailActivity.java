package com.zbxn.activity.organize;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbxn.R;
import com.zbxn.bean.Member;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.ConfigUtils;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import utils.ScreenUtils;
import utils.StringUtils;
import widget.pulltozoomview.PullToZoomScrollViewEx;

/**
 * 人员详情
 *
 * @author GISirFive
 * @time 2016/8/10
 */
public class EmployeeDetailActivity extends AbsToolbarActivity {

    /**
     * 从其它页面传入的参数{@link Contacts}
     */
    public static final String Flag_Input_Contactor = "contactor";

    private Contacts mContacts;

    @BindView(R.id.mScrollView)
    PullToZoomScrollViewEx mScrollView;
    @BindView(R.id.mPortrait)
    CircleImageView mPortrait;
    @BindView(R.id.mRemarkName)
    TextView mRemarkName;
    @BindView(R.id.mCompanyName)
    TextView mCompanyName;
    @BindView(R.id.mDepartmentName)
    TextView mDepartmentName;
    @BindView(R.id.mPosition)
    TextView mPosition;
    @BindView(R.id.mMobileNumber)
    TextView mMobileNumber;
    @BindView(R.id.mPhoneNumber)
    TextView mPhoneNumber;
    @BindView(R.id.mEmail)
    TextView mEmail;
    @BindView(R.id.mUserNumber)
    TextView mUserNumber;

    @Override
    public int getMainLayout() {
        return R.layout.activity_organize_employee_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContacts = (Contacts) getIntent().getSerializableExtra(Flag_Input_Contactor);

        if (mContacts == null)
            finish();

        init();
        refreshUI();
        updateSuccessView();
    }

    @Override
    public void loadData() {

    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("同事");
        params.overlay = true;
        params.colorResId = R.color.app_colorPrimary;
        params.shadowEnable = false;
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    public boolean getSwipeBackEnable() {
        return true;
    }

    @Override
    public int getTranslucentColorResource() {
        return R.color.app_colorPrimary;
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }


    @OnClick({R.id.mLayoutEmail, R.id.mLayoutMobile, R.id.mLayoutPhone})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mLayoutEmail:
                if (!TextUtils.isEmpty(mContacts.getEmail())) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setType("text/plain");
                        intent.setData(Uri.parse("mailto:" + mContacts.getEmail()));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "邮件标题");
                        intent.putExtra(Intent.EXTRA_TEXT, "邮件内容");
                        startActivity(intent);
                    } catch (Exception e) {
//                        e.printStackTrace();
                        new MaterialDialog.Builder(this).title("提示")
                                .theme(Theme.LIGHT)
                                .content("手机上未安装任何邮件应用")
                                .positiveText("确定")
                                .show();
                    }
                }
                break;
            case R.id.mLayoutMobile:
                if (!TextUtils.isEmpty(mContacts.getLoginname())) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + mContacts.getLoginname());
                    intent.setData(data);
                    startActivity(intent);
//                    showCallDialog(mContacts.getTelephone());
                }
                break;
            case R.id.mLayoutPhone:
                if (!TextUtils.isEmpty(mContacts.getTelephone())) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + mContacts.getTelephone());
                    intent.setData(data);
                    startActivity(intent);
//                    showCallDialog(mContacts.getOfficeTelOut());
                }
                break;
        }
    }

    private void init() {
        // 调整ZoomView的高度
        int screenWidth = ScreenUtils.getScreenWidth(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                screenWidth, (int) (screenWidth * 9 / 16));
        mScrollView.setHeaderLayoutParams(params);

    }

    private void refreshUI() {
        mUserNumber.setText(mContacts.getNumber());//用户号
        mRemarkName.setText(mContacts.getUserName());//姓名   userName
        mCompanyName.setText(Member.get().getZmsCompanyName());//公司名称
        mDepartmentName.setText(mContacts.getDepartmentName());//部门
        mPosition.setText(mContacts.getPositionName());//职位

        if (!StringUtils.isEmpty(mContacts.getLoginname())) {
            mMobileNumber.setText(mContacts.getLoginname());//手机号
        } else {
            mMobileNumber.setText(" ");//手机号

        }
        String extTel = "";
        if (!StringUtils.isEmpty(mContacts.getExtTel())) {
            extTel = "-" + mContacts.getExtTel();
        }
        if (!StringUtils.isEmpty(mContacts.getLoginname())) {
            mPhoneNumber.setText(mContacts.getTelephone() + extTel);//电话
        } else {
            mPhoneNumber.setText("");//电话
        }


        mEmail.setText(mContacts.getEmail());//邮箱
        String mBaseUrl = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.userhead_img)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.userhead_img)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.userhead_img)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                //.displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                .build();                                   // 创建配置过得DisplayImageOption对象
        ImageLoader.getInstance().displayImage(mBaseUrl + mContacts.getPortrait(), mPortrait, options);
    }

    /**
     * 创建按钮显示
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_organize_employee, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 创建按钮监听
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    /**
     * 创建按钮的点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.mEdit:
                intent = new Intent(this, CompileStaffActivity.class);
                intent.putExtra("name", mContacts.getUserName());
                intent.putExtra("id", mContacts.getId() + "");
                intent.putExtra("isActive", mContacts.getIsactive());
                intent.putExtra("gender", mContacts.getGender());
//                intent.putExtra("keyword", mKeyword);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
