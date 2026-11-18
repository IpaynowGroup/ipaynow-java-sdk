package cn.ipaynow.sdk.response;

import cn.ipaynow.sdk.IpaynowResponse;

public class PaymentNotifyResponse extends IpaynowResponse {
    @Override
    public boolean isSuccess() {
        return "A001".equals(getStatus());
    }

    public String getTransStatus() {
        return get("transStatus");
    }

    /**
     * 部分通道（如支付宝 APP 支付）下发的是 tradeStatus 而非 transStatus。
     */
    public String getTradeStatus() {
        return get("tradeStatus");
    }

    /**
     * 交易状态：优先取 transStatus，缺失时回退 tradeStatus。
     */
    public String getStatus() {
        String transStatus = get("transStatus");
        return transStatus == null || transStatus.isEmpty() ? get("tradeStatus") : transStatus;
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
