package com.zbar.lib;

import android.app.Activity;
import android.content.Intent;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Button button = new Button(this);
		setContentView(button);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TestActivity.this, CaptureActivity.class);
				startActivity(intent);
			}
		});
	}
	
}
