package net.mofancy.security.common.msg.auth;

import net.mofancy.security.common.constant.RestCodeConstants;
import net.mofancy.security.common.msg.BaseResponse;

/**
 * Created by ace on 2017/8/25.
 */
public class TokenForbiddenResponse  extends BaseResponse {
    public TokenForbiddenResponse(String message) {
        super(RestCodeConstants.TOKEN_FORBIDDEN_CODE, message);
    }
}
