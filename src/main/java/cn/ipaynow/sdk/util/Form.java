package cn.ipaynow.sdk.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public final class Form {
    private Form() {}

    static String decode(String value) {
        try {
            return URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
}
