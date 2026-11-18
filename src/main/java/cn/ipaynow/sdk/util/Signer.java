package cn.ipaynow.sdk.util;

import cn.ipaynow.sdk.IpaynowConfig;
import cn.ipaynow.sdk.SignTypeEnum;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public final class Signer {
    private static final char[] HEX = "0123456789abcdef".toCharArray();
    private static final SignatureStrategy MD5_STRATEGY = new Md5SignatureStrategy();
    private static final SignatureStrategy RSA_STRATEGY = new RsaSignatureStrategy();

    private Signer() {}

    /**
     * 正向交易（WP001/MQ002/N001）的 signType 参与签名。
     * 退款和撤销链路（R001/Q001/R002/Q002/RN001）的 signType 不参与签名。
     * mhtSignType 是正向交易请求里的签名类型字段，字段名不同，不会被排除。
     */
    public static String sign(Map<String, String> params, IpaynowConfig config, boolean excludeSignType, String... signatureFields) {
        return strategy(config.signType()).sign(canonical(params, excludeSignType, signatureFields), config);
    }

    public static boolean verify(Map<String, String> params, IpaynowConfig config, String signatureField, boolean excludeSignType) {
        String actual = params.get(signatureField);
        if (actual == null || actual.isEmpty()) {
            return false;
        }
        String actualSignType = params.get("signType");
        if (actualSignType != null && !actualSignType.isEmpty()
                && !config.signType().signType().equalsIgnoreCase(actualSignType)) {
            return false;
        }
        return strategy(config.signType()).verify(
                canonical(params, excludeSignType, signatureField, "mhtSignature"),
                actual,
                config
        );
    }

    public static Map<String, String> clean(Map<String, String> params) {
        Map<String, String> out = new LinkedHashMap<String, String>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()) out.put(entry.getKey(), entry.getValue());
        }
        return out;
    }

    static String canonical(Map<String, String> params, boolean excludeSignType, String... signatureFields) {
        Map<String, String> cleaned = clean(params);
        if (excludeSignType) {
            cleaned.remove("signType");
        }
        cleaned.remove("mhtSignature");
        cleaned.remove("signature");
        for (String field : signatureFields) cleaned.remove(field);
        StringBuilder canonical = new StringBuilder();
        for (Map.Entry<String, String> entry : new TreeMap<String, String>(cleaned).entrySet()) {
            if (canonical.length() > 0) canonical.append('&');
            canonical.append(entry.getKey()).append('=').append(entry.getValue());
        }
        return canonical.toString();
    }

    static String md5Hex(String value) {
        try {
            byte[] bytes = java.security.MessageDigest.getInstance("MD5").digest(value.getBytes(StandardCharsets.UTF_8));
            return toHex(bytes);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private static SignatureStrategy strategy(SignTypeEnum signType) {
        return signType == SignTypeEnum.RSA ? RSA_STRATEGY : MD5_STRATEGY;
    }

    private static String toHex(byte[] bytes) {
        char[] out = new char[bytes.length * 2];
        for (int i = 0, j = 0; i < bytes.length; i++) {
            out[j++] = HEX[(bytes[i] >> 4) & 0xF];
            out[j++] = HEX[bytes[i] & 0xF];
        }
        return new String(out);
    }
}
