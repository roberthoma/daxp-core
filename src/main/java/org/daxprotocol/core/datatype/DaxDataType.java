package org.daxprotocol.core.datatype;
import org.daxprotocol.core.annotation.DaxpEntity;
import org.daxprotocol.core.model.tag.DaxTag;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * DAXP Data Types Definition
 * Based on the universal mapping between Oracle Database and Java.
 */
/* TODO in future
Typ DAXP	Kod (Byte)	Odpowiednik w Java	Rozmiar w bajtach
UINT8	0x01	int (maskowany)	1
INT32	0x02	int	4
FLOAT64	0x03	double	8
STRING_UTF8	0x04	String	Zmienny (L+V)

   OR

Nazwa DAXP	Rozmiar	Zakres	Zastosowanie w DAXP
UINT8	1 bajt	0 do 255	Kody bloków, flagi kolekcji, małe liczniki.
INT8	1 bajt	-128 do 127	Małe wartości różnicowe, temperatury.
UINT16	2 bajty	0 do 65,535	Porty sieciowe, długości krótkich stringów.
UINT32	4 bajty	0 do 4 mld	Offsety w dużych plikach, ID obiektów.
FLOAT64	0x03	double	8
STRING_UTF8	0x04	String	Zmienny (L+V)
*/
public enum DaxDataType {
    // 1. Core Types
    STRING("STR", "Standard text ",  String.class, true),
    INTEGER("INT", "32-bit signed integer (Oracle NUMBER(9,0))", Integer.class, true),
    LONG("LNG", "64-bit signed integer (Oracle NUMBER(18,0))", Long.class, true),
    DOUBLE("DBL", "Binary floating point (Oracle BINARY_DOUBLE)", Double.class, true),
    DECIMAL("DEC", "Precise fixed-point decimal (Oracle NUMBER)", BigDecimal.class, true),
    BOOLEAN("BLN", "Boolean value (Oracle CHAR(1) Y/N)", Boolean.class, true),
    CHARACTER("CHR", "Boolean value (Oracle CHAR(1) )",Character.class, true),

    // 2. Temporal Types
    LOCAL_DATE("LDD", "Date without time (YYYY-MM-DD)", LocalDate.class, true),
    LOCAL_DATE_TIME("LDT", "Date and time (TIMESTAMP)", LocalDateTime.class, true),
    DAY_OF_MONTH("DAY", "Day of month value (1-31)", Integer.class,  true),
    DURATION("DUR", "Time interval/duration", Duration.class,true),

    // 3. Specialized Strings
    JSON("JSON", "JavaScript Object Notation structured string", String.class, true),
    XML("XML", "eXtensible Markup Language structured string", String.class, true),
    CSV("CSV", "Comma Separated Values stream", String.class, true),
    REGEXP("REGX", "Regular expression pattern", String.class, true),
    EMAIL("EML", "Email address validation format", String.class, true),

    // 4. International Standards
//    COUNTRY("CNR", "ISO 3166 Country code (2 or 3 chars)"), //, String.class),
//    CURRENCY("CUR", "ISO 4217 Currency code (3 chars)"), //, String.class),
//
//    // 5. Structural Types
    ENTITY("ENT", "DAXP Entity / Object structure", Object.class,false), //, null),
    COLLECTION("COL", "Universal DAXP Collection (C) with attributes", Collection.class , false), //, null),
    TAG("TAG", "Reference to another tag/field within the frame",DaxTag.class, false), //, DaxTag.class),
    FIELD("FIELD", "Reference to field within the frame",DaxTag.class, false), //, DaxTag.class),   ??????????????????
//
    MESSAGE_TYPE("MSG","Message type",String.class,true),
//
    QUANTITY("QNT", "Quantity",Integer.class,true),
//    BINARY("RAW", "Quantity",byte[].class), // ?????????
    NONE("NONE","None, not try detected",void.class, false),
//    BASE64("B64", "Base 64"),  ///maybe byte[]
    UNKNOWN("UNKNOWN", "Unknown", void.class, false);  ///maybe byte[]

    //BASE64


    private final String code;
    private final String description;
    private final Class<?> javaClass;
    private final boolean isPrimitiveType;
    /**
     * Determines the DAXP type based on a Java Object.
     * Useful when serializing from Java to DAXP.
     */
     // Reverse lookup map for performance
    private static final Map<String, DaxDataType> CODE_LOOKUP = new HashMap<>();
    static {
        for (DaxDataType type : values()) {
            CODE_LOOKUP.put(type.code, type);
        }
    }


//    DaxDataType(String code, String description){ //, Class<?> javaType) {
//        this(code, description, Void.class, );
//
//    }
    DaxDataType(String code, String description, Class<?> javaClass, boolean isPrimitiveType){
        this.code = code;
        this.description = description;
        this.javaClass = javaClass;
        this.isPrimitiveType = isPrimitiveType;
    }


    public String getCode() { return code; }
    public String getDescription() { return description; }


    @Override
    public String toString() {
        return String.format("[%s] %s (%s)", code, name(), description);
    }

    /**
     * Finds DaxDataType by its 3-letter code.
     * @param code e.g. "INT", "STR"
     * @return DaxDataType or null if not found
     */
    public static DaxDataType fromCode(String code) {
        if (code == null) return null;
        return CODE_LOOKUP.get(code.toUpperCase());
    }
    public boolean isPrimitiveType(){
        return isPrimitiveType;
    }
}