package cn.ipaynow.sdk.notify;

import cn.ipaynow.sdk.IpaynowErrorCode;
import cn.ipaynow.sdk.IpaynowException;
import cn.ipaynow.sdk.IpaynowConfig;
import cn.ipaynow.sdk.IpaynowResponse;
import cn.ipaynow.sdk.util.Parser;
import cn.ipaynow.sdk.util.Signer;
import cn.ipaynow.sdk.response.PaymentNotifyResponse;
import cn.ipaynow.sdk.response.RefundNotifyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public final class NotifyParser {
    private static final Logger log = LoggerFactory.getLogger(NotifyParser.class);

    private NotifyParser() {}

    public static PaymentNotifyResponse parsePayment(String body, IpaynowConfig config) {
        return parse(body, config, false, new PaymentNotifyResponse());
    }

    public static RefundNotifyResponse parseRefund(String body, IpaynowConfig config) {
        return parse(body, config, true, new RefundNotifyResponse());
    }

    private static <T extends IpaynowResponse> T parse(String body, IpaynowConfig config, boolean excludeSignType, T response) {
        Map<String, String> fields = Parser.parseResponse(body);
        if (!Signer.verify(fields, config, "signature", excludeSignType)) {
            log.warn("拒绝现在支付回调通知，签名缺失或验签失败");
            throw new IpaynowException(IpaynowErrorCode.SIGN_ERROR, "现在支付回调通知验签失败");
        }
        response.load(fields, body);
        return response;
    }
}
