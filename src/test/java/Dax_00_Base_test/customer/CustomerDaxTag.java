package Dax_00_Base_test.customer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.daxprotocol.core.annotation.DaxpDictionary;
import org.daxprotocol.core.annotation.DaxpTag;

@DaxpDictionary
public class CustomerDaxTag {

    @DaxpTag( uiLabel = "Id customer")
    public static final int CUSTOMER_ID            = 2001;

    @NotNull
    @Size(min = 2 ,max = 120)
    @DaxpTag(uiLabel = "First name")
    public static final int CUSTOMER_NAME          = 2002;

    public static final int CUSTOMER_SURNAME       = 2003;

    @DaxpTag(uiLabel = "Date of birth")
    public static final int CUSTOMER_YEAR_OF_BIRTH = 2005;

    public static final int CUSTOMER_EMAIL         = 2011;

    @Size(min=2)
    @DaxpTag(uiLabel = "Telephone")
    public static final int CUSTOMER_TELEPHONE     = 2073;

    @DaxpTag( uiLabel = "Town")
    public static final int CUSTOMER_TOWN          = 2074;

    @DaxpTag( uiLabel = "Customer type")
    public static final int CUSTOMER_TYPE          = 2075;

    public static final int CUSTOMER_RELATION      = 2076;

    @DaxpTag(uiLabel = "Is Citizen")
    public static final int CUSTOMER_IS_CITIZEN    = 2077;

    @DaxpTag(uiLabel = "Best toy", dataType = "S", readOnly = true)
    public static final int BEST_TOY_M = 2080;

}
