package org.daxprotocol.core.unit_test.dax_00_02_dictionary;

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
    void checkAppnamespaceId(){
        System.out.println("CheckAppnamespaceId app Id = "+daxEngine.getConfig().getAppnamespaceId());
        Assertions.assertEquals(1, daxEngine.getConfig().getAppnamespaceId());
    }

    @Test
    void checkAppTagPrefix(){
        String appCtx = "XYZ";
        Assertions.assertEquals(appCtx, config.getAppnamespaceTagPrefix());
        Assertions.assertEquals(config.getAppnamespaceId(),namespaceMapper.getReferenceId(appCtx));
    }


}
