package org.daxprotocol.core.application;

import org.daxprotocol.core.annotation.DaxpRegister;

//@DaxpSchema(context = DaxCoreConstants.DAXP_CONTEXT_TAG_PREFIX)
@DaxpRegister("$.MSG")
public class DaxCoreMessages {


    private static final String daxPrefix = DaxCoreConstants.DAXP_CONTEXT_TAG_PREFIX+ DaxCoreConstants.CONTEXT_TAG_SEPARATOR;

    public static final String  DIC_REQ     =  "DR"; // 	REQ 	Request for a dictionary
    public static final String  DATA_DIC    =  "DD";  // 	RES 	Dictionary of data types and their attributes
    public static final String  CONTEXT_DIC =  "XD";  // 	RES 	Dictionary of data types and their attributes

    public static final String  OK_RES      =  "OK";  // 	RES 	Error request
    public static final String  ERR_RES     =  "ERR";  // 	RES 	Error respond
    public static final String  DIC_RELOAD  =  "RL";   //	EVN 	Dictionary or attributes change, dictionary reload recommended
    public static final String  DIC_LOG     =  "LOG";   //	EVN 	Dictionary or attributes change, dictionary reload recommended

    // new sys message .. daxp configuration  : set pairSeparator ..

    //ABOUT
    //

}

/*
    //request : introduce yourself
    //TODO Message DEPENDENCY from required tags/fields
    //TODO Message respond
    //TODO Add to preferences


GET	Read (retrieve data)	/customers/123
POST	Create (new resource)	/orders
PUT	Replace entire resource	/customers/123
PATCH	Partially update	/customers/123 (only name)
DELETE	Delete resource	/customers/123
CALL - Remote Procedure Call
   or

CRUD
- Create
- Read
- Update
- Delete


*/

/*
Description ,
* */

/*
$DR
#HB   – Heartbeat
@LOG  – Log or trace message

Use the hybrid format:
SYS.DR, SYS.DD, SYS.ER, SYS.RA for predefined
and
CRM.DR, CNT.DD, ORD.ER for user messages.
*/