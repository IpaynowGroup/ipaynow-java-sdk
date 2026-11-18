package cn.ipaynow.sdk;

import cn.ipaynow.sdk.domain.JsapiUnifiedOrderParams;
import cn.ipaynow.sdk.notify.NotifyParser;
import cn.ipaynow.sdk.request.JsapiUnifiedOrderRequest;
import cn.ipaynow.sdk.response.AggregateCodeUnifiedOrderResponse;
import cn.ipaynow.sdk.response.AppUnifiedOrderResponse;
import cn.ipaynow.sdk.response.H5UnifiedOrderResponse;
import cn.ipaynow.sdk.response.JsapiUnifiedOrderResponse;
import cn.ipaynow.sdk.response.MiniProgramUnifiedOrderResponse;
import cn.ipaynow.sdk.response.PaymentNotifyResponse;
import cn.ipaynow.sdk.response.RefundQueryResponse;
import cn.ipaynow.sdk.response.RefundResponse;
import cn.ipaynow.sdk.response.RefundNotifyResponse;
import cn.ipaynow.sdk.response.ReverseQueryResponse;
import cn.ipaynow.sdk.response.ReverseResponse;
import cn.ipaynow.sdk.util.Signer;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CoreFlowTest {
    @Test
    void configRejectsBlankCredentials() {
        assertThrows(IllegalArgumentException.class, () -> new IpaynowConfig(null, "key"));
        assertThrows(IllegalArgumentException.class, () -> new IpaynowConfig("app", " "));
        assertThrows(IllegalArgumentException.class, () -> new IpaynowConfig("app", "key", ""));
        assertThrows(IllegalArgumentException.class, () ->
                new IpaynowConfig("app", SignTypeEnum.RSA, "", "pub", "https://pay.test", 5000L, 15000L));
    }

    @Test
    void rsaConfigParsesBase64KeysAndRejectsPem() throws Exception {
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

        assertNotNull(config.mchPrivateKeyObject());
        assertNotNull(config.ipaynowPublicKeyObject());
        assertThrows(IllegalArgumentException.class, () ->
                new IpaynowConfig(
                        "app",
                        SignTypeEnum.RSA,
                        toPrivatePem(merchant.getPrivate()),
                        toPublicPem(platform.getPublic()),
                        "https://pay.test",
                        5000L,
                        15000L
                ));
    }

    @Test
    void configRejectsNonPositiveTimeouts() {
        assertThrows(IllegalArgumentException.class,
                () -> new IpaynowConfig("app", "key", null, null,
                        SignTypeEnum.MD5, "https://pay.test", 0L, 15000L));
        assertThrows(IllegalArgumentException.class,
                () -> new IpaynowConfig("app", "key",  null, null,
                        SignTypeEnum.MD5,"https://pay.test", 5000L, -1L));
    }

    @Test
    void signSortsFieldsAndSkipsSignatureFields() {
        IpaynowConfig config = new IpaynowConfig("app", "secret");
        Map<String, String> params = new HashMap<String, String>();
        params.put("b", "2");
        params.put("a", "1");
        params.put("empty", "");
        params.put("signType", "MD5");
        params.put("mhtSignature", "bad");
        String got = Signer.sign(params, config, true);
        assertEquals("c319c9529d781b179bff6bfdc3f82202", got);
    }

    @Test
    void specRequestAcceptsRequestParamsObject() {
        RequestParams params = new RequestParams() {
            public Map<String, String> toMap() {
                Map<String, String> out = new HashMap<String, String>();
                out.put("mhtOrderNo", "O2");
                return out;
            }
        };
        IpaynowRequest<JsapiUnifiedOrderResponse> request =
                new SpecRequest<JsapiUnifiedOrderResponse>("WP001", "1.0.0", "UTF-8", params, JsapiUnifiedOrderResponse.class, "mhtSignType");
        assertEquals("O2", request.params().get("mhtOrderNo"));
    }

    @Test
    void executeSignsAndHydratesResponse() {
        Map<String, String> responseFields = new LinkedHashMap<String, String>();
        responseFields.put("responseCode", "A001");
        responseFields.put("nowPayOrderNo", "N1");
        // JsapiUnifiedOrderRequest 属于正向交易，signType 参与签名。
        String signature = Signer.sign(responseFields, new IpaynowConfig("app", "secret"), false);

        CapturingTransport transport = new CapturingTransport("responseCode=A001&nowPayOrderNo=N1&signature=" + signature);
        IpaynowClient client = new IpaynowClient(new IpaynowConfig("app", "secret", "https://pay.test"), transport);
        JsapiUnifiedOrderResponse response = client.execute(
                new JsapiUnifiedOrderRequest(JsapiUnifiedOrderParams.builder().mhtOrderNo("O1").mhtOrderAmt(1L).build())
        );

        assertEquals("https://pay.test/", transport.baseUrl);
        assertEquals("app", transport.params.get("appId"));
        assertEquals("WP001", transport.params.get("funcode"));
        assertEquals("MD5", transport.params.get("mhtSignType"));
        assertNotNull(transport.params.get("mhtSignature"));
        assertEquals("A001", response.get("responseCode"));
        assertEquals("N1", response.get("nowPayOrderNo"));
        assertEquals("A001", response.code());
        assertEquals("responseCode=A001&nowPayOrderNo=N1&signature=" + signature, response.rawBody());
        assertTrue(response.success());
    }

    @Test
    void executeUsesRsaStrategyFromConfig() throws Exception {
        KeyPair merchant = generateKeyPair();
        KeyPair platform = generateKeyPair();
        Map<String, String> responseFields = new LinkedHashMap<String, String>();
        responseFields.put("responseCode", "A001");
        responseFields.put("signType", "RSA");
        responseFields.put("signature", signWithPrivateKey(
                canonical(responseFields, false, "signature", "mhtSignature"),
                platform.getPrivate()
        ));

        CapturingTransport transport = new CapturingTransport(
                "responseCode=A001&signType=RSA&signature=" + java.net.URLEncoder.encode(responseFields.get("signature"), "UTF-8")
        );
        IpaynowConfig config = new IpaynowConfig(
                "app",
                SignTypeEnum.RSA,
                Base64.getEncoder().encodeToString(merchant.getPrivate().getEncoded()),
                Base64.getEncoder().encodeToString(platform.getPublic().getEncoded()),
                "https://pay.test",
                5000L,
                15000L
        );
        IpaynowClient client = new IpaynowClient(config, transport);

        client.execute(new JsapiUnifiedOrderRequest(JsapiUnifiedOrderParams.builder().mhtOrderNo("O1").mhtOrderAmt(1L).build()));

        assertEquals("RSA", transport.params.get("mhtSignType"));
        assertNotNull(transport.params.get("mhtSignature"));
    }

    @Test
    void buildFormFieldsReturnsSignedRequestWithoutPosting() {
        CapturingTransport transport = new CapturingTransport("unused");
        IpaynowClient client = new IpaynowClient(new IpaynowConfig("app", "secret", "https://pay.test"), transport);
        JsapiUnifiedOrderRequest request =
                new JsapiUnifiedOrderRequest(JsapiUnifiedOrderParams.builder().mhtOrderNo("O1").mhtOrderAmt(1L).build());

        Map<String, String> fields = client.buildFormFields(request);

        assertEquals("O1", fields.get("mhtOrderNo"));
        assertEquals("WP001", fields.get("funcode"));
        assertEquals("1.0.0", fields.get("version"));
        assertEquals("app", fields.get("appId"));
        assertEquals("UTF-8", fields.get("mhtCharset"));
        assertEquals("MD5", fields.get("mhtSignType"));
        assertNotNull(fields.get("mhtSignature"));
        assertFalse(fields.containsKey("empty"));
        assertFalse(transport.called);
    }

    @Test
    void refundRequestUsesRefundEndpointAndRequestOwnedSignPolicy() {
        Map<String, String> responseFields = new LinkedHashMap<String, String>();
        responseFields.put("responseCode", "A001");
        String signature = Signer.sign(responseFields, new IpaynowConfig("app", "secret"), true);

        CapturingTransport transport = new CapturingTransport("responseCode=A001&signature=" + signature);
        IpaynowClient client = new IpaynowClient(new IpaynowConfig("app", "secret", "https://pay.test/"), transport);
        cn.ipaynow.sdk.domain.RefundParams params = cn.ipaynow.sdk.domain.RefundParams.builder()
                .mhtOrderNo("O1")
                .mhtRefundNo("R1")
                .amount(1L)
                .build();

        client.execute(new cn.ipaynow.sdk.request.RefundRequest(params));

        assertEquals("https://pay.test/refund/refundOrder", transport.baseUrl);
        assertEquals("signType", new cn.ipaynow.sdk.request.RefundRequest(params).signTypeField());
        assertTrue(new cn.ipaynow.sdk.request.RefundRequest(params).excludeSignType());
    }

    @Test
    void notifyParserVerifiesSignature() {
        Map<String, String> fields = new LinkedHashMap<String, String>();
        fields.put("responseCode", "A001");
        fields.put("mhtOrderNo", "O1");
        // N001 支付结果通知属于正向交易通知，signType 参与签名。
        IpaynowConfig config = new IpaynowConfig("app", "secret");
        fields.put("signature", Signer.sign(fields, config, false));

        PaymentNotifyResponse response = NotifyParser.parsePayment(
                "responseCode=A001&mhtOrderNo=O1&signature=" + fields.get("signature"),
                config
        );

        assertEquals("A001", response.get("responseCode"));
        assertEquals("O1", response.get("mhtOrderNo"));
    }

    @Test
    void executeRejectsMissingResponseSignature() {
        CapturingTransport transport = new CapturingTransport("responseCode=A001&nowPayOrderNo=N1");
        IpaynowClient client = new IpaynowClient(new IpaynowConfig("app", "secret", "https://pay.test"), transport);
        IpaynowRequest<JsapiUnifiedOrderResponse> request =
                new JsapiUnifiedOrderRequest(JsapiUnifiedOrderParams.builder().mhtOrderNo("O1").mhtOrderAmt(1L).build());

        IpaynowException ex = assertThrows(IpaynowException.class, () -> client.execute(request));
        assertEquals("现在支付响应验签失败，功能码=WP001", ex.getMessage());
        assertEquals(IpaynowErrorCode.SIGN_ERROR, ex.code());
    }

    @Test
    void notifyParserRejectsMissingSignature() {
        IpaynowException ex = assertThrows(IpaynowException.class, () ->
                NotifyParser.parsePayment("responseCode=A001&mhtOrderNo=O1", new IpaynowConfig("app", "secret")));
        assertEquals("现在支付回调通知验签失败", ex.getMessage());
        assertEquals(IpaynowErrorCode.SIGN_ERROR, ex.code());
    }

    @Test
    void executePropagatesIpaynowExceptionThrownByTransport() {
        // 自定义 HttpTransport 直接抛出 IpaynowException，execute 不再捕获或二次包装。
        HttpTransport failingTransport = new HttpTransport() {
            public String post(String baseUrl, Map<String, String> params) {
                throw new IpaynowException(IpaynowErrorCode.HTTP_EXCEPTION, "现在支付HTTP请求失败，功能码=WP001");
            }
        };
        IpaynowClient client = new IpaynowClient(new IpaynowConfig("app", "secret", "https://pay.test"), failingTransport);
        IpaynowRequest<JsapiUnifiedOrderResponse> request =
                new JsapiUnifiedOrderRequest(JsapiUnifiedOrderParams.builder().mhtOrderNo("O1").mhtOrderAmt(1L).build());

        IpaynowException ex = assertThrows(IpaynowException.class, () -> client.execute(request));
        assertEquals(IpaynowErrorCode.HTTP_EXCEPTION, ex.code());
    }

    @Test
    void executeReportsSystemErrorForMalformedResponseClass() {
        Map<String, String> responseFields = new LinkedHashMap<String, String>();
        responseFields.put("responseCode", "A001");
        String signature = Signer.sign(responseFields, new IpaynowConfig("app", "secret"), false);
        CapturingTransport transport = new CapturingTransport("responseCode=A001&signature=" + signature);
        IpaynowClient client = new IpaynowClient(new IpaynowConfig("app", "secret", "https://pay.test"), transport);
        IpaynowRequest<NoNoArgConstructorResponse> request = new SpecRequest<NoNoArgConstructorResponse>(
                "WP001", "1.0.0", "UTF-8",
                JsapiUnifiedOrderParams.builder().mhtOrderNo("O1").mhtOrderAmt(1L).build(),
                NoNoArgConstructorResponse.class, "mhtSignType");

        IpaynowException ex = assertThrows(IpaynowException.class, () -> client.execute(request));
        assertEquals(IpaynowErrorCode.SYSTEM_ERROR, ex.code());
    }

    private static final class NoNoArgConstructorResponse extends IpaynowResponse {
        NoNoArgConstructorResponse(String unused) {}
    }

    @Test
    void isSuccessReflectsResponseCode() {
        JsapiUnifiedOrderResponse success = new JsapiUnifiedOrderResponse();
        success.load(Collections.singletonMap("responseCode", "A001"));
        JsapiUnifiedOrderResponse failure = new JsapiUnifiedOrderResponse();
        failure.load(Collections.singletonMap("responseCode", "A002"));

        assertTrue(success.isSuccess());
        assertFalse(failure.isSuccess());
        assertTrue(success.success());
    }

    @Test
    void isSuccessUsesTransStatusForPaymentNotify() {
        PaymentNotifyResponse success = new PaymentNotifyResponse();
        success.load(Collections.singletonMap("transStatus", "A001"));
        PaymentNotifyResponse failure = new PaymentNotifyResponse();
        failure.load(Collections.singletonMap("transStatus", "A002"));

        assertTrue(success.isSuccess());
        assertFalse(failure.isSuccess());
    }

    @Test
    void paymentNotifyFallsBackToTradeStatus() {
        PaymentNotifyResponse success = new PaymentNotifyResponse();
        success.load(Collections.singletonMap("tradeStatus", "A001"));
        PaymentNotifyResponse failure = new PaymentNotifyResponse();
        failure.load(Collections.singletonMap("tradeStatus", "A002"));

        assertTrue(success.isSuccess());
        assertFalse(failure.isSuccess());
        assertEquals("A001", success.getTradeStatus());
    }

    @Test
    void paymentNotifyPrefersTransStatusOverTradeStatus() {
        PaymentNotifyResponse response = new PaymentNotifyResponse();
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("transStatus", "A002");
        fields.put("tradeStatus", "A001");
        response.load(fields);

        assertFalse(response.isSuccess());
    }

    @Test
    void isSuccessUsesTradeStatusForRefundNotify() {
        RefundNotifyResponse success = new RefundNotifyResponse();
        success.load(Collections.singletonMap("tradeStatus", "A001"));
        RefundNotifyResponse failure = new RefundNotifyResponse();
        failure.load(Collections.singletonMap("tradeStatus", "A002"));

        assertTrue(success.isSuccess());
        assertFalse(failure.isSuccess());
    }

    @Test
    void refundAndReverseResponsesUseR000AsSuccessCode() {
        RefundResponse refund = new RefundResponse();
        refund.load(Collections.singletonMap("responseCode", "R000"));
        RefundQueryResponse refundQuery = new RefundQueryResponse();
        refundQuery.load(Collections.singletonMap("responseCode", "R000"));
        ReverseResponse reverse = new ReverseResponse();
        reverse.load(Collections.singletonMap("responseCode", "R000"));
        ReverseQueryResponse reverseQuery = new ReverseQueryResponse();
        reverseQuery.load(Collections.singletonMap("responseCode", "R000"));

        assertTrue(refund.isSuccess());
        assertTrue(refundQuery.isSuccess());
        assertTrue(reverse.isSuccess());
        assertTrue(reverseQuery.isSuccess());
        assertTrue(refund.success());
    }

    @Test
    void unifiedOrderResponsesExposeTn() {
        Map<String, String> fields = new LinkedHashMap<String, String>();
        fields.put("tn", "TN001");
        JsapiUnifiedOrderResponse jsapi = new JsapiUnifiedOrderResponse();
        jsapi.load(fields);
        AppUnifiedOrderResponse app = new AppUnifiedOrderResponse();
        app.load(fields);
        H5UnifiedOrderResponse h5 = new H5UnifiedOrderResponse();
        h5.load(fields);
        MiniProgramUnifiedOrderResponse miniProgram = new MiniProgramUnifiedOrderResponse();
        miniProgram.load(fields);
        AggregateCodeUnifiedOrderResponse aggregateCode = new AggregateCodeUnifiedOrderResponse();
        aggregateCode.load(fields);

        assertEquals("TN001", jsapi.getTn());
        assertEquals("TN001", app.getTn());
        assertEquals("TN001", h5.getTn());
        assertEquals("TN001", miniProgram.getTn());
        assertEquals("TN001", aggregateCode.getTn());
    }

    @Test
    void unifiedOrderResponsesExposeUppercaseTnFallback() {
        JsapiUnifiedOrderResponse response = new JsapiUnifiedOrderResponse();
        response.load(Collections.singletonMap("TN", "TN002"));

        assertEquals("TN002", response.getTn());
    }

    private static final class CapturingTransport implements HttpTransport {
        private final String response;
        private String baseUrl;
        private Map<String, String> params;
        private boolean called;

        private CapturingTransport(String response) {
            this.response = response;
        }

        public String post(String baseUrl, Map<String, String> params) {
            this.called = true;
            this.baseUrl = baseUrl;
            this.params = new LinkedHashMap<String, String>(params);
            return response;
        }
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

    private static String signWithPrivateKey(String canonical, PrivateKey privateKey) throws Exception {
        java.security.Signature signature = java.security.Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(canonical.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    private static String canonical(Map<String, String> params, boolean excludeSignType, String... signatureFields) {
        Map<String, String> cleaned = new java.util.TreeMap<String, String>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                cleaned.put(entry.getKey(), entry.getValue());
            }
        }
        if (excludeSignType) {
            cleaned.remove("signType");
        }
        cleaned.remove("mhtSignature");
        cleaned.remove("signature");
        for (String field : signatureFields) {
            cleaned.remove(field);
        }
        StringBuilder canonical = new StringBuilder();
        for (Map.Entry<String, String> entry : cleaned.entrySet()) {
            if (canonical.length() > 0) {
                canonical.append('&');
            }
            canonical.append(entry.getKey()).append('=').append(entry.getValue());
        }
        return canonical.toString();
    }
}
