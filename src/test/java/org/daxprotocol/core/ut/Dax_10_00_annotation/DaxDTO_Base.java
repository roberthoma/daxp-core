package org.daxprotocol.core.ut.Dax_10_00_annotation;


import org.daxprotocol.core.annotation.DaxpDTO;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.annotation.DaxpTag;

import static org.daxprotocol.core.ut.Dax_10_00_annotation.DaxpSchema_Base.*;

@DaxpDTO( DaxpSchema_Base.TEST_TAG_dto)
public class DaxDTO_Base {


    @DaxpField(TEST_TAG_int)
    int anyInt;

    @DaxpField(TEST_TAG_String)
    String anyString;

    @DaxpField(TEST_TAG_char)
    char anyChar;


    public DaxDTO_Base(String testStr, char testChar, int testInt){

        this.anyInt = testInt;
        this.anyString = testStr;
        this.anyChar = testChar;

    }
}
