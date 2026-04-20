package org.daxprotocol.core.unit_test.dax_10_00_annotation;

import org.daxprotocol.core.annotation.DaxpDTO;
import org.daxprotocol.core.annotation.DaxpField;

@DaxpDTO(tagId = DaxpSchema_Base.TEST_TAG_SUB_DTO)
public class DaxSubDTO {
    @DaxpField("8001")
    private int value_1;

    @DaxpField("8002")
    private String value2;

    @DaxpField(value = "FIX:8003",uiLabel = "Fix label")
    private Double dd;

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


    public DaxSubDTO(int value_1, String value2, Double dd) {
        this.value_1 = value_1;
        this.value2 = value2;
        this.dd = dd;
    }
}
