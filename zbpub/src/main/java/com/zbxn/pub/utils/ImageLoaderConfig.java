package com.zbxn.pub.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.zbxn.pub.R;

/**
 * ImageLoader配置
 * 
 * @author LYQ
 * 
 */
public class ImageLoaderConfig {

	/**
	 * 初始化展示配置项
	 * 
	 * @param isShowDefault
	 *            是否显示默认图片
	 * @return
	 */
	public static DisplayImageOptions initDisplayOptions(boolean isShowDefault) {
		DisplayImageOptions.Builder displayImageOptionsBuilder = new DisplayImageOptions.Builder();
		displayImageOptionsBuilder.imageScaleType(ImageScaleType.EXACTLY);
		if (isShowDefault) {
			displayImageOptionsBuilder
					.showImageOnLoading(R.mipmap.nonews);
			displayImageOptionsBuilder
					.showImageForEmptyUri(R.mipmap.nonews);
			displayImageOptionsBuilder
					.showImageOnFail(R.mipmap.nonews);
		}
		displayImageOptionsBuilder.cacheOnDisk(true);
		displayImageOptionsBuilder.cacheInMemory(true);
		displayImageOptionsBuilder.bitmapConfig(Bitmap.Config.RGB_565);
		//displayImageOptionsBuilder.displayer(new com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer(300));
		return displayImageOptionsBuilder.build();
	}

	/**
	 * 初始化ImageLoader
	 * 
	 * @param context
	 *            上下文
	 */
	public static void initImageLoader(Context context) {
		// java.io.File cacheDir =
		// com.nostra13.universalimageloader.utils.StorageUtils.getCacheDirectory(context);
		// System.out.println("cacheDir:" + cacheDir);
		ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
				context);
		builder.memoryCacheExtraOptions(480, 800);// 缓存在内存的图片的宽和高度
		builder.threadPoolSize(3);// 设置线程数
		builder.threadPriority(Thread.NORM_PRIORITY - 2);// 设置线程的优先级
		builder.tasksProcessingOrder(QueueProcessingType.LIFO);// 设置图片下载和显示的工作队列排序
		builder.denyCacheImageMultipleSizesInMemory();// 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
		builder.memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13);
		builder.diskCacheSize(50 * 1024 * 1024);// 50MB
		builder.imageDownloader(new BaseImageDownloader(context, 10000, 60000));
		builder.imageDecoder(new BaseImageDecoder(false));
	builder.defaultDisplayImageOptions(initDisplayOptions(true));// 设置展示配置
	ImageLoader.getInstance().init(builder.build());// ImageLoader初始化
}
}
