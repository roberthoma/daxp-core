package org.daxprotocol.core.unit_test.dax_00_02_dictionary;

import org.daxprotocol.core.unit_test.dax_00_00_base_config.DaxConfigBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DaxReferenceMapperBaseTest extends DaxConfigBaseTest {


    @Test
    void contextMapperTest01(){
        int contextId = contextMapper.getReferenceId("$");
        Assertions.assertEquals(0,contextId);


    }

    @Test
    void contextMapperTest10(){
        String contextStr = "FIX";
        int fixContextId = contextMapper.getReferenceId(contextStr);
        Assertions.assertEquals(contextStr, contextMapper.getReference(fixContextId));

    }



}
