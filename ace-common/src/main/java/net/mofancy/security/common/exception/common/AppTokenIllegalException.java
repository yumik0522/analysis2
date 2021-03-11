package net.mofancy.security.common.exception.common;

/**
 * @author zwq
 * @version 1.0
 * @date 2019/12/3 0003 下午 6:18
 */
public class AppTokenIllegalException extends RuntimeException {
    private static final long serialVersionUID = 1140292201333010148L;

    public AppTokenIllegalException(String msg) {
        super(msg);
    }
}
