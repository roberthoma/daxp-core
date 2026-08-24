package org.daxprotocol.core.unit_test.dax_10_00_annotation;


import org.daxprotocol.core.annotation.DaxpEntity;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.annotation.DaxpValue;
import org.daxprotocol.core.application.DaxCoreConstants;

import java.util.*;

import static org.daxprotocol.core.unit_test.dax_10_00_annotation.DaxAnyManifest.MODEL_BASE_UT;
import static org.daxprotocol.core.unit_test.dax_10_00_annotation.DaxAnySchemaRegister.*;

@DaxpEntity( tagId = TEST_TAG_ENTITY_5000
            ,description = "Any desc from entity class"
            ,schema= MODEL_BASE_UT)
public class DaxAnyTestEntity {

    @DaxpField(tagId = TEST_RECURSIVE_ENT_5055)
    DaxAnyTestEntity recursiveEntity;

    @DaxpField(tagId = TEST_TAG_int_5001)
    int anyInt_5001;

    @DaxpField(tagId = TEST_TAG_String_5002)
    String anyString_5002;

    @Deprecated
    @DaxpField(tagId = TEST_TAG_Deprecated_String_5005)
    String anyDeprecatedString_5005;

    //@Deprecated
    @DaxpField(tagId = TEST_TAG_char_5003, description = "Desc from main test Entity")
    char anyChar_5003;

    @DaxpField(tagId = TEST_TAG_Boolean_5004, description = "Any tested boolean from entity")
    Boolean anyBoolean;


    @Deprecated
    @DaxpField(TEST_CTX_TAG_StrVal_FIX51)
    char fixTestValue_FIX_51;

    @DaxpField(tagId = TEST_ENUM_VALUE_5032)
    DaxAnyTestEnum enumValue_5032;


    @DaxpField(tagId = TEST_VALUE_SUB_ENTITY_5077)
    DaxSubEntity subEntity_5077;

    @DaxpField(tagId = 5078)
    DaxSubEntity subEntity_5078;

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

    @DaxpField("FIX:5112")
    String  emptyStringS1;


    @DaxpField("5115")
    Map<Integer,String> strMap_5115;


    @DaxpField("5116")
    Map<Integer,DaxSubEntity> subEntityMap_5116;

    @DaxpField("5117")
    Map<DaxSubEntity,DaxSubEntity> subEntityMap_5117;

    public int getAnyInt_5001() {
        return anyInt_5001;
    }

    public String getAnyString_5002() {
        return anyString_5002;
    }

    public char getAnyChar_5003() {
        return anyChar_5003;
    }

    public Boolean getAnyBoolean() {
        return anyBoolean;
    }

    public char getFixTestValue_FIX_51() {
        return fixTestValue_FIX_51;
    }

    public DaxAnyTestEnum getEnumValue_5032() {
        return enumValue_5032;
    }

    public DaxSubEntity getSubEntity_5077() {
        return subEntity_5077;
    }

    public DaxAnyTestEntity(){

    }
    public DaxAnyTestEntity(String testStr, char testChar, int testInt){

        this.anyInt_5001 = testInt;
        this.anyString_5002 = testStr;
        this.anyChar_5003 = testChar;
        this.anyBoolean = true;
        this.fixTestValue_FIX_51 = 'X';

        subNoTagEntity = new DaxSubNoTagEntity() ;

        this.stringSet_5089 = new HashSet<>(); //TODO idea how to present .. AS NULL or empty . DAXP is universal protocol.
        this.stringSet_5089.add("ABC");
        this.stringSet_5089.add("DEF");
        this.stringSet_5089.add("GHI");

        this.enumValue_5032 = DaxAnyTestEnum.ENUM_VALUE1;

        this.subEntity_5077 = new DaxSubEntity(14,"Kowalski"+ DaxCoreConstants.DAXP_NAMESPACE_TAG_PREFIX +"N",761.24);
        this.subEntity_5078 = new DaxSubEntity(34,"subEntity@2=",654.66);

        this.daxSubEntitySet_5090 = new HashSet<>();
        daxSubEntitySet_5090.add( new DaxSubEntity(57,"set_@subEntity6",74.56));
        daxSubEntitySet_5090.add( new DaxSubEntity(89,"set_=subEntity7",22.54));
        daxSubEntitySet_5090.add( new DaxSubEntity(149,"set_#subEntity8",2376.47));

        this.stringList_5082 = List.of("strVal1","strVal2","strVal3","strVal4");

        this.linkedListStr_5092 = new LinkedList<>();
        this.linkedListStr_5092.add("strLinkedVal1");
        this.linkedListStr_5092.add("strLinkedVal2");
        this.linkedListStr_5092.add("strLinkedVal3");
        this.linkedListStr_5092.add("strLinkedVal4");
        this.linkedListStr_5092.add("strLinkedVal2");

        strMap_5115 = new HashMap<>();
        strMap_5115.put(1,"mapTestString1");
        strMap_5115.put(2,"mapTestString2");
        strMap_5115.put(3,"mapTestString3");

        subEntityMap_5116 = new HashMap<>();
        subEntityMap_5116.put(11,new DaxSubEntity(237,"set_subEntity11",72.56));
        subEntityMap_5116.put(12, new DaxSubEntity(849,"set_subEntity12",55.54));
        subEntityMap_5116.put(14, new DaxSubEntity(69,"set_subEntity14",234.47));


        subEntityMap_5117 = new HashMap<>();
        subEntityMap_5117.put(new DaxSubEntity(123,"key_subEntity11",72.56),
                              new DaxSubEntity(237,"set_subEntity11",72.56));

        subEntityMap_5117.put(new DaxSubEntity(949,"key_subEntity12",45.54)
                , new DaxSubEntity(849,"set_subEntity12",55.54));

        subEntityMap_5117.put(new DaxSubEntity(456,"key_subEntity14",54.47)
                , new DaxSubEntity(69,"set_subEntity14",234.47));




    }
}