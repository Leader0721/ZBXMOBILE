package com.zbxn.init.bean;

import java.util.HashMap;

import com.zbxn.pub.application.BaseApp;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

/**
 * 功能模块授权类型
 * 
 * @author GISirFive
 * @since 2016-7-5 下午3:02:37
 */
class AuthorUtils {

	/**
	 * 功能模块授权类型
	 */
	enum Author {
		/** 登录 */
		verifyLogin,
	}

	/**
	 * 加载授权信息
	 * 
	 * @author GISirFive
	 */
	static HashMap<String, Bundle> getAuthorMap() {
		HashMap<String, Bundle> map = new HashMap<>();
		try {
			PackageManager manager = BaseApp.CONTEXT.getPackageManager();
			ActivityInfo[] actInfo = manager.getPackageInfo(
					BaseApp.CONTEXT.getPackageName(),
					PackageManager.GET_ACTIVITIES
							| PackageManager.GET_META_DATA).activities;
			map = new HashMap<String, Bundle>();
			if (actInfo != null && actInfo.length != 0) {
				for (ActivityInfo info : actInfo) {
					String activityName = info.name;
					Bundle bundle = info.metaData;
					map.put(activityName, bundle);
				}
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return map;
	}
}