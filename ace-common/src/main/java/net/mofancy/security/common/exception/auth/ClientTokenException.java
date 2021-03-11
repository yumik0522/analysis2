package net.mofancy.security.common.exception.auth;


import net.mofancy.security.common.constant.CommonConstants;
import net.mofancy.security.common.exception.BaseException;

/**
 * Created by ace on 2017/9/10.
 */
public class ClientTokenException extends BaseException {
    public ClientTokenException(String message) {
        super(message, CommonConstants.EX_CLIENT_INVALID_CODE);
    }
}
