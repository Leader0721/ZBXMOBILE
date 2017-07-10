package com.zbxn.pub.image;

import java.io.File;

import com.bumptech.glide.Glide;

import android.widget.ImageView;

public interface IImageLoader {

	/**
	 * 获取图片显示核心类
	 * @return
	 * @author GISirFive
	 */
	Glide getLoader();
	
	/**
	 * 公共图片显示
	 * @param uri
	 * @param imageView
	 * @author GISirFive
	 */
	void display(String uri, ImageView imageView);

	/**
	 * 自定义图像显示，可以使用程序封装好的options{@link ImageLoaderOptions}，也可以自己实现{@link DisplayImageOptions}<br>
	 * @param uri
	 * @param imageView
	 * @param options
	 * @author GISirFive
	 */
//	void display(String uri, ImageView imageView, DisplayImageOptions options);
	
	/**
	 * 显示原图
	 * @param uri
	 * @param imageView
	 * @author GISirFive
	 */
	void displaySource(String uri, ImageView imageView);
	
	/**
	 * 迷你型缩略图显示，缓存缩略图
	 * @param uri
	 * @param imageView
	 * @author GISirFive
	 */
	void displayThumbMini(String uri, ImageView imageView);
	
	/**
	 * 微型（比迷你型更小）缩略图显示，缓存缩略图
	 * @param uri
	 * @param imageView
	 * @author GISirFive
	 */
	void displayThumbMicro(String uri, ImageView imageView);
	
	/***
	 * 根据uri获取对应的文件<br>
	 * 图片返回顺序：缓存的原图、缓存的迷你型缩略图、缓存的微型缩略图、原图对应的文件（原图存在）、null（原图不存在）
	 * @param uri
	 * @return
	 * @author GISirFive
	 */
	File getCacheFile(String uri);
	
}