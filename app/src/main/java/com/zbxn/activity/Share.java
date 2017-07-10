package com.zbxn.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.zbxn.R;
import com.zbxn.bean.Member;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的二维码
 */
public class Share extends AbsToolbarActivity {

    @BindView(R.id.mQRCode)//二维码
            ImageView mQRCode;
    @BindView(R.id.mShareAll)//分享QQ
            RelativeLayout mShareAll;
    @BindView(R.id.mSharePic)//分享微信
            RelativeLayout mSharePic;

    private SHARE_MEDIA share_media = SHARE_MEDIA.ALIPAY;

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("我的二维码");
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    public int getMainLayout() {
        return R.layout.activity_share;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //友盟分享 适配android6.0
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }

        updateSuccessView();
    }

    @Override
    public void loadData() {

    }

    /**
     * 滑动关闭当前Activitiv
     */
    @Override
    public boolean getSwipeBackEnable() {
//        return super.getSwipeBackEnable();
        return true;
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @OnClick({R.id.mShareAll, R.id.mSharePic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mShareAll:
                new ShareAction(Share.this)
                        .withTitle("五的N次方")
                        .withText("享受工作的每一分")
                        .withMedia(new UMImage(Share.this, R.mipmap.ic_launcher))
                        .withTargetUrl("http://n.zbzbx.com/Login/Login/Register?SharerNO=" + Member.get().getNumber())
                        .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setCallback(umShareListener).open();

//                message().show("功能建设中...");
//                new ShareAction(Share.this).setPlatform(SHARE_MEDIA.QQ)
//                        .withTitle("五的N次方")
//                        .withText("享受工作的每一分")
//                        .withMedia(new UMImage(Share.this, R.mipmap.ic_launcher))
//                        .withTargetUrl("http://n.zbzbx.com/Login/Login/Register?SharerNO=" + Member.get().getNumber())
//                        .setCallback(umShareListener)
//                        .share();
                break;
            case R.id.mSharePic://设置微信朋友圈分享内容
                //http://www.zbzbx.com/appdownload/AndroidIOSDownload.html
                UMImage image = new UMImage(Share.this, R.mipmap.zmes);//资源文件
                new ShareAction(Share.this)
                        .withTitle("五的N次方")
                        .withText("享受工作的每一分")
                        .withMedia(image)
                        .setDisplayList(SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setCallback(umShareListener).open();

//                message().show("功能建设中...");
//                new ShareAction(Share.this).setPlatform(SHARE_MEDIA.WEIXIN)
//                        .withTitle("五的N次方")
//                        .withText("享受工作的每一分")
//                        .withMedia(new UMImage(Share.this, R.mipmap.ic_launcher))
//                        .withTargetUrl("http://n.zbzbx.com/Login/Login/Register?SharerNO=" + Member.get().getNumber())
//                        .setCallback(umShareListener)
//                        .share();
                break;
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);

            Toast.makeText(Share.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(Share.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(Share.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
            Log.e("cancael", "platform" + platform);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        Log.d("result", "onActivityResult");
    }


}
