package cn.ipaynow.sdk;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public final class IpaynowConfig {
    private final String appId;
    private final String appKey;
    private final String mchPrivateKey;
    private final String ipaynowPublicKey;
    private final PrivateKey mchPrivateKeyObject;
    private final PublicKey ipaynowPublicKeyObject;
    private final SignTypeEnum signType;
    private final String baseUrl;
    private final long connectTimeoutMillis;
    private final long readTimeoutMillis;

    public IpaynowConfig(String appId, String appKey) {
        this(appId, appKey, "https://pay.ipaynow.cn");
    }

    public IpaynowConfig(String appId, String appKey, String baseUrl) {
        this(appId, appKey, null, null, SignTypeEnum.MD5,
                baseUrl, 5000L, 15000L);
    }

    public IpaynowConfig(String appId, SignTypeEnum signType, String mchPrivateKey, String ipaynowPublicKey,
                         String baseUrl,
                         long connectTimeoutMillis, long readTimeoutMillis) {
        this(appId, null, mchPrivateKey, ipaynowPublicKey, signType, baseUrl, connectTimeoutMillis, readTimeoutMillis);
    }

    public IpaynowConfig(String appId, String appKey, String mchPrivateKey, String ipaynowPublicKey, SignTypeEnum signType,
                         String baseUrl,
                         long connectTimeoutMillis, long readTimeoutMillis) {
        this.appId = requireNonBlank(appId, "appId");
        this.signType = signType == null ? SignTypeEnum.MD5 : signType;
        if (this.signType == SignTypeEnum.MD5) {
            this.appKey = requireNonBlank(appKey, "appKey");
            this.ipaynowPublicKey = null;
            this.mchPrivateKey = null;
            this.mchPrivateKeyObject = null;
            this.ipaynowPublicKeyObject = null;
        } else if (this.signType == SignTypeEnum.RSA) {
            this.appKey = null;
            this.mchPrivateKey = requireNonBlank(mchPrivateKey, "mchPrivateKey");
            this.ipaynowPublicKey = requireNonBlank(ipaynowPublicKey, "ipaynowPublicKey");
            this.mchPrivateKeyObject = parsePrivateKey(this.mchPrivateKey);
            this.ipaynowPublicKeyObject = parsePublicKey(this.ipaynowPublicKey);
        } else {
            throw new IllegalArgumentException("不支持的签名类型: " + this.signType);
        }
        this.baseUrl = requireNonBlank(baseUrl, "baseUrl");
        if (connectTimeoutMillis <= 0 || readTimeoutMillis <= 0) {
            throw new IllegalArgumentException("超时时间必须大于 0：connectTimeoutMillis="
                    + connectTimeoutMillis + "，readTimeoutMillis=" + readTimeoutMillis);
        }
        this.connectTimeoutMillis = connectTimeoutMillis;
        this.readTimeoutMillis = readTimeoutMillis;
    }

    private static String requireNonBlank(String value, String name) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(name + " 不能为空");
        }
        return value;
    }

    public String appId() {
        return appId;
    }

    public String appKey() {
        return appKey;
    }

    public String baseUrl() {
        return baseUrl;
    }

    public String mchPrivateKey() {
        return mchPrivateKey;
    }

    public String ipaynowPublicKey() {
        return ipaynowPublicKey;
    }

    public PrivateKey mchPrivateKeyObject() {
        return mchPrivateKeyObject;
    }

    public PublicKey ipaynowPublicKeyObject() {
        return ipaynowPublicKeyObject;
    }

    public SignTypeEnum signType() {
        return signType;
    }

    public String endpoint(String path) {
        String normalizedPath = path == null || path.isEmpty() ? "/" : path;
        if (!normalizedPath.startsWith("/")) {
            normalizedPath = "/" + normalizedPath;
        }
        return trimRight(baseUrl) + normalizedPath;
    }

    public long connectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public long readTimeoutMillis() {
        return readTimeoutMillis;
    }

    private static String trimRight(String value) {
        return value.replaceAll("/+$", "");
    }

    private static PrivateKey parsePrivateKey(String base64) {
        try {
            return KeyFactory.getInstance("RSA").generatePrivate(
                    new PKCS8EncodedKeySpec(decodeBase64(base64, "mchPrivateKey"))
            );
        } catch (GeneralSecurityException e) {
            throw new IllegalArgumentException("mchPrivateKey 非法，需传裸 base64 PKCS8 私钥", e);
        }
    }

    private static PublicKey parsePublicKey(String base64) {
        try {
            return KeyFactory.getInstance("RSA").generatePublic(
                    new X509EncodedKeySpec(decodeBase64(base64, "ipaynowPublicKey"))
            );
        } catch (GeneralSecurityException e) {
            throw new IllegalArgumentException("ipaynowPublicKey 非法，需传裸 base64 X509 公钥", e);
        }
    }

    private static byte[] decodeBase64(String value, String name) {
        if (value.contains("-----BEGIN") || value.contains("-----END")) {
            throw new IllegalArgumentException(name + " 仅支持裸 base64，不支持 PEM");
        }
        try {
            return Base64.getDecoder().decode(value.replaceAll("\\s+", ""));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(name + " 非法，需传裸 base64", e);
        }
    }
}
