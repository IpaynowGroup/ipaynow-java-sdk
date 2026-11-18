package cn.ipaynow.sdk;

/**
 * SDK 异常统一携带错误码，调用方可通过 {@link IpaynowException#code()} 分支处理，
 * 不需要解析异常消息。这里仅定义 SDK 实际会抛出的失败码；业务成功或失败从
 * {@link IpaynowResponse#isSuccess()} 读取。
 */
public enum IpaynowErrorCode {
    /** 响应类没有公开无参构造方法，属于 SDK 使用方式错误 */
    SYSTEM_ERROR("E0001", "组件内部异常"),
    /** HTTP 请求异常，包含连接失败、非 2xx 响应等 */
    HTTP_EXCEPTION("E0002", "HTTP异常"),
    /** 建立连接时超时 */
    CONNECT_TIMEOUT("E0003", "ConnectTimeOut"),
    /** 等待或读取响应时超时 */
    SOCKET_TIMEOUT("E0004", "SocketTimeOut"),
    /** 同步响应或回调通知签名缺失或验签失败 */
    SIGN_ERROR("E0012", "渠道报文验签失败");

    private final String code;
    private final String defaultMessage;

    IpaynowErrorCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public String code() {
        return code;
    }

    public String defaultMessage() {
        return defaultMessage;
    }
}
