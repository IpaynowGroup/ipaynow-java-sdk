package cn.ipaynow.sdk.request;

import cn.ipaynow.sdk.SpecRequest;
import cn.ipaynow.sdk.domain.JsapiUnifiedOrderParams;
import cn.ipaynow.sdk.response.JsapiUnifiedOrderResponse;

public class JsapiUnifiedOrderRequest extends SpecRequest<JsapiUnifiedOrderResponse> {
    public JsapiUnifiedOrderRequest(JsapiUnifiedOrderParams params) {
        super("WP001", "1.0.0", "UTF-8", params, JsapiUnifiedOrderResponse.class, "mhtSignType");
    }

    public JsapiUnifiedOrderRequest(String version, JsapiUnifiedOrderParams params) {
        super("WP001", version, "UTF-8", params, JsapiUnifiedOrderResponse.class, "mhtSignType");
    }
}
