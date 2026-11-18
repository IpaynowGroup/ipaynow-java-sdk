package cn.ipaynow.sdk.request;

import cn.ipaynow.sdk.SpecRequest;
import cn.ipaynow.sdk.domain.RefundQueryParams;
import cn.ipaynow.sdk.response.RefundQueryResponse;

public class RefundQueryRequest extends SpecRequest<RefundQueryResponse> {
    public RefundQueryRequest(RefundQueryParams params) {
        super("Q001", "1.0.0", "UTF-8", params, RefundQueryResponse.class, "signType", "/refund/refundQuery", true);
    }
    public RefundQueryRequest(String version, RefundQueryParams params) {
        super("Q001", version, "UTF-8", params, RefundQueryResponse.class, "signType", "/refund/refundQuery", true);
    }
}
