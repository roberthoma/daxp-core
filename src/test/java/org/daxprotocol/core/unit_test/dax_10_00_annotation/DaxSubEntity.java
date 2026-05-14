package org.daxprotocol.core.unit_test.dax_10_00_annotation;

import org.daxprotocol.core.annotation.DaxpEntity;
import org.daxprotocol.core.annotation.DaxpField;

import static org.daxprotocol.core.unit_test.dax_10_00_annotation.DaxpSchema_Base.TEST_TAG_char_5003;

@DaxpEntity(tagId = DaxpSchema_Base.TEST_TAG_SUB_ENTITY_8000)
public class DaxSubEntity {
    @DaxpField("8001")
    private int value_1;

    @DaxpField("8002")
    private String value2;

    @DaxpField(value = "FIX:8003",description = "Fix any double", name = "anyDouble")
    private Double dd;

    @Deprecated
    @DaxpField(tagId = TEST_TAG_char_5003, description = "Desc from subEntity")
    char anyChar;


    public void setValue_1(int value_1) {
        this.value_1 = value_1;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public void setDd(Double dd) {
        this.dd = dd;
    }


    public int getValue_1() {
        return value_1;
    }

    public String getValue2() {
        return value2;
    }

    public Double getDd() {
        return dd;
    }


    public DaxSubEntity(int value_1, String value2, Double dd) {
        this.value_1 = value_1;
        this.value2 = value2;
        this.dd = dd;
    }
}
