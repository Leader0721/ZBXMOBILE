package com.zbxn.activity.main.contacts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.zbxn.R;
import com.zbxn.activity.ContactsDetail;
import com.zbxn.bean.Member;
import com.zbxn.bean.adapter.ContactsPeopleAdapter;
import com.zbxn.init.eventbus.EventCustom;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.frame.mvp.AbsBaseFragment;
import com.zbxn.utils.KeyEvent;
import com.zbxn.widget.MyLetterListView;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 通讯录按拼音排序
 *
 * @author GISirFive
 * @time 2016/8/8
 */
public class ContactsPeopleFragment extends AbsBaseFragment {

    @BindView(R.id.mListView)
    ListView mListView;
    @BindView(R.id.mMyLetterListView)
    MyLetterListView mMyLetterListView;
    @BindView(R.id.mLayout)
    RelativeLayout mLayout;

    private View mLayoutOverlay;
    private TextView mOverlay;

    private Activity mContext;

    //    private BrandListAdapter adapter;
    private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
    private String[] sections;// 存放存在的汉语拼音首字母
    private Handler handler;
    private OverlayThread overlayThread;
    private List<Contacts> lists;

    private ContactsPeopleAdapter adapter;

    private int mPosition = -1;

    //0-查看 1-多选 2-单选
    private int mType;

    public void setFragmentTitle(String title) {
        mTitle = title;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_contacts_people, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {

        mType = getArguments().getInt("type", 0);

        mContext = getActivity();

        try {
            lists = BaseApp.DBLoader.findAll(Selector.from(Contacts.class).where("isDepartment", "=", null).orderBy("captialChar"));//

            Contacts contacts = new Contacts();
            contacts.setCaptialChar("#");
            contacts.setId(-1000);
            contacts.setDepartmentName("按组织架构");
            lists.add(0, contacts);
        } catch (DbException e) {
            e.printStackTrace();
        }

//        String[] b = {"A", "B"};
//        mMyLetterListView.setWord(b);

        //设置选中数据
        setData();

        mMyLetterListView.setOnTouchingLetterChangedListener(new LetterListViewListener());
        alphaIndexer = new HashMap<String, Integer>();
        handler = new Handler();
        overlayThread = new OverlayThread();
        initOverlay();

        alphaIndexer = new HashMap<String, Integer>();
        sections = new String[lists.size()];
        adapter = new ContactsPeopleAdapter(mContext, lists, listener, alphaIndexer, sections, true, mType);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if (lists.get(position).getId() == -1000) {
                    intent = new Intent(getActivity(), ContactsOrganizeActivity.class);
                    intent.putExtra("type", mType);
                    startActivity(intent);
                    return;
                }
                if (mType == 0) {
                    //判断当前点击的用户是否为登录用户
                    Contacts contacts = (Contacts) mListView.getAdapter().getItem(position);
                    int itemId = contacts.getId();
                    int memberId = Member.get().getId();
                    if (memberId == itemId) {
                        return;
                    }
                    intent = new Intent(getActivity(), ContactsDetail.class);
                    intent.putExtra(ContactsDetail.Flag_Input_Contactor, contacts);
                    startActivity(intent);
                } else if (mType == 1) {
                } else if (mType == 2) {
                }
            }
        });
    }

    private ICustomListener listener = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            mPosition = position;
            Contacts result = (Contacts) obj1;
            switch (obj0) {
                case 0:
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }


    // 初始化汉语拼音首字母弹出提示框
    private void initOverlay() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mLayoutOverlay = (View) inflater.inflate(R.layout.view_letterlist_overlay, null);
        mOverlay = (TextView) mLayoutOverlay.findViewById(R.id.mText);
        mLayoutOverlay.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(mLayoutOverlay, lp);
    }

    private class LetterListViewListener implements MyLetterListView.OnTouchingLetterChangedListener {

        @Override
        public void onTouchingLetterChanged(final String s) {
            if (alphaIndexer.get(s) != null) {
                int position = alphaIndexer.get(s);
                mListView.setSelection(position);
                mOverlay.setText(sections[position]);
                mLayoutOverlay.setVisibility(View.VISIBLE);
                handler.removeCallbacks(overlayThread);
                // 延迟一秒后执行，让overlay为不可见
                handler.postDelayed(overlayThread, 1000);
            }
        }

    }

    // 设置overlay不可见
    private class OverlayThread implements Runnable {

        @Override
        public void run() {
            mLayoutOverlay.setVisibility(View.GONE);
        }
    }

    @Subscriber
    public void onEventMainThread(EventCustom eventCustom) {
        if (KeyEvent.UPDATECONTACTSSELECT.equals(eventCustom.getTag())) {
            //取得map中所有的key和value
            /*for (Map.Entry<String, String> entry : ContactsChoseActivity.mHashMap.entrySet()) {
                contacts = new Contacts();
                String key = entry.getKey();
                String value = entry.getValue();
                contacts.setId(Integer.parseInt(key));
                contacts.setUserName(value);
            }*/
            setData();
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置选中数据
     */
    private void setData(){
        List<Contacts> listTemp = new ArrayList<>();
        listTemp.addAll(lists);
        lists.clear();
        for (int i = 0; i < listTemp.size(); i++) {
            if (ContactsChoseActivity.mHashMap.containsKey(listTemp.get(i).getId() + "")) {
                listTemp.get(i).setSelected(true);
            } else {
                listTemp.get(i).setSelected(false);
            }
        }
        lists.addAll(listTemp);
    }
}
