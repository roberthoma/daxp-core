package org.daxprotocol.core.unit_test.dax_10_00_annotation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.daxprotocol.core.annotation.DaxpMessage;
import org.daxprotocol.core.annotation.DaxpRegistry;
import org.daxprotocol.core.annotation.DaxpTag;
import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.datatype.DaxDataType;

@DaxpRegistry(DaxAnyManifest.MODEL_BASE_UT)
public class DaxAnySchemaRegister {

    @DaxpTag( description = "Base Entity ")
    public static final int TEST_TAG_ENTITY_5000 = 5000;

    @DaxpTag( description = "Recursive Entity ")
    public static final int TEST_RECURSIVE_ENT_5055 = 5055;

    @DaxpTag( description = "Test TAG int", clazz = Integer.class)
    public static final int TEST_TAG_int_5001 = 5001;

    @NotNull
    @Size(min = 2 ,max = 120)
    @DaxpTag( description = "Test TAG String", clazz = String.class)
    public static final int TEST_TAG_String_5002 = 5002;


    @DaxpTag( description = "Test TAG char", clazz = Character.class)
    public static final int TEST_TAG_char_5003 = 5003;

    @DaxpTag( description = "Test TAG Boolean", clazz = Boolean.class)
    public static final int TEST_TAG_Boolean_5004 = 5004;

    @DaxpTag( description = "Test TAG String", clazz = String.class)
    public static final int TEST_TAG_Deprecated_String_5005 = 5005;


    public static final String CTX_FIX= "FIX";

    @DaxpTag( description = "Other CTX Test TAG String", clazz = String.class, namespace = CTX_FIX)
    public static final int TEST_CTX_TAG_String            = 5050;


    @DaxpTag( description = "Other CTX Test TAG String", clazz = String.class)
    public static final String TEST_CTX_TAG_StrVal_FIX51 = CTX_FIX + DaxCoreConstants.NAMESPACE_TAG_SEPARATOR +  "51";

    @DaxpTag( description = "Test enum value", clazz = DaxAnyTestEnum.class)
    public static final int TEST_ENUM_VALUE_5032 = 5032;




    @DaxpTag(description = "Test enum", daxDataType = DaxDataType.COLLECTION, clazz = DaxAnyTestEnum.class)
    public static final  int TEST_COLLECTION_ENUM_6001 = 6001;

    @DaxpTag(description = "Test dic enum", daxDataType = DaxDataType.COLLECTION)
    public static final  int TEST_COLLECTION_DIC_ENUM_6002 = 6002;


    @DaxpTag( description = "SUB  ENTITY 8000 ", daxDataType = DaxDataType.ENTITY)
    public static final int TEST_TAG_SUB_ENTITY_8000 = 8000;


    @DaxpTag( description = "VALUE type SUB ENTITY 8000", daxDataType = DaxDataType.ENTITY)
    public static final int TEST_VALUE_SUB_ENTITY_5077 =  5077;


    @DaxpTag( description = "Tested not used TAG", daxDataType = DaxDataType.TAG)
    public static final int TEST_NOT_USE_TAG_987654321 =  999999999;


    @DaxpMessage(description = "Base Entity DATA")
    public static final String MSG_BASE_ENTITY_DATA =   "BD.DATA"; // 	REs 	Customer Data

    //Messages
    @DaxpMessage(description = "Base ENTITY Request", respMsg = {MSG_BASE_ENTITY_DATA})
    public static final String MSG_BASE_ENTITY_Req =  "BD.REQ"; // 	REs 	Customer Data



    //TODO Create new tested schema for exceptions
//    @DaxpField("ABC:1123")
//    public String testVal;

}
