package cn.ipaynow.sdk;

import java.util.LinkedHashMap;
import java.util.Map;

public class IpaynowResponse {
    private Map<String, String> fields = new LinkedHashMap<String, String>();
    private String rawBody;

    public void load(Map<String, String> fields) {
        load(fields, null);
    }

    public void load(Map<String, String> fields, String rawBody) {
        this.fields = new LinkedHashMap<String, String>(fields);
        this.rawBody = rawBody;
    }

    public String get(String name) {
        return fields.get(name);
    }

    public Map<String, String> fields() {
        return new LinkedHashMap<String, String>(fields);
    }

    public String code() {
        return get("responseCode");
    }

    public String message() {
        return get("responseMsg");
    }

    public String rawBody() {
        return rawBody;
    }

    public boolean success() {
        return isSuccess();
    }

    /**
     * 根据现在支付同步响应的 responseCode 判断接口调用是否成功。
     * 回调通知使用不同字段，由子类覆盖此方法，见
     * {@link cn.ipaynow.sdk.response.PaymentNotifyResponse} 和
     * {@link cn.ipaynow.sdk.response.RefundNotifyResponse}.
     */
    public boolean isSuccess() {
        return "A001".equals(get("responseCode"));
    }
}
