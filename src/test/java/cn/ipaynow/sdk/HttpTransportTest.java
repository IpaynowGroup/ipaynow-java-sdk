package cn.ipaynow.sdk;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HttpTransportTest {
    private MockWebServer server;

    @BeforeEach
    void startServer() throws IOException {
        server = new MockWebServer();
        server.start();
    }

    @AfterEach
    void stopServer() throws IOException {
        server.shutdown();
    }

    @Test
    void postReturnsSuccessfulResponseBody() {
        server.enqueue(new MockResponse().setBody("responseCode=A001"));
        HttpTransport transport = new HttpTransport.Default();
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("funcode", "WP001");

        String body = transport.post(server.url("/").toString(), params);

        assertEquals("responseCode=A001", body);
    }

    @Test
    void postThrowsOnNon2xxAndIncludesBody() {
        server.enqueue(new MockResponse().setResponseCode(500).setBody("internal error"));
        HttpTransport transport = new HttpTransport.Default();
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("funcode", "WP001");

        IpaynowException ex = assertThrows(IpaynowException.class, () -> transport.post(server.url("/").toString(), params));

        assertTrue(ex.getMessage().contains("internal error"));
        assertTrue(ex.getMessage().contains("500"));
        assertEquals(IpaynowErrorCode.HTTP_EXCEPTION, ex.code());
    }

    @Test
    void postReportsSocketTimeoutCodeOnReadTimeout() {
        server.enqueue(new MockResponse().setBody("late").setBodyDelay(2, TimeUnit.SECONDS));
        HttpTransport transport = new HttpTransport.Default(1000L, 200L);
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("funcode", "WP001");

        IpaynowException ex = assertThrows(IpaynowException.class, () -> transport.post(server.url("/").toString(), params));

        assertEquals(IpaynowErrorCode.SOCKET_TIMEOUT, ex.code());
    }
}
