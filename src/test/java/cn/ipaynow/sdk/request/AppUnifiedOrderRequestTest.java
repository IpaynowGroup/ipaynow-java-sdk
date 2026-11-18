package cn.ipaynow.sdk.request;

import cn.ipaynow.sdk.domain.AppUnifiedOrderParams;
import cn.ipaynow.sdk.response.AppUnifiedOrderResponse;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AppUnifiedOrderRequestTest {
    @Test
    void buildsAppUnifiedOrderRequest() {
        AppUnifiedOrderRequest request = new AppUnifiedOrderRequest(
                AppUnifiedOrderParams.builder().mhtOrderNo("O5").mhtOrderAmt(5L).consumerId("U1").build()
        );

        RequestTestSupport.assertRequest(request, "WP001", "1.0.3", "mhtSignType", AppUnifiedOrderResponse.class);
        assertEquals("O5", request.params().get("mhtOrderNo"));
        assertEquals("5", request.params().get("mhtOrderAmt"));
        assertEquals("01", request.params().get("deviceType"));
        assertEquals("U1", request.params().get("consumerId"));
    }

    @Test
    void appUnifiedOrderMergesExtraParams() {
        Map<String, String> extra = new LinkedHashMap<String, String>();
        extra.put("bankCode", "CMB");
        AppUnifiedOrderParams params = AppUnifiedOrderParams.builder()
                .mhtOrderNo("O5")
                .mhtOrderAmt(5L)
                .extraParams(extra)
                .build();

        assertEquals("CMB", params.toMap().get("bankCode"));
        assertFalse(params.toMap().containsKey("extraParams"));
    }

    @Test
    void extraParamsCannotOverrideSdkFields() {
        Map<String, String> extra = new LinkedHashMap<String, String>();
        extra.put("funcode", "BAD");
        AppUnifiedOrderParams params = AppUnifiedOrderParams.builder()
                .mhtOrderNo("O5")
                .mhtOrderAmt(5L)
                .extraParams(extra)
                .build();

        assertThrows(IllegalArgumentException.class, params::toMap);
    }
}
