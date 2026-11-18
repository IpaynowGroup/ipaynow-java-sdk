package cn.ipaynow.sdk.domain;

import cn.ipaynow.sdk.RequestParams;
import cn.ipaynow.sdk.util.BeanMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUnifiedOrderParams implements RequestParams {
    /**
     * 消费者下单ip，微信支付时必填
     */
    private String consumerCreateIp;
    /**
     * 消费者ID，payChannelType=17时必传
     */
    private String consumerId;
    /**
     * 消费者名称
     */
    private String consumerName;
    /**
     * 设备类型，定值01
     */
    @Builder.Default
    private String deviceType = "01";

    /**
     * 商户订单币种类型，156人民币
     */
    @Builder.Default
    private String mhtCurrencyType = "156";
    /**
     * 是否支持信用卡支付，0禁用，1允许
     */
    private String mhtLimitPay;
    /**
     * 商户订单交易金额，单位分（整数，无小数点）
     */
    private long mhtOrderAmt;
    /**
     * 商户订单详情
     */
    private String mhtOrderDetail;
    /**
     * 商户商品名称
     */
    private String mhtOrderName;
    /**
     * 商户订单号
     */
    private String mhtOrderNo;
    /**
     * 商户订单开始时间，格式yyyyMMddHHmmss
     */
    private String mhtOrderStartTime;
    /**
     * 商户订单超时时间，60~3600秒，默认3600
     */
    @Builder.Default
    private Long mhtOrderTimeOut = 120L;
    /**
     * 商户交易类型，05代理消费
     */
    @Builder.Default
    private String mhtOrderType = "05";
    /**
     * 商户保留域，格式mchBankId=123&商户自定义参数
     */
    private String mhtReserved;
    /**
     * 微信开放平台appId
     */
    private String mhtSubAppId;
    /**
     * 商户后台通知URL，HTTP或HTTPS协议，POST方式
     */
    private String notifyUrl;
    /**
     * 用户所选渠道类型，见渠道列表
     */
    private String payChannelType;
    @Builder.Default
    private String outputType = "0";
    /**
     * 商户前端跳转地址，当渠道是微众钱包(101)时必填
     */
    private String frontNotifyUrl;

    private String mhtStoreId;

    private Map<String, String> extraParams;

    public Map<String, String> toMap() {
        return BeanMap.of(this, extraParams);
    }
}
