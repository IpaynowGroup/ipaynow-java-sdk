package cn.ipaynow.sdk;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface HttpTransport {
    String post(String baseUrl, Map<String, String> params);

    final class Default implements HttpTransport {
        private static final Logger log = LoggerFactory.getLogger(HttpTransport.class);

        private final OkHttpClient client;

        public Default() {
            this(5000L, 15000L);
        }

        public Default(long connectTimeoutMillis, long readTimeoutMillis) {
            this.client = new OkHttpClient.Builder()
                    .connectTimeout(connectTimeoutMillis, TimeUnit.MILLISECONDS)
                    .readTimeout(readTimeoutMillis, TimeUnit.MILLISECONDS)
                    .build();
        }

        public String post(String baseUrl, Map<String, String> params) {
            String funcode = params.get("funcode");
            FormBody.Builder formBuilder = new FormBody.Builder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formBuilder.add(entry.getKey(), entry.getValue());
            }
            Request request = new Request.Builder()
                    .url(baseUrl)
                    .post(formBuilder.build())
                    .build();
            try (Response response = client.newCall(request).execute()) {
                ResponseBody body = response.body();
                String text = body == null ? "" : body.string();
                if (!response.isSuccessful()) {
                    log.error("现在支付HTTP请求失败，功能码={}，状态码={}，响应报文={}", funcode, response.code(), text);
                    throw new IpaynowException(IpaynowErrorCode.HTTP_EXCEPTION,
                            "现在支付HTTP请求失败，状态码=" + response.code() + "，功能码=" + funcode + "，响应报文=" + text);
                }
                return text;
            } catch (SocketTimeoutException e) {
                // OkHttp 的连接超时和读超时都是 SocketTimeoutException，只能通过消息区分。
                IpaynowErrorCode code = isConnectTimeoutMessage(e.getMessage())
                        ? IpaynowErrorCode.CONNECT_TIMEOUT
                        : IpaynowErrorCode.SOCKET_TIMEOUT;
                log.error("现在支付请求超时，功能码={}，错误码={}，原因={}", funcode, code.code(), e.getMessage());
                throw new IpaynowException(code, "现在支付请求超时，功能码=" + funcode, e);
            } catch (IOException e) {
                log.error("现在支付请求异常，功能码={}，原因={}", funcode, e.getMessage());
                throw new IpaynowException(IpaynowErrorCode.HTTP_EXCEPTION, "现在支付请求异常，功能码=" + funcode, e);
            }
        }

        private static boolean isConnectTimeoutMessage(String message) {
            return message != null && message.toLowerCase(Locale.ROOT).contains("connect");
        }

    }
}
