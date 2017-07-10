package com.zbxn.activity.main.tools.advert;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.zbxn.R;
import com.zbxn.bean.AdvertEntity;
import com.zbxn.http.BaseAsyncTask;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.data.ResultData;
import com.zbxn.pub.data.base.BaseAsyncTaskInterface;
import com.zbxn.pub.frame.mvp.AbsBaseFragment;
import com.zbxn.pub.frame.mvp.base.ControllerCenter;
import com.zbxn.pub.frame.mvp.base.LoadingController;
import com.zbxn.pub.frame.mvp.base.MessageController;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.JsonUtil;
import com.zbxn.utils.KEY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import utils.PreferencesUtils;
import utils.ScreenUtils;

/**
 * @author GISirFive
 * @time 2016/8/5
 */
public class AdvertBanner extends AbsBaseFragment implements CBViewHolderCreator<AdvertHolder>, ControllerCenter {

    @BindView(R.id.mBanner)
    ConvenientBanner mBanner;

    private List<String> imgList = new ArrayList<>();

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.main_toolscenter_advertbanner, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {
        // 调整该页面布局大小
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mBanner
                .getLayoutParams();
        layoutParams.width = ScreenUtils.getScreenWidth(getContext());
        layoutParams.height = (int) (layoutParams.width * 9f / 16 * 0.618);//自动适配
        mBanner.setLayoutParams(layoutParams);


        String images = PreferencesUtils.getString(BaseApp.getContext(), KEY.MOBILEIMAGES, "[]");

        List<AdvertEntity> list = JsonUtil.fromJsonList(images, AdvertEntity.class);
        setImages(list);

        getImage(getActivity());
    }

    /**
     * 查看考勤记录
     *
     * @param context
     */
    public void getImage(Context context) {
        Map<String, String> map = new HashMap<String, String>();

        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTask(context, false, 0, server + "/mobilePicture/dataList.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return new ResultData<AdvertEntity>().parse(json, AdvertEntity.class);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                ResultData mResult = (ResultData) result;
                List<AdvertEntity> list = (List<AdvertEntity>) mResult.getRows();

                String json = JsonUtil.toJsonString(list);
                //轮播图列表
                PreferencesUtils.putString(BaseApp.getContext(), KEY.MOBILEIMAGES, json);

                setImages(list);
            }

            @Override
            public void dataError(int funId) {
            }
        }).execute(map);
    }

    /**
     * 显示图片
     *
     * @param list
     */
    private void setImages(List<AdvertEntity> list) {
        imgList.clear();
        for (int i = 0; i < list.size(); i++) {
            imgList.add(list.get(i).getPicturesrc());
        }
        mBanner.setPages(AdvertBanner.this, imgList);
        //修改小点
        mBanner.setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused});
        mBanner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        mBanner.startTurning(3000);
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        mBanner.stopTurning();
    }

    @Override
    public AdvertHolder createHolder() {
        return new AdvertHolder();
    }

    @Override
    public LoadingController loading() {
        return null;
    }

    @Override
    public MessageController message() {
        return null;
    }


}
