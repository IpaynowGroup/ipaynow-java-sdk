package cn.ipaynow.sdk.notify;

public final class NotifyAck {
    private NotifyAck() {}

    public static String success() {
        return "success=Y";
    }

    public static String fail() {
        return "success=N";
    }
}
