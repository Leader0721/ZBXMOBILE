package widget.pulltorefresh.util;

import com.orhanobut.logger.Logger;

import android.util.Log;

/**
 * An encapsulation of {@link Log}, enable log level and print log with parameters.
 *
 * @author http://www.liaohuqiu.net/
 */
public class PtrCLog {

    public static final int LEVEL_VERBOSE = 0;
    public static final int LEVEL_DEBUG = 1;
    public static final int LEVEL_INFO = 2;
    public static final int LEVEL_WARNING = 3;
    public static final int LEVEL_ERROR = 4;
    public static final int LEVEL_FATAL = 5;

    private static int sLevel = LEVEL_VERBOSE;

    /**
     * set log level, the level lower than this level will not be logged
     *
     * @param level
     */
    public static void setLogLevel(int level) {
        sLevel = level;
    }

    /**
     * Send a VERBOSE log message.
     *
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg) {
        if (sLevel > LEVEL_VERBOSE) {
            return;
        }
        Logger.t(tag).v(msg);
    }

    /**
     * Send a VERBOSE log message.
     *
     * @param tag
     * @param msg
     * @param throwable
     */
    public static void v(String tag, String msg, Throwable throwable) {
        if (sLevel > LEVEL_VERBOSE) {
            return;
        }
        Logger.t(tag).v(msg);
    }

    /**
     * Send a VERBOSE log message.
     *
     * @param tag
     * @param msg
     * @param args
     */
    public static void v(String tag, String msg, Object... args) {
        if (sLevel > LEVEL_VERBOSE) {
            return;
        }
        if (args.length > 0) {
            msg = String.format(msg, args);
        }
        Logger.t(tag).v(msg);
    }

    /**
     * Send a DEBUG log message
     *
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (sLevel > LEVEL_DEBUG) {
            return;
        }
        Logger.t(tag).d(msg);
    }

    /**
     * Send a DEBUG log message
     *
     * @param tag
     * @param msg
     * @param args
     */
    public static void d(String tag, String msg, Object... args) {
        if (sLevel > LEVEL_DEBUG) {
            return;
        }
        if (args.length > 0) {
            msg = String.format(msg, args);
        }
        Logger.t(tag).d(msg);
    }

    /**
     * Send a DEBUG log message
     *
     * @param tag
     * @param msg
     * @param throwable
     */
    public static void d(String tag, String msg, Throwable throwable) {
        if (sLevel > LEVEL_DEBUG) {
            return;
        }
        Logger.t(tag).d(msg);
    }

    /**
     * Send an INFO log message
     *
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (sLevel > LEVEL_INFO) {
            return;
        }
        Logger.t(tag).i(msg);
    }

    /**
     * Send an INFO log message
     *
     * @param tag
     * @param msg
     * @param args
     */
    public static void i(String tag, String msg, Object... args) {
        if (sLevel > LEVEL_INFO) {
            return;
        }
        if (args.length > 0) {
            msg = String.format(msg, args);
        }
        Logger.t(tag).i(msg);
    }

    /**
     * Send an INFO log message
     *
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg, Throwable throwable) {
        if (sLevel > LEVEL_INFO) {
            return;
        }
        Logger.t(tag).i(msg);
    }

    /**
     * Send a WARNING log message
     *
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        if (sLevel > LEVEL_WARNING) {
            return;
        }
        Logger.t(tag).w(msg);
    }

    /**
     * Send a WARNING log message
     *
     * @param tag
     * @param msg
     * @param args
     */
    public static void w(String tag, String msg, Object... args) {
        if (sLevel > LEVEL_WARNING) {
            return;
        }
        if (args.length > 0) {
            msg = String.format(msg, args);
        }
        Logger.t(tag).w(msg);
    }

    /**
     * Send a WARNING log message
     *
     * @param tag
     * @param msg
     * @param throwable
     */
    public static void w(String tag, String msg, Throwable throwable) {
        if (sLevel > LEVEL_WARNING) {
            return;
        }
        Logger.t(tag).w(msg);
    }

    /**
     * Send an ERROR log message
     *
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (sLevel > LEVEL_ERROR) {
            return;
        }
        Logger.t(tag).e(msg);
    }

    /**
     * Send an ERROR log message
     *
     * @param tag
     * @param msg
     * @param args
     */
    public static void e(String tag, String msg, Object... args) {
        if (sLevel > LEVEL_ERROR) {
            return;
        }
        if (args.length > 0) {
            msg = String.format(msg, args);
        }
        Logger.t(tag).e(msg);
    }

    /**
     * Send an ERROR log message
     *
     * @param tag
     * @param msg
     * @param throwable
     */
    public static void e(String tag, String msg, Throwable throwable) {
        if (sLevel > LEVEL_ERROR) {
            return;
        }
        Logger.t(tag).e(throwable, msg);
    }

    /**
     * Send a FATAL ERROR log message
     *
     * @param tag
     * @param msg
     */
    public static void f(String tag, String msg) {
        if (sLevel > LEVEL_FATAL) {
            return;
        }
        Logger.t(tag).wtf(msg);
    }

    /**
     * Send a FATAL ERROR log message
     *
     * @param tag
     * @param msg
     * @param args
     */
    public static void f(String tag, String msg, Object... args) {
        if (sLevel > LEVEL_FATAL) {
            return;
        }
        if (args.length > 0) {
            msg = String.format(msg, args);
        }
        Logger.t(tag).wtf(msg);
    }

    /**
     * Send a FATAL ERROR log message
     *
     * @param tag
     * @param msg
     * @param throwable
     */
    public static void f(String tag, String msg, Throwable throwable) {
        if (sLevel > LEVEL_FATAL) {
            return;
        }
        Logger.t(tag).wtf(msg);
    }
}
