package com.zbxn.createworkblog;

import org.json.JSONObject;

import butterknife.BindView;
import widget.slidebottompanel.SlideBottomPanel;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.zbxn.R;
import com.zbxn.activity.CreateWorkBlog;
import com.zbxn.pub.frame.mvc.AbsBaseView;
import com.zbxn.pub.http.RequestUtils.Code;

/**
 * 写日志时的常用语提示
 * 
 * @author GISirFive
 * @since 2016-7-12 上午10:32:05
 */
public class BlogHintView extends AbsBaseView implements OnItemClickListener {

	// 父布局的Handler
	private Handler mHandler;

	private SlideBottomPanel mPanel;

	@BindView(R.id.listview_content)
	ListView mListView;
	@BindView(R.id.button_ok)
	Button mBtnOk;
	@BindView(R.id.button_cancel)
	Button mBtnCancel;
	@BindView(R.id.textview_message)
	TextView mTvMessage;
	
	private HintAdapter mAdapter;

	public BlogHintView(Context context, View view, Handler parentHandler) {
		super(context, view);
		mPanel = (SlideBottomPanel) view;
		mHandler = parentHandler;
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public void onSuccess(Code code, JSONObject response) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void onFailure(Code code, JSONObject errorResponse) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void initialize(View root) {
		ButterKnife.bind(this, root);

		mAdapter = new HintAdapter(getContext());
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		
		mBtnOk.setVisibility(View.INVISIBLE);
		mBtnCancel.setVisibility(View.INVISIBLE);
		mTvMessage.setText("点击列表项，选择常用语");
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Message message = new Message();
		message.what = CreateWorkBlog.Flag_callback_BlogHintView;
		message.obj = mAdapter.getItem(position);
		mHandler.sendMessage(message);
		if(position == 0)
			return;
		mListView.setEnabled(false);
		mPanel.hide();
		mListView.setEnabled(true);
	}

	@Override
	public void show() {
		if (!mPanel.isPanelShowing()){
			mPanel.displayPanel();
		}
	}

	@Override
	public void hide() {
		if (mPanel.isPanelShowing())
			mPanel.hide();
	}
	
	@Override
	public boolean isShowing() {
		return mPanel.isPanelShowing();
	}
	
	@OnClick({R.id.button_cancel, R.id.button_ok})
	public void onClick(View v) {
		hide();
	}

}
