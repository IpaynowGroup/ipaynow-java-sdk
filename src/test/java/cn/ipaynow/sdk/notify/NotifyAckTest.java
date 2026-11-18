package cn.ipaynow.sdk.notify;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotifyAckTest {
    @Test
    void successReturnsYAck() {
        assertEquals("success=Y", NotifyAck.success());
    }

    @Test
    void failReturnsNAck() {
        assertEquals("success=N", NotifyAck.fail());
    }
}
