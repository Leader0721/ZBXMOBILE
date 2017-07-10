package com.zbxn.pub.frame.mvc;


import android.text.TextUtils;
import android.widget.Toast;

import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.frame.base.BaseFragment;
import com.zbxn.pub.http.ICallback;
import com.zbxn.pub.http.IRequest;
import com.zbxn.pub.http.IRequestParams;
import com.zbxn.pub.http.RequestUtils.Code;

public abstract class AbsBaseFragment extends BaseFragment implements IRequest,
        ICallback {

    /**
     * 消息提示
     */
    private Toast mToast;

    @Override
    public void onStart(Code code) {
    }

    @Override
    public void post(Code code, IRequestParams params) {
        BaseApp.getHttpClient().post(code, params, this);
    }

    @Override
    public void get(Code code, IRequestParams params) {
        post(code, params);
    }

    /**
     * 显示消息提示
     *
     * @param msg      消息内容
     * @param interval 该消息提示在屏幕上停留的时间(毫秒)
     */
    public final void showToast(String msg, int interval) {
        if (TextUtils.isEmpty(msg))
            msg = msg + "";
        if (interval < 0)
            return;
        if (mToast == null) {

            mToast = Toast.makeText(getActivity(), msg, interval);
        } else {
            mToast.cancel();// 关闭吐司显示
            mToast = Toast.makeText(getActivity(), msg, interval);
        }
        mToast.show();

    }

}
