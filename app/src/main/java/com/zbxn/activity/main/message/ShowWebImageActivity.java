package com.zbxn.activity.main.message;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

import com.zbxn.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 项目名称：点击wb中图片展示的主类
 * 创建人：LiangHanXin
 * 创建时间：2016/10/13 15:56
 */

public class ShowWebImageActivity extends Activity {
    private TextView imageTextView = null;
    private String imagePath = null;
    private ZoomableImageView imageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_webimage);
        imagePath = getIntent().getStringExtra("image");

        this.imageTextView = (TextView) findViewById(R.id.show_webimage_imagepath_textview);
        imageTextView.setText(imagePath);
        imageView = (ZoomableImageView) findViewById(R.id.show_webimage_imageview);

/**
 *子线程请求网络
 */
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    final Bitmap bitmap = ((BitmapDrawable) ShowWebImageActivity.loadImageFromUrl(imagePath)).getBitmap();
                    //发往主线程更新UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    public static Drawable loadImageFromUrl(String url) throws IOException {

        URL m = new URL(url);
        InputStream i = (InputStream) m.getContent();
        Drawable d = Drawable.createFromStream(i, "src");
        return d;
    }
}
