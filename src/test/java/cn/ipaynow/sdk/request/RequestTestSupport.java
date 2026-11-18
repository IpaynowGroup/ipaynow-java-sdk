package cn.ipaynow.sdk.request;

import cn.ipaynow.sdk.IpaynowRequest;
import cn.ipaynow.sdk.IpaynowResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class RequestTestSupport {
    private RequestTestSupport() {}

    static <T extends IpaynowResponse> void assertRequest(
            IpaynowRequest<T> request,
            String funcode,
            String version,
            String signTypeField,
            Class<T> responseClass
    ) {
        assertEquals(funcode, request.funcode());
        assertEquals(version, request.version());
        assertEquals("UTF-8", request.mhtCharset());
        assertEquals(signTypeField, request.signTypeField());
        assertEquals("mhtSignature", request.signatureField());
        assertEquals("signature", request.responseSignatureField());
        assertEquals(responseClass, request.responseClass());
    }
}
