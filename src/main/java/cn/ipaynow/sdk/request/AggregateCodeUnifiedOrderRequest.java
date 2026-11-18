package cn.ipaynow.sdk.request;

import cn.ipaynow.sdk.SpecRequest;
import cn.ipaynow.sdk.domain.AggregateCodeUnifiedOrderParams;
import cn.ipaynow.sdk.response.AggregateCodeUnifiedOrderResponse;

public class AggregateCodeUnifiedOrderRequest extends SpecRequest<AggregateCodeUnifiedOrderResponse> {
    public AggregateCodeUnifiedOrderRequest(AggregateCodeUnifiedOrderParams params) {
        super("WP001", "1.0.3", "UTF-8", params, AggregateCodeUnifiedOrderResponse.class, "mhtSignType");
    }

    public AggregateCodeUnifiedOrderRequest(String version, AggregateCodeUnifiedOrderParams params) {
        super("WP001", version, "UTF-8", params, AggregateCodeUnifiedOrderResponse.class, "mhtSignType");
    }
}
