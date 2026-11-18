package cn.ipaynow.sdk.request;

import cn.ipaynow.sdk.domain.JsapiUnifiedOrderParams;
import cn.ipaynow.sdk.response.JsapiUnifiedOrderResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsapiUnifiedOrderRequestTest {
    @Test
    void buildsJsapiUnifiedOrderRequest() {
        JsapiUnifiedOrderRequest request = new JsapiUnifiedOrderRequest(
                JsapiUnifiedOrderParams.builder().mhtOrderNo("O1").mhtOrderAmt(1L).build()
        );

        RequestTestSupport.assertRequest(request, "WP001", "1.0.0", "mhtSignType", JsapiUnifiedOrderResponse.class);
        assertEquals("O1", request.params().get("mhtOrderNo"));
        assertEquals("1", request.params().get("mhtOrderAmt"));
        assertEquals("0600", request.params().get("deviceType"));
        assertEquals("156", request.params().get("mhtCurrencyType"));
    }
}
