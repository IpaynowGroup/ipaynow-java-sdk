package cn.ipaynow.sdk.request;

import cn.ipaynow.sdk.domain.AggregateCodeUnifiedOrderParams;
import cn.ipaynow.sdk.response.AggregateCodeUnifiedOrderResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AggregateCodeUnifiedOrderRequestTest {
    @Test
    void buildsAggregateCodeUnifiedOrderRequest() {
        AggregateCodeUnifiedOrderRequest request = new AggregateCodeUnifiedOrderRequest(
                AggregateCodeUnifiedOrderParams.builder().mhtOrderNo("O2").mhtOrderAmt(2L).build()
        );

        RequestTestSupport.assertRequest(request, "WP001", "1.0.3", "mhtSignType", AggregateCodeUnifiedOrderResponse.class);
        assertEquals("O2", request.params().get("mhtOrderNo"));
        assertEquals("2", request.params().get("mhtOrderAmt"));
        assertEquals("20", request.params().get("deviceType"));
        assertEquals("156", request.params().get("mhtCurrencyType"));
    }
}
