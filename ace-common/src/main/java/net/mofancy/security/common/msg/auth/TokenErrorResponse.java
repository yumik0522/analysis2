package net.mofancy.security.common.msg.auth;

import net.mofancy.security.common.constant.RestCodeConstants;
import net.mofancy.security.common.msg.BaseResponse;

/**
 * Created by ace on 2017/8/23.
 */
public class TokenErrorResponse extends BaseResponse {
    public TokenErrorResponse(String message) {
        super(RestCodeConstants.TOKEN_ERROR_CODE, message);
    }
}
