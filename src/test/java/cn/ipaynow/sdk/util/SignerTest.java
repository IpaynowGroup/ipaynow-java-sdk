package cn.ipaynow.sdk.util;

import cn.ipaynow.sdk.IpaynowConfig;
import cn.ipaynow.sdk.SignTypeEnum;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SignerTest {
    @Test
    void rsaSignAndVerifyUseConfiguredStrategy() throws Exception {
        KeyPair merchant = generateKeyPair();
        KeyPair platform = generateKeyPair();
        IpaynowConfig config = new IpaynowConfig(
                "app",
                SignTypeEnum.RSA,
                Base64.getEncoder().encodeToString(merchant.getPrivate().getEncoded()),
                Base64.getEncoder().encodeToString(platform.getPublic().getEncoded()),
                "https://pay.test",
                5000L,
                15000L
        );

        Map<String, String> requestFields = new LinkedHashMap<String, String>();
        requestFields.put("mhtOrderNo", "O1");
        requestFields.put("signType", "RSA");
        String requestSignature = Signer.sign(requestFields, config, true);
        assertFalse(requestSignature.matches("[0-9a-f]{32}"));

        Map<String, String> responseFields = new LinkedHashMap<String, String>();
        responseFields.put("responseCode", "A001");
        responseFields.put("signType", "RSA");
        responseFields.put("signature", signWithPrivateKey(responseFields, true, platform.getPrivate(), "signature", "mhtSignature"));

        assertTrue(Signer.verify(responseFields, config, "signature", true));
    }

    @Test
    void excludeSignTypeTrueStripsSignTypeFromCanonicalString() {
        IpaynowConfig config = md5Config();
        Map<String, String> withSignType = new LinkedHashMap<String, String>();
        withSignType.put("mhtOrderNo", "O1");
        withSignType.put("signType", "MD5");
        Map<String, String> withoutSignType = new LinkedHashMap<String, String>();
        withoutSignType.put("mhtOrderNo", "O1");

        assertEquals(Signer.sign(withoutSignType, config, true), Signer.sign(withSignType, config, true));
    }

    @Test
    void excludeSignTypeFalseKeepsSignTypeInCanonicalString() {
        IpaynowConfig config = md5Config();
        Map<String, String> withSignType = new LinkedHashMap<String, String>();
        withSignType.put("mhtOrderNo", "O1");
        withSignType.put("signType", "MD5");
        Map<String, String> withoutSignType = new LinkedHashMap<String, String>();
        withoutSignType.put("mhtOrderNo", "O1");

        assertFalse(Signer.sign(withoutSignType, config, false).equals(Signer.sign(withSignType, config, false)));
    }

    @Test
    void mhtSignTypeAlwaysParticipatesRegardlessOfExcludeSignTypeFlag() {
        IpaynowConfig config = md5Config();
        Map<String, String> withMhtSignType = new LinkedHashMap<String, String>();
        withMhtSignType.put("mhtOrderNo", "O1");
        withMhtSignType.put("mhtSignType", "MD5");
        Map<String, String> without = new LinkedHashMap<String, String>();
        without.put("mhtOrderNo", "O1");

        assertFalse(Signer.sign(without, config, true).equals(Signer.sign(withMhtSignType, config, true)));
        assertFalse(Signer.sign(without, config, false).equals(Signer.sign(withMhtSignType, config, false)));
    }

    @Test
    void verifyAcceptsCorrectSignature() {
        IpaynowConfig config = md5Config();
        Map<String, String> fields = new LinkedHashMap<String, String>();
        fields.put("responseCode", "A001");
        fields.put("signature", Signer.sign(fields, config, false));

        assertTrue(Signer.verify(fields, config, "signature", false));
    }

    @Test
    void verifyRejectsWrongSignature() {
        IpaynowConfig config = md5Config();
        Map<String, String> fields = new LinkedHashMap<String, String>();
        fields.put("responseCode", "A001");
        fields.put("signature", "deadbeefdeadbeefdeadbeefdeadbeef");

        assertFalse(Signer.verify(fields, config, "signature", false));
    }

    @Test
    void verifyRejectsMissingSignature() {
        IpaynowConfig config = md5Config();
        Map<String, String> fields = new LinkedHashMap<String, String>();
        fields.put("responseCode", "A001");

        assertFalse(Signer.verify(fields, config, "signature", false));
    }

    @Test
    void verifyForwardResponseIncludesSignTypeInSignature() {
        IpaynowConfig config = md5Config();
        // 正向交易响应中的 signType 参与签名，验签时必须保留在待签名串里。
        Map<String, String> fields = new LinkedHashMap<String, String>();
        fields.put("responseCode", "A001");
        fields.put("signType", "MD5");
        fields.put("signature", Signer.sign(fields, config, false));

        assertTrue(Signer.verify(fields, config, "signature", false));
        // 如果按退款链路规则排除 signType，则同一响应不应通过验签。
        assertFalse(Signer.verify(fields, config, "signature", true));
    }

    @Test
    void verifyRefundResponseExcludesSignTypeFromSignature() {
        IpaynowConfig config = md5Config();
        // 退款和撤销链路响应中的 signType 不参与签名，验签时必须排除。
        Map<String, String> fields = new LinkedHashMap<String, String>();
        fields.put("mhtRefundNo", "RF1");
        fields.put("signType", "MD5");
        fields.put("signature", Signer.sign(fields, config, true));

        assertTrue(Signer.verify(fields, config, "signature", true));
        // 如果按正向交易规则保留 signType，则同一响应不应通过验签。
        assertFalse(Signer.verify(fields, config, "signature", false));
    }

    private static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }

    private static String toPrivatePem(PrivateKey privateKey) {
        return "-----BEGIN PRIVATE KEY-----\n"
                + Base64.getMimeEncoder(64, new byte[]{'\n'}).encodeToString(privateKey.getEncoded())
                + "\n-----END PRIVATE KEY-----";
    }

    private static String toPublicPem(PublicKey publicKey) {
        return "-----BEGIN PUBLIC KEY-----\n"
                + Base64.getMimeEncoder(64, new byte[]{'\n'}).encodeToString(publicKey.getEncoded())
                + "\n-----END PUBLIC KEY-----";
    }

    private static String signWithPrivateKey(
            Map<String, String> params,
            boolean excludeSignType,
            PrivateKey privateKey,
            String... signatureFields
    ) {
        return new RsaSignatureStrategy().sign(Signer.canonical(params, excludeSignType, signatureFields), privateKey);
    }

    private static IpaynowConfig md5Config() {
        return new IpaynowConfig("app", "secret");
    }
}
