package org.daxprotocol.core.unit_test.dax_00_02_dictionary;

import org.daxprotocol.core.exceptions.DaxTagException;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.unit_test.dax_00_01_base_config.DaxConfigBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DaxSchemaRegisterTestBaseTest extends DaxConfigBaseTest {

    @Test
    void testDic_01(){
      int contextId = contextMapper.getReferenceId("$");
      Assertions.assertThrowsExactly(DaxTagException.class, () -> DaxTag.of(contextId,999));
    }


}
