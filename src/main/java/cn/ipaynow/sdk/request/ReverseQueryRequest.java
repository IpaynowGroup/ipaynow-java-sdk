package cn.ipaynow.sdk.request;

import cn.ipaynow.sdk.SpecRequest;
import cn.ipaynow.sdk.domain.ReverseQueryParams;
import cn.ipaynow.sdk.response.ReverseQueryResponse;

public class ReverseQueryRequest extends SpecRequest<ReverseQueryResponse> {
    public ReverseQueryRequest(ReverseQueryParams params) {
        super("Q002", "1.0.0", "UTF-8", params, ReverseQueryResponse.class, "signType", "/refund/refundQuery", true);
    }

    public ReverseQueryRequest(String version, ReverseQueryParams params) {
        super("Q002", version, "UTF-8", params, ReverseQueryResponse.class, "signType", "/refund/refundQuery", true);
    }
}
