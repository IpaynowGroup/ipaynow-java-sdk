package cn.ipaynow.sdk.response;

import cn.ipaynow.sdk.IpaynowResponse;

public class OrderQueryResponse extends IpaynowResponse {

    public String getTransStatus() {
        return get("transStatus");
    }

    public String getNowPayOrderNo() {
        return get("nowPayOrderNo");
    }

    public String getPayTime() {
        return get("payTime");
    }

    public String getChannelOrderNo() {
        return get("channelOrderNo");
    }

}
