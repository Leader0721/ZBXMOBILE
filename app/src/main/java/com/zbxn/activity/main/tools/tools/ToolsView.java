package com.zbxn.activity.main.tools.tools;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.zbxn.R;
import com.zbxn.activity.CollectCenter;
import com.zbxn.activity.WorkBlogCenter;
import com.zbxn.activity.attendance.AttendanceActivity;
import com.zbxn.activity.examinationandapproval.ApplyActivity;
import com.zbxn.activity.mission.MissionListActivity;
import com.zbxn.activity.okr.OkrActivity;
import com.zbxn.activity.okr.OkrRankingActivity;
import com.zbxn.activity.okr.OkrStatisticsActivity;
import com.zbxn.activity.schedule.ScheduleActivity;
import com.zbxn.pub.frame.mvp.AbsBaseFragment;
import com.zbxn.pub.utils.JsonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import utils.ScreenUtils;
import utils.StringUtils;

/**
 * @author GISirFive
 * @time 2016/8/5
 */
public class ToolsView extends AbsBaseFragment implements IToolsView, AdapterView.OnItemClickListener {
    @BindView(R.id.mGridView)
    GridView mGridView;

    private ToolsAdapter mToolsAdapter;
//    private ApprovalPresenter mToolsPresenter;
//    private ToolsPresenter mToolsPresenter;

    private List<RecTool> listTemp = new ArrayList<>();
    private String toolsList = "[{\"id\":1,\"title\":\"日志\",\"menuid\":112,\"img\":\"temp112\",\"isvisible\":true}," +
            "{\"id\":2,\"title\":\"考勤\",\"menuid\":117,\"img\":\"temp117\",\"isvisible\":true}," +
            "{\"id\":7,\"title\":\"日程\",\"menuid\":116,\"img\":\"temp116\",\"isvisible\":true}," +
            "{\"id\":10,\"title\":\"审批\",\"menuid\":120,\"img\":\"temp120\",\"isvisible\":true}," +
            "{\"id\":3,\"title\":\"OKR\",\"menuid\":113,\"img\":\"temp113\",\"isvisible\":true}," +
            "{\"id\":9,\"title\":\"任务\",\"menuid\":119,\"img\":\"temp119\",\"isvisible\":true}," +
            "{\"id\":16,\"title\":\"预告\",\"menuid\":126,\"img\":\"temp126\",\"isvisible\":true}," +
            "{\"id\":1622,\"title\":\"\",\"menuid\":13326,\"img\":\"\",\"isvisible\":true}]";


    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.main_toolscenter_toolsview, container, false);
        ButterKnife.bind(this, root);
//        initView();
        return root;
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {
        mToolsAdapter = new ToolsAdapter(getActivity(), null);
        mGridView.setAdapter(mToolsAdapter);
        mGridView.setOnItemClickListener(this);
        mGridView.setFocusable(false);
//        mToolsPresenter = new ToolsPresenter(this);
//        mToolsPresenter.getToolsByAuthor();
//        mToolsPresenter = new ApprovalPresenter(this);

        // 保存rows到本地
//        String toolsList = PreferencesUtils.getString(BaseApp.CONTEXT, "toolsList", "[]");
        try {
            listTemp = JsonUtil.fromJsonList(toolsList, RecTool.class);
            if (!StringUtils.isEmpty(listTemp)) {
                resetData(listTemp);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public void resetData(List<RecTool> list) {
        listTemp = list;
        int size = list.size() + 1;
        int line = size / 4;//一共有几行
        if (size % 4 > 0)
            line += 1;

        int height = ScreenUtils.dipToPx(getContext(), line * 96);
        ViewGroup.LayoutParams params = mGridView.getLayoutParams();
        params.height = height;
        mGridView.setLayoutParams(params);

        mToolsAdapter.resetData(list);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            String menuId = listTemp.get(position).getMenuid();
            switch (menuId) {
                case "112"://日志
                    startActivity(new Intent(getActivity(), WorkBlogCenter.class));
                    break;
                case "113"://OKR
                    startActivity(new Intent(getActivity(), OkrRankingActivity.class));
                    break;
                case "115"://收藏
                    startActivity(new Intent(getActivity(), CollectCenter.class));
                    break;
                case "116"://日程
                    startActivity(new Intent(getActivity(), ScheduleActivity.class));
                    break;
                case "117"://考勤
                    startActivity(new Intent(getActivity(), AttendanceActivity.class));
                    break;
                case "119"://任务
                    startActivity(new Intent(getActivity(), MissionListActivity.class));
                    break;
                case "120"://审批
                    startActivity(new Intent(getActivity(), ApplyActivity.class));
                    break;
                case "126"://预告
                    startActivity(new Intent(getActivity(), TrailerActivity.class));
                    break;
                default:
                    break;

            }
        } catch (Exception e) {

        }
    }
}
