package Dax_00_Base_test;

import Dax_00_Base_test.address.Address;
import Dax_00_Base_test.crm_application.customer.Customer;
import Dax_00_Base_test.crm_application.customer.CustomerDaxSchema;
import Dax_00_Base_test.crm_application.customer.CustomerDaxpController;
import Dax_00_Base_test.crm_application.customer.CustomerRelation;
import org.daxprotocol.core.config.DaxpConfigFactory;
import org.daxprotocol.core.provider.DaxProvider;
import org.junit.jupiter.api.BeforeAll;

public abstract class DaxTestConfig {

    public static DaxProvider crmProvider;
    public static DaxProvider cntProvider;

    public static CustomerDaxpController customerDaxpController;

    @BeforeAll
    public static void initAll(){

        if (crmProvider == null) {
            crmProvider = new DaxProvider(DaxpConfigFactory
                    .createConfig(DaxpConfigFactory
                            .createProperties("application_CRM.properties")));

            cntProvider = new DaxProvider(DaxpConfigFactory
                    .createConfig(DaxpConfigFactory
                            .createProperties("application_CNT.properties")));


            crmProvider.getCoreStrategy().populateFromAnnotations(Customer.class);
            crmProvider.getCoreStrategy().populateFromAnnotations(Address.class);
            crmProvider.getCoreStrategy().populateFromAnnotations(CustomerDaxSchema.class);
            crmProvider.getCoreStrategy().populateFromAnnotations(CustomerRelation.class);
            crmProvider.getCoreStrategy().populateFromAnnotations(CustomerDaxpController.class);


            customerDaxpController = new CustomerDaxpController(crmProvider);



            crmProvider.getDictionary().registerCtrl(customerDaxpController);
            System.out.println("Po inicjacji");
        }
    }

}
