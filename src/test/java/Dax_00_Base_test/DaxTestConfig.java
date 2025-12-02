package Dax_00_Base_test;

import org.daxprotocol.core.provider.DaxProvider;
import org.daxprotocol.core.provider.DaxProviderImpl;
import org.junit.jupiter.api.BeforeAll;

public abstract class DaxTestConfig {

    public static DaxProvider crmProvider;
    public static DaxProvider cntProvider;

    @BeforeAll
    public static void initAll(){

        crmProvider = new DaxProviderImpl("application_CRM.properties");
        cntProvider = new DaxProviderImpl("application_CNT.properties");


}

}
