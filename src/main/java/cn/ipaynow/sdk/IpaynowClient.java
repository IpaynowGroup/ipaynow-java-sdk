package cn.ipaynow.sdk;

import cn.ipaynow.sdk.util.Parser;
import cn.ipaynow.sdk.util.Signer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public final class IpaynowClient {
    private static final Logger log = LoggerFactory.getLogger(IpaynowClient.class);
    private final IpaynowConfig config;
    private final HttpTransport transport;

    public IpaynowClient(IpaynowConfig config) {
        this(config, new HttpTransport.Default(config.connectTimeoutMillis(), config.readTimeoutMillis()));
    }

    public IpaynowClient(IpaynowConfig config, HttpTransport transport) {
        this.config = config;
        this.transport = transport;
    }

    public <T extends IpaynowResponse> T execute(IpaynowRequest<T> request) {
        String funcode = request.funcode();
        boolean excludeSignType = request.excludeSignType();
        Map<String, String> params = signedFields(request);
        // 报文可能含用户/订单敏感信息，只在 DEBUG 级别输出，且签名值脱敏。
        if (log.isDebugEnabled()) {
            log.debug("功能码={}，请求报文={}", funcode, params);
        }

        // HttpTransport 实现会按错误类型直接抛出 IpaynowException，这里不再二次包装。
        String responseText = transport.post(config.endpoint(request.path()), Signer.clean(params));

        Map<String, String> fields = Parser.parseResponse(responseText);
        if (log.isDebugEnabled()) {
            log.debug("功能码={}，响应报文={}", funcode, fields);
        }
        if (!Signer.verify(fields, config, request.responseSignatureField(), excludeSignType)) {
            log.warn("功能码={}，签名缺失或验签失败", funcode);
            throw new IpaynowException(IpaynowErrorCode.SIGN_ERROR, "现在支付响应验签失败，功能码=" + funcode);
        }
        T response = newResponse(request.responseClass());
        response.load(fields, responseText);
        return response;
    }

    public Map<String, String> buildFormFields(IpaynowRequest<?> request) {
        return signedFields(request);
    }

    private Map<String, String> signedFields(IpaynowRequest<?> request) {
        Map<String, String> params = new LinkedHashMap<String, String>(request.params());
        params.put("funcode", request.funcode());
        params.put("version", request.version());
        params.put("appId", config.appId());
        params.put("mhtCharset", request.mhtCharset());
        params.put(request.signTypeField(), config.signType().signType());
        params.put(request.signatureField(), Signer.sign(params, config, request.excludeSignType(), request.signatureField(), request.responseSignatureField()));
        return Signer.clean(params);
    }

    private static <T extends IpaynowResponse> T newResponse(Class<T> type) {
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IpaynowException(IpaynowErrorCode.SYSTEM_ERROR,
                    "响应类必须提供公开无参构造方法: " + type.getName(), e);
        }
    }
}
