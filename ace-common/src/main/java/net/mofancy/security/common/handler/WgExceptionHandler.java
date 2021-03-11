package net.mofancy.security.common.handler;

import net.mofancy.security.common.constant.CommonConstants;
import net.mofancy.security.common.exception.common.AppTokenIllegalException;
import net.mofancy.security.common.exception.common.ParameterIllegalException;
import net.mofancy.security.common.web.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author zwq
 * @version 1.0
 * @date 2019/12/3 0003 下午 6:17
 */
@RestControllerAdvice("net.mofancy.security")
public class WgExceptionHandler {
    private Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(ParameterIllegalException.class)
    public ApiResponse parameterIllegalExceptionHandler(ParameterIllegalException e) {
        ApiResponse apiResponse = new ApiResponse(CommonConstants.CODE_ILLEGAL_ARGUMENT, e.getMessage());
        if (e.getData() != null) {
            apiResponse.setData(e.getData());
        }
        return apiResponse;
    }

    @ExceptionHandler(AppTokenIllegalException.class)
    public ApiResponse appTokenIllegalException(AppTokenIllegalException e) {
        return new ApiResponse(CommonConstants.CODE_ILLEGAL_APPTOKEN, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse exceptionHandler(Exception e) {
        log.error("发生服务器级异常", e);
        return new ApiResponse(CommonConstants.CODE_SERVER_ERROR, e.getMessage());
    }
}
