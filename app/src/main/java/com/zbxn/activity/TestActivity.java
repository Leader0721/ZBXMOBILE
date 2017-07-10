package com.zbxn.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.widget.ImageView;

import com.zbar.lib.QRCodeUtil;
import com.zbxn.R;
import com.zbxn.bean.Tests;
import com.zbxn.pub.frame.mvc.AbsBaseActivity;
import com.zbxn.pub.http.RequestUtils.Code;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import widget.treelistview.TreeListViewAdapter;

public class TestActivity extends AbsBaseActivity {

//    @BindView(R.id.testList)
//    ListView testList;

    private TreeListViewAdapter mAdapter;
    private List<Tests> mDatas = new ArrayList<>();

    @BindView(R.id.eQma)
    ImageView eQma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Bitmap bitmap = QRCodeUtil.createQRImage("http://imtt.dd.qq.com/16891/1393A21ABED0316ABD0877AD3353EA18.apk?fsname=com.zbxn_1.0.16_10016.apk&csr=4d5s", 600, 600, logo);
        eQma.setImageBitmap(bitmap);
//        initData();
//        init();
    }

    private void initData() {
        mDatas.add(new Tests(1, 0, "研发部"));
        mDatas.add(new Tests(2, 1, "济南研发部"));
        mDatas.add(new Tests(3, 1, "淄博研发部"));
        mDatas.add(new Tests(4, 2, "开发五部"));
        mDatas.add(new Tests(12, 4, "Android开发"));
        mDatas.add(new Tests(13, 4, "ios开发"));

        mDatas.add(new Tests(11, 3, "开发X部"));
        mDatas.add(new Tests(14, 11, "开发XX部"));

        mDatas.add(new Tests(5, 0, "产品部"));
        mDatas.add(new Tests(9, 5, "产品一部"));
        mDatas.add(new Tests(10, 5, "产品二部"));
        mDatas.add(new Tests(6, 0, "行政部"));
        mDatas.add(new Tests(7, 6, "行政一部"));
        mDatas.add(new Tests(8, 6, "行政一部"));
    }

//    private void init() {
//        try {
////            mAdapter = new SimpleTreeAdapter<Tests>(testList, this, mDatas, 10);
//            mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
//                @Override
//                public void onClick(Node node, int i) {
//                    if (node.isLeaf()) {
//                        showToast(node.getName(), Toast.LENGTH_SHORT);
//                    }
//                }
//            });
//            testList.setAdapter(mAdapter);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public void onSuccess(Code code, JSONObject response) {

    }

    @Override
    public void onFailure(Code code, JSONObject error) {

    }


}
