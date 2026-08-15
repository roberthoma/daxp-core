package org.daxprotocol.core.unit_test.dax_50_00_collections;

import org.daxprotocol.core.collection.DaxBulkCollectionBuilder;
import org.daxprotocol.core.unit_test.dax_00_01_base_config.DaxConfigBaseTest;
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