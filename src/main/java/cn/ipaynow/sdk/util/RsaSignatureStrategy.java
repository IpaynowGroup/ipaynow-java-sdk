package cn.ipaynow.sdk.util;

import cn.ipaynow.sdk.IpaynowConfig;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

final class RsaSignatureStrategy implements SignatureStrategy {
    private static final String ALGORITHM = "SHA256withRSA";

    @Override
    public String sign(String canonical, IpaynowConfig config) {
        return sign(canonical, config.mchPrivateKeyObject());
    }

    @Override
    public boolean verify(String canonical, String actual, IpaynowConfig config) {
        return verify(canonical, actual, config.ipaynowPublicKeyObject());
    }

    String sign(String canonical, PrivateKey privateKey) {
        try {
            Signature signature = Signature.getInstance(ALGORITHM);
            signature.initSign(privateKey);
            signature.update(canonical.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(signature.sign());
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("RSA 签名失败", e);
        }
    }

    boolean verify(String canonical, String actual, PublicKey publicKey) {
        try {
            Signature signature = Signature.getInstance(ALGORITHM);
            signature.initVerify(publicKey);
            signature.update(canonical.getBytes(StandardCharsets.UTF_8));
            return signature.verify(Base64.getDecoder().decode(actual));
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("RSA 验签失败", e);
        }
    }
}
