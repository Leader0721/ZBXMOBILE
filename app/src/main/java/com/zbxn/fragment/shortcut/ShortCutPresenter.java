package com.zbxn.fragment.shortcut;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.zbxn.bean.Member;
import com.zbxn.R;
import com.zbxn.init.http.RequestParams;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.WorkBlog;
import com.zbxn.pub.frame.mvp.AbsBasePresenterOld;
import com.zbxn.pub.frame.mvp.base.RequestResult;
import com.zbxn.pub.http.RequestUtils;
import com.zbxn.utils.BaseModel;

import org.json.JSONObject;

import utils.PreferencesUtils;

/**
 * @author GISirFive
 * @time 2016/8/16
 */
public class ShortCutPresenter extends AbsBasePresenterOld<IShortCutView> {

    private BaseModel mBaseModel;
    private WorkBlog mWorkBlog;

    public ShortCutPresenter(IShortCutView controller) {
        super(controller);
        init();
    }

    @Override
    public void onSuccess(RequestUtils.Code code, JSONObject response, RequestResult result) {
        if (code == RequestUtils.Code.WORKBLOG_CHECKTODAY) {
            mWorkBlog = (WorkBlog) result.obj1;
            Member.get().setBlogStateToday(mWorkBlog == null ? 0 : 1);
        }
    }

    @Override
    public void onFailure(RequestUtils.Code code, JSONObject error, RequestResult result) {
        if (code == RequestUtils.Code.WORKBLOG_CHECKTODAY) {
            Member.get().setBlogStateToday(mWorkBlog == null ? 0 : 1);
        }
    }

    private void init() {
        if (Member.get() != null) {
            if (Member.get().getBlogStateToday() == -1) {
                mBaseModel = new BaseModel(this);
                RequestParams params = new RequestParams();
                //将用户输入的账号保存起来，以便下次使用
                String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
                params.put("tokenid", ssid);
                mBaseModel.post(RequestUtils.Code.WORKBLOG_CHECKTODAY, params);
            }
        }
    }

    /**
     * 查询今天是否已写日志
     *
     * @return
     */
    public void checkBlogToday() {
        Fragment fragment = (Fragment) mController;
        int state = Member.get().getBlogStateToday();
        switch (state) {
            case -1://未检查
                mController.openCreateWorkBlog(null);
                break;
            case 0://未写
                mController.openCreateWorkBlog(null);
                break;
            case 1://已写
                MaterialDialog.Builder builder = new MaterialDialog.Builder(fragment.getActivity())
                        .title("提示").theme(Theme.LIGHT)
                        .content(fragment.getString(R.string.app_main_blogcenter_message))
                        .positiveText("编辑")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                mController.openCreateWorkBlog(mWorkBlog);
                            }
                        })
                        .negativeText("取消");
                builder.show();
                break;
        }
    }

}
