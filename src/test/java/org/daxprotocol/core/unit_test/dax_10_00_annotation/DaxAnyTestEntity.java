package org.daxprotocol.core.unit_test.dax_10_00_annotation;


import org.daxprotocol.core.annotation.DaxpEntity;
import org.daxprotocol.core.annotation.DaxpField;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.daxprotocol.core.unit_test.dax_10_00_annotation.DaxpSchema_Base.*;

@DaxpEntity(tagId = DaxpSchema_Base.TEST_TAG_ENTITY, description = "Any desc from entity class")
public class DaxAnyTestEntity {


    @DaxpField(tagId = TEST_TAG_int)
    int anyInt;

    @DaxpField(tagId = TEST_TAG_String)
    String anyString;

    @Deprecated
    @DaxpField(tagId = TEST_TAG_Deprecated_String)
    String anyDeprecatedString;


    @DaxpField(tagId = TEST_TAG_char)
    char anyChar;

    @DaxpField(tagId = TEST_TAG_Boolean, description = "Any tested boolean from entity")
    Boolean anyBoolean;


    @Deprecated
    @DaxpField(TEST_CTX_TAG_StrVal)
    char fixTestValue;

    @DaxpField(tagId = TEST_ENUM_VALUE)
    DaxAnyTestEnum enumValue;

    //@DaxpField("5077")
    @DaxpField(tagId = TEST_SUB_DTO_1 )
    DaxSubEntity subDTO;

    @DaxpField(tagId = 5078)
    DaxSubEntity subDTO2;





    /// Collection test

    @DaxpField(tagId = 5082)
    List<String> stringList;

    @DaxpField(tagId = 5083)
    Map<Integer,String>  stringMap;

    @DaxpField(tagId = 5089)
    Set<String> stringSet;

    @DaxpField(tagId = 5090)
    Set<Integer> integerSet;



    public int getAnyInt() {
        return anyInt;
    }

    public String getAnyString() {
        return anyString;
    }

    public char getAnyChar() {
        return anyChar;
    }

    public Boolean getAnyBoolean() {
        return anyBoolean;
    }

    public char getFixTestValue() {
        return fixTestValue;
    }

    public DaxAnyTestEnum getEnumValue() {
        return enumValue;
    }

    public DaxSubEntity getSubDTO() {
        return subDTO;
    }

    public DaxAnyTestEntity(){
//        this.fixTestValue = 'X';
//        this.enumValue = DaxEnumSample.ENUM_VALUE1;
//        this.subDTO = new DaxSubDTO(14,"testSubDTO",12.34);

    }
    public DaxAnyTestEntity(String testStr, char testChar, int testInt){

        this.anyInt = testInt;
        this.anyString = testStr;
        this.anyChar = testChar;
        this.anyBoolean = true;
        this.fixTestValue = 'X';
        this.enumValue = DaxAnyTestEnum.ENUM_VALUE1;
        this.subDTO = new DaxSubEntity(14,"testSubDTO",12.34);
        this.subDTO2 = new DaxSubEntity(34,"testSubDTO2",74.56);

        stringList = List.of("strVal1","strVal2","strVal3","strVal1");


    }
}
