package Dax_00_Base_test.customer;


import org.daxprotocol.core.annotation.DaxpController;
import org.daxprotocol.core.annotation.DaxpHandler;
import org.daxprotocol.core.annotation.DaxpMsg;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.provider.DaxProvider;

import static Dax_00_Base_test.customer.CustomerDaxSchema.*;

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
        return provider.getMessageFactory().toDaxMessage(CRM_DATA,customer);
    }

    @DaxpHandler(CRM_DATA_UPD)
    public DaxMessage customerDaxSetter(DaxMessage msg){
        System.out.println("CALLing  customer Setter ");

        Customer customer = new Customer(1,"Robert Upd");
        return provider.getMessageFactory().toDaxMessage(CRM_DATA,customer);
    }


}


//    public  static void initDictionaryBeforeTest(DaxpConfig config, DaxDictionary dictionary){
//        DaxMessageItem mgs ;
//
//        mgs = new DaxMessageItem(CRM_NOT_ACCESS, "Customer Data NOT ACCESS");
//        dictionary.putMsgItem(mgs);
//        //-------
//        mgs = new DaxMessageItem(CRM_DATA_REQ, "Customer Data Request");
//        mgs.addReqTag(new DaxTag(config.getAppContextId() , CustomerDaxTag.CUSTOMER_ID));
//        mgs.addRelatedMsgType(CRM_DATA);
//        mgs.addRelatedMsgType(CRM_NOT_ACCESS);
//
//        dictionary.putMsgItem(mgs);
//        //--------------------
//
//        mgs =new DaxMessageItem(CRM_DATA, "Customer Data");
//        mgs.addReqTag(new DaxTag(config.getAppContextId() , CustomerDaxTag.CUSTOMER_GRP));
//        dictionary.putMsgItem(mgs);
//
//        mgs = new DaxMessageItem(CRM_INSERT, "New Customer");
//        mgs.addReqTag(new DaxTag(config.getAppContextId() , CustomerDaxTag.CUSTOMER_GRP));
//        dictionary.putMsgItem(mgs);
//
//    }


