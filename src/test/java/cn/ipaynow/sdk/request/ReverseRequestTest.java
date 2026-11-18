package cn.ipaynow.sdk.request;

import cn.ipaynow.sdk.domain.ReverseParams;
import cn.ipaynow.sdk.response.ReverseResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReverseRequestTest {
    @Test
    void buildsReverseRequest() {
        ReverseRequest request = new ReverseRequest(
                ReverseParams.builder().mhtOrderNo("O7").oriTransId("T1").reason("reverse").build()
        );

        RequestTestSupport.assertRequest(request, "R002", "1.0.0", "signType", ReverseResponse.class);
        assertEquals("O7", request.params().get("mhtOrderNo"));
        assertEquals("T1", request.params().get("oriTransId"));
        assertEquals("reverse", request.params().get("reason"));
    }
}
