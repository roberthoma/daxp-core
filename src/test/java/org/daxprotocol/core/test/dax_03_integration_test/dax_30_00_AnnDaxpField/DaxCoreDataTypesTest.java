package org.daxprotocol.core.test.dax_03_integration_test.dax_30_00_AnnDaxpField;

import org.daxprotocol.core.annotation.DaxpEntity;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.model.tag.DaxTag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DaxCoreDataTypesTest extends DaxConfigBaseInit{

    @Test
    void checkAnnDaxpEntity(){
        @DaxpEntity(tagId = 1000)
        class TestClass01{
        }

        daxEngine.register(TestClass01 .class);
        printSemanticRegister();
        DaxTag tag1000 =  DaxTag.of(config.getAppNamespaceId() , 1000);

        //
        Assertions.assertEquals(2,semanticRegistry.getTagAttributeMap().get(tag1000).size());

        Assertions.assertEquals(DaxDataType.ENTITY, semanticInspector.getDataType(tag1000));

    }

    @Test
    void checkCoreDataTypes(){
        @DaxpEntity(tagId = 1000, description = "Test Entity ")
        class TestClass01{
            @DaxpField(tagId = 1010)
            public String testString1;

            @DaxpField(tagId = 1020,name = "INTEGER_NAME")
            public Integer testInteger;

            @DaxpField(tagId = 1021,name = "Primitive_INTEGER_NAME")
            public int testPrimitiveInt;

            @DaxpField(tagId = 1030,name = "LONG_NAME")
            public Long testLong;

            @DaxpField(tagId = 1031,name = "Primitive_LONG_NAME")
            public long testPrimLong;

            @DaxpField(tagId = 1040,name = "DECIMAL_NAME")
            public BigDecimal testDecimal;

            @DaxpField(tagId = 1050,name = "CHAR_NAME")
            public Character testCharacter;

            @DaxpField(tagId = 1051,name = "Primitive_CHAR_NAME")
            public char testPrimCharacter;

            @DaxpField(tagId = 1060,name = "DOUBLE_NAME")
            public Double testDouble;

            @DaxpField(tagId = 1061,name = "Primitive_DOUBLE_NAME")
            public double testPrimDouble;

            @DaxpField(tagId = 1070,name = "BOOLEAN_NAME")
            public Boolean testBoolean;

            @DaxpField(tagId = 1071,name = "Primitive_BOOLEAN_NAME")
            public boolean testPrimBoolean;

        }

        daxEngine.register(TestClass01 .class);

        DaxTag tag1010 =  DaxTag.of(config.getAppNamespaceId() , 1010);
        DaxTag tag1020 =  DaxTag.of(config.getAppNamespaceId() , 1020);
        DaxTag tag1021 =  DaxTag.of(config.getAppNamespaceId() , 1021);
        DaxTag tag1030 =  DaxTag.of(config.getAppNamespaceId() , 1030);
        DaxTag tag1031 =  DaxTag.of(config.getAppNamespaceId() , 1031);
        DaxTag tag1040 =  DaxTag.of(config.getAppNamespaceId() , 1040);
        DaxTag tag1050 =  DaxTag.of(config.getAppNamespaceId() , 1050);
        DaxTag tag1051 =  DaxTag.of(config.getAppNamespaceId() , 1051);
        DaxTag tag1060 =  DaxTag.of(config.getAppNamespaceId() , 1060);
        DaxTag tag1061 =  DaxTag.of(config.getAppNamespaceId() , 1061);
        DaxTag tag1070 =  DaxTag.of(config.getAppNamespaceId() , 1070);
        DaxTag tag1071 =  DaxTag.of(config.getAppNamespaceId() , 1071);

        Assertions.assertEquals(DaxDataType.STRING,  semanticInspector.getDataType(tag1010));
        Assertions.assertEquals(DaxDataType.INTEGER, semanticInspector.getDataType(tag1020));
        Assertions.assertEquals(DaxDataType.INTEGER, semanticInspector.getDataType(tag1021));
        Assertions.assertEquals(DaxDataType.LONG,    semanticInspector.getDataType(tag1030));
        Assertions.assertEquals(DaxDataType.LONG,    semanticInspector.getDataType(tag1031));
        Assertions.assertEquals(DaxDataType.DECIMAL, semanticInspector.getDataType(tag1040));
        Assertions.assertEquals(DaxDataType.CHARACTER, semanticInspector.getDataType(tag1050));
        Assertions.assertEquals(DaxDataType.CHARACTER, semanticInspector.getDataType(tag1051));
        Assertions.assertEquals(DaxDataType.DOUBLE, semanticInspector.getDataType(tag1060));
        Assertions.assertEquals(DaxDataType.DOUBLE, semanticInspector.getDataType(tag1061));
        Assertions.assertEquals(DaxDataType.BOOLEAN, semanticInspector.getDataType(tag1070));
        Assertions.assertEquals(DaxDataType.BOOLEAN, semanticInspector.getDataType(tag1071));

        printSemanticRegister();

    }
    @Test
    void checkSetCollection(){
        @DaxpEntity(tagId = 1000)
        class TestClass01{
            @DaxpField(tagId=1001)
            Set<String> strList;
        }
        daxEngine.register(TestClass01 .class);
        printSemanticRegister();
        DaxTag tag1001 =  DaxTag.of(config.getAppNamespaceId() , 1001);
        Assertions.assertEquals(DaxDataType.COLLECTION, semanticInspector.getDataType(tag1001));
        Assertions.assertEquals(DaxDataType.STRING, semanticInspector.getValueDataType(tag1001));
        Assertions.assertFalse( semanticInspector.hasKey(tag1001));
    }

    @Test
    void checkListCollection(){
        @DaxpEntity(tagId = 1000)
        class TestClass01{
            @DaxpField(tagId=1001)
            List<String> strList;
        }
        daxEngine.register(TestClass01 .class);
        printSemanticRegister();
        DaxTag tag1001 =  DaxTag.of(config.getAppNamespaceId() , 1001);
        Assertions.assertEquals(DaxDataType.COLLECTION, semanticInspector.getDataType(tag1001));
        Assertions.assertEquals(DaxDataType.STRING, semanticInspector.getValueDataType(tag1001));
        Assertions.assertFalse( semanticInspector.hasKey(tag1001));
        Assertions.assertTrue( semanticInspector.isAllowDuplicates(tag1001));
    }

    @Test
    void checkMapCollection(){
        @DaxpEntity(tagId = 1000)
        class TestClass01{
            @DaxpField(tagId=1001)
            Map<Integer,String> strMap;
        }
        daxEngine.register(TestClass01 .class);
        printSemanticRegister();

        DaxTag tag1001 =  DaxTag.of(config.getAppNamespaceId() , 1001);
        Assertions.assertEquals(DaxDataType.COLLECTION, semanticInspector.getDataType(tag1001));
        Assertions.assertEquals(DaxDataType.INTEGER, semanticInspector.getKeyDataType(tag1001));
        Assertions.assertEquals(DaxDataType.STRING, semanticInspector.getValueDataType(tag1001));
        Assertions.assertTrue(semanticInspector.hasKey(tag1001));

    }


    @Test
    void checkEnumCollection(){
        enum TestEnum{
          ENUM_VAL1,
          ENUM_VAL2
        }
        @DaxpEntity(tagId = 1000)
        class TestClass01{
            @DaxpField(tagId=1001)
            TestEnum enumColl;
        }
        daxEngine.register(TestClass01 .class);
        printSemanticRegister();

        DaxTag tag1001 =  DaxTag.of(config.getAppNamespaceId() , 1001);
        Assertions.assertEquals(DaxDataType.COLLECTION, semanticInspector.getDataType(tag1001));
        Assertions.assertEquals(DaxDataType.STRING, semanticInspector.getValueDataType(tag1001));
        Assertions.assertTrue(semanticInspector.hasKey(tag1001));
        Assertions.assertTrue(semanticInspector.isDictionary(tag1001));
        Assertions.assertTrue(semanticInspector.isCollectionClosed(tag1001));


    }
}



/*

TODO
 ENUMS,
 DaxpCollection
 java.util.Queue
 java.util.LinkedList

 ========================

    public static final DaxTag COLLECTION_ID = daxpSysTag(129);
    public static final DaxTag COLLECTION_KEY = daxpSysTag(132);
    public static final DaxTag COLLECTION_VALUE = daxpSysTag(133);


    public static final DaxTag COLLECTION_IS_ORDERED = daxpSysTag(212);
    public static final DaxTag COLLECTION_IS_NAVIGABLE = daxpSysTag(213);

    public static final DaxTag COLLECTION_IS_CLOSED = daxpSysTag(215);

    public static final DaxTag COLLECTION_KEY_DATA_TYPE = daxpSysTag(225);
    public static final DaxTag COLLECTION_KEY_TYPE_REF_ID = daxpSysTag(226);

    public static final DaxTag COLLECTION_VALUE_DATA_TYPE = daxpSysTag(227);
    public static final DaxTag COLLECTION_VALUE_TYPE_REF_ID = daxpSysTag(228);

    public static final DaxTag COLLECTION_BULK_VALUE = daxpSysTag(230);

    public static final DaxTag IS_COLLECTION_INSTANCE = daxpSysTag(240);

*/