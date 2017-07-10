package com.zbxn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zbxn.R;
import com.zbxn.activity.createbulletin.BulletinTypeAdapter;
import com.zbxn.activity.createbulletin.CreateBulletinPresenter;
import com.zbxn.activity.createbulletin.ICreateBulletin;
import com.zbxn.activity.main.contacts.ContactsChoseActivity;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.dialog.ProgressDialog;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.MyToast;
import com.zbxn.widget.slidedatetimepicker.NewSlideDateTimeDialogFragment;
import com.zbxn.widget.slidedatetimepicker.SlideDateTimeListener;
import com.zbxn.widget.slidedatetimepicker.SlideDateTimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import utils.DateUtils;
import utils.StringUtils;

/**
 * 发公告
 *
 * @author GISirFive
 * @time 2016/8/18
 */
public class CreateBulletin extends AbsToolbarActivity implements ICreateBulletin {

    /**
     * 选择接收人回调
     */
    private static final int Flag_Callback_ContactsPicker = 1001;

    @BindView(R.id.mTitle)
    EditText mTitle;
    @BindView(R.id.mContent)
    EditText mContent;
    /*  @BindView(R.id.mCreateBulletin)
      FloatingActionButton mCreateBulletin;*/
    @BindView(R.id.mReceiveUsers)
    TextView mReceiveUsers;
    @BindView(R.id.mSelectReceiveUser)
    LinearLayout mSelectReceiveUser;
    @BindView(R.id.spinner_label)
    Spinner spinnerLabel;
    @BindView(R.id.spinner_type)
    Spinner spinnerType;
    @BindView(R.id.bulletin_stick_true)
    RadioButton bulletinStickTrue;
    @BindView(R.id.bulletin_stick_false)
    RadioButton bulletinStickFalse;
    @BindView(R.id.stick_time)
    TextView stickTime;
    @BindView(R.id.stick_layout)
    LinearLayout mStickLayout;
    @BindView(R.id.bulletin_group)
    RadioGroup mBulletinGroup;
    @BindView(R.id.comment_group)
    RadioGroup commentGroup;
    private MenuItem mCollect;
    //选择人员
    private ArrayList<Contacts> mListContacts = new ArrayList<>();
    private String[] mReceiveArray;//接收人Id数组
    private static final String label[] = {"紧急", "重要", "普通"};
    private static final String type[] = {"热点新闻", "企业动态", "文档中心", "通知", "规章制度"};
    private int isComment = 1;//是否可评论

    private int labels;
    private int types;
    private ProgressDialog mProgressDialog;

    private CreateBulletinPresenter presenter;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public int getMainLayout() {
        return R.layout.activity_createbulletin;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        updateSuccessView();
    }

    @Override
    public void loadData() {

    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("发布公告");
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notice, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        mCollect = menu.findItem(R.id.mNotice);
        mCollect.setEnabled(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item == mCollect) {
            presenter.toSubmit();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean handleMessage(Message msg) {
        //showAddButton();
        return false;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Flag_Callback_ContactsPicker) {
            if (resultCode == RESULT_OK) {
//                List<Contacts> list = data.getParcelableArrayListExtra(ContactsPicker.Flag_Output_Checked);
                mListContacts = (ArrayList<Contacts>) data.getExtras().getSerializable(ContactsPicker.Flag_Output_Checked);
                mReceiveArray = new String[mListContacts.size()];
                String content = "";
                for (int i = 0; i < mListContacts.size(); i++) {
                    mReceiveArray[i] = mListContacts.get(i).getId() + "";
                    content += mListContacts.get(i).getUserName() + ",";
                }
                content = content.substring(0, content.length() - 1);
                mReceiveUsers.setText(content);
            } else {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({/*R.id.mCreateBulletin,*/ R.id.mSelectReceiveUser, R.id.stick_time})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mSelectReceiveUser:
                Intent intent = new Intent(this, ContactsChoseActivity.class);
                intent.putExtra("list",mListContacts);
                intent.putExtra("type",1);
                startActivityForResult(intent, Flag_Callback_ContactsPicker);
                break;
           /* case R.id.mCreateBulletin:
                mProgressDialog.setCancelable(false);
                mProgressDialog.show("正在提交...");
                presenter.toSubmit();
                break;*/
            case R.id.stick_time:
                String time = StringUtils.getEditText(stickTime);
                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(new SlideDateTimeListener() {
                            @Override
                            public void onDateTimeSet(Date date) {
                                stickTime.setText(format.format(date));
                            }
                        })
                        .setInitialDate(StringUtils.convertToDate(format, time))
                        .setIs24HourTime(true)
                        .setIsHaveTime(NewSlideDateTimeDialogFragment.Have_Date)
                        .build()
                        .show();
                break;
        }
    }

    private void init() {
        String date = DateUtils.getDate("yyyy-MM-dd");
        stickTime.setText(date);
        mProgressDialog = new ProgressDialog(this);
        presenter = new CreateBulletinPresenter(this);
        spinnerLabel.setAdapter(new BulletinTypeAdapter(this, label));

        mBulletinGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.bulletin_stick_true:
                        mStickLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.bulletin_stick_false:
                        mStickLayout.setVisibility(View.GONE);
                        break;
                }
            }
        });
        commentGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.bulletin_comment_true:
                        isComment = 1;
                        break;
                    case R.id.bulletin_comment_false:
                        isComment = 0;
                        break;
                }
            }
        });

        spinnerType.setAdapter(new BulletinTypeAdapter(this, type));
        spinnerLabel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                labels = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                types = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sendMessageDelayed(100, 1000);
    }

  /*  private void showAddButton() {
        mCreateBulletin.setVisibility(View.VISIBLE);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(mCreateBulletin, "rotation", 360, 0),
                ObjectAnimator.ofFloat(mCreateBulletin, "scaleX", 0, 1),
                ObjectAnimator.ofFloat(mCreateBulletin, "scaleY", 0, 1));
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.setDuration(300);
        animatorSet.start();
    }
*/

    @Override
    public String getBulletinTitle() {
        return mTitle.getText().toString();
    }

    @Override
    public String getBulletinContent() {
        return mContent.getText().toString();
    }

    @Override
    public int getLabel() {
        return labels;
    }

    @Override
    public int getType() {
        return types;
    }

    @Override
    public String getAttachmentguid() {
        return null;
    }

    @Override
    public int getIsTop() {
        if (bulletinStickTrue.isChecked()) {
            return 1;
        }
        return 0;
    }

    @Override
    public String[] getPersons() {
        if (mReceiveArray == null) {
            return null;
        }
        return mReceiveArray;
    }

    @Override
    public void finishForResult(boolean b) {
        if (b) {
            Toast.makeText(this, "发布成功", Toast.LENGTH_SHORT).show();
            finish();
        } else {
//            message().show("发布失败");
            MyToast.showToast("发布失败");
        }
    }

    @Override
    public String getTopTime() {
        if (getIsTop() == 1) {
            return StringUtils.getEditText(stickTime);
        }
        return null;
    }

    @Override
    public int getAllowComment() {
        return isComment;
    }

}
