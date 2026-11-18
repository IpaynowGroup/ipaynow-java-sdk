package cn.ipaynow.sdk.request;

import cn.ipaynow.sdk.SpecRequest;
import cn.ipaynow.sdk.domain.OrderQueryParams;
import cn.ipaynow.sdk.response.OrderQueryResponse;

import java.util.Map;

public class OrderQueryRequest extends SpecRequest<OrderQueryResponse> {
    public OrderQueryRequest(OrderQueryParams params) {
        super("MQ002", version(params), "UTF-8", params, OrderQueryResponse.class, "mhtSignType");
    }

    private static String version(OrderQueryParams params) {
        Map<String, String> fields = params.toMap();
        return fields.containsKey("version") ? fields.get("version") : "1.0.0";
    }
}
