package org.daxprotocol.core.unit_test.dax_10_00_annotation;


import org.daxprotocol.core.annotation.DaxpDTO;
import org.daxprotocol.core.annotation.DaxpEnum;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.application.DaxCoreMessages;
import org.daxprotocol.core.application.DaxCoreTags;

import static org.daxprotocol.core.unit_test.dax_10_00_annotation.DaxpSchema_Base.*;

@DaxpDTO(tagId = DaxpSchema_Base.TEST_TAG_dto)
public class DaxDTO_Base {


    @DaxpField(tagId = TEST_TAG_int)
    int anyInt;

    @DaxpField(tagId = TEST_TAG_String)
    String anyString;

    @DaxpField(tagId = TEST_TAG_char)
    char anyChar;

    @DaxpField(tagId = TEST_TAG_Boolean)
    Boolean anyBoolean;


    @DaxpField(TEST_CTX_TAG_StrVal)
    char fixTestValue;

    @DaxpField(tagId = TEST_ENUM_VALUE)
    DaxEnumSample enumValue;

    @DaxpField("5077")
    DaxSubDTO  subDTO;



    public DaxDTO_Base(String testStr, char testChar, int testInt){

        this.anyInt = testInt;
        this.anyString = testStr;
        this.anyChar = testChar;
        this.anyBoolean = true;
        this.fixTestValue = 'X';
        this.enumValue = DaxEnumSample.ENUM_VALUE1;
        this.subDTO = new DaxSubDTO(14,"testSubDTO",12.34);


    }
}
