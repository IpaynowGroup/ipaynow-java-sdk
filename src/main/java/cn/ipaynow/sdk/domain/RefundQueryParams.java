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
public class RefundQueryParams implements RequestParams {
    /**
     * 商户退款单号（需查询的退款单号）
     */
    private String mhtRefundNo;
    private Map<String, String> extraParams;

    public Map<String, String> toMap() {
        return BeanMap.of(this, extraParams);
    }
}
