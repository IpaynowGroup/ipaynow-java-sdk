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
public class H5UnifiedOrderParams implements RequestParams {
    /**
     * 用户支付ip，微信渠道特定outputType时必填
     */
    private String consumerCreateIp;
    /**
     * 消费者id，招行一网通渠道必填
     */
    private String consumerId;
    /**
     * 设备类型，固定值
     */
    @Builder.Default
    private String deviceType = "0601";
    /**
     * 商户前台通知URL，HTTPS协议
     */
    private String frontNotifyUrl;
    /**
     * 商户订单币种类型，固定值
     */
    @Builder.Default
    private String mhtCurrencyType = "156";
    /**
     * 是否支持信用卡支付，0禁用，1允许，默认0
     */
    private String mhtLimitPay;
    /**
     * 商户订单交易金额，单位分
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
     * 商户交易类型，固定值05 代理消费
     */
    @Builder.Default
    private String mhtOrderType = "05";
    /**
     * 商户保留域，给商户使用的字段，商户可以
     * H5 支付商户接口规范
     * 13 / 27
     * 对交易进行标记，必须严格遵
     * 循以下格式：mchBankId=123&
     * 商户自定义参数，见附录 6.9
     * 保留域说明
     */
    private String mhtReserved;
    /**
     * 商户后台通知URL，HTTPS协议
     */
    private String notifyUrl;
    /**
     * 输出格式，取值参考支付宝和微信对应说明
     */
    private String outputType;
    /**
     * 用户所选渠道类型，银联:20，支付宝:12，微信:13等
     */
    private String payChannelType;

    private String specifiedChannel;

    private String payAccNo;

    private String guideId;

    private Map<String, String> extraParams;

    public Map<String, String> toMap() {
        return BeanMap.of(this, extraParams);
    }
}
