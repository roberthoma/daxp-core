package org.daxprotocol.core.test.dax_02_use_case_tests.dax_070_00_collections;

import org.daxprotocol.core.annotation.DaxpEntity;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.parsers.DaxBulkCollectionParser;
import org.daxprotocol.core.test.dax_00_service.DaxMessageDecorator;
import org.daxprotocol.core.test.dax_03_integration_test.dax_00_00_base.DaxNamespaceConfigBaseInit;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class DaxBulkCollectionEntityBuilderTest extends DaxNamespaceConfigBaseInit {
    @DaxpEntity(tagId = 1000)
    static class TestClass01 {
        @DaxpField(tagId = 1001) final Set<String> strSet;

        public Set<String> getSetOfString(){
            return strSet;
        }

        public TestClass01(){
            this.strSet = new HashSet<>();
            this.strSet.add("ABC");
            this.strSet.add("DEF");
            this.strSet.add("GHI");
            this.strSet.add("JKL");
        }

    }

    @Test
    void baseListBulk(){

        TestClass01 ttObj = new TestClass01();
        daxEngine.register(TestClass01.class);


        String setStrBulk =  bulkCollectionBuilder.collectionToBulk(ttObj.getSetOfString());


        System.out.println("---------");
        System.out.println(setStrBulk);
        System.out.println("---------");
        System.out.println(DaxMessageDecorator.decorate(setStrBulk));
        System.out.println("---------");


        Set<String> strSet2 = DaxBulkCollectionParser.parseBulkToSet(setStrBulk.getBytes());

        strSet2.forEach(s -> System.out.println(s));


    }

}
