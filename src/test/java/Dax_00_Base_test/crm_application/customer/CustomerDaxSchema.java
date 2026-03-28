package Dax_00_Base_test.crm_application.customer;

import Dax_00_Base_test.address.District;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.daxprotocol.core.annotation.DaxpMsg;
import org.daxprotocol.core.annotation.DaxpSchema;
import org.daxprotocol.core.annotation.DaxpTag;

@DaxpSchema
public class CustomerDaxSchema {

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
    public static final int SHOE_SIZE = 2085;  // in msg to should be CRM:5


//    @DaxpTag(uiLabel = "Address",  dataType = "O")
    @DaxpTag(uiLabel = "Address")
    public static final int ADDRESS_DTO = 2100 ;

    @DaxpTag(uiLabel = "Address" )
    public static final int CUSTOMER_ADDRESS = 2101 ;

    @DaxpTag(uiLabel = "Correspondence address",  dataType = "O")
    public static final int CUSTOMER_ADDRESS_CORRESPONDENCE = 2102 ;

    @DaxpTag(uiLabel = "Address Id")
    public static final int ADDRESS_ID = 2111;

    @DaxpTag( uiLabel = "Street")
    public static final int STREET = 2114;

    @DaxpTag( uiLabel = "Town")
    public static final int TOWN   = 2115;


    @DaxpTag( uiLabel = "District", clazz = District.class)
    public static final int DISTRICT_DTO   = 2120;

    @DaxpTag( uiLabel = "District Code")
    public static final int DISTRICT_CODE   = 2121;

    @DaxpTag( uiLabel = "District name")
    public static final int DISTRICT_NAME   = 2122;



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
