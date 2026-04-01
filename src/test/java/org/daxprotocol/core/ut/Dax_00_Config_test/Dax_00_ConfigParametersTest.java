package org.daxprotocol.core.ut.Dax_00_Config_test;

import org.daxprotocol.core.ut.Dax_00_Base_test.DaxTestConfig;
import org.daxprotocol.core.config.DaxConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Dax_00_ConfigParametersTest extends DaxTestConfig {

    @Test
    public void test_reading_properties(){

        DaxConfig crmConfig = crmProvider.getConfig();
        DaxConfig cntConfig = cntProvider.getConfig();


        Assertions.assertEquals(1,crmConfig.getAppContextId());
        Assertions.assertEquals(1,cntConfig.getAppContextId());

        Assertions.assertEquals("CUSTOMER",crmConfig.getAppContextSymbol());
        Assertions.assertEquals("CONTRACTS",cntConfig.getAppContextSymbol());
        Assertions.assertEquals("CRM",crmConfig.getAppContextTagPrefix());
        Assertions.assertEquals("CNT",cntConfig.getAppContextTagPrefix());



    }
}
