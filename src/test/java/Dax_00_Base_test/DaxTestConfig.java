package Dax_00_Base_test;

import Dax_00_Base_test.customer.Customer;
import Dax_00_Base_test.customer.CustomerDaxTag;
import Dax_00_Base_test.customer.CustomerMessages;
import org.daxprotocol.core.config.DaxpConfigFactory;
import org.daxprotocol.core.provider.DaxProvider;
import org.junit.jupiter.api.BeforeAll;

public abstract class DaxTestConfig {

    public static DaxProvider cmrProvider;
    public static DaxProvider cntProvider;

    @BeforeAll
    public static void initAll(){

        cmrProvider = new DaxProvider(DaxpConfigFactory
                                           .createConfig( DaxpConfigFactory
                                                         .createProperties("application_CMR.properties")));

        cntProvider = new DaxProvider(DaxpConfigFactory
                                          .createConfig( DaxpConfigFactory
                                                        .createProperties("application_CNT.properties")));


        cmrProvider.getCoreStrategy()
                   .populateFromAnnotations( Customer.class);

        cmrProvider.getCoreStrategy()
                   .populateFromAnnotations(CustomerDaxTag.class);


        CustomerMessages.initDictionaryBeforeTest(cmrProvider.getDictionary());

    }

}
