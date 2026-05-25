package org.daxprotocol.core.datatype;

import org.daxprotocol.core.application.DaxEngine;
import org.daxprotocol.core.model.pair.*;
import org.daxprotocol.core.model.tag.DaxTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

import static org.daxprotocol.core.application.DaxCoreTags.*;

public class DaxDataTypeCodec {
    private static final Logger logger = LoggerFactory.getLogger(DaxDataTypeCodec.class);
    private  final Map<Class<?>, Function<String, Object>> CONVERTERS = new HashMap<>();
    DaxDataTypeService dataTypeService;
    public DaxDataTypeCodec(DaxDataTypeService dataTypeService){
        this.dataTypeService = dataTypeService;

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

        if (tagPairMap.containsKey(COLLECTION_HAS_KEY)) {
            if (tagPairMap.get(COLLECTION_HAS_KEY).getBooleanValue()){
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
                      //todo develop
                      default         -> null; /// TODO add log and exception
        };

    }

    public Set<DaxPair<?>> encode(Class<?> clazz){
        return encode(clazz, null);
    }
    public Set<DaxPair<?>> encode(Class<?> clazz, Type generitType){



        if (dataTypeService.isCollection(clazz)){
            return dataTypeService.collectionEncode(clazz,generitType);
        }

        //develop as generic collection
        Set< DaxPair<?>> map = new HashSet<>();

        map.add( new DaxPairDataType(ATR_DATA_TYPE,
                DaxDataType.fromCode(  dataTypeService.decodeClass(clazz).getCode())));
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

        if (dataTypeService.isObjInstanceOfCollection( obj )) return DaxDataType.COLLECTION;

        return DaxDataType.UNKNOWN;
    }


    public  Object convert(String value, Class<?> type) {
        if (value == null){
            return null;
        }

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

    public Set<DaxPair<?>> convertToValue(DaxTag tag,  Object obj) {
        DaxDataType dataType = decodeFromObject(obj);
        return switch (dataType){
            case COLLECTION ->  encode(obj.getClass());
            case STRING ->  Set.of( new DaxPairString(tag, (String) obj));
            case INTEGER -> Set.of(new DaxPairInteger(tag,(Integer) obj));
            case CHARACTER -> Set.of(new DaxPairCharacter(tag,(Character) obj));
            case BOOLEAN -> Set.of(new DaxPairBoolean(tag,(Boolean) obj));
            case DOUBLE -> Set.of(new DaxPairDouble(tag,(Double) obj));
            case TAG       -> Set.of(new DaxPairTag(tag,(DaxTag) obj));
            default      -> { //TODO Logger
                             throw new RuntimeException("NO DATA TYPE CONVERTING !!!");}
        };

    }
}


