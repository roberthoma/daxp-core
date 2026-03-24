package Dax_00_Base_test.customer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.daxprotocol.core.annotation.DaxpMsg;
import org.daxprotocol.core.annotation.DaxpSchema;
import org.daxprotocol.core.annotation.DaxpTag;

@DaxpSchema
public class CustomerDaxSchema {


    public String  MSG_TYPE_CDR =  "CDR";

    @DaxpTag( uiLabel = "Customer")
    public static final int CUSTOMER_DTO           = 2000;


    @DaxpTag( uiLabel = "Id customer")
    public static final int CUSTOMER_ID            = 2001;

    @NotNull
    @Size(min = 2 ,max = 120)
    @DaxpTag(uiLabel = "First name")
    public static final int CUSTOMER_NAME          = 2002;

    @NotNull
    @Size(min = 2 ,max = 120)
    @DaxpTag(uiLabel = "Surname")
    public static final int CUSTOMER_SURNAME       = 2003;

    @DaxpTag(uiLabel = "Date of birth")
    public static final int CUSTOMER_YEAR_OF_BIRTH = 2005;

    @NotNull
    @Size(min = 2 ,max = 120)
    @DaxpTag(uiLabel = "Email")
    public static final int CUSTOMER_EMAIL         = 2011;

    @Size(min=2)
    @DaxpTag(uiLabel = "Telephone")
    public static final int CUSTOMER_TELEPHONE     = 2073;

    @DaxpTag( uiLabel = "Town")
    public static final int CUSTOMER_TOWN          = 2074;

    @DaxpTag( uiLabel = "Customer type")
    public static final int CUSTOMER_TYPE          = 2075;

    @DaxpTag( uiLabel = "Customer relation" ) //, context = "FIX")
    public static final int CUSTOMER_RELATION      = 2076;

    @DaxpTag(uiLabel = "Is Citizen")
    public static final int CUSTOMER_IS_CITIZEN    = 2077;

    @DaxpTag(uiLabel = "Best toy", dataType = "S", readOnly = true)
    public static final int BEST_TOY_M = 2080;

//    @DaxpTag(uiLabel = "Shoe size", dataType = "I", readOnly = true)
    @DaxpTag(uiLabel = "Shoe size",  dataType = "I", readOnly = true)
    public static final int SHOE_SIZE = 5;  // in msg to should be CRM:5



    @DaxpTag(uiLabel = "Customer Relation type")
    public static final int CUSTOMER_RELATION_ENUM   = 3000;




    @DaxpMsg(description = "Customer Data Request ",
            respMsg = {"CDD","DAX_ERR"},
            reqTag = {"2001"}
    )
    public static final String  CRM_DATA_REQ     =  "CDR";


    @DaxpMsg(description = "Customer Data Update ",
            respMsg = {"CDD","DAX_ERR"},
            reqTag = {"2001"}
    )
    public static final String  CRM_DATA_UPD     =  "CDU";


    @DaxpMsg(description = "Customer DTO "
    )
    public static final String CRM_DTO =  "CDD"; // 	REs 	Customer Data


    public static final String  CRM_INSERT       =  "CDI"; // 	REs 	New Customer
    public static final String  CRM_UPDATE       =  "CDU"; // 	REs 	Update Customer
    public static final String  CRM_NOT_ACCESS    =  "CNA"; // 	REs 	Update Customer


}
