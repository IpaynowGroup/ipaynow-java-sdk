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
public class ReverseParams implements RequestParams {
    /**
     * 撤销单号
     */
    private String mhtRefundNo;
    /**
     * 商户订单号（需撤销的原订单号）
     */
    private String mhtOrderNo;
    /**
     * 商户一笔订单存在重复支付时，通过现在支付原交易单号可以指定要撤销的交易
     */
    private String oriTransId;
    /**
     * 撤销原因
     */
    private String reason;
    private Map<String, String> extraParams;

    public Map<String, String> toMap() {
        return BeanMap.of(this, extraParams);
    }
}
