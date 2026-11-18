package cn.ipaynow.sdk.domain;

import cn.ipaynow.sdk.RequestParams;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

import cn.ipaynow.sdk.util.BeanMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JsapiUnifiedOrderParams implements RequestParams {

    /**
     * 客户端IP，选填
     */
    private String consumerCreateIp;
    /**
     * 消费者ID（outputType=1必填，渠道类型为11时不填）
     */
    private String consumerId;
    /**
     * 设备类型，固定值"0600"（公众号）
     */
    @Builder.Default
    private String deviceType = "0600";
    /**
     * 商户前台通知URL（outputType=0时必填），微信配合商家小票使用
     */
    private String frontNotifyUrl;
    /**
     * 商户订单币种类型，固定值"156"（人民币）
     */
    @Builder.Default
    private String mhtCurrencyType = "156";
    /**
     * 商品标记（用于营销活动）
     */
    private String mhtGoodsTag;
    /**
     * 营销活动
     * 单品 [{"goods_name":"","goods_id":"595315","quantity":"2","price":"100","wxpay_goods_id":""}]
     */
    private String mhtGoodsDetails;

    /**
     * 是否支持信用卡支付（0=不限制卡类型，1=不能使用信用卡）
     */
    private String mhtLimitPay;
    /**
     * 商户订单交易金额（单位：分，整数无小数点）
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
     * 商户订单号（必须唯一）
     */
    private String mhtOrderNo;
    /**
     * 商户订单开始时间（格式：yyyyMMddHHmmss）
     */
    private String mhtOrderStartTime;
    /**
     * 商户订单超时时间（秒，默认3600）
     */
    @Builder.Default
    private Long mhtOrderTimeOut = 120L;
    /**
     * 商户交易类型，固定值"05"（代理消费）
     */
    @Builder.Default
    private String mhtOrderType = "05";
    /**
     * 商户保留域
     */
    private String mhtReserved;
    /**
     * 商户appId（outputType=1且payChannelType=13/11时必传）
     */
    private String mhtSubAppId;
    /**
     * 商户后台通知URL（HTTP/HTTPS协议，POST方式）
     */
    private String notifyUrl;
    /**
     * 输出格式（0=直接调起支付，1=返回支付凭证）
     */
    private String outputType;
    /**
     * 用户所选渠道类型（12=支付宝，13=微信，11=银联）
     */
    private String payChannelType;
    private Map<String, String> extraParams;

    @Override
    public Map<String, String> toMap() {
        return BeanMap.of(this, extraParams);
    }
}
