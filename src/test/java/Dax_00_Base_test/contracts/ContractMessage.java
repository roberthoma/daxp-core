package Dax_00_Base_test.contracts;


import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.dictionary.DaxMessageItem;

public class ContractMessage {


    public static final String  CRM_DATA_REQ     =  "CRM.DR"; // 	REQ 	Request for Customer Data
    public static final String  CRM_DATA         =  "CRM.DD"; // 	REs 	Customer Data
    public static final String  CRM_INSERT       =  "CRM.DI"; // 	REs 	New Customer
    public static final String  CRM_UPDATE       =  "CRM.DU"; // 	REs 	New Customer

    public static final String  CNT_DATA_REQ     =  "CNT.DR"; // 	REQ 	Request for a contract data
    public static final String  CNT_DATA         =  "CNT.DD"; // 	RES 	Contract data
    public static final String  CNT_INSERT       =  "CNT.DI"; // 	RES 	Contract data


    public void init (DaxDictionary dictionary){

        dictionary.putMsgItem(new DaxMessageItem(CRM_DATA_REQ, "Request for Customer Data"));
        dictionary.putMsgItem(new DaxMessageItem(CRM_DATA, "Customer Data"));
        dictionary.putMsgItem(new DaxMessageItem(CRM_INSERT, "New Customer"));

    }





}


