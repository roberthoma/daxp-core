package org.daxprotocol.core.test.dax_03_integration_test.dax_30_00_AnnDaxpField;

import jakarta.validation.constraints.Min;
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

public class DaxPrimitiveBulkCollectionTest extends DaxNamespaceConfigBaseInit {

    @Test
    void checkSetCollection() {

        @DaxpEntity(tagId = 1000)
        class TestClass01 {
            @DaxpField(tagId = 1001) final Set<String> strList;
            @DaxpField(tagId = 1002)  String str1;


        public TestClass01(){
            this.strList = new HashSet<>();
            this.strList.add("ABC");
            this.strList.add("DEF");
            this.strList.add("GHI");
            this.strList.add("JKL");
            this.str1 = "Test value";
        }

        }
        daxEngine.register(TestClass01.class);
        printSemanticRegister();
        TestClass01 ttObj = new TestClass01();

        DaxMessage msg = msgFactory.toDaxMessage("BULK.TTT",ttObj);

        DaxPreamble preamble = new DaxPreamble();

        System.out.println(DaxMessageDecorator.decorate(messageCodec.encode(msg, preamble)));
//        System.out.println(messageCodec.encode(msg, preamble));
        DaxTag tag1001 = DaxTag.of(config.getAppNamespaceId(), 1001);
        Assertions.assertEquals(DaxDataType.COLLECTION, semanticInspector.getDataType(tag1001));
        Assertions.assertEquals(DaxDataType.STRING, semanticInspector.getCollectionValueDataType(tag1001));
        Assertions.assertFalse(semanticInspector.hasKey(tag1001));
        System.out.println("----------------------------------------");
    }

    @Test
    void checkListCollection() {

        @DaxpEntity(tagId = 1000)
        class TestClass01 {
            @DaxpField(tagId = 1001) final List<String> strList;
            @DaxpField(tagId = 1002)  String str1;


            public TestClass01(){
                this.strList = new ArrayList<>();
                this.strList.add("ABC");
                this.strList.add("DEF");
                this.strList.add("GHI");
                this.strList.add("JKL");
                this.str1 = "Test value";
            }

        }
        daxEngine.register(TestClass01.class);
        printSemanticRegister();
        TestClass01 ttObj = new TestClass01();

        DaxMessage msg = msgFactory.toDaxMessage("BULK.TTT",ttObj);

        DaxPreamble preamble = new DaxPreamble();

        System.out.println(DaxMessageDecorator.decorate(messageCodec.encode(msg, preamble)));
//        System.out.println(messageCodec.encode(msg, preamble));
        DaxTag tag1001 = DaxTag.of(config.getAppNamespaceId(), 1001);
        Assertions.assertEquals(DaxDataType.COLLECTION, semanticInspector.getDataType(tag1001));
        Assertions.assertEquals(DaxDataType.STRING, semanticInspector.getCollectionValueDataType(tag1001));
        Assertions.assertFalse(semanticInspector.hasKey(tag1001));
        System.out.println("----------------------------------------");
    }

    @Test
    void checkMapCollection() {

        @DaxpEntity(tagId = 1000)
        class TestClass01 {
            @DaxpField(tagId = 1001) final Map<Integer,String> strMap;
            @DaxpField(tagId = 1002)  String str1;

            @Min(20) Integer testInt;


            public TestClass01(){
                this.strMap = new HashMap<>();
                this.strMap.put(1,"ABC");
                this.strMap.put(2,"DEF");
                this.strMap.put(3,"GHI");
                this.strMap.put(4,"JKL");
                this.str1 = "Test value";
                this.testInt = 24;
            }

        }
        daxEngine.register(TestClass01.class);
        printSemanticRegister();
        DaxPreamble preamble = new DaxPreamble();

        TestClass01 ttObj = new TestClass01();

        DaxMessage msg = msgFactory.toDaxMessage("BULK.TTT",ttObj);
        System.out.println(DaxMessageDecorator.decorate(messageCodec.encode(msg, preamble)));
//        System.out.println(messageCodec.encode(msg, preamble));

        daxEngine.getCoreConfig().setMinBulkSize(100);
        DaxMessage msg2 = msgFactory.toDaxMessage("BULK.TTT2",ttObj);
        System.out.println(DaxMessageDecorator.decorate(messageCodec.encode(msg2, preamble)));
        daxEngine.getCoreConfig().resetMinBulkSize();

        DaxTag tag1001 = DaxTag.of(config.getAppNamespaceId(), 1001);
        Assertions.assertEquals(DaxDataType.COLLECTION, semanticInspector.getDataType(tag1001));
        Assertions.assertEquals(DaxDataType.STRING, semanticInspector.getCollectionValueDataType(tag1001));
        Assertions.assertTrue(semanticInspector.hasKey(tag1001));
        System.out.println("----------------------------------------");
    }


}

/////////////
/*
Map<Customer, Contract>
Map<Customer, List<Contract>>
Map<Customer, Map<Product, List<Price>>>
*/