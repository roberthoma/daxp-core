package Dax_00_Base_test;

import Dax_00_Base_test.customer.*;
import org.daxprotocol.core.config.DaxpConfigFactory;
import org.daxprotocol.core.provider.DaxProvider;
import org.junit.jupiter.api.BeforeAll;

public abstract class DaxTestConfig {

    public static DaxProvider cmrProvider;
    public static DaxProvider cntProvider;

    public static CustomerDaxpController customerDaxpController;

    @BeforeAll
    public static void initAll(){

        if (cmrProvider == null) {
            cmrProvider = new DaxProvider(DaxpConfigFactory
                    .createConfig(DaxpConfigFactory
                            .createProperties("application_CMR.properties")));

            cntProvider = new DaxProvider(DaxpConfigFactory
                    .createConfig(DaxpConfigFactory
                            .createProperties("application_CNT.properties")));


            cmrProvider.getCoreStrategy()
                    .populateFromAnnotations(Customer.class);

            cmrProvider.getCoreStrategy()
                    .populateFromAnnotations(CustomerDaxSchema.class);

            cmrProvider.getCoreStrategy()
                    .populateFromAnnotations(CustomerRelation.class);

            cmrProvider.getCoreStrategy()
                    .populateFromAnnotations(CustomerDaxpController.class);


            customerDaxpController = new CustomerDaxpController(cmrProvider);



            cmrProvider.getDictionary().registerCtrl(customerDaxpController);
            System.out.println("Po inicjacji");
        }
    }

}
