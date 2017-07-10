package com.zbxn.activity.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.umeng.analytics.MobclickAgent;
import com.zbxn.bean.AttendanceRecordEntity;
import com.zbxn.bean.AttendanceRuleTimeEntity;
import com.zbxn.bean.adapter.AttendanceRecordAdapter;
import com.zbxn.listener.ICustomListener;
import com.zbxn.widget.MyListView;
import com.zbxn.R;
import com.zbxn.bean.adapter.AttendanceRuleTimeAdapter;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.frame.common.ToolbarParams;
import com.zbxn.pub.frame.mvp.AbsToolbarActivity;
import com.zbxn.pub.utils.MyToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import utils.StringUtils;

/**
 * 项目名称：考勤 首页
 * 创建人：wuzy
 * 创建时间：2016/9/29 8:24
 */
public class AttendanceActivity extends AbsToolbarActivity implements View.OnClickListener {

    /**
     * 回调处理
     */
    private static final int Flag_Callback_Record = 1002;

    @BindView(R.id.mapview)
    MapView mapview;
    @BindView(R.id.listview)
    MyListView listview;
    @BindView(R.id.listview1)
    MyListView listview1;
    @BindView(R.id.image_location)
    ImageView imageLocation;
    @BindView(R.id.mTime)
    TextView mTime;
    @BindView(R.id.mAddr)
    TextView mAddr;
    @BindView(R.id.mAdd)
    ImageView mAdd;

    private List<AttendanceRuleTimeEntity> mList;
    private AttendanceRuleTimeAdapter mAdapter;

    private List<AttendanceRecordEntity> mList1;
    private AttendanceRecordAdapter mAdapter1;

    private AttendancePresenter mPresenter;

    private MyLocationConfiguration.LocationMode m_CurrentMode;
    private BaiduMap m_BaiduMap = null;

    private double mLatitude = 0.0;
    private double mLongitude = 0.0;
    private String mAddress = "";

    private SimpleDateFormat sfDay = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

    private MyLocationListenner myLocationListenner;

    private String date = "";
    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("考勤");// 定义上面的名字
        return super.onToolbarConfiguration(toolbar, params);
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
    public int getMainLayout() {
        return R.layout.activity_attendance;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        //显示加载成功界面
        updateSuccessView();


        myLocationListenner = new MyLocationListenner();


        initView();
        initData();

        mList = new ArrayList<>();
        mList1 = new ArrayList<>();

        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    @Override
    public void loadData() {
        mPresenter = new AttendancePresenter(this);
        mPresenter.RuleTimeDataList(this, mICustomListener);


        mPresenter = new AttendancePresenter(this);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        date = sf.format(new Date());
        mPresenter.dataList(this, date, mICustomListener);
    }

    /**
     * 创建按钮显示
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_attendancedetails, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 创建按钮监听
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        return true;
    }

    /**
     * 创建按钮的点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mScheduleEdit:
                startActivity(new Intent(this, AttendanceRecordActivity.class));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        m_BaiduMap = mapview.getMap();
        m_BaiduMap.setMyLocationEnabled(true);
        m_BaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
    }

    private void initData() {
        m_CurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        m_BaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(m_CurrentMode, false, null));

        // 开启定位图层
        m_BaiduMap.setMyLocationEnabled(true);

        m_BaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {

            @Override
            public void onMapLoaded() {
                // 设置缩放级别
                MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(17.0f);
                m_BaiduMap.setMapStatus(msu);
            }
        });
        // 启动百度地图获取当前经纬度
        if (((BaseApp) getApplication()).isStartedLocationClient()) {
            ((BaseApp) getApplication()).requestLocationClient(myLocationListenner);
        } else {
            ((BaseApp) getApplication()).startLocationClient(myLocationListenner);
        }

        String date = StringUtils.StringData("");
        mTime.setText(date);
    }

    /**
     * 回调需要接收的
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Flag_Callback_Record) {
            if (resultCode == RESULT_OK) {
                mPresenter.dataList(this, date, mICustomListener);
            } else {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.image_location, R.id.mAdd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_location://标题
                // 启动百度地图获取当前经纬度
                if (((BaseApp) getApplication()).isStartedLocationClient()) {
                    ((BaseApp) getApplication()).requestLocationClient(myLocationListenner);
                } else {
                    ((BaseApp) getApplication()).startLocationClient(myLocationListenner);
                }
                break;
            case R.id.mAdd://
                mPresenter.save(getApplicationContext(), sf.format(new Date()), "", mAddress, mLongitude + "", mLatitude + "", mICustomListener);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((BaseApp) getApplication()).stopLocationClient(myLocationListenner);   //添加这句就行了
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapview.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapview.onResume();
        MobclickAgent.onPageStart("AttendanceActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapview.onPause();
        MobclickAgent.onPageEnd("AttendanceActivity");
        MobclickAgent.onPause(this);
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapview == null)
                return;

            MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
                    // 如果不显示定位精度圈，将accuracy赋值为0即可
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    // .direction(100)
                    .latitude(location.getLatitude()).longitude(location.getLongitude()).build();
            m_BaiduMap.setMyLocationData(locData);

            // 定位
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
            m_BaiduMap.animateMapStatus(u);

            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();
            try {

                mAddress = location.getProvince() + location.getCity() + location.getDistrict() + location.getStreet() + location.getStreetNumber();

            } catch (Exception e) {
                mAddress = location.getAddrStr();
            }

            if (mAddress.equals("nullnullnullnullnull")) {
                mAddress = "";
            }

            mAddr.setText(mAddress);

        }
    }

    private ICustomListener mICustomListener = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            switch (obj0) {
                case 0:
                    List<AttendanceRuleTimeEntity> list1 = (List<AttendanceRuleTimeEntity>) obj1;
                    mList.clear();
                    mList.addAll(list1);
                    mAdapter = new AttendanceRuleTimeAdapter(getApplicationContext(), mList, this);
                    listview.setAdapter(mAdapter);
                    break;
                case 1:
                    AttendanceRecordEntity entity1 = (AttendanceRecordEntity) obj1;

                    Intent intent1 = new Intent(AttendanceActivity.this, GrievanceActivity.class);
                    intent1.putExtra("item", entity1);
                    intent1.putExtra("type", "1");//1迟到  2早退
                    startActivityForResult(intent1, Flag_Callback_Record);
                    break;
                case 2:
                    AttendanceRecordEntity entity = (AttendanceRecordEntity) obj1;

                    Intent intent = new Intent(AttendanceActivity.this, GrievanceActivity.class);
                    intent.putExtra("item", entity);
                    intent.putExtra("type", "2");//1迟到  2早退
                    startActivityForResult(intent, Flag_Callback_Record);
                    break;
                case 3:
                    List<AttendanceRecordEntity> list2 = (List<AttendanceRecordEntity>) obj1;
                    mList1.clear();
                    mList1.addAll(list2);
                    mAdapter1 = new AttendanceRecordAdapter(getApplicationContext(), mList1, this);
                    listview1.setAdapter(mAdapter1);
                    break;
                case 6:
                    MyToast.showToast("签到成功");
                    mPresenter.dataList(getApplicationContext(), date, mICustomListener);
                    break;
            }
        }
    };

}
