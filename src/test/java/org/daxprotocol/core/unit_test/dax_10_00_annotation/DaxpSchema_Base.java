package org.daxprotocol.core.unit_test.dax_10_00_annotation;

import org.daxprotocol.core.annotation.DaxpMsg;
import org.daxprotocol.core.annotation.DaxpSchema;
import org.daxprotocol.core.annotation.DaxpTag;

@DaxpSchema
public class DaxpSchema_Base {

    @DaxpTag( uiLabel = "Ui Base DTO ")
    public static final int TEST_TAG_dto            = 5000;


    @DaxpTag( uiLabel = "Ui Test TAG int", clazz = Integer.class)
    public static final int TEST_TAG_int            = 5001;

    @DaxpTag( uiLabel = "Ui Test TAG String", clazz = String.class)
    public static final int TEST_TAG_String            = 5002;


    @DaxpTag( uiLabel = "Ui Test TAG char", clazz = Character.class)
    public static final int TEST_TAG_char            = 5003;

    @DaxpTag( uiLabel = "Ui Test TAG Boolean", clazz = Boolean.class)
    public static final int TEST_TAG_Boolean            = 5004;


    @DaxpMsg(description = "Base DTO Request"
    )
    public static final String MSG_BASE_DTO_Req =  "BDR"; // 	REs 	Customer Data

    @DaxpMsg(description = "Base DTO DATA"
    )
    public static final String MSG_BASE_DTO_DATA =  "BD"; // 	REs 	Customer Data


}
