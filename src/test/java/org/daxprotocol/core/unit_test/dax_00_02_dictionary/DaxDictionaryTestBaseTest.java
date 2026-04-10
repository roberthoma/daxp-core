package org.daxprotocol.core.unit_test.dax_00_02_dictionary;

import org.daxprotocol.core.field.DaxDataType;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.unit_test.dax_00_00_base_config.DaxConfigBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DaxDictionaryTestBaseTest extends DaxConfigBaseTest {

    @Test
    void testDic_01(){
      int contextId = contextMapper.getReferenceId("$");
      DaxTag testTag = new DaxTag(contextId,999);
      dictionary.putAtrDataType(testTag, Integer.class);

      DaxDataType dataType =  dictionary.getAtrDataType(testTag);

      Assertions.assertEquals(DaxDataType.INTEGER,dataType);

      Assertions.assertEquals(0,contextId);


    }


}
