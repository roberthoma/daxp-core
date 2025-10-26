package org.daxprotocol.core.codec;

public class DaxMsgType {

    public static final String  DIC_REQ     =  "DR"; // 	REQ 	Request for a dictionary
    public static final String  DATA_DIC    =  "DD";  // 	RES 	Message containing a full dictionary of data types and their attributes
    public static final String  ERR_RES     =  "ER";  // 	RES 	Error request
    public static final String  DIC_RELOAD  =  "RL";   //	EVN 	Dictionary or attributes change, dictionary reload recommended
}
