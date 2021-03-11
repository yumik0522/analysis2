package net.mofancy.security.common.exception.common;

/**
 * 非法参数异常
 * @author zwq
 * @version 1.0
 * @date 2019/12/3 0003 下午 6:17
 */
public class ParameterIllegalException extends RuntimeException {

    private static final long serialVersionUID = -5416232933485291796L;

    private Object data = null;

    public ParameterIllegalException(String msg) {
        this(msg, null);
    }

    public ParameterIllegalException(String msg, Object data) {
        super(msg);
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
