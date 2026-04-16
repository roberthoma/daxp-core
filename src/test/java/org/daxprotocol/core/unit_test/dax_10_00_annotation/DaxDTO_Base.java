package org.daxprotocol.core.unit_test.dax_10_00_annotation;


import org.daxprotocol.core.annotation.DaxpDTO;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.application.DaxCoreMessages;
import org.daxprotocol.core.application.DaxCoreTags;

import static org.daxprotocol.core.unit_test.dax_10_00_annotation.DaxpSchema_Base.*;

@DaxpDTO( DaxpSchema_Base.TEST_TAG_dto)
public class DaxDTO_Base {


    @DaxpField(TEST_TAG_int)
    int anyInt;

    @DaxpField(TEST_TAG_String)
    String anyString;

    @DaxpField(TEST_TAG_char)
    char anyChar;

    @DaxpField(TEST_TAG_Boolean)
    Boolean anyBoolean;


    @DaxpField(tagStrId = TEST_CTX_TAG_StrVal)
    char fixTestValue;

    public DaxDTO_Base(String testStr, char testChar, int testInt){

        this.anyInt = testInt;
        this.anyString = testStr;
        this.anyChar = testChar;
        this.anyBoolean = true;

    }
}
