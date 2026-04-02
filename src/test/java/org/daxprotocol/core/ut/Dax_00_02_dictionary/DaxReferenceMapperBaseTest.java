package org.daxprotocol.core.ut.Dax_00_02_dictionary;

import org.daxprotocol.core.field.DaxDataType;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.ut.Dax_00_00_base_config.DaxConfigBaseTest;
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
