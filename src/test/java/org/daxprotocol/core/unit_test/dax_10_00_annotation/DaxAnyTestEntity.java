package org.daxprotocol.core.unit_test.dax_10_00_annotation;


import org.daxprotocol.core.annotation.DaxpEntity;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.annotation.DaxpValue;

import java.util.*;

import static org.daxprotocol.core.unit_test.dax_10_00_annotation.DaxpManifest_Base.*;

@DaxpEntity( tagId = TEST_TAG_ENTITY_5000
            ,description = "Any desc from entity class"
            ,schema= SCHEMA_BASE_UT)
public class DaxAnyTestEntity {

    @DaxpField(tagId = TEST_RECURSIVE_ENT_5055)
    DaxAnyTestEntity recursiveEntity;

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
    char fixTestValue_FIX_51;

    @DaxpField(tagId = TEST_ENUM_VALUE_5032)
    DaxAnyTestEnum enumValue;


    @DaxpField(tagId = TEST_VALUE_SUB_ENTITY_5077)
    DaxSubEntity subEntity;

    @DaxpField(tagId = 5078)
    DaxSubEntity subDTO2;

    @DaxpValue(tagId = 5079)
    String strValueReadOnly = "Test value string only for read";

    @DaxpValue(tagId = 5080)
   public String getAnyStr() { return "Test method string only for read from method";}

    /// Collection test

    @DaxpField(tagId = 5082)
    List<String> stringList_5082;

    @DaxpField(tagId = 5083)
    Map<Integer,String>  stringMap;

    @DaxpField(tagId = 5089)
    Set<String> stringSet_5089;

    @DaxpField(tagId = 5090)
    Set<DaxSubEntity> daxSubEntitySet_5090;

    @Deprecated
    @DaxpField(tagId = 5091)
    Queue<String>  queueStr;


    @DaxpField(tagId = 5092, name = "LinkedNameByAnn", description = "Any Description by Ann :) ")
    LinkedList<String> linkedListStr_5092;

    @DaxpField("5099")
    DaxTestEnumNoAnnotation  testEnumNoAnnotation;

    @DaxpField("5100")
    DaxSubNoTagEntity subNoTagEntity;

    @DaxpField("5112")
    String  emptyStringS1;


   @DaxpField("5115")
    Map<Integer,String>  strMap;



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

    public char getFixTestValue_FIX_51() {
        return fixTestValue_FIX_51;
    }

    public DaxAnyTestEnum getEnumValue() {
        return enumValue;
    }

    public DaxSubEntity getSubEntity() {
        return subEntity;
    }

    public DaxAnyTestEntity(){

    }
    public DaxAnyTestEntity(String testStr, char testChar, int testInt){

        this.anyInt = testInt;
        this.anyString = testStr;
        this.anyChar = testChar;
        this.anyBoolean = true;
        this.fixTestValue_FIX_51 = 'X';

        this.stringSet_5089 = new HashSet<>(); //TODO idea how to prezent .. AS NULL or empty . DAXP is universal protocol.
        this.stringSet_5089.add("ABC");
        this.stringSet_5089.add("DEF");
        this.stringSet_5089.add("GHI");

        this.enumValue = DaxAnyTestEnum.ENUM_VALUE1;
        this.subEntity = new DaxSubEntity(14,"subEntity1",761.24);
        this.subDTO2   = new DaxSubEntity(34,"subEntity2",654.66);

        this.daxSubEntitySet_5090 = new HashSet<>();
        daxSubEntitySet_5090.add( new DaxSubEntity(57,"set_subEntity6",74.56));
        daxSubEntitySet_5090.add( new DaxSubEntity(89,"set_subEntity7",22.54));
        daxSubEntitySet_5090.add( new DaxSubEntity(149,"set_subEntity8",2376.47));

        this.stringList_5082 = List.of("strVal1","strVal2","strVal3","strVal4");

        this.linkedListStr_5092 = new LinkedList<>();
        this.linkedListStr_5092.add("strLinkedVal1");
        this.linkedListStr_5092.add("strLinkedVal2");
        this.linkedListStr_5092.add("strLinkedVal3");
        this.linkedListStr_5092.add("strLinkedVal4");

//        strMap = new HashMap<>();
//        strMap.put(1,"maoTestString1");
//        strMap.put(2,"maoTestString2");
//        strMap.put(3,"maoTestString3");

    }
}
