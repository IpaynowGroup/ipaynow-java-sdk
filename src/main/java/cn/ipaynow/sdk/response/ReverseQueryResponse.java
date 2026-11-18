package cn.ipaynow.sdk.response;

import cn.ipaynow.sdk.IpaynowResponse;

public class ReverseQueryResponse extends IpaynowResponse {
    @Override
    public boolean isSuccess() {
        return "R000".equals(get("responseCode"));
    }
}
