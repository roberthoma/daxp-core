package org.daxprotocol.core.test.dax_02_use_case_tests.dax_050_00_collections;

import org.daxprotocol.core.collection.DaxBulkCollectionBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DaxBulkCollectionTest { //extends DaxConfigBaseTest {

    DaxBulkCollectionBuilder bulkCollectionBuilder = new DaxBulkCollectionBuilder();

    @Test
    void test1(){


        System.out.println(bulkCollectionBuilder.build());
        Assertions.assertEquals(1,1);
    }

}