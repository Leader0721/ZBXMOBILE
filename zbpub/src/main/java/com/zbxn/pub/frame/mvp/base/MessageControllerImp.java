package com.zbxn.pub.frame.mvp.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.zbxn.pub.frame.base.BaseActivity;

/**
 * Created by GISirFive on 2016/7/26.
 */
public class MessageControllerImp implements MessageController {

    private Activity mActivity;

    public MessageControllerImp(Activity activity) {
        this.mActivity = activity;

        if(mActivity instanceof BaseActivity){
            BaseActivity temp = (BaseActivity) mActivity;
            setContainerView(temp.getToolbarHelper().getRootView());
        }
        else
            setContainerView(mActivity.getWindow().getDecorView());
    }

    private MaterialDialog.Builder mBuilder;
    private Snackbar mSnackbar;

    public Snackbar getSnackbar() {
        if (mSnackbar != null && mSnackbar.isShown())
            mSnackbar.dismiss();
        mSnackbar = Snackbar.make(getContainerView(), "", Snackbar.LENGTH_SHORT);
        return mSnackbar;
    }

    private View mContainerView;

    public View getContainerView() {
        return mContainerView;
    }

    public void setContainerView(View containerView) {
        mContainerView = containerView;
    }

    @Override
    public void show(String content) {
        Snackbar snackbar = getSnackbar();
        snackbar.setText(content);
        snackbar.show();
    }

    @Override
    public void show(String content, int duration) {
        Snackbar snackbar = getSnackbar();
        snackbar.setDuration(duration);
        show(content);
    }

    @Override
    public void showDialog(String content) {
        try{
            getDialogBuilder()
                    .content(content)
                    .show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void showDialog(String title, String content) {
        try{
        getDialogBuilder()
                .title(title)
                .content(content)
                .show();

        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
    }

    @Override
    public void showDialog(String title, String content, final DialogInterface.OnClickListener listener) {
        try {
            MaterialDialog.Builder builder = getDialogBuilder()
                    .title(title)
                    .content(content);
            if (listener != null) {
                builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        listener.onClick(dialog, 0);
                    }
                });
            }
            builder.show();
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
    }

    @Override
    public MaterialDialog.Builder getDialogBuilder() {
        if (mBuilder == null) {
            mBuilder = new MaterialDialog.Builder(mActivity);
            mBuilder.theme(Theme.LIGHT);
            mBuilder.positiveText("确定");
        }
        return mBuilder;
    }

}
