package net.mofancy.security.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ace on 2017/9/10.
 */
public class StringHelper {
    public static String getObjectValue(Object obj){
        return obj==null?"":obj.toString();
    }

    public static int getNumber (String str) {
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return Integer.parseInt(m.replaceAll("").trim());
    }
}
