package org.daxprotocol.core.unit_test.dax_10_00_annotation;

import org.daxprotocol.core.annotation.DaxpCollection;
import org.daxprotocol.core.annotation.DaxpDictionaryEntry;

@DaxpCollection(tagId = DaxAnySchemaRegister.TEST_COLLECTION_DIC_ENUM_6002)
public enum DaxAnyTestDicEnum implements DaxpDictionaryEntry<Integer, String> {

    DIC_VAL1(341, "DIC_SYMB_VAL1", "Test dic 1 value"),
    DIC_VAL2(342, "DIC_SYMB_VAL2", "Test dic 2 value"),
    DIC_VAL3(343, "DIC_SYMB_VAL3", "Test dic 3 value"),
    DIC_VAL4(344, "DIC_SYMB_VAL4", "Test dic 4 value"),
    DIC_VAL5(345, "DIC_SYMB_VAL5", "Test dic 5 value");

    private final int key;
    private final String symbol;
    private final String description;

    DaxAnyTestDicEnum(int key, String symbol, String description) {
        this.key = key;
        this.symbol = symbol;
        this.description = description;
    }

    @Override
    public Integer key() {
        return key;
    }

    @Override
    public String value() {
        return symbol;
    }

    @Override
    public String symbol() {
        return symbol;
    }

    @Override
    public String description() {
        return description;
    }

//    @Override
//    public Map<Integer, String> getMap() {
//        return Map.of();
//    }
}