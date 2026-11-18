package cn.ipaynow.sdk.request;

import cn.ipaynow.sdk.domain.H5UnifiedOrderParams;
import cn.ipaynow.sdk.response.H5UnifiedOrderResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class H5UnifiedOrderRequestTest {
    @Test
    void buildsH5UnifiedOrderRequest() {
        H5UnifiedOrderRequest request = new H5UnifiedOrderRequest(
                H5UnifiedOrderParams.builder().mhtOrderNo("O4").mhtOrderAmt(4L).frontNotifyUrl("https://front.test").build()
        );

        RequestTestSupport.assertRequest(request, "WP001", "1.0.0", "mhtSignType", H5UnifiedOrderResponse.class);
        assertEquals("O4", request.params().get("mhtOrderNo"));
        assertEquals("4", request.params().get("mhtOrderAmt"));
        assertEquals("0601", request.params().get("deviceType"));
        assertEquals("https://front.test", request.params().get("frontNotifyUrl"));
    }
}
