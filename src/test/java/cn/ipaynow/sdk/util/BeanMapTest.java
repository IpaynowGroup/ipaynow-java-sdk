package cn.ipaynow.sdk.util;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class BeanMapTest {
    static class Sample {
        private String name = "n1";
        private long amount = 100L;
        private String skipped;
    }

    @Test
    void mapsNonNullInstanceFieldsToStrings() {
        Map<String, String> map = BeanMap.of(new Sample());

        assertEquals("n1", map.get("name"));
        assertEquals("100", map.get("amount"));
        assertFalse(map.containsKey("skipped"));
    }

    @Test
    void repeatedCallsOnSameClassReturnEqualResults() {
        Map<String, String> first = BeanMap.of(new Sample());
        Map<String, String> second = BeanMap.of(new Sample());

        assertEquals(first, second);
    }
}
