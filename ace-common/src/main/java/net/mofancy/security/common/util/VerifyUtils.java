package net.mofancy.security.common.util;




import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式校验
 * @author zwq
 * @version 1.0
 * @date 2019/12/18 0018 下午 4:08
 */
public class VerifyUtils {

    /**
     * 校验手机号码
     * @author zwq
     * @date 2019/12/18 0018
     * @param [str]
     * @return boolean
     */
    public static boolean isMobile(String str) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(str);
        boolean b = m.matches();
        return b;
    }

    public static void main(String[] args) {
        System.out.println(isMobile("1"));

    }
}
