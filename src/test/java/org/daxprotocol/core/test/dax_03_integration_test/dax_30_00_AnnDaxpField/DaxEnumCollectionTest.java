package org.daxprotocol.core.test.dax_03_integration_test.dax_30_00_AnnDaxpField;

import org.daxprotocol.core.annotation.DaxpCollection;
import org.daxprotocol.core.annotation.DaxpEntity;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.model.tag.DaxTag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DaxEnumCollectionTest extends DaxConfigBaseInit{


//---------------------


    @Test
    void checkDetectEnumCollection(){
        @DaxpCollection(tagId=1001)
        enum TestEnum{
            ENUM_VAL1,
            ENUM_VAL2
        }
        daxEngine.register(TestEnum .class);

        printSemanticRegister();

        DaxTag tag1001 =  DaxTag.of(config.getAppNamespaceId() , 1001);
        Assertions.assertEquals(DaxDataType.COLLECTION, semanticInspector.getDataType(tag1001));
        Assertions.assertEquals(DaxDataType.STRING, semanticInspector.getCollectionValueDataType(tag1001));
        Assertions.assertTrue(semanticInspector.hasKey(tag1001));
        Assertions.assertTrue(semanticInspector.isDictionary(tag1001));
        Assertions.assertTrue(semanticInspector.isCollectionClosed(tag1001));

    }

    @Test
    void checkEnumCollection(){
        @DaxpCollection(tagId=1001)
        enum TestEnum{
          ENUM_VAL1,
          ENUM_VAL2
        }

        @DaxpEntity(tagId = 1000)
        class TestClass01{
            @DaxpField(tagId=1010)
            TestEnum enumColl;
        }
        daxEngine.register(TestEnum .class);
        daxEngine.register(TestClass01 .class);

        printSemanticRegister();


    }
    @Test
    void checkEnumNOTDaxpCollection(){
        // IT IS NOT GOOD
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
        daxEngine.register(TestEnum .class);

        printSemanticRegister();

        DaxTag tag1001 =  DaxTag.of(config.getAppNamespaceId() , 1001);
        Assertions.assertEquals(DaxDataType.COLLECTION, semanticInspector.getDataType(tag1001));
        Assertions.assertEquals(DaxDataType.STRING, semanticInspector.getCollectionValueDataType(tag1001));
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