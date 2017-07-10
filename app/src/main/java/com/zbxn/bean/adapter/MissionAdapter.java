package com.zbxn.bean.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbxn.R;
import com.zbxn.activity.login.LoginActivity;
import com.zbxn.bean.MissionEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import utils.PreferencesUtils;
import utils.StringUtils;

/**
 * Created by ysj on 2016/11/8.
 */
public class MissionAdapter extends BaseAdapter {

    private Context mCxontext;
    private List<MissionEntity> mList;
    private ItemCallBack callBack;

    //按钮标识
    private boolean isAgree;//true:同意 false:通过
    private boolean isStop;//true:拒绝 false:驳回
    private boolean isCommit;//true:提交审核 false:已完成
    private boolean isTrunDownPerson;//是否为监督人

    private String leadId;//负责人id
    private String executeIds;//执行人id（多个）
    private String checkId;//审核人id
    private String creatId;//创建人id

    private String loginId;
    private int mState;
    private int mStatePerson;

    /**
     * @param context
     * @param list
     */
    public MissionAdapter(Context context, List list, ItemCallBack callBack) {
        this.mCxontext = context;
        this.mList = list;
        this.loginId = PreferencesUtils.getString(mCxontext, LoginActivity.FLAG_INPUT_ID);
        this.callBack = callBack;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mCxontext, R.layout.list_item_missionlist, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final MissionEntity entity = mList.get(position);

        leadId = entity.getPersonLeadingId() + "";//负责人id
        executeIds = entity.getPersonExecuteIds();//执行人id（多个）
        checkId = entity.getPersonCheckId() + "";//审核人id
        creatId = entity.getPersonCreateId() + "";//创建人id
        isTrunDownPerson = entity.isTaskTrunDownPerson();//是否为监督人

        holder.mLeadName.setText(entity.getPersonLeadingName());
        holder.mCheckName.setText(entity.getPersonCheckNames());
        holder.mExecuteName.setText(entity.getPersonExecuteNames());

        holder.mName.setText(entity.getPersonCreateName());//创建人姓名
        holder.missionProgress.setText("完成进度:" + entity.getDoneProgress() + "%");//完成进度
        holder.creatTime.setText(entity.getCreateTime().substring(0, 10));//创建时间
        if (!StringUtils.isEmpty(entity.getEndTime())) {
            holder.endTime.setText(getWeekData(entity.getEndTime()));//限期
        } else {
//            holder.endTime.setText("[ 限期:" + " ]");//限期
        }
        holder.mContent.setText(entity.getTaskTitle());//内容
        if (entity.getDifficultyLevel() == 0) { //普通任务
            holder.missionLevel.setText("普通");//任务级别
        } else if (entity.getDifficultyLevel() == 1) { // 困难任务
            holder.missionLevel.setText("困难");//任务级别
        }
        mState = entity.getTaskState();
        mStatePerson = entity.getPersonTaskState();
        //任务状态
        final String taskState;
        switch (entity.getTaskState()) {
            case 0://待接受
                taskState = "待接受";
                holder.mState.setTextColor(Color.parseColor("#ec6945"));
                break;
            case 1://进行中
                taskState = "进行中";
                holder.mState.setTextColor(Color.parseColor("#ec6945"));
                break;
            case 2://待审核
                taskState = "待审核";
                holder.mState.setTextColor(Color.parseColor("#ec6945"));
                break;
            case 3://已完成
                taskState = "已完成";
                holder.mState.setTextColor(mCxontext.getResources().getColor(R.color.tvc3));
                break;
            case 4://已拒绝
                taskState = "已拒绝";
                holder.mState.setTextColor(mCxontext.getResources().getColor(R.color.tvc9));
                break;
            case 5://已驳回
                taskState = "已驳回";
                holder.mState.setTextColor(mCxontext.getResources().getColor(R.color.tvc9));
                break;
            case 6://已作废
                taskState = "已延期";
                holder.mState.setTextColor(mCxontext.getResources().getColor(R.color.tvc9));
                break;
            case 7:
                taskState = "已超期";
                holder.mState.setTextColor(mCxontext.getResources().getColor(R.color.tvc3));
                break;
            case 8:
                taskState = "进行中";
                holder.mState.setTextColor(Color.parseColor("#ec6945"));
                break;
            default:
                taskState = "任务状态";
                break;
        }
        holder.mState.setText(taskState);//状态
        initView(holder);

        //按钮点击事件回调
        //评论
        holder.mComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onTextViewClick(v, position, entity.getTaskState(), entity.getPersonTaskState(), false);
            }
        });
        //已完成 or 提交审核
        holder.mComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.getEditText(holder.mComplete).equals("提交审核")) {
                    isCommit = true;
                } else {
                    isCommit = false;
                }
                callBack.onTextViewClick(v, position, entity.getTaskState(), entity.getPersonTaskState(), isCommit);
            }
        });
        //接收 or 同意
        holder.mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.getEditText(holder.mAccept).equals("接受")) {
                    isAgree = false;
                } else {
                    isAgree = true;
                }
                callBack.onTextViewClick(v, position, entity.getTaskState(), entity.getPersonTaskState(), isAgree);
            }
        });
        //拒绝 or 驳回
        holder.mRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.getEditText(holder.mRefuse).equals("拒绝")) {
                    isStop = true;
                } else {
                    isStop = false;
                }
                callBack.onTextViewClick(v, position, entity.getTaskState(), entity.getPersonTaskState(), isStop);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.mName)
        TextView mName;
        @BindView(R.id.mission_level)
        TextView missionLevel;
        @BindView(R.id.mission_progress)
        TextView missionProgress;
        @BindView(R.id.creat_time)
        TextView creatTime;
        @BindView(R.id.end_time)
        TextView endTime;
        @BindView(R.id.mContent)
        TextView mContent;
        @BindView(R.id.mState)
        TextView mState;
        @BindView(R.id.mExecuteName)
        TextView mExecuteName;
        @BindView(R.id.mLeadName)
        TextView mLeadName;
        @BindView(R.id.mCheckName)
        TextView mCheckName;
        @BindView(R.id.mComment)
        TextView mComment;
        @BindView(R.id.mRefuse_view)
        View mRefuseView;
        @BindView(R.id.mRefuse)
        TextView mRefuse;
        @BindView(R.id.mComplete_view)
        View mCompleteView;
        @BindView(R.id.mComplete)
        TextView mComplete;
        @BindView(R.id.mAccept_view)
        View mAcceptView;
        @BindView(R.id.mAccept)
        TextView mAccept;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 是否为创建人
     *
     * @return
     */
    public boolean isCreatPerson() {
        if (!StringUtils.isEmpty(creatId)) {
            if (creatId.equals(loginId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否为负责人
     *
     * @return
     */
    public boolean isLeadPerson() {
        if (!StringUtils.isEmpty(leadId)) {
            if (leadId.equals(loginId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否为执行人
     *
     * @return
     */
    public boolean isExecutePerson() {
        if (!StringUtils.isEmpty(executeIds)) {
            String[] array = executeIds.split(",");
            for (int i = 0; i < array.length; i++) {
                if (array[i].equals(loginId)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 执行人是否只有一个
     *
     * @return
     */
    public boolean isExecuteOne() {
        if (!StringUtils.isEmpty(executeIds)) {
            String[] array = executeIds.split(",");
            List<String> list = new ArrayList<>();
            for (int i = 0; i < array.length; i++) {
                if (!StringUtils.isEmpty(array[i])) {
                    list.add(array[i]);
                }
            }
            if (list.size() == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否为审核人
     *
     * @return
     */
    public boolean isCheckPerson() {
        if (!StringUtils.isEmpty(checkId)) {
            if (checkId.equals(loginId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 负责人与执行人是否相同
     *
     * @return
     */
    public boolean isAlike() {
        if (!StringUtils.isEmpty(leadId) && !StringUtils.isEmpty(executeIds)) {
            if (leadId.equals(executeIds)) {
                return true;
            }
        }
        return false;
    }

    //初始化下方的按钮
    public void initView(ViewHolder holder) {
        hideView(holder);

        //待接受的任务
        if (mState == 0) {

            //如果是负责人
            if (isLeadPerson()) {
                hideView(holder);
                holder.mComplete.setVisibility(View.VISIBLE);
                holder.mCompleteView.setVisibility(View.VISIBLE);
                holder.mComplete.setText("提交审核");
                isCommit = true;
            }

            if (isExecutePerson()) {
                //个人待接受
                if (mStatePerson == 10) {
                    hideView(holder);
                    holder.mAccept.setVisibility(View.VISIBLE);
                    holder.mAcceptView.setVisibility(View.VISIBLE);
                    holder.mAccept.setText("接受");
                    holder.mRefuse.setVisibility(View.VISIBLE);
                    holder.mRefuseView.setVisibility(View.VISIBLE);
                    holder.mRefuse.setText("拒绝");
                    isAgree = false;
                    isStop = true;
                }

                //个人已接受
                if (mStatePerson == 11) {
                    hideView(holder);
                    if (mState == 1) {
                        holder.mComplete.setVisibility(View.VISIBLE);
                        holder.mCompleteView.setVisibility(View.VISIBLE);
                        holder.mComplete.setText("已完成");
                        isCommit = false;
                    }
                }

                //个人进行中
                if (mStatePerson == 12) {
                    hideView(holder);
                    holder.mComplete.setVisibility(View.VISIBLE);
                    holder.mCompleteView.setVisibility(View.VISIBLE);
                    holder.mComplete.setText("已完成");
                    isCommit = false;
                }

                //个人已完成
                if (mStatePerson == 13) {
                    hideView(holder);
                }

                //个人已拒绝
                if (mStatePerson == 14) {
                    hideView(holder);
                    holder.mAccept.setVisibility(View.VISIBLE);
                    holder.mAcceptView.setVisibility(View.VISIBLE);
                    holder.mAccept.setText("接受");
                    isAgree = false;
                }
            }
        } else if (mState == 1) {//进行中的任务

            //如果是负责人
            if (isLeadPerson()) {
                holder.mComplete.setVisibility(View.VISIBLE);
                holder.mCompleteView.setVisibility(View.VISIBLE);
                holder.mComplete.setText("提交审核");
                isCommit = true;
            }

            if (isExecutePerson()) {
                //个人待接受
                if (mStatePerson == 10) {
                    hideView(holder);
                    holder.mAccept.setVisibility(View.VISIBLE);
                    holder.mAcceptView.setVisibility(View.VISIBLE);
                    holder.mAccept.setText("接受");
                    holder.mRefuse.setVisibility(View.VISIBLE);
                    holder.mRefuseView.setVisibility(View.VISIBLE);
                    holder.mRefuse.setText("拒绝");
                    isAgree = false;
                    isStop = true;
                }

                //个人已接受
                if (mStatePerson == 11) {
                    hideView(holder);
                    holder.mComplete.setVisibility(View.VISIBLE);
                    holder.mCompleteView.setVisibility(View.VISIBLE);
                    holder.mComplete.setText("已完成");
                    isCommit = false;
                }

                //个人进行中
                if (mStatePerson == 12) {
                    hideView(holder);
                    holder.mComplete.setVisibility(View.VISIBLE);
                    holder.mCompleteView.setVisibility(View.VISIBLE);
                    holder.mComplete.setText("已完成");
                    isCommit = false;
                }

                //个人已完成
                if (mStatePerson == 13) {
                    hideView(holder);
                }

                //个人已拒绝
                if (mStatePerson == 14) {
                    hideView(holder);
                    holder.mAccept.setVisibility(View.VISIBLE);
                    holder.mAcceptView.setVisibility(View.VISIBLE);
                    holder.mAccept.setText("接受");
                    isAgree = false;
                }
            }
        } else if (mState == 2) {//待审核的任务
            //如果是审核人
            if (isCheckPerson()) {
                holder.mAccept.setVisibility(View.VISIBLE);
                holder.mAcceptView.setVisibility(View.VISIBLE);
                holder.mAccept.setText("同意");
                holder.mRefuse.setVisibility(View.VISIBLE);
                holder.mRefuseView.setVisibility(View.VISIBLE);
                holder.mRefuse.setText("驳回");
                isAgree = true;
                isStop = false;
            }
        } else if (mState == 8) {//执行人已完成

            //如果是负责人
            if (isLeadPerson()) {
                hideView(holder);
                holder.mComplete.setVisibility(View.VISIBLE);
                holder.mCompleteView.setVisibility(View.VISIBLE);
                holder.mComplete.setText("提交审核");
                isCommit = true;
            }
        } else if (mState == 3) {
            if (isTrunDownPerson) {
                hideView(holder);
                holder.mRefuse.setVisibility(View.VISIBLE);
                holder.mRefuseView.setVisibility(View.VISIBLE);
                holder.mRefuse.setText("驳回");
                isStop = false;
            }
        } else {
            hideView(holder);
        }
    }

    /**
     * 隐藏按钮
     */
    public void hideView(ViewHolder holder) {
        holder.mAccept.setVisibility(View.GONE);
        holder.mAcceptView.setVisibility(View.GONE);
        holder.mComplete.setVisibility(View.GONE);
        holder.mCompleteView.setVisibility(View.GONE);
        holder.mRefuse.setVisibility(View.GONE);
        holder.mRefuseView.setVisibility(View.GONE);
    }

    //按钮点击回调
    public interface ItemCallBack {
        void onTextViewClick(View view, int position, int taskState, int taskPersonState, boolean isType);
    }

    /**
     * 加了星期的时间
     *
     * @param time 格式：yyyy-MM-dd HH:mm:ss
     * @return 格式：yyyy-MM-dd 星期 HH:mm:ss
     */
    public String getWeekData(String time) {
//        2016-11-29 11:44:12
        String weekDay = null;
        String day = null;
        String[] days = new String[]{"日", "一", "二", "三", "四", "五", "六"};
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 输入的日期格式必须是这种
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        Date date = null;// 把字符串转化成日期
        try {
            date = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        weekDay = "星期" + days[date.getDay()];
        String yearMonthDay = sdf1.format(date);
        String hourMinute = sdf2.format(date);
        day = yearMonthDay + " " + weekDay + " " + hourMinute;
        return day;
    }

}
