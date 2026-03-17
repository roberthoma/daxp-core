package Dax_00_Base_test.customer;


import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.dictionary.DaxMessageItem;
import org.daxprotocol.core.model.tag.DaxTag;

public class CustomerMessages {


    public static final String  CRM_DATA_REQ     =  "CDR"; // 	REQ 	Request for Customer Data
    public static final String  CRM_DATA         =  "CDD"; // 	REs 	Customer Data
    public static final String  CRM_INSERT       =  "CDI"; // 	REs 	New Customer
    public static final String  CRM_UPDATE       =  "CDU"; // 	REs 	Update Customer
    public static final String  CRM_NOT_ACCESS    =  "CNA"; // 	REs 	Update Customer




    public  static void initDictionaryBeforeTest(DaxpConfig config, DaxDictionary dictionary){
        DaxMessageItem mgs ;

        mgs = new DaxMessageItem(CRM_NOT_ACCESS, "Customer Data NOT ACCESS");
        dictionary.putMsgItem(mgs);
        //-------
        mgs = new DaxMessageItem(CRM_DATA_REQ, "Customer Data Request");
        mgs.addReqTag(new DaxTag(config.getAppContextId() , CustomerDaxTag.CUSTOMER_ID));
        mgs.addRelatedMsgType(CRM_DATA);
        mgs.addRelatedMsgType(CRM_NOT_ACCESS);

        dictionary.putMsgItem(mgs);
        //--------------------

        mgs =new DaxMessageItem(CRM_DATA, "Customer Data");
        mgs.addReqTag(new DaxTag(config.getAppContextId() , CustomerDaxTag.CUSTOMER_GRP));
        dictionary.putMsgItem(mgs);

        mgs = new DaxMessageItem(CRM_INSERT, "New Customer");
        mgs.addReqTag(new DaxTag(config.getAppContextId() , CustomerDaxTag.CUSTOMER_NAME));
        mgs.addReqTag(new DaxTag(config.getAppContextId() , CustomerDaxTag.CUSTOMER_SURNAME));
        mgs.addReqTag(new DaxTag(config.getAppContextId() , CustomerDaxTag.CUSTOMER_EMAIL));
        mgs.addReqTag(new DaxTag(config.getAppContextId() , CustomerDaxTag.CUSTOMER_IS_CITIZEN));
        dictionary.putMsgItem(mgs);

    }





}


