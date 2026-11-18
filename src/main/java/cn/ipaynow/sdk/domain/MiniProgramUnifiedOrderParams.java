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
public class MiniProgramUnifiedOrderParams implements RequestParams {

    /**
     * 消费者ID（outputType=1必填，微信openid或支付宝buyer_id）
     */
    private String consumerId;
    /**
     * 设备类型（14：小程序支付，24：小程序收银台）
     */
    @Builder.Default
    private String deviceType = "14";
    /**
     * 订单优惠金额（单位：分）
     */
    private Long discountAmt;
    /**
     * 商户订单币种类型
     */
    @Builder.Default
    private String mhtCurrencyType = "156";
    /**
     * 商品标记（用于营销活动）
     */
    private String mhtGoodsTag;
    /**
     * 优惠，[{"goods_id":"595315","quantity":"2","price":"100","wxpay_goods_id":""}]
     */
    private String mhtGoodsDetails;
    /**
     * 是否支持信用卡支付（0：不支持，1：允许）
     */
    private String mhtLimitPay;
    /**
     * 用户实付金额（单位：分）
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
     * 商户订单开始时间（格式：yyyyMMddHHmmss）
     */
    private String mhtOrderStartTime;
    /**
     * 商户订单超时时间（秒，默认3600）
     */
    @Builder.Default
    private Long mhtOrderTimeOut = 120L;
    /**
     * 商户交易类型（05：代理消费）
     */
    @Builder.Default
    private String mhtOrderType = "05";
    /**
     * 商户保留域
     */
    private String mhtReserved;
    /**
     * 商户appId（deviceType=14且payChannelType=13/11时必传）
     */
    private String mhtSubAppId;

    /**
     * 商户后台通知URL
     */
    private String notifyUrl;
    /**
     * 订单原始金额（单位：分）
     */
    private Long oriMhtOrderAmt;
    /**
     * 输出格式（固定值1）
     */
    @Builder.Default
    private String outputType = "1";
    /**
     * 用户所选渠道类型（13：微信，12：支付宝，11：银联）
     */
    private String payChannelType;

    private String mhtStoreId;

    private Map<String, String> extraParams;

    public Map<String, String> toMap() {
        return BeanMap.of(this, extraParams);
    }
}
