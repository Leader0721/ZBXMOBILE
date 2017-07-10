package utils;

import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class SystemUtils {

	/**
	 * 判断应用是否已经启动
	 * 
	 * @param context
	 *            一个context
	 * @param packageName
	 *            要判断应用的包名
	 * @return boolean
	 */
	public static boolean isAppAlive(Context context, String packageName) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager
				.getRunningAppProcesses();
		for (int i = 0; i < processInfos.size(); i++) {
			if (processInfos.get(i).processName.equals(packageName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检测Intent是否可用
	 * 
	 * @return
	 */
	public static boolean hasIntent(ContextWrapper contextWrapper, Intent intent) {
		PackageManager packageManager = contextWrapper.getPackageManager();
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return !list.isEmpty();
	}

}
