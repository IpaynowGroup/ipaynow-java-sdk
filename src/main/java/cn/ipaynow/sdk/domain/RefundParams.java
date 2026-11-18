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
public class RefundParams implements RequestParams {
    /**
     * 退款金额（单位：分，必须小于等于原订单金额）
     */
    private long amount;
    /**
     * 功能码，固定值"R001"
     */
    private String funcode;
    /**
     * 商户订单号（需退款的原订单号）
     */
    private String mhtOrderNo;
    /**
     * 商户退款单号（必须唯一）
     */
    private String mhtRefundNo;
    /**
     * 商户退款结果通知URL（HTTP/HTTPS协议，POST方式）
     */
    private String notifyUrl;
    /**
     * 商户一笔订单存在重复支付时通过现在支付原交易单号可以指定要退款的交易
     */
    private String oriTransId;
    /**
     * 退款原因
     */
    private String reason;
    private Map<String, String> extraParams;


    public Map<String, String> toMap() {
        return BeanMap.of(this, extraParams);
    }
}
