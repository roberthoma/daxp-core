package org.daxprotocol.core.field;

import Dax_00_Base_test.DaxTestConfig;
import Dax_00_Base_test.customer.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DaxAtrDataTypeTest extends DaxTestConfig {

    @Test
    void classToCharString() {

        DaxDataType classType =  DaxDataType.fromClass(String.class);
        Assertions.assertEquals(DaxDataType.STRING.getCode(),classType.getCode());
    }
    @Test
    void classToCharENUM() {

        DaxDataType classType =  DaxDataType.fromClass(Enum.class);
        Assertions.assertEquals(DaxDataType.ENUM.getCode(),classType.getCode());
    }

    @Test
    void classCustomerToCharGroup() {

        DaxDataType classType =  DaxDataType.fromClass(Customer.class);
        Assertions.assertEquals(DaxDataType.DTO.getCode(),classType.getCode());
    }

    @Test
    void classToCharInteger() {

        DaxDataType classType =  DaxDataType.fromClass(Integer.class);
        Assertions.assertEquals(DaxDataType.INTEGER.getCode(), classType.getCode());
    }

    @Test
    void classToCharBoolean() {

        DaxDataType classType =  DaxDataType.fromClass(Boolean.class);
        Assertions.assertEquals(DaxDataType.BOOLEAN.getCode(), classType.getCode());

    }

    @Test
    void classToLongBoolean() {

        DaxDataType classType =  DaxDataType.fromClass(Long.class);
        Assertions.assertEquals(DaxDataType.LONG.getCode(), classType.getCode());

    }
}