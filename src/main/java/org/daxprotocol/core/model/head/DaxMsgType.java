package org.daxprotocol.core.model.head;

import org.daxprotocol.core.codec.DaxPair;
import org.daxprotocol.core.codec.DaxTag;

public class DaxMsgType extends DaxPair<String> {

    public static final String  DIC_REQ     =  "DR"; // 	REQ 	Request for a dictionary
    public static final String  DATA_DIC    =  "DD";  // 	RES 	Message containing a full dictionary of data types and their attributes
    public static final String  ERR_RES     =  "ER";  // 	RES 	Error request
    public static final String  DIC_RELOAD  =  "RL";   //	EVN 	Dictionary or attributes change, dictionary reload recommended

    public DaxMsgType(String value) {
        super(DaxTag.MSG_TYPE, value);
    }
}
