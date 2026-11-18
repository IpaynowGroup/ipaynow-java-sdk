package cn.ipaynow.sdk.util;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParserTest {
    @Test
    void parsesFormEncodedResponse() {
        Map<String, String> fields = Parser.parseResponse("responseCode=A001&mhtOrderNo=O1");

        assertEquals("A001", fields.get("responseCode"));
        assertEquals("O1", fields.get("mhtOrderNo"));
    }

    @Test
    void returnsEmptyMapForBlankInput() {
        assertTrue(Parser.parseResponse("").isEmpty());
        assertTrue(Parser.parseResponse(null).isEmpty());
    }

    @Test
    void decodesUrlEncodedValues() {
        Map<String, String> fields = Parser.parseResponse("mhtOrderName=%E5%95%86%E5%93%81");

        assertEquals("商品", fields.get("mhtOrderName"));
    }

    @Test
    void treatsJsonLookingInputAsOpaqueFormValue() {
        Map<String, String> fields = Parser.parseResponse("{\"responseCode\":\"A001\"}");

        assertEquals(1, fields.size());
        assertEquals("", fields.get("{\"responseCode\":\"A001\"}"));
    }
}
