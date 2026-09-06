package org.daxprotocol.core.unit_test.dax_00_02_SemanticRegistry;

import org.daxprotocol.core.exceptions.DaxTagException;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.unit_test.dax_00_01_base_config.DaxConfigBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DaxNamespaceMapperTest extends DaxConfigBaseTest {


    @Test
    void namespaceMapperTest01(){
        int namespaceId = namespaceMapper.getReferenceId("$");
        Assertions.assertEquals(0,namespaceId);


    }

    @Test
    void namespaceMapperTest10(){
        String namespaceStr = "FIX";
        int fixnamespaceId = namespaceMapper.getReferenceId(namespaceStr);
        Assertions.assertEquals(namespaceStr, namespaceMapper.getReference(fixnamespaceId));

    }


    @Test
    void checkAppNamespaceId(){
        System.out.println("CheckAppNamespaceId app Id = "+daxEngine.getConfig().getAppNamespaceId());
        Assertions.assertEquals(1, daxEngine.getConfig().getAppNamespaceId());
    }

    @Test
    void checkAppTagPrefix(){
        String appCtx = "XYZ";
        Assertions.assertEquals(appCtx, config.getAppNamespaceTagPrefix());
        Assertions.assertEquals(config.getAppNamespaceId(),namespaceMapper.getReferenceId(appCtx));
    }

    @Test
    void testTagException(){
        int namespaceId = namespaceMapper.getReferenceId("$");
        Assertions.assertThrowsExactly(DaxTagException.class, () -> DaxTag.of(namespaceId,999));
    }


}
