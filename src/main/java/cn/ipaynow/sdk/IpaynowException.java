package cn.ipaynow.sdk;

/**
 * 现在支付 SDK 统一异常，覆盖请求传输失败、响应或回调验签失败、响应类定义错误等。
 * 调用方只需捕获这一种异常，再通过 {@link #code()} 判断具体失败类型。
 */
public class IpaynowException extends RuntimeException {
    private final IpaynowErrorCode code;

    public IpaynowException(IpaynowErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public IpaynowException(IpaynowErrorCode code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public IpaynowErrorCode code() {
        return code;
    }
}
