package com.zbxn.pub.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class ProgressWebView extends WebView{

	private ProgressBar mProgressBar;
	
	@TargetApi(21)
	public ProgressWebView(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initialize(context);
	}

	public ProgressWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize(context);
	}

	public ProgressWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}

	public ProgressWebView(Context context) {
		super(context);
		initialize(context);
	}

	private void initialize(Context context){
		mProgressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
		mProgressBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 12, 0, 0));
        addView(mProgressBar);
        setWebChromeClient(new WebChromeClient());
	}
	
	class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
            	mProgressBar.setVisibility(GONE);
            } else {
                if (mProgressBar.getVisibility() == GONE)
                	mProgressBar.setVisibility(VISIBLE);
                mProgressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		LayoutParams lp = (LayoutParams) mProgressBar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        mProgressBar.setLayoutParams(lp);
		super.onScrollChanged(l, t, oldl, oldt);
	}
}
