package cn.ipaynow.sdk.request;

import cn.ipaynow.sdk.SpecRequest;
import cn.ipaynow.sdk.domain.AppUnifiedOrderParams;
import cn.ipaynow.sdk.response.AppUnifiedOrderResponse;

public class AppUnifiedOrderRequest extends SpecRequest<AppUnifiedOrderResponse> {
    public AppUnifiedOrderRequest(AppUnifiedOrderParams params) {
        super("WP001", "1.0.3", "UTF-8", params, AppUnifiedOrderResponse.class, "mhtSignType");
    }
    public AppUnifiedOrderRequest(String version, AppUnifiedOrderParams params) {
        super("WP001", version, "UTF-8", params, AppUnifiedOrderResponse.class, "mhtSignType");
    }
}
