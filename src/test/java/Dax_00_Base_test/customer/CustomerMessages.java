package Dax_00_Base_test.customer;


import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.dictionary.DaxMessageDicItem;

public class CustomerMessages {


    public static final String  CRM_DATA_REQ     =  "CRM.DR"; // 	REQ 	Request for Customer Data
    public static final String  CRM_DATA         =  "CRM.DD"; // 	REs 	Customer Data
    public static final String  CRM_INSERT       =  "CRM.DI"; // 	REs 	New Customer
    public static final String  CRM_UPDATE       =  "CRM.DU"; // 	REs 	New Customer



    public  static void initDictionaryBeforeTest(DaxDictionary dictionary){

        dictionary.putMsgItem(new DaxMessageDicItem(CRM_DATA_REQ, "Request for Customer Data"));
        dictionary.putMsgItem(new DaxMessageDicItem(CRM_DATA, "Customer Data"));
        dictionary.putMsgItem(new DaxMessageDicItem(CRM_INSERT, "New Customer"));

    }





}


