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
public class AggregateCodeUnifiedOrderParams implements RequestParams {

    /**
     * 支付场景
     */
    @Builder.Default
    private String deviceType = "20";
    /**
     * 商户订单币种类型
     */
    @Builder.Default
    private String mhtCurrencyType = "156";
    /**
     * 是否支持信用卡支付（0：不限制，1：禁用）
     */
    private String mhtLimitPay;
    /**
     * 商户订单交易金额（单位：分）
     */
    private long mhtOrderAmt;
    /**
     * 商户订单详情
     */
    private String mhtOrderDetail;
    /**
     * 商户订单名称
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
     * 商户订单类型 05-代理消费
     */
    @Builder.Default
    private String mhtOrderType = "05";
    /**
     * 商户保留域
     */
    private String mhtReserved;
    /**
     * 商户后台通知URL
     */
    private String notifyUrl;
    /**
     * 输出类型（0：返回二维码串，1：返回支付链接， 默认1，返回支付链接）
     */
    private String outputType;
    private Map<String, String> extraParams;

    public Map<String, String> toMap() {
        return BeanMap.of(this, extraParams);
    }
}
