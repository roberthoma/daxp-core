package org.daxprotocol.core.unit_test.dax_10_00_annotation;


import org.daxprotocol.core.annotation.DaxpEntity;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.annotation.DaxpValue;
import org.daxprotocol.core.datatype.DaxDataType;

import java.util.*;

import static org.daxprotocol.core.unit_test.dax_10_00_annotation.DaxpSchema_Base.*;

@DaxpEntity(tagId = DaxpSchema_Base.TEST_TAG_ENTITY_5000, description = "Any desc from entity class")
public class DaxAnyTestEntity {


    @DaxpField(tagId = TEST_TAG_int_5001)
    int anyInt;

    @DaxpField(tagId = TEST_TAG_String_5002)
    String anyString;

    @Deprecated
    @DaxpField(tagId = TEST_TAG_Deprecated_String_5005)
    String anyDeprecatedString;

    //@Deprecated
    @DaxpField(tagId = TEST_TAG_char_5003, description = "Desc from main test Entity")
    char anyChar;

    @DaxpField(tagId = TEST_TAG_Boolean_5004, description = "Any tested boolean from entity")
    Boolean anyBoolean;


    @Deprecated
    @DaxpField(TEST_CTX_TAG_StrVal_FIX51)
    char fixTestValue;

    @DaxpField(tagId = TEST_ENUM_VALUE_5032)
    DaxAnyTestEnum enumValue;


    @DaxpField(tagId = TEST_SUB_DTO_1_5077)
    DaxSubEntity subEntity;

    @DaxpField(tagId = 5078)
    DaxSubEntity subDTO2;

    @DaxpValue(tagId = 5079)
    String strValueReadOnly = "Test value string only for read";

    @DaxpValue(tagId = 5080)
   public String getAnyStr() { return "Test method string only for read";}

    /// Collection test

    @DaxpField(tagId = 5082)
    List<String> stringList;

    @DaxpField(tagId = 5083)
    Map<Integer,String>  stringMap;

    @DaxpField(tagId = 5089)
    Set<String> stringSet;

    @DaxpField(tagId = 5090)
    Set<DaxSubEntity> daxSubEntitySet;

    @Deprecated
    @DaxpField(tagId = 5091)
    Queue<String>  queueStr;


    @DaxpField(tagId = 5092, name = "LinkedNameByAnn", description = "Any Description by Ann :) ")
    LinkedList<String>  linkedListStr;

    @DaxpField("5099")
    DaxTestEnumNoAnnotation  testEnumNoAnnotation;


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

    public DaxSubEntity getSubEntity() {
        return subEntity;
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
        this.subEntity = new DaxSubEntity(14,"testSubDTO",12.34);
        this.subDTO2 = new DaxSubEntity(34,"testSubDTO2",74.56);

        this.stringList    = List.of("strVal1","strVal2","strVal3","strVal1");
        this.linkedListStr  = new LinkedList<>( List.of("strLinkedVal1","strLinkedVal2","strLinkedVal2","strLinkedVal2"));

    }
}
