package net.mofancy.security.common.exception.auth;


import net.mofancy.security.common.constant.CommonConstants;
import net.mofancy.security.common.exception.BaseException;

/**
 * Created by ace on 2017/9/8.
 */
public class AppTokenException extends BaseException {
    public AppTokenException(String message) {
        super(message, CommonConstants.EX_USER_INVALID_CODE);
    }
}
