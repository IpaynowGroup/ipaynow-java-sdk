package cn.ipaynow.sdk.request;

import cn.ipaynow.sdk.domain.ReverseQueryParams;
import cn.ipaynow.sdk.response.ReverseQueryResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReverseQueryRequestTest {
    @Test
    void buildsReverseQueryRequest() {
        ReverseQueryRequest request = new ReverseQueryRequest(
                ReverseQueryParams.builder().mhtRefundNo("RQ1").build()
        );

        RequestTestSupport.assertRequest(request, "Q002", "1.0.0", "signType", ReverseQueryResponse.class);
        assertEquals("RQ1", request.params().get("mhtRefundNo"));
    }
}
