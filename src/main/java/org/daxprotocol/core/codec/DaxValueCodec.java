package org.daxprotocol.core.codec;

import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.datatype.DaxDataTypeCodec;
import org.daxprotocol.core.model.pair.*;
import org.daxprotocol.core.model.tag.DaxTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class DaxValueCodec {
    private static final Logger logger = LoggerFactory.getLogger(DaxValueCodec.class);

    private  final Map<Class<?>, Function<String, Object>> CONVERTERS = new HashMap<>();

    DaxDataTypeCodec dataTypeCodec;

    public DaxValueCodec(DaxDataTypeCodec dataTypeCodec){
        this.dataTypeCodec = dataTypeCodec;

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

    //--------------------------------------------------------------------------------------

    public Set<DaxPair<?>> encode(DaxTag tag,  Object obj) {
        DaxDataType dataType = dataTypeCodec.decodeBaseDataType(obj);
        return switch (dataType){
            case COLLECTION ->  dataTypeCodec.encode(obj.getClass());   //TODO to change ?????
            case STRING ->  Set.of( new DaxPairString(tag, obj.toString()));
            case INTEGER -> Set.of(new DaxPairInteger(tag,(Integer) obj));
            case CHARACTER -> Set.of(new DaxPairCharacter(tag,(Character) obj));
            case BOOLEAN -> Set.of(new DaxPairBoolean(tag,(Boolean) obj));
            case DOUBLE -> Set.of(new DaxPairDouble(tag,(Double) obj));
            case TAG       -> Set.of(new DaxPairTag(tag,(DaxTag) obj));
            default      -> { //TODO Logger
                throw new RuntimeException("NO DATA TYPE CONVERTING !!!");}
        };

    }

    public  Object decode(String value, Class<?> type) {
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

}
