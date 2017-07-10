package com.zbxn.pub.frame.mvp;


import android.os.Handler;

import com.zbxn.pub.frame.base.BaseFragment;
import com.zbxn.pub.frame.mvp.base.ControllerCenter;
import com.zbxn.pub.frame.mvp.base.LoadingController;
import com.zbxn.pub.frame.mvp.base.LoadingControllerImp;
import com.zbxn.pub.frame.mvp.base.MessageController;
import com.zbxn.pub.frame.mvp.base.MessageControllerImp;

/**
 * Fragment抽象类
 * Created by GISirFive on 2016/7/26.
 */
public abstract class AbsBaseFragment extends BaseFragment implements
        ControllerCenter, Handler.Callback {

    private LoadingController mLoadingController;
    private MessageController mMessageController;

    @Override
    public LoadingController loading() {
        if (mLoadingController == null)
            mLoadingController = new LoadingControllerImp(getActivity());
        return mLoadingController;
    }

    @Override
    public MessageController message() {
        if (mMessageController == null)
            mMessageController = new MessageControllerImp(getActivity());
        return mMessageController;
    }

}
