package com.github.yoojia.anyversion;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.orhanobut.logger.Logger;

/**
 * Created by Yoojia.Chen yoojia.chen@gmail.com 2015-01-04 AnyVersion - 自动更新 APK
 */
public class AnyVersion {

	private static final String TAG = "AnyVersion";

	private static final Lock LOCK = new ReentrantLock();

	private static AnyVersion ANY_VERSION = null;

	Application context;

	final VersionParser parser;

	private Future<?> workingTask;
	private Callback callback;
	private String url;
	private RemoteHandler remoteHandler;
	private final Version currentVersion;
	private final Handler mainHandler;
	private final ExecutorService threads;
	private final Installations installations;
	private final Downloads downloads;

	private Activity activity;

	public static AnyVersion getInstance() {
		try {
			LOCK.lock();
			if (ANY_VERSION == null) {
				throw new IllegalStateException("AnyVersion NOT init !");
			}
			return ANY_VERSION;
		} finally {
			LOCK.unlock();
		}
	}

	public static AnyVersion getInstance(Activity activity) {
		try {
			LOCK.lock();
			if (ANY_VERSION == null) {
				throw new IllegalStateException("AnyVersion NOT init !");
			}
			ANY_VERSION.activity = activity;
			return ANY_VERSION;
		} finally {
			LOCK.unlock();
		}
	}

	private AnyVersion(Application context, VersionParser parser) {
//		Logger.d(TAG, "AnyVersion init...");
		Logger.d("AnyVersion init...");
		this.context = context;
		this.parser = parser;
		this.threads = Executors.newSingleThreadExecutor();
		this.installations = new Installations();
		this.downloads = new Downloads();
		this.mainHandler = new Handler(Looper.getMainLooper()) {
			@Override
			public void handleMessage(Message msg) {
				Version version = (Version) msg.obj;
				new VersionDialog(activity, version, AnyVersion.this.downloads)
						.show();
			}
		};
		String versionName = null;
		int versionCode = 0;
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			versionName = pi.versionName;
			versionCode = pi.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			Logger.e(e.getMessage());
		}
		currentVersion = new Version(versionName, null, null, versionCode);
	}

	/**
	 * 初始化 AnyVersion。
	 * 
	 * @param context
	 *            必须是 Application
	 * @param parser
	 *            服务端响应数据解析接口
	 */
	public static void init(Application context, VersionParser parser) {
		Preconditions.requiredMainUIThread();
		try {
			LOCK.lock();
			if (ANY_VERSION != null) {
				Logger.e("Duplicate init AnyVersion ! This VersionParser  will be discard !");
				Logger.e("AnyVersion recommend init on YOUR-Application.onCreate(...) .");
				return;
			}
		} finally {
			LOCK.unlock();
		}
		if (context == null) {
			throw new NullPointerException(
					"Application Context CANNOT be null !");
		}
		if (parser == null) {
			throw new NullPointerException("Parser CANNOT be null !");
		}
		ANY_VERSION = new AnyVersion(context, parser);
		ANY_VERSION.installations.register(context);
	}

	/**
	 * 注册接收新版本通知的 Receiver。
	 */
	public static void registerReceiver(Context context,
			VersionReceiver receiver) {
		Broadcasts.register(context, receiver);
	}

	/**
	 * 反注册接收新版本通知的 Receiver
	 */
	public static void unregisterReceiver(Context context,
			VersionReceiver receiver) {
		Broadcasts.unregister(context, receiver);
	}

	/**
	 * 设置发现新版本时的回调接口。当 check(NotifyStyle.Callback) 时，此接口参数生效。
	 */
	public void setCallback(Callback callback) {
		Preconditions.requireInited();
		if (callback == null) {
			throw new NullPointerException("Callback CANNOT be null !");
		}
		this.callback = callback;
	}

	/**
	 * 设置检测远程版本的 URL。在使用内置 RemoteHandler 时，URL 是必须的。
	 */
	public void setURL(String url) {
		Preconditions.requireInited();
		checkRequiredURL(url);
		this.url = url;
	}

	/**
	 * 设置自定义检测远程版本数据的接口
	 */
	public void setCustomRemote(RemoteHandler remoteHandler) {
		Preconditions.requireInited();
		if (remoteHandler == null) {
			throw new NullPointerException("RemoteHandler CANNOT be null !");
		}
		this.remoteHandler = remoteHandler;
	}

	/**
	 * 检测新版本，并指定发现新版本的处理方式
	 */
	public void check(NotifyStyle style) {
		createRemoteRequestIfNeed();
		check(this.url, this.remoteHandler, style);
	}

	/**
	 * 按指定的 URL，检测新版本，并指定发现新版本的处理方式
	 */
	public void check(String url, NotifyStyle style) {
		createRemoteRequestIfNeed();
		check(url, this.remoteHandler, style);
	}

	private void check(final String url, final RemoteHandler remote,
			final NotifyStyle style) {
		Preconditions.requireInited();
		if (NotifyStyle.Callback.equals(style) && callback == null) {
			throw new NullPointerException(
					"If reply by callback, callback CANNOT be null ! "
							+ "Call 'setCallback(...) to setup !'");
		}
		final Callback core = new Callback() {
			@Override
			public void onVersion(Version remoteVersion, boolean notVersion) {
				// 检查是否为新版本
				if (remoteVersion == null)
					return;
				// 检测最新版本是否大于当前版本
				// if (currentVersion.code >= remoteVersion.code) return;
				if(callback != null){
					boolean hasNewVersion = currentVersion.code < remoteVersion.code;
					callback.onVersion(remoteVersion, hasNewVersion);
				}
				switch (style) {
				case Broadcast:
					if (currentVersion.code >= remoteVersion.code) {
						return;
					} else {
						Broadcasts.send(context, remoteVersion);
					}
					break;
				case Dialog:
					if (currentVersion.code >= remoteVersion.code) {
						return;
					} else {
						final Message msg = Message.obtain(
								ANY_VERSION.mainHandler, 0, remoteVersion);
						msg.sendToTarget();
					}
					break;
				}
			}

		};
		remote.setOptions(url, parser, core);
		workingTask = threads.submit(remote);
	}

	/**
	 * 取消当前正在检测的工作线程
	 */
	public void cancelCheck() {
		Preconditions.requireInited();
		if (workingTask != null && !workingTask.isDone()) {
			workingTask.cancel(true); // force interrupt
		}
	}

	private void createRemoteRequestIfNeed() {
		if (remoteHandler == null) {
			// 使用内置请求时，URL 地址是必须的。
			checkRequiredURL(this.url);
			remoteHandler = new SimpleRemoteHandler();
		}
	}

	private void checkRequiredURL(String url) {
		if (TextUtils.isEmpty(url)) {
			throw new NullPointerException("URL CANNOT be null or empty !");
		}
	}
}
