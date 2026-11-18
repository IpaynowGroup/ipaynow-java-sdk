package cn.ipaynow.sdk.util;

import cn.ipaynow.sdk.IpaynowConfig;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Locale;

final class Md5SignatureStrategy implements SignatureStrategy {
    @Override
    public String sign(String canonical, IpaynowConfig config) {
        return Signer.md5Hex(canonical + "&" + Signer.md5Hex(config.appKey()));
    }

    @Override
    public boolean verify(String canonical, String actual, IpaynowConfig config) {
        String expected = sign(canonical, config);
        return MessageDigest.isEqual(
                expected.getBytes(StandardCharsets.UTF_8),
                actual.toLowerCase(Locale.ROOT).getBytes(StandardCharsets.UTF_8)
        );
    }
}
