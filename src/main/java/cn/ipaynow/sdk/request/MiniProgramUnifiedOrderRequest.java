package cn.ipaynow.sdk.request;

import cn.ipaynow.sdk.SpecRequest;
import cn.ipaynow.sdk.domain.MiniProgramUnifiedOrderParams;
import cn.ipaynow.sdk.response.MiniProgramUnifiedOrderResponse;

public class MiniProgramUnifiedOrderRequest extends SpecRequest<MiniProgramUnifiedOrderResponse> {
    public MiniProgramUnifiedOrderRequest(MiniProgramUnifiedOrderParams params) {
        super("WP001", "1.0.0", "UTF-8", params, MiniProgramUnifiedOrderResponse.class, "mhtSignType");
    }

    public MiniProgramUnifiedOrderRequest(String version, MiniProgramUnifiedOrderParams params) {
        super("WP001", version, "UTF-8", params, MiniProgramUnifiedOrderResponse.class, "mhtSignType");
    }
}
