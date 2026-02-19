package Dax_00_Config_test;

import Dax_00_Base_test.DaxTestConfig;
import org.daxprotocol.core.config.DaxpConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Dax_00_ConfigParametersTest extends DaxTestConfig {

    @Test
    public void test_reading_properties(){

        DaxpConfig crmConfig = cmrProvider.getConfig();
        DaxpConfig cntConfig = cntProvider.getConfig();


        Assertions.assertEquals(1,crmConfig.getAppContextId());
        Assertions.assertEquals(1,cntConfig.getAppContextId());

        Assertions.assertEquals("CUSTOMER",crmConfig.getAppContextSymbol());
        Assertions.assertEquals("CONTRACTS",cntConfig.getAppContextSymbol());
        Assertions.assertEquals("CMR",crmConfig.getAppContextTagPrefix());
        Assertions.assertEquals("CNT",cntConfig.getAppContextTagPrefix());


    }
}
