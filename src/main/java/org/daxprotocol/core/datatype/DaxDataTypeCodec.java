package org.daxprotocol.core.datatype;

import org.daxprotocol.core.annotation.DaxpEntity;
import org.daxprotocol.core.model.pair.*;
import org.daxprotocol.core.model.tag.DaxTag;

import java.lang.reflect.Field;
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
        CONVERTERS.put(boolean.class, Boolean::parseBoolean);
        CONVERTERS.put(Boolean.class, Boolean::valueOf);
        CONVERTERS.put(double.class, Double::parseDouble);
        CONVERTERS.put(Double.class, Double::valueOf);
        CONVERTERS.put(Character.class, c->c.charAt(0));
        CONVERTERS.put(char.class, c->c.charAt(0));

    }

    private Class<?> decodeCOLLECTION(Map<DaxTag, DaxPair<?>> tagPairMap){

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



    public Class<?> decode(Map<DaxTag, DaxPair<?>> tagPairMap){

        if (tagPairMap.isEmpty()) return null;

        if (!tagPairMap.containsKey(ATR_DATA_TYPE)) return null;
        DaxDataType dataType = tagPairMap.get(ATR_DATA_TYPE).getDataTypeValue();

        return switch (dataType){
                      case COLLECTION ->  decodeCOLLECTION(tagPairMap);
                      case STRING ->  String.class;
                      case INTEGER ->  Integer.class;
                      default         -> null; /// TODO add log and exception
        };

//        return null;
    }

    public Map<DaxTag, DaxPair<?>> encode(Class<?> clazz){
        Map<DaxTag, DaxPair<?>> map = new HashMap<>();

        if (clazz == List.class){
            map.put(ATR_DATA_TYPE, new DaxPairString(ATR_DATA_TYPE,DaxDataType.COLLECTION.getCode()));
            map.put(COLLECTION_ALLOW_DUPLICATES, new DaxPairBoolean(COLLECTION_ALLOW_DUPLICATES,true));
            return map;
        }

        if (clazz.isEnum()){
            map.put(ATR_DATA_TYPE, new DaxPairDataType(ATR_DATA_TYPE,DaxDataType.COLLECTION));
            map.put(COLLECTION_HAS_KEYS, new DaxPairBoolean(COLLECTION_HAS_KEYS,true));
            return map;
        }

        map.put(ATR_DATA_TYPE, new DaxPairDataType(ATR_DATA_TYPE,DaxDataType.fromCode(  decodeClass(clazz).getCode())));

        return map;
    }

    //--------------------------------
    private   DaxDataType decodeFromObject(Object obj) {
        if (obj == null) return DaxDataType.STRING; // Default
        if (obj instanceof Integer) return DaxDataType.INTEGER;
        if (obj instanceof Long) return DaxDataType.LONG;
        if (obj instanceof BigDecimal) return DaxDataType.DECIMAL;
        if (obj instanceof Double) return DaxDataType.DOUBLE;
        if (obj instanceof Boolean) return DaxDataType.BOOLEAN;
        if (obj instanceof LocalDate) return DaxDataType.LOCAL_DATE;
        if (obj instanceof LocalDateTime) return DaxDataType.LOCAL_DATE_TIME;
        if (obj instanceof String) return DaxDataType.STRING;
        if (obj instanceof Character) return DaxDataType.CHARACTER;


        return DaxDataType.UNKNOWN;
    }



    public    DaxDataType decodeClass(Class<?> clazz) {
        if (clazz == null) { return DaxDataType.UNKNOWN;}

        if (clazz.equals(Enum.class)) {return DaxDataType.COLLECTION;}
        if (clazz == String.class) return DaxDataType.STRING;
        if (clazz.equals(Integer.class)) return DaxDataType.INTEGER;
        if (clazz.equals(Long.class)) return DaxDataType.LONG;
        if (clazz.equals(BigDecimal.class)) return DaxDataType.DECIMAL;
        if (clazz.equals(Double.class)) return DaxDataType.DOUBLE;
        if (clazz.equals(Boolean.class)) return DaxDataType.BOOLEAN;
        if (clazz.equals(LocalDate.class)) return DaxDataType.LOCAL_DATE;
        if (clazz.equals(LocalDateTime.class)) return DaxDataType.LOCAL_DATE_TIME;
        if (clazz.equals(Character.class)) return DaxDataType.CHARACTER;


        if (clazz.isEnum()) {
            return DaxDataType.ENTITY;
        }

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

    public Map<DaxTag, DaxPair<?>> encode(DaxDataType daxDataType) {
        return null;
    }

//    public DaxPair<?> convertToValue(DaxTag tag, Field field, Object obj) {
    public DaxPair<?> convertToValue(DaxTag tag,  Object obj) {
        DaxDataType dataType = decodeFromObject(obj);
        System.out.println(tag.getTagId()+" >>>>"+obj);
        return switch (dataType){
        //    case COLLECTION ->  decodeCOLLECTION(tagPairMap);
            case STRING ->  new DaxPairString(tag, (String) obj);
            case INTEGER -> new DaxPairInteger(tag,(Integer) obj);
            case CHARACTER -> new DaxPairCharacter(tag,(Character) obj);
            case BOOLEAN -> new DaxPairBoolean(tag,(Boolean) obj);
            case TAG       -> new DaxPairTag(tag,(DaxTag) obj);
            default      -> throw new RuntimeException("NO DATA TYPE CONVERTING !!!");
        };

    }
}


