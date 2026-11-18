package cn.ipaynow.sdk.util;

import cn.ipaynow.sdk.IpaynowConfig;

interface SignatureStrategy {
    String sign(String canonical, IpaynowConfig config);

    boolean verify(String canonical, String actual, IpaynowConfig config);
}
