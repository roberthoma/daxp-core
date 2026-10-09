package org.daxprotocol.core.test.dax_03_integration_test.dax_30_00_AnnDaxpField;

import org.daxprotocol.core.annotation.DaxpEntity;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.test.dax_00_service.DaxMessageDecorator;
import org.daxprotocol.core.test.dax_03_integration_test.dax_00_00_base.DaxNamespaceConfigBaseInit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class DaxNestedEntityBulkCollectionTest extends DaxNamespaceConfigBaseInit {



    @Test
    void checkSetEntityCollection() {
        System.out.println("------------------------------------");
        System.out.println("checkSetEntityCollection");
        System.out.println("-");

        @DaxpEntity(tagId = 900)
        class NestedEntityClass{
            @DaxpField(tagId = 902)  String str1;
            @DaxpField(tagId = 903, namespace = "FIX")  String str2;
            @DaxpField(tagId = 904)  Integer int1;

            public NestedEntityClass (String str1, String str2){
                this.str1 = str1;
                this.str2 = str2;
            }
            public NestedEntityClass (String str1, String str2, Integer int1){
                this.str1 = str1;
                this.str2 = str2;
                this.int1 = int1;
            }
        }


        @DaxpEntity(tagId = 1000)
        class BaseEntityClass{
            @DaxpField(tagId = 1002)  String str1;
            @DaxpField(tagId = 1003)  NestedEntityClass nestedEntity;

            public BaseEntityClass (String str1, String str2){
                this.str1 = str1;
                this.nestedEntity = new NestedEntityClass(str2,"abc",3);
            }
            public BaseEntityClass (String str1, String str2, Integer int1){
                this.str1 = str1;
                this.nestedEntity = new NestedEntityClass(str2,"cde",4);
            }
        }

        @DaxpEntity(tagId = 1010)
        class TestClass01 {
            @DaxpField(tagId = 1011) final Set<BaseEntityClass> entitySet;
            @DaxpField(tagId = 1012)  String testStr ;


            public TestClass01(){
                this.entitySet = new HashSet<>();
                this.entitySet.add(new BaseEntityClass( "ABC","xyz"));
                this.entitySet.add(new BaseEntityClass( "DEF GG","prz",123));
                this.entitySet.add(new BaseEntityClass( "ZAZE tt","uer tt"));
                this.entitySet.add(new BaseEntityClass( "TETE tt","WEWE fg",457));
                this.testStr = "ABS STR";

            }

        }

        daxEngine.register(NestedEntityClass.class);
        daxEngine.register(BaseEntityClass.class);
        daxEngine.register(TestClass01.class);

        printSemanticRegister();
        TestClass01 ttObj = new TestClass01();

        daxEngine.getCoreConfig().setMinBulkSize(100);

        DaxMessage msg = msgFactory.toDaxMessage("BULK.TTT",ttObj);
        DaxPreamble preamble = new DaxPreamble();

//        System.out.println(messageCodec.encode(msg, preamble));
        System.out.println(DaxMessageDecorator.decorate(messageCodec.encode(msg, preamble)));
//        System.out.println(messageCodec.encode(msg, preamble));
        DaxTag tag1011 = DaxTag.of(config.getAppNamespaceId(), 1011);

        daxEngine.getCoreConfig().resetMinBulkSize();
        DaxMessage msg2 = msgFactory.toDaxMessage("BULK.TTT",ttObj);
        System.out.println(DaxMessageDecorator.decorate(messageCodec.encode(msg2, preamble)));

        Assertions.assertEquals(DaxDataType.COLLECTION, semanticInspector.getDataType(tag1011));
//        Assertions.assertEquals(DaxDataType.STRING, semanticInspector.getCollectionValueDataType(tag1001));
//        Assertions.assertFalse(semanticInspector.hasKey(tag1001));
        System.out.println("----------------------------------------");
    }
    @Test
    void checkListEntityCollection() {
        System.out.println("------------------------------------");
        System.out.println("checkListEntityCollection");
        System.out.println("-");


        @DaxpEntity(tagId = 1000)
        class BaseEntityClass{
            @DaxpField(tagId = 1002)  String str1;
            @DaxpField(tagId = 1003)  String str2;
            @DaxpField(tagId = 1004)  Integer int1;

            public BaseEntityClass (String str1, String str2){
                this.str1 = str1;
                this.str2 = str2;
            }
            public BaseEntityClass (String str1, String str2, Integer int1){
                this.str1 = str1;
                this.str2 = str2;
                this.int1 = int1;
            }
        }

        @DaxpEntity(tagId = 1010)
        class TestClass01 {
            @DaxpField(tagId = 1011) final List<BaseEntityClass> entitySet;
            @DaxpField(tagId = 1012)  String testStr ;


            public TestClass01(){
                this.entitySet = new ArrayList<>();
                this.entitySet.add(new BaseEntityClass( "ABC","xyz"));
                this.entitySet.add(new BaseEntityClass( "DEF GG","prz",123));
                this.entitySet.add(new BaseEntityClass( "ZAZE tt","uer tt"));
                this.entitySet.add(new BaseEntityClass( "TETE tt","WEWE fg",457));
                this.testStr = "ABS STR";

            }

        }
        daxEngine.register(BaseEntityClass.class);
        daxEngine.register(TestClass01.class);

        printSemanticRegister();
        TestClass01 ttObj = new TestClass01();

        DaxMessage msg = msgFactory.toDaxMessage("BULK.TTT",ttObj);

        DaxPreamble preamble = new DaxPreamble();

//        System.out.println(messageCodec.encode(msg, preamble));
        System.out.println(DaxMessageDecorator.decorate(messageCodec.encode(msg, preamble)));
//        System.out.println(messageCodec.encode(msg, preamble));
        DaxTag tag1011 = DaxTag.of(config.getAppNamespaceId(), 1011);
        Assertions.assertEquals(DaxDataType.COLLECTION, semanticInspector.getDataType(tag1011));
//        Assertions.assertEquals(DaxDataType.STRING, semanticInspector.getCollectionValueDataType(tag1001));
//        Assertions.assertFalse(semanticInspector.hasKey(tag1001));
        System.out.println("----------------------------------------");
    }

    @Test
    void checkMapEntityCollection() {
        System.out.println("------------------------------------");
        System.out.println("checkMapEntityCollection");
        System.out.println("-");

        @DaxpEntity(tagId = 1000)
        class BaseEntityClass{
            @DaxpField(tagId = 1002)  String str1;
            @DaxpField(tagId = 1003)  String str2;
            @DaxpField("FIX:1004")  Integer int1;

            public BaseEntityClass (String str1, String str2){
                this.str1 = str1;
                this.str2 = str2;
            }
            public BaseEntityClass (String str1, String str2, Integer int1){
                this.str1 = str1;
                this.str2 = str2;
                this.int1 = int1;
            }
        }

        @DaxpEntity(tagId = 1010)
        class TestClass01 {
            @DaxpField(tagId = 1011) final Map<Integer,BaseEntityClass> entitySet;
            @DaxpField(tagId = 1012)  String testStr ;


            public TestClass01(){
                this.entitySet = new HashMap<>();
                this.entitySet.put(1,new BaseEntityClass( "ABC","xyz"));
                this.entitySet.put(2,new BaseEntityClass( "DEF GG","prz",123));
                this.entitySet.put(3,new BaseEntityClass( "ZAZE tt","uer tt"));
                this.entitySet.put(4,new BaseEntityClass( "TETE tt","WEWE fg",457));
                this.testStr = "ABS STR";

            }

        }
        daxEngine.register(BaseEntityClass.class);
        daxEngine.register(TestClass01.class);

        printSemanticRegister();
        TestClass01 ttObj = new TestClass01();

        DaxMessage msg = msgFactory.toDaxMessage("BULK.TTT",ttObj);

        DaxPreamble preamble = new DaxPreamble();

        System.out.println(messageCodec.encode(msg, preamble));
        System.out.println(DaxMessageDecorator.decorate(messageCodec.encode(msg, preamble)));
//        System.out.println(messageCodec.encode(msg, preamble));
        DaxTag tag1011 = DaxTag.of(config.getAppNamespaceId(), 1011);
        Assertions.assertEquals(DaxDataType.COLLECTION, semanticInspector.getDataType(tag1011));
//        Assertions.assertEquals(DaxDataType.STRING, semanticInspector.getCollectionValueDataType(tag1001));
//        Assertions.assertFalse(semanticInspector.hasKey(tag1001));
        System.out.println("----------------------------------------");
    }

    @Test
    void checkMapEntKeyEntityCollection() {
        System.out.println("------------------------------------");
        System.out.println("checkMapEntKeyEntityCollection");
        System.out.println("-");

        @DaxpEntity(tagId = 900)
        class BaseKeyEntityClass{
            @DaxpField(tagId = 902)  String str1;
            @DaxpField(tagId = 903)  String str2;
            @DaxpField("FIX:904")  Integer int1;

            public BaseKeyEntityClass (String str1, String str2){
                this.str1 = str1;
                this.str2 = str2;
            }
            public BaseKeyEntityClass (String str1, String str2, Integer int1){
                this.str1 = str1;
                this.str2 = str2;
                this.int1 = int1;
            }
        }



        @DaxpEntity(tagId = 1000)
        class BaseEntityClass{
            @DaxpField(tagId = 1002)  String str1;
            @DaxpField(tagId = 1003)  String str2;
            @DaxpField("FIX:1004")  Integer int1;

            public BaseEntityClass (String str1, String str2){
                this.str1 = str1;
                this.str2 = str2;
            }
            public BaseEntityClass (String str1, String str2, Integer int1){
                this.str1 = str1;
                this.str2 = str2;
                this.int1 = int1;
            }
        }

        @DaxpEntity(tagId = 1010)
        class TestClass01 {
            @DaxpField(tagId = 1011) final Map<BaseKeyEntityClass,BaseEntityClass> entitySet;
            @DaxpField(tagId = 1012)  String testStr ;


            public TestClass01(){
                this.entitySet = new HashMap<>();
                this.entitySet.put(new BaseKeyEntityClass( "Ak2","xK2"),
                        new BaseEntityClass( "ABC","xyz"));
                this.entitySet.put(new BaseKeyEntityClass( "Ak3","xK3"),
                        new BaseEntityClass( "DEF GG","prz",123));
                this.entitySet.put(new BaseKeyEntityClass( "Ak4","xKK4")
                        ,new BaseEntityClass( "ZAZE tt","uer tt"));
                this.entitySet.put(new BaseKeyEntityClass( "Ak4","xK5"),
                        new BaseEntityClass( "TETE tt","WEWE fg",457));
                this.testStr = "ABS STR";

            }

        }

        daxEngine.register(BaseKeyEntityClass.class);
        daxEngine.register(BaseEntityClass.class);
        daxEngine.register(TestClass01.class);

        printSemanticRegister();
        TestClass01 ttObj = new TestClass01();

        DaxMessage msg = msgFactory.toDaxMessage("BULK.TTT",ttObj);

        DaxPreamble preamble = new DaxPreamble();

        System.out.println(messageCodec.encode(msg, preamble));
        System.out.println(DaxMessageDecorator.decorate(messageCodec.encode(msg, preamble)));
//        System.out.println(messageCodec.encode(msg, preamble));
        DaxTag tag1011 = DaxTag.of(config.getAppNamespaceId(), 1011);
        Assertions.assertEquals(DaxDataType.COLLECTION, semanticInspector.getDataType(tag1011));
//        Assertions.assertEquals(DaxDataType.STRING, semanticInspector.getCollectionValueDataType(tag1001));
//        Assertions.assertFalse(semanticInspector.hasKey(tag1001));
        System.out.println("----------------------------------------");
    }

}
