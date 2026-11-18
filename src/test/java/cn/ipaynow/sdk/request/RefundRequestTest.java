package cn.ipaynow.sdk.request;

import cn.ipaynow.sdk.domain.RefundParams;
import cn.ipaynow.sdk.response.RefundResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RefundRequestTest {
    @Test
    void buildsRefundRequest() {
        RefundRequest request = new RefundRequest(
                RefundParams.builder().mhtRefundNo("R1").amount(10L).reason("refund").build()
        );

        RequestTestSupport.assertRequest(request, "R001", "1.0.0", "signType", RefundResponse.class);
        assertEquals("R1", request.params().get("mhtRefundNo"));
        assertEquals("10", request.params().get("amount"));
        assertEquals("refund", request.params().get("reason"));
    }
}
