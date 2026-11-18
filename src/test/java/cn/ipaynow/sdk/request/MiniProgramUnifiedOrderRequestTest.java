package cn.ipaynow.sdk.request;

import cn.ipaynow.sdk.domain.MiniProgramUnifiedOrderParams;
import cn.ipaynow.sdk.response.MiniProgramUnifiedOrderResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class MiniProgramUnifiedOrderRequestTest {
    @Test
    void buildsMiniProgramUnifiedOrderRequest() {
        MiniProgramUnifiedOrderRequest request = new MiniProgramUnifiedOrderRequest(
                MiniProgramUnifiedOrderParams.builder().mhtOrderNo("O3").mhtOrderAmt(3L).consumerId("OPENID").build()
        );

        RequestTestSupport.assertRequest(request, "WP001", "1.0.0", "mhtSignType", MiniProgramUnifiedOrderResponse.class);
        assertEquals("O3", request.params().get("mhtOrderNo"));
        assertEquals("3", request.params().get("mhtOrderAmt"));
        assertEquals("14", request.params().get("deviceType"));
        assertEquals("1", request.params().get("outputType"));
        assertEquals("OPENID", request.params().get("consumerId"));
    }

    @Test
    void optionalAmountsAreOmittedWhenUnset() {
        MiniProgramUnifiedOrderParams params = MiniProgramUnifiedOrderParams.builder()
                .mhtOrderNo("O3")
                .mhtOrderAmt(3L)
                .consumerId("OPENID")
                .build();

        assertFalse(params.toMap().containsKey("discountAmt"));
        assertFalse(params.toMap().containsKey("oriMhtOrderAmt"));
    }
}
