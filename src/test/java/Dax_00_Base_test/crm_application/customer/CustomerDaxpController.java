package Dax_00_Base_test.crm_application.customer;


import org.daxprotocol.core.annotation.DaxpController;
import org.daxprotocol.core.annotation.DaxpHandler;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.provider.DaxProvider;

import static Dax_00_Base_test.crm_application.customer.CustomerDaxSchema.*;

@DaxpController
public class CustomerDaxpController {
    DaxProvider provider;
   public CustomerDaxpController(DaxProvider provider){
       this.provider = provider;

   }



    @DaxpHandler(CRM_DATA_REQ)
    public DaxMessage customerDaxGetter(DaxMessage msg){
        System.out.println("CALLing  customer DaxGetter ");


        Customer customer = new Customer(1,"Marzena");
        return provider.getMessageFactory().toDaxMessage(CRM_DTO,customer);
    }

    @DaxpHandler(CRM_DATA_UPD)
    public DaxMessage customerDaxSetter(DaxMessage msg){
        System.out.println("CALLing  customer Setter ");

        Customer customer = new Customer(1,"Robert Upd");
        return provider.getMessageFactory().toDaxMessage(CRM_DTO,customer);
    }


}



