package cn.ipaynow.sdk.util;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class BeanMap {
    private BeanMap() {}

    private static final Map<Class<?>, Field[]> FIELD_CACHE = new ConcurrentHashMap<Class<?>, Field[]>();
    private static final Set<String> RESERVED_FIELDS = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
            "funcode", "version", "appId", "mhtCharset", "mhtSignType", "signType", "mhtSignature", "signature"
    )));

    /**
     * 将任意 POJO 无损转为 Map<String, String>。
     * - 通过反射读取所有实例字段(继承字段一并收集，按 Class 缓存)
     * - null 值跳过(由 Signer.clean 进一步过滤)
     * - 基本类型与包装类型统一 String.valueOf
     * - 使用 LinkedHashMap 保持字段声明顺序,便于签名与排错
     */
    @SneakyThrows
    public static Map<String, String> of(Object bean) {
        return of(bean, null);
    }

    @SneakyThrows
    public static Map<String, String> of(Object bean, Map<String, String> extraParams) {
        if (bean == null) return new LinkedHashMap<String, String>();
        Map<String, String> out = new LinkedHashMap<String, String>();
        for (Field field : fieldsOf(bean.getClass())) {
            if ("extraParams".equals(field.getName())) continue;
            Object value = field.get(bean);
            if (value == null) continue;
            out.put(field.getName(), String.valueOf(value));
        }
        if (extraParams != null) {
            for (Map.Entry<String, String> entry : extraParams.entrySet()) {
                if (RESERVED_FIELDS.contains(entry.getKey())) {
                    throw new IllegalArgumentException("extraParams 不能覆盖 SDK 保留字段: " + entry.getKey());
                }
                if (entry.getValue() != null) {
                    out.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return out;
    }

    private static Field[] fieldsOf(Class<?> type) {
        Field[] cached = FIELD_CACHE.get(type);
        if (cached != null) return cached;
        List<Field> collected = new ArrayList<Field>();
        Class<?> current = type;
        while (current != null && current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) continue;
                field.setAccessible(true);
                collected.add(field);
            }
            current = current.getSuperclass();
        }
        Field[] result = collected.toArray(new Field[0]);
        FIELD_CACHE.put(type, result);
        return result;
    }
}
