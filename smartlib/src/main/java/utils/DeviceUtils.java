package utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Locale;

public class DeviceUtils {
	private Context context;
	private static DeviceUtils deviceHelper;

	public static DeviceUtils getInstance(Context c) {
		if ((deviceHelper == null) && (c != null))
			deviceHelper = new DeviceUtils(c);
		return deviceHelper;
	}

	private DeviceUtils(Context context) {
		this.context = context.getApplicationContext();
	}

	/**
	 * 获取WIFI名称
	 * @return
	 * @author GISirFive
	 */
	public String getSSID() {
		WifiManager wifi = (WifiManager) getSystemService("wifi");
		if (wifi == null)
			return null;
		WifiInfo info = wifi.getConnectionInfo();
		if (info != null) {
			String ssid = info.getSSID().replace("\"", "");
			return ((ssid == null) ? null : ssid);
		}
		return null;
	}

	/**
	 * 获取网络连接的mac地址
	 * 
	 * @return
	 * @author GISirFive
	 */
	public String getMacAddress() {
		WifiManager wifi = (WifiManager) getSystemService("wifi");
		if (wifi == null)
			return null;
		WifiInfo info = wifi.getConnectionInfo();
		if (info != null) {
			String mac = info.getMacAddress();
			return ((mac == null) ? null : mac);
		}
		return null;
	}

	/**
	 * 手机型号
	 * 
	 * @return
	 * @author GISirFive
	 */
	public String getModel() {
		return Build.MODEL;
	}

	/**
	 * 手机品牌
	 * 
	 * @return
	 * @author GISirFive
	 */
	public String getManufacturer() {
		return Build.MANUFACTURER;
	}

	/**
	 * 设备识别码
	 * 
	 * @return
	 * @author GISirFive
	 */
	public String getIMEI() {
		TelephonyManager phone = (TelephonyManager) getSystemService("phone");
		if (phone == null)
			return null;
		String deviceId = null;
		try {
			if (checkPermission("android.permission.READ_PHONE_STATE"))
				deviceId = phone.getDeviceId();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		if (TextUtils.isEmpty(deviceId))
			return null;
		return deviceId;
	}

	/**
	 * 获取当前设备的系统版本号
	 * 
	 * @return
	 * @author GISirFive
	 */
	public int getOSVersionInt() {
		return Build.VERSION.SDK_INT;
	}

	public String getOSVersionName() {
		return Build.VERSION.RELEASE;
	}

	/** 获取系统语言 */
	public String getOSLanguage() {
		return Locale.getDefault().getLanguage();
	}

	/** 获取系统国家 */
	public String getOSCountry() {
		return Locale.getDefault().getCountry();
	}

	/**
	 * 获取SIM卡提供的移动国家码和移动网络码
	 * 
	 * @return
	 * @author GISirFive
	 */
	public String getCarrier() {
		TelephonyManager tm = (TelephonyManager) getSystemService("phone");
		if (tm == null)
			return "-1";
		String operator = tm.getSimOperator();
		if (TextUtils.isEmpty(operator))
			operator = "-1";
		return operator;
	}

	/**
	 * 获取蓝牙名称
	 * @return
	 * @author GISirFive
	 */
	public String getBluetoothName() {
		try {
			BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
			if ((myDevice != null)
					&& (checkPermission("android.permission.BLUETOOTH")))
				return myDevice.getName();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	private Object getSystemService(String name) {
		try {
			return this.context.getSystemService(name);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取网络类型
	 * @return
	 * @author GISirFive
	 */
	public String getNetworkType() {
		ConnectivityManager conn = (ConnectivityManager) getSystemService("connectivity");
		if (conn == null)
			return "none";
		try {
			if (!(checkPermission("android.permission.ACCESS_NETWORK_STATE")))
				return "none";
		} catch (Throwable t) {
			t.printStackTrace();
			return "none";
		}
		NetworkInfo network = conn.getActiveNetworkInfo();
		if ((network == null) || (!(network.isAvailable())))
			return "none";
		int type = network.getType();
		switch (type) {
		case 1:
			return "wifi";
		case 0:
			if (is4GMobileNetwork())
				return "4G";
			return ((isFastMobileNetwork()) ? "3G" : "2G");
		case 7:
			return "bluetooth";
		case 8:
			return "dummy";
		case 9:
			return "ethernet";
		case 6:
			return "wimax";
		case 2:
		case 3:
		case 4:
		case 5:
		}
		return String.valueOf(type);
	}

	/**
	 * 获取网络类型
	 * @return
	 * @author GISirFive
	 */
	public String getDetailNetworkTypeForStatic() {
		String networkType = getNetworkType().toLowerCase();
		if ((TextUtils.isEmpty(networkType)) || ("none".equals(networkType)))
			return "none";
		if (networkType.startsWith("wifi"))
			return "wifi";
		if (networkType.startsWith("4g"))
			return "4g";
		if (networkType.startsWith("3g"))
			return "3g";
		if (networkType.startsWith("2g"))
			return "2g";
		if (networkType.startsWith("bluetooth"))
			return "bluetooth";
		return networkType;
	}

	/**
	 * 当前网络是否为4G网络
	 * 
	 * @return
	 * @author GISirFive
	 */
	private boolean is4GMobileNetwork() {
		TelephonyManager phone = (TelephonyManager) getSystemService("phone");
		if (phone == null)
			return false;
		return (phone.getNetworkType() == 13);
	}

	/**
	 * 当前网络连接网速是否很快
	 * 
	 * @return
	 * @author GISirFive
	 */
	private boolean isFastMobileNetwork() {
		TelephonyManager phone = (TelephonyManager) getSystemService("phone");
		if (phone == null)
			return false;
		switch (phone.getNetworkType()) {
		case 7:
			return false;
		case 4:
			return false;
		case 2:
			return false;
		case 5:
			return true;
		case 6:
			return true;
		case 1:
			return false;
		case 8:
			return true;
		case 10:
			return true;
		case 9:
			return true;
		case 3:
			return true;
		case 14:
			return true;
		case 12:
			return true;
		case 15:
			return true;
		case 11:
			return false;
		case 13:
			return true;
		case 0:
			return false;
		}
		return false;
	}

	/**
	 * 获取包名
	 * @return
	 * @author GISirFive
	 */
	public String getPackageName() {
		return this.context.getPackageName();
	}

	/**
	 * 获取APP名称
	 * @return
	 * @author GISirFive
	 */
	public String getAppName() {
		String appName = this.context.getApplicationInfo().name;
		if (appName != null)
			return appName;
		int appLbl = this.context.getApplicationInfo().labelRes;
		if (appLbl > 0)
			appName = this.context.getString(appLbl);
		else
			appName = String
					.valueOf(this.context.getApplicationInfo().nonLocalizedLabel);
		return appName;
	}

	/**
	 * 获取当前版本号
	 * 
	 * @return
	 * @author GISirFive
	 */
	public int getAppVersion() {
		try {
			PackageManager pm = this.context.getPackageManager();
			PackageInfo pi = pm
					.getPackageInfo(this.context.getPackageName(), 0);
			return pi.versionCode;
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取当前版本名称
	 * 
	 * @return
	 * @author GISirFive
	 */
	public String getAppVersionName() {
		try {
			PackageManager pm = this.context.getPackageManager();
			PackageInfo pi = pm
					.getPackageInfo(this.context.getPackageName(), 0);
			return pi.versionName;
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return "1.0";
	}

	/**
	 * 检查当前应用是否拥有某项权限
	 * 
	 * @param permission
	 * @return
	 * @throws Throwable
	 * @author GISirFive
	 */
	public boolean checkPermission(String permission) throws Throwable {
		int res;
		if (Build.VERSION.SDK_INT >= 23) {
			try {
				Integer ret = context.checkCallingOrSelfPermission(permission);
				res = (ret == null) ? -1 : ret.intValue();
			} catch (Throwable t) {
				res = -1;
			}
		} else {
			this.context.checkPermission(permission,
					android.os.Process.myPid(), android.os.Process.myUid());
			res = this.context.getPackageManager().checkPermission(permission,
					getPackageName());
		}
		return (res == 0);
	}

	/**
	 * SD卡是否可用
	 * 
	 * @return
	 * @author GISirFive
	 */
	public boolean getSdcardState() {
		try {
			return "mounted".equals(Environment.getExternalStorageState());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取SD卡路径
	 * 
	 * @return
	 * @author GISirFive
	 */
	public String getSdcardPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	/**
	 * 隐藏输入法
	 * 
	 * @param view
	 * @author GISirFive
	 */
	public void hideSoftInput(View view) {
		Object service = getSystemService("input_method");
		if (service == null)
			return;
		InputMethodManager imm = (InputMethodManager) service;
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/**
	 * 显示输入法
	 * 
	 * @param view
	 * @author GISirFive
	 */
	public void showSoftInput(View view) {
		Object service = getSystemService("input_method");
		if (service == null)
			return;
		InputMethodManager imm = (InputMethodManager) service;
		imm.toggleSoftInputFromWindow(view.getWindowToken(), 2, 0);
	}

	/**
	 * 获取设备的IMSI
	 * 
	 * @return
	 * @author GISirFive
	 */
	public String getIMSI() {
		TelephonyManager phone = (TelephonyManager) getSystemService("phone");
		if (phone == null)
			return null;
		String imsi = null;
		try {
			if (checkPermission("android.permission.READ_PHONE_STATE"))
				imsi = phone.getSubscriberId();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		if (TextUtils.isEmpty(imsi))
			return null;
		return imsi;
	}

	/**
	 * 获取设备IP
	 * 
	 * @return
	 * @author GISirFive
	 */
	public String getIPAddress() {
		try {
			if (checkPermission("android.permission.INTERNET")) {
				Enumeration en = NetworkInterface.getNetworkInterfaces();
				while (en.hasMoreElements()) {
					NetworkInterface intf = (NetworkInterface) en.nextElement();
					Enumeration enumIpAddr = intf.getInetAddresses();
					while (enumIpAddr.hasMoreElements()) {
						InetAddress inetAddress = (InetAddress) enumIpAddr
								.nextElement();
						if ((!(inetAddress.isLoopbackAddress()))
								&& (inetAddress instanceof Inet4Address))
							return inetAddress.getHostAddress();
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return "0.0.0.0";
	}

	/**
	 * 检测是否有网络连接
	 *
	 * @return
	 */
	public boolean hasInternet() {
		ConnectivityManager manager = (ConnectivityManager) this.context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info == null || !info.isConnected()) {
			return false;
		}
		if (info.isRoaming()) {
			return true;
		}
		return true;
	}
}