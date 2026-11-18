package cn.ipaynow.sdk.request;

import cn.ipaynow.sdk.domain.RefundQueryParams;
import cn.ipaynow.sdk.response.RefundQueryResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RefundQueryRequestTest {
    @Test
    void buildsRefundQueryRequest() {
        RefundQueryRequest request = new RefundQueryRequest(
                RefundQueryParams.builder().mhtRefundNo("R2").build()
        );

        RequestTestSupport.assertRequest(request, "Q001", "1.0.0", "signType", RefundQueryResponse.class);
        assertEquals("R2", request.params().get("mhtRefundNo"));
    }
}
