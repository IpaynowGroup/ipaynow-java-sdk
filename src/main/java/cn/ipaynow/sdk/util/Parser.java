package cn.ipaynow.sdk.util;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Parser {
    private Parser() {
    }

    public static Map<String, String> parseResponse(String text) {
        String trimmed = text == null ? "" : text.trim();
        if (trimmed.isEmpty()) return new LinkedHashMap<String, String>();
        Map<String, String> out = new LinkedHashMap<String, String>();
        for (String part : trimmed.split("&")) {
            String[] pieces = part.split("=", 2);
            out.put(Form.decode(pieces[0]), pieces.length > 1 ? Form.decode(pieces[1]) : "");
        }
        return out;
    }
}
