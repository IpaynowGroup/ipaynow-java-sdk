package cn.ipaynow.sdk.request;

import cn.ipaynow.sdk.SpecRequest;
import cn.ipaynow.sdk.domain.H5UnifiedOrderParams;
import cn.ipaynow.sdk.response.H5UnifiedOrderResponse;

public class H5UnifiedOrderRequest extends SpecRequest<H5UnifiedOrderResponse> {
    public H5UnifiedOrderRequest(H5UnifiedOrderParams params) {
        super("WP001", "1.0.0", "UTF-8", params, H5UnifiedOrderResponse.class, "mhtSignType");
    }

    public H5UnifiedOrderRequest(String version, H5UnifiedOrderParams params) {
        super("WP001", version, "UTF-8", params, H5UnifiedOrderResponse.class, "mhtSignType");
    }
}
