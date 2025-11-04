package org.daxprotocol.core.conventer;
import org.daxprotocol.core.annotation.DaxpTag;
import org.daxprotocol.core.model.DaxMessage;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
public class DaxMessageConverter {

    private static final Map<Class<?>, Function<String, Object>> CONVERTERS = new HashMap<>();
    static {
        CONVERTERS.put(String.class, s -> s);
        CONVERTERS.put(int.class,    Integer::parseInt);
        CONVERTERS.put(Integer.class,Integer::valueOf);
        CONVERTERS.put(long.class,   Long::parseLong);
        CONVERTERS.put(Long.class,   Long::valueOf);
        CONVERTERS.put(boolean.class,s -> Boolean.parseBoolean(s));
        CONVERTERS.put(Boolean.class,Boolean::valueOf);
        CONVERTERS.put(double.class, Double::parseDouble);
        CONVERTERS.put(Double.class, Double::valueOf);
        // add more as needed (char, BigDecimal, enums, etc.)
    }

    public static <T> T fromMessage(DaxMessage message, Class<T> targetClass) {
        try {
            T instance = targetClass.getDeclaredConstructor().newInstance();

            for (Field f : targetClass.getDeclaredFields()) {
                DaxpTag ann = f.getAnnotation(DaxpTag.class);
                if (ann == null) continue; // skip non-annotated fields (e.g., town)

                int tag = ann.tag();
                var maybeVal = message.get(tag);
             //???   if (maybeVal.isEmpty()) continue; // gracefully ignore missing tags

                String raw = maybeVal.getStrValue();
                Object converted = convert(raw, f.getType());

                f.setAccessible(true);
                f.set(instance, converted);
            }
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to map DAXP to " + targetClass.getSimpleName(), e);
        }
    }

    private static Object convert(String raw, Class<?> type) {
        Function<String, Object> fn = CONVERTERS.get(type);
        if (fn == null) {
            throw new IllegalArgumentException("No converter for type: " + type.getName());
        }
        return fn.apply(raw);
    }
}