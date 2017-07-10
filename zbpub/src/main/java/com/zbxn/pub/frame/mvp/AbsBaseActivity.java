package com.zbxn.pub.frame.mvp;

import com.zbxn.pub.frame.base.BaseActivity;
import com.zbxn.pub.frame.mvp.base.ControllerCenter;
import com.zbxn.pub.frame.mvp.base.LoadingController;
import com.zbxn.pub.frame.mvp.base.LoadingControllerImp;
import com.zbxn.pub.frame.mvp.base.MessageController;
import com.zbxn.pub.frame.mvp.base.MessageControllerImp;

/**
 * Activity抽象类，MVP中的V层
 * Created by GISirFive on 2016/7/26.
 */
public abstract class AbsBaseActivity extends BaseActivity implements ControllerCenter{

    private LoadingController mLoadingController;
    private MessageController mMessageController;

    @Override
    public LoadingController loading() {
        if (mLoadingController == null)
            mLoadingController = new LoadingControllerImp(this);
        return mLoadingController;
    }

    @Override
    public MessageController message() {
        if (mMessageController == null)
            mMessageController = new MessageControllerImp(this);
        return mMessageController;
    }

    @Override
    protected void onDestroy() {
        mMessageController = null;
        super.onDestroy();
    }
}