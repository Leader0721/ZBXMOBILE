package com.zbxn.activity.main.contacts;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;
import com.zbxn.R;
import com.zbxn.bean.adapter.ContactsDepartmentAdapter;
import com.zbxn.init.eventbus.EventCustom;
import com.zbxn.listener.ICustomListener;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.bean.ContactsDepartment;
import com.zbxn.pub.frame.mvp.AbsBaseFragment;
import com.zbxn.utils.KeyEvent;
import com.zbxn.widget.MyLetterListView;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import utils.StringUtils;

/**
 * 通讯录按拼音排序
 *
 * @author GISirFive
 * @time 2016/8/8
 */
public class ContactsDepartmentFragment extends AbsBaseFragment {

    @BindView(R.id.mListView)
    ExpandableListView mListView;
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
    private ContactsDepartmentFragment.OverlayThread overlayThread;
    private List<ContactsDepartment> lists;

    private ContactsDepartmentAdapter adapter;

    private int mPosition = -1;

    //0-查看 1-多选 2-单选
    private int mType;

    public void setFragmentTitle(String title) {
        mTitle = title;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_contacts_department, container, false);
        ButterKnife.bind(this, root);
        mType = getArguments().getInt("type", 0);
        mListView.setDivider(null);
        return root;
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();

        try {
            List<Contacts> listTemp = BaseApp.DBLoader.findAll(Selector.from(Contacts.class).where("isDepartment", "=", null));
            //例：分组聚合查询出  Parent表中 非重复的name和它的对应数量
            List<DbModel> dbModels = BaseApp.DBLoader.findDbModelAll(Selector.from(Contacts.class).select("distinct departmentId", "departmentName")
                    .where("isDepartment", "=", null).orderBy("departmentName"));
            lists = new ArrayList<>();
            ContactsDepartment contactsDepartment = null;
            for (int i = 0; i < dbModels.size(); i++) {
                contactsDepartment = new ContactsDepartment();
                contactsDepartment.setCaptialChar(StringUtils.getPinYinHeadChar(dbModels.get(i).getString("departmentName")));
                contactsDepartment.setDepartmentId(dbModels.get(i).getString("departmentId"));
                contactsDepartment.setDepartmentName(dbModels.get(i).getString("departmentName"));
                int countSelect = 0;
                int countAll = 0;
                for (int j = 0; j < listTemp.size(); j++) {
                    if (listTemp.get(j).getDepartmentId().equals(contactsDepartment.getDepartmentId())) {
                        countAll++;
                        if (ContactsChoseActivity.mHashMap.containsKey(listTemp.get(j).getId() + "")) {
                            listTemp.get(j).setSelected(true);
                            countSelect++;
                        } else {
                            listTemp.get(j).setSelected(false);
                        }
                        contactsDepartment.getListContacts().add(listTemp.get(j));

                        //设置是否选中group check
                        if (countSelect >= countAll) {
                            contactsDepartment.setSelected(true);
                        } else {
                            contactsDepartment.setSelected(false);
                        }
                    }
                }
                lists.add(contactsDepartment);
            }
            //JDK7中的Collections.Sort方法实现中，如果两个值是相等的，那么compare方法需要返回0，否则可能会在排序时抛错，而JDK6是没有这个限制的
            Collections.sort(lists, new Comparator<ContactsDepartment>() {
                @Override
                public int compare(ContactsDepartment a, ContactsDepartment b) {
                    String idA = a.getCaptialChar();
                    String idB = b.getCaptialChar();
                    if (idA.compareTo(idB) == 0) {
                        return 0;
                    } else if (idA.compareTo(idB) > 0)
                        return 1;
                    else
                        return -1;
                }
            });
        } catch (DbException e) {
            e.printStackTrace();
        }

//        String[] b = {"A", "B"};
//        mMyLetterListView.setWord(b);

        mMyLetterListView.setOnTouchingLetterChangedListener(new ContactsDepartmentFragment.LetterListViewListener());
        alphaIndexer = new HashMap<String, Integer>();
        handler = new Handler();
        overlayThread = new ContactsDepartmentFragment.OverlayThread();
        initOverlay();

        alphaIndexer = new HashMap<String, Integer>();
        sections = new String[lists.size()];
        adapter = new ContactsDepartmentAdapter(mContext, lists, listener, alphaIndexer, sections, mType);
        mListView.setAdapter(adapter);
        /*mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });*/
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
            List<ContactsDepartment> listTemp = new ArrayList<>();
            listTemp.addAll(lists);
            lists.clear();
            for (int i = 0; i < listTemp.size(); i++) {
                List<Contacts> listChild = listTemp.get(i).getListContacts();
                if (StringUtils.isEmpty(listChild)) {
                    continue;
                }
                int countSelect = 0;
                int countAll = 0;
                for (int j = 0; j < listChild.size(); j++) {
                    countAll++;
                    if (ContactsChoseActivity.mHashMap.containsKey(listChild.get(j).getId() + "")) {
                        listChild.get(j).setSelected(true);
                        countSelect++;
                    } else {
                        listChild.get(j).setSelected(false);
                    }
                    //设置是否选中group check
                    if (countSelect >= countAll) {
                        listTemp.get(i).setSelected(true);
                    } else {
                        listTemp.get(i).setSelected(false);
                    }
                }
            }

            lists.addAll(listTemp);
            adapter.notifyDataSetChanged();
        }
    }
}
