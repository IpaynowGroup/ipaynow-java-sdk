package cn.ipaynow.sdk.response;

import cn.ipaynow.sdk.IpaynowResponse;

public class UnifiedOrderResponse extends IpaynowResponse {
    /**
     * 返回下单响应里的支付要素。
     */
    public String getTn() {
        String tn = get("tn");
        return tn != null ? tn : get("TN");
    }

    /**
     * 返回现在支付流水号。
     */
    public String getNowPayOrderNo() {
        return get("nowPayOrderNo");
    }
}
