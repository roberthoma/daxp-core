package org.daxprotocol.core.codec;

import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.datatype.DaxDataTypeCodec;
import org.daxprotocol.core.model.pair.*;
import org.daxprotocol.core.model.tag.DaxTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
//TODO refactor
    public Set<DaxPair<?>> encodeToPairs(DaxTag tag,  Object obj) {
        if(obj == null){
            return Set.of(new DaxPairString(tag, "N",DaxCoreConstants.OPERATOR_ACTION));
        }

        DaxDataType dataType = dataTypeCodec.decodeBaseDataType(obj);

        if (dataType.equals(DaxDataType.STRING) &&  obj.toString().trim().isBlank()){
            return Set.of(new DaxPairString(tag, "N",DaxCoreConstants.OPERATOR_ACTION));
        }

        if (dataType.equals(DaxDataType.CHARACTER) && ((Character) obj)==0){
            return Set.of(new DaxPairString(tag, "N",DaxCoreConstants.OPERATOR_ACTION));
        }


        return switch (dataType){
            case COLLECTION ->  dataTypeCodec.encode(obj.getClass());   //TODO to change ?????
            case STRING ->  Set.of( new DaxPairString(tag, obj.toString()));
            case INTEGER -> Set.of(new DaxPairInteger(tag,(Integer) obj));
            case CHARACTER -> Set.of(new DaxPairCharacter(tag,(Character) obj));
            case BOOLEAN -> Set.of(new DaxPairBoolean(tag,(Boolean) obj));
            case DOUBLE -> Set.of(new DaxPairDouble(tag,(Double) obj));
            case TAG       -> Set.of(new DaxPairTag(tag,(DaxTag) obj));
            default      -> { logger.error(" NO DATA TYPE CONVERTING {}",obj.getClass().getName());
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


    public String toBlockRefString(Set<Integer> blockSet) {
        if (blockSet == null || blockSet.isEmpty()) {
            return "";
        }

        return blockSet.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(DaxCoreConstants.TAG_LIST_SEPARATOR));
    }


    //TODO REFACTOR TO ONE METHOD    toBlockSet   OR  toBlockSetByteByByte
    public Set<Integer> toBlockSet(String blockRefString) {
        // Handle empty or null strings safely
        if (blockRefString == null || blockRefString.trim().isEmpty()) {
            return Collections.emptySet();
        }

        return Arrays.stream(blockRefString.split(String.valueOf(DaxCoreConstants.TAG_LIST_SEPARATOR)))
                .map(String::trim)          // Removes any accidental spaces
                .map(Integer::parseInt)     // Converts String to Integer
                .collect(Collectors.toCollection(LinkedHashSet::new));

    }


    public Set<Integer> toBlockSetByteByByte(String blockRefString) {
        if (blockRefString == null || blockRefString.isEmpty()) {
            return Collections.emptySet();
        }

        Set<Integer> result = new LinkedHashSet<>();
        int currentNumber = 0;
        boolean hasDigit = false;

        int len = blockRefString.length();
        for (int i = 0; i < len; i++) {
            char c = blockRefString.charAt(i);

            if (c >= '0' && c <= '9') {
                // Shift the previous total left (multiply by 10) and add the new digit
                currentNumber = (currentNumber * 10) + (c - '0');
                hasDigit = true;
            } else if (c == DaxCoreConstants.TAG_LIST_SEPARATOR_CHAR) {
                // When we hit a semicolon, save the accumulated number
                if (hasDigit) {
                    result.add(currentNumber);
                    currentNumber = 0;
                    hasDigit = false;
                }
            }
            // Ignore whitespaces or invalid characters automatically
        }

        // Don't forget to add the very last number after the final loop iteration
        if (hasDigit) {
            result.add(currentNumber);
        }

        return result;
    }
}
