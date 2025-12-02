package Dax_00_Config_test;

import Dax_00_Base_test.DaxTestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DaxConfigParametersTest extends DaxTestConfig {

    @Test
    public void test_reading_properties(){

        Assertions.assertEquals(1,crmConfig.getApplicationContextId());
        Assertions.assertEquals(3,cntConfig.getApplicationContextId());

        Assertions.assertEquals("DEC",crmConfig.getTagFormat());
        Assertions.assertEquals("HEX",cntConfig.getTagFormat());


        Assertions.assertEquals(0x0001,crmConfig.getPairSeparator());
        Assertions.assertEquals('|',cntConfig.getPairSeparator());

    }
}
