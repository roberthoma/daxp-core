package org.daxprotocol.core.unit_test.dax_00_02_SemanticRegistry;

import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.exceptions.DaxTagException;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.unit_test.dax_00_01_base_config.DaxConfigBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DaxSemanticRegistryTestBaseTest extends DaxConfigBaseTest {

    @Test
    void testDic_01(){
      int namespaceId = namespaceMapper.getReferenceId("$");
      Assertions.assertThrowsExactly(DaxTagException.class, () -> DaxTag.of(namespaceId,999));
    }

    @Test
    void getTagsByDataTypeTEST(){
        List<DaxTag> entityTagList = semanticRegistry.getTagsByDataType(DaxDataType.ENTITY);

        entityTagList.forEach(daxTag -> System.out.println(tagCodec.encode(daxTag)));

        Assertions.assertEquals(3, entityTagList.size());

    }

}
