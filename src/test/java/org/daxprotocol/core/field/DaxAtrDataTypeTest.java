package org.daxprotocol.core.field;

import Dax_00_Base_test.DaxTestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DaxAtrDataTypeTest extends DaxTestConfig {

    @Test
    void classToCharString() {

        Character classType =  DaxAtrDataType.classToChar(String.class);
        Assertions.assertEquals('S',classType);
    }
    @Test

    void classToCharENUM() {

        Character classType =  DaxAtrDataType.classToChar(Enum.class);
        Assertions.assertEquals('E',classType);
    }

//    @Test
//    void charToClass() {
//    }
}