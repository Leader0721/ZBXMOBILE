package com.orhanobut.logger;

/**
 * Logger is a wrapper of {@link android.util.Log} But more pretty, simple and
 * powerful
 */
public final class Logger {
	private static final String DEFAULT_TAG = "Logger";

	private static IPrinter printer = null;

	// no instance
	private Logger() {
	}

	/**
	 * It is used to get the settings object in order to change settings
	 * 
	 * @return the settings object
	 */
	private static Settings init() {
		if(isInit())
			return printer.getSettings();
		Settings settings = init(DEFAULT_TAG);
		settings.methodCount(3)
			.hideThreadInfo()
			.logLevel(LogLevel.FULL)
			.methodOffset(2)
			.logTool(new AndroidLogTool());
		return settings;
	}

	/**
	 * It is used to change the tag
	 * 
	 * @param tag
	 *            is the given string which will be used in Logger as TAG
	 */
	public static Settings init(String tag) {
		printer = new LoggerPrinter();
		return printer.init(tag);
	}

	public static void clear() {
		if(!isInit())
			return;
		printer.clear();
		printer = null;
	}

	public static IPrinter t(String tag) {
		init();
		return printer.t(tag, printer.getSettings().getMethodCount());
	}

	public static IPrinter t(int methodCount) {
		init();
		return printer.t(null, methodCount);
	}

	public static IPrinter t(String tag, int methodCount) {
		init();
		return printer.t(tag, methodCount);
	}

	public static void d(String message, Object... args) {
		init();
		printer.d(message, args);
	}

	public static void e(String message, Object... args) {
		init();
		printer.e(null, message, args);
	}

	public static void e(Throwable throwable, String message, Object... args) {
		init();
		printer.e(throwable, message, args);
	}

	public static void i(String message, Object... args) {
		init();
		printer.i(message, args);
	}

	public static void v(String message, Object... args) {
		init();
		printer.v(message, args);
	}

	public static void w(String message, Object... args) {
		init();
		printer.w(message, args);
	}

	public static void wtf(String message, Object... args) {
		init();
		printer.wtf(message, args);
	}

	/**
	 * Formats the json content and print it
	 * 
	 * @param json
	 *            the json content
	 */
	public static void json(String json) {
		init();
		printer.json(json);
	}

	/**
	 * Formats the json content and print it
	 * 
	 * @param xml
	 *            the xml content
	 */
	public static void xml(String xml) {
		init();
		printer.xml(xml);
	}

	/**
	 * 检查是否初始化
	 * 
	 * @return
	 * @author GISirFive
	 */
	private static boolean isInit() {
		return printer != null;
	}
}
