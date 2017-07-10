package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验
 *
 * @author GISirFive
 */
public class ValidationUtil {

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        if (str == null || str.trim().length() == 0)
            return false;

        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 验证数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (str == null || str.trim().length() == 0)
            return false;

        Pattern p = Pattern.compile("^[0-9]*$");
        Matcher m = p.matcher(str);

        return m.matches();
    }

    /**
     * 验证字母与数字的组合
     *
     * @param str
     * @return
     */
    public static boolean letterdigit(String str) {
        if (str == null || str.trim().length() == 0)
            return false;

        Pattern p = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,10}$");
        Matcher m = p.matcher(str);

        return m.matches();
    }

    /**
     * 验证字母
     *
     * @param str
     * @return
     */
    public static boolean isABC(String str) {
        if (str == null || str.trim().length() == 0)
            return false;

        Pattern p = Pattern.compile("^[A-Za-z]+$");
        Matcher m = p.matcher(str);

        return m.matches();
    }

    /**
     * 验证数字+字母+特殊符号
     *
     * @param str
     * @return
     */
    public static boolean isABCNumber(String str) {
        if (str == null || str.trim().length() == 0)
            return false;

//		Pattern p = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~!@#$%^&*()_+`\\-={}:\";'<>?,.\\/]).{8,16}$");//必须包含数字，字母，特殊符号
        Pattern p = Pattern.compile("^(?![\\d]+$)(?![a-zA-Z]+$)(?![^\\da-zA-Z]+$).{8,20}$");//数字，字母，特殊符号任选其二
        Matcher m = p.matcher(str);

        return m.matches();
    }

    /**
     * 验证汉字
     *
     * @param str
     * @return
     */
    public static boolean isChineseCharacters(String str) {
        if (str == null || str.trim().length() == 0)
            return false;

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]+");
        Matcher m = p.matcher(str);

        return m.matches();
    }

    /**
     * 验证邮箱
     *
     * @param str
     * @return
     */
    public static boolean isMailbox(String str) {
        if (str == null || str.trim().length() == 0)
            return false;

        Pattern p = Pattern
                .compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher m = p.matcher(str);

        return m.matches();
    }
}
