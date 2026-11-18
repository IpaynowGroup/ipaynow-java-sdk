package cn.ipaynow.sdk.response;

import cn.ipaynow.sdk.IpaynowResponse;

public class RefundNotifyResponse extends IpaynowResponse {
    @Override
    public boolean isSuccess() {
        return "A001".equals(get("tradeStatus"));
    }


    public String getTradeStatus() {
        return get("tradeStatus");
    }

    /**
     * @deprecated 字段名误写，实际返回 tradeStatus，请改用 {@link #getTradeStatus()}。
     */
    @Deprecated
    public String getTransStatus() {
        return getTradeStatus();
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
