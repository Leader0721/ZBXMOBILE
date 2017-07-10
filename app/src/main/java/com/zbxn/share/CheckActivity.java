package com.zbxn.share;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.umeng.socialize.UmengTool;
import com.zbxn.R;

/**
 * Created by wangfei on 16/8/23.
 */
public class CheckActivity extends Activity{
    TextView contentTv;
    Button checksignBtn,checkurlBtn,checkWXBtn,checkSinaBtn,checkAlipayBtn,checkpermissionBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_check);
        contentTv = (TextView) findViewById(R.id.umeng_text);
        checksignBtn = (Button)findViewById(R.id.umeng_sign);
        checkurlBtn = (Button)findViewById(R.id.umeng_redirecturl);
        checkWXBtn = (Button)findViewById(R.id.umeng_wx);
        checkSinaBtn = (Button)findViewById(R.id.umeng_sina);
        checkAlipayBtn = (Button)findViewById(R.id.umeng_alipay);
        checkpermissionBtn = (Button) findViewById(R.id.umeng_permission);
        contentTv.setText("自检工具只是提供给开发者调试使用，上线app不需要该功能");
        checksignBtn.setOnClickListener(listener);
        checkurlBtn.setOnClickListener(listener);
        checkWXBtn.setOnClickListener(listener);
        checkSinaBtn.setOnClickListener(listener);
        checkAlipayBtn.setOnClickListener(listener);
       // checkpermissionBtn.setOnClickListener(listener);

    }
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v.getId() ==R.id.umeng_sign){
                UmengTool.getSignature(CheckActivity.this);
            }else if(v.getId() ==R.id.umeng_redirecturl){
                UmengTool.getREDICRECT_URL(CheckActivity.this);
            }else if(v.getId() ==R.id.umeng_wx){
                UmengTool.checkWx(CheckActivity.this);
            }else if(v.getId() ==R.id.umeng_sina){
                UmengTool.checkSina(CheckActivity.this);
            }else if(v.getId() ==R.id.umeng_alipay){
                UmengTool.checkAlipay(CheckActivity.this);
            }
        }
    };
}
