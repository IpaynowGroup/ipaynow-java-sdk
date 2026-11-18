package cn.ipaynow.sdk;

import java.util.Map;

public interface IpaynowRequest<T extends IpaynowResponse> {
    String funcode();
    String version();
    String mhtCharset();
    Map<String, String> params();
    Class<T> responseClass();

    String signTypeField();
    String signatureField();
    String responseSignatureField();

    default String path() {
        return "/";
    }

    default boolean excludeSignType() {
        return false;
    }
}
