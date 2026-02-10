package Dax_00_Config_test;

import Dax_00_Base_test.DaxTestConfig;
import org.daxprotocol.core.config.DaxpConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Dax_00_ConfigParametersTest extends DaxTestConfig {

    @Test
    public void test_reading_properties(){

        DaxpConfig crmConfig = crmProvider.getConfig();
        DaxpConfig cntConfig = cntProvider.getConfig();

        Assertions.assertEquals(10,crmConfig.getApplicationContextId());
        Assertions.assertEquals(11,cntConfig.getApplicationContextId());

        Assertions.assertEquals("CRM",crmConfig.getApplicationContext());
        Assertions.assertEquals("CNT",cntConfig.getApplicationContext());


    }
}
