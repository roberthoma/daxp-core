package org.daxprotocol.core.unit_test.dax_10_00_annotation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.daxprotocol.core.annotation.DaxpMsg;
import org.daxprotocol.core.annotation.DaxpSchema;
import org.daxprotocol.core.annotation.DaxpTag;
import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.datatype.DaxDataType;

@DaxpSchema(name = "TestSchemaB")
public class DaxpSchema_Base {

    @DaxpTag( description = "Base Entity ")
    public static final int TEST_TAG_ENTITY = 5000;


    @DaxpTag( description = "Test TAG int", clazz = Integer.class)
    public static final int TEST_TAG_int            = 5001;

    @NotNull
    @Size(min = 2 ,max = 120)
    @DaxpTag( description = "Test TAG String", clazz = String.class)
    public static final int TEST_TAG_String            = 5002;


    @DaxpTag( description = "Test TAG char", clazz = Character.class)
    public static final int TEST_TAG_char            = 5003;

    @DaxpTag( description = "Test TAG Boolean", clazz = Boolean.class)
    public static final int TEST_TAG_Boolean            = 5004;

    @DaxpTag( description = "Test TAG String", clazz = String.class)
    public static final int TEST_TAG_Deprecated_String  = 5005;


    @DaxpTag( description = "Other CTX Test TAG String", clazz = String.class, context = "FIX")
    public static final int TEST_CTX_TAG_String            = 5050;


    public static final String CTX_FIX= "FIX";

    @DaxpTag( description = "Other CTX Test TAG String", clazz = String.class)
    public static final String TEST_CTX_TAG_StrVal     = CTX_FIX + DaxCoreConstants.CONTEXT_TAG_SEPARATOR +  "51";

    @DaxpTag( description = "Test enum value", clazz = Enum.class)
    public static final int TEST_ENUM_VALUE     = 5032;


    @DaxpMsg(description = "Base DTO Request")
    public static final String MSG_BASE_DTO_Req =  "BDR"; // 	REs 	Customer Data

    @DaxpMsg(description = "Base DTO DATA")
    public static final String MSG_BASE_DTO_DATA =   "BD"; // 	REs 	Customer Data



    @DaxpTag(description = "Test enum", daxDataType = DaxDataType.COLLECTION)
    public static final  int TEST_COLLECTION_ENUM = 6001;

    @DaxpTag( description = "SUB Base DTO ", daxDataType = DaxDataType.ENTITY)
    public static final int TEST_TAG_SUB_ENTITY = 8000;

    @DaxpTag( description = "SUB Base DTO ")
    public static final int TEST_SUB_DTO_1 =  5077;
}
