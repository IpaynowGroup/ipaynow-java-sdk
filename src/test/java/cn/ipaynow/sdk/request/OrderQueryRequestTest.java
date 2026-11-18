package cn.ipaynow.sdk.request;

import cn.ipaynow.sdk.domain.OrderQueryParams;
import cn.ipaynow.sdk.response.OrderQueryResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderQueryRequestTest {
    @Test
    void buildsOrderQueryRequest() {
        OrderQueryRequest request = new OrderQueryRequest(
                OrderQueryParams.builder().mhtOrderNo("O6").version("1.0.0").build()
        );

        RequestTestSupport.assertRequest(request, "MQ002", "1.0.0", "mhtSignType", OrderQueryResponse.class);
        assertEquals("O6", request.params().get("mhtOrderNo"));
    }
}
