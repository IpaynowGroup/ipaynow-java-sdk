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
public class OrderQueryParams implements RequestParams {
    /**
     * 设备类型
     */
    private String deviceType;
    /**
     * 商户订单号（需查询的订单号）
     */
    private String mhtOrderNo;
    /**
     * 接口版本号，固定值"1.0.0"
     */
    private String version;
    private Map<String, String> extraParams;

    public Map<String, String> toMap() {
        return BeanMap.of(this, extraParams);
    }
}
