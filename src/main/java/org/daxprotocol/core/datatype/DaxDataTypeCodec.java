package org.daxprotocol.core.datatype;

import org.daxprotocol.core.annotation.DaxpEntity;
import org.daxprotocol.core.model.value.DaxValue;
import org.daxprotocol.core.model.tag.DaxTag;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static org.daxprotocol.core.application.DaxCoreTags.*;

public class DaxDataTypeCodec {
    private  final Map<Class<?>, Function<String, Object>> CONVERTERS = new HashMap<>();

    public DaxDataTypeCodec(){
        CONVERTERS.put(String.class, s -> s);
        CONVERTERS.put(int.class, Integer::parseInt);
        CONVERTERS.put(Integer.class, Integer::valueOf);
        CONVERTERS.put(long.class, Long::parseLong);
        CONVERTERS.put(Long.class, Long::valueOf);
        CONVERTERS.put(boolean.class, s -> Boolean.parseBoolean(s));
        CONVERTERS.put(Boolean.class, Boolean::valueOf);
        CONVERTERS.put(double.class, Double::parseDouble);
        CONVERTERS.put(Double.class, Double::valueOf);
        CONVERTERS.put(Character.class, c->c.charAt(0));
        CONVERTERS.put(char.class, c->c.charAt(0));

    }

    private Class<?> decodeCOLLECTION(Map<DaxTag, DaxValue<?>> tagPairMap){

        if (tagPairMap.containsKey(COLLECTION_HAS_KEYS)) {
            if (tagPairMap.get(COLLECTION_HAS_KEYS).getBooleanValue()){
                return Map.class;
            }
        };

        if (tagPairMap.containsKey(COLLECTION_ALLOW_DUPLICATES)) {
            if (tagPairMap.get(COLLECTION_ALLOW_DUPLICATES).getBooleanValue()){
                return List.class;
            }
        };

      return Set.class;
    }



    public Class<?> decode(Map<DaxTag, DaxValue<?>> tagPairMap){

        if (tagPairMap.isEmpty()) return null;

        if (!tagPairMap.containsKey(DATA_TYPE)) return null;
        DaxDataType dataType = DaxDataType.fromCode(tagPairMap.get(DATA_TYPE).getStrValue());

        return switch (dataType){
                      case COLLECTION ->  decodeCOLLECTION(tagPairMap);
                      case STRING ->  String.class;
                      case INTEGER ->  Integer.class;
                      default         -> null; /// TODO add log and exception
        };

//        return null;
    }

    public Map<DaxTag, DaxValue<?>> encode(Class<?> clazz){
        Map<DaxTag, DaxValue<?>> map = new HashMap<>();

        if (clazz == List.class){
            map.put(DATA_TYPE, new DaxValue<>(DATA_TYPE,DaxDataType.COLLECTION.getCode()));
            map.put(COLLECTION_ALLOW_DUPLICATES, new DaxValue<>(COLLECTION_ALLOW_DUPLICATES,true));
            return map;
        }

        if (clazz.isEnum()){
            map.put(DATA_TYPE, new DaxValue<>(DATA_TYPE,DaxDataType.COLLECTION.getCode()));
            map.put(COLLECTION_HAS_KEYS, new DaxValue<>(COLLECTION_HAS_KEYS,true));
            return map;
        }

        map.put(DATA_TYPE, new DaxValue<>(DATA_TYPE,decodeClass(clazz).getCode()));

        return map;
    }

    //--------------------------------
    public  DaxDataType decodeFromObject(Object obj) {
        if (obj == null) return DaxDataType.STRING; // Default
        if (obj instanceof Integer) return DaxDataType.INTEGER;
        if (obj instanceof Long) return DaxDataType.LONG;
        if (obj instanceof BigDecimal) return DaxDataType.DECIMAL;
        if (obj instanceof Double) return DaxDataType.DOUBLE;
        if (obj instanceof Boolean) return DaxDataType.BOOLEAN;
        if (obj instanceof LocalDate) return DaxDataType.LOCAL_DATE;
        if (obj instanceof LocalDateTime) return DaxDataType.LOCAL_DATE_TIME;
        return DaxDataType.UNKNOWN;
    }



    public    DaxDataType decodeClass(Class<?> clazz) {
        if (clazz == null) { return DaxDataType.UNKNOWN;}

        if (clazz.isEnum()) {return DaxDataType.COLLECTION;}
        if (clazz == String.class) return DaxDataType.STRING;
        if (clazz.equals(Integer.class)) return DaxDataType.INTEGER;
        if (clazz.equals(Long.class)) return DaxDataType.LONG;
        if (clazz.equals(BigDecimal.class)) return DaxDataType.DECIMAL;
        if (clazz.equals(Double.class)) return DaxDataType.DOUBLE;
        if (clazz.equals(Boolean.class)) return DaxDataType.BOOLEAN;
        if (clazz.equals(LocalDate.class)) return DaxDataType.LOCAL_DATE;
        if (clazz.equals(LocalDateTime.class)) return DaxDataType.LOCAL_DATE_TIME;


        if (clazz.isAnnotationPresent(DaxpEntity.class)) {
            return DaxDataType.ENTITY;
        }



        return DaxDataType.UNKNOWN;
    }




    public  Object convert(String value, Class<?> type) {
        Function<String, Object> fn = CONVERTERS.get(type);
        if (fn != null) {
            return fn.apply(value);
        }
        //Enum support
        if (type.isEnum()) {
            @SuppressWarnings({"unchecked", "rawtypes"})
            Object enumValue = Enum.valueOf((Class<Enum>) type, value);
            return enumValue;
        }
        throw new IllegalArgumentException("No converter for type: " + type.getName());
    }

    public Map<DaxTag, DaxValue<?>> encode(DaxDataType daxDataType) {
        Map<DaxTag, DaxValue<?>> map = new HashMap<>();
        map.put(DATA_TYPE, new DaxValue<>(DATA_TYPE, daxDataType.getCode()));

        return map;
    }
}


