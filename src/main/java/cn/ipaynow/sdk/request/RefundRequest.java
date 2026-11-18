package cn.ipaynow.sdk.request;

import cn.ipaynow.sdk.SpecRequest;
import cn.ipaynow.sdk.domain.RefundParams;
import cn.ipaynow.sdk.response.RefundResponse;

public class RefundRequest extends SpecRequest<RefundResponse> {
    public RefundRequest(RefundParams params) {
        super("R001", "1.0.0", "UTF-8", params, RefundResponse.class, "signType", "/refund/refundOrder", true);
    }

    public RefundRequest(String version, RefundParams params) {
        super("R001", version, "UTF-8", params, RefundResponse.class, "signType", "/refund/refundOrder", true);
    }
}
