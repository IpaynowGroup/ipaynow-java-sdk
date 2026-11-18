package cn.ipaynow.sdk.request;

import cn.ipaynow.sdk.SpecRequest;
import cn.ipaynow.sdk.domain.ReverseParams;
import cn.ipaynow.sdk.response.ReverseResponse;

public class ReverseRequest extends SpecRequest<ReverseResponse> {
    public ReverseRequest(ReverseParams params) {
        super("R002", "1.0.0", "UTF-8", params, ReverseResponse.class, "signType", "/refund/cancelOrder", true);
    }

    public ReverseRequest(String version, ReverseParams params) {
        super("R002", version, "UTF-8", params, ReverseResponse.class, "signType", "/refund/cancelOrder", true);
    }
}
