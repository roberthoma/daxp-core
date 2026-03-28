package Dax_01_10_AtrDataType_test;

import Dax_00_Base_test.DaxTestConfig;
import Dax_00_Base_test.crm_application.customer.Customer;
import org.daxprotocol.core.field.DaxDataType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Dax_01_10_AtrDataTypeTest extends DaxTestConfig {


    @Test
    void classNullToUnknow() {

        DaxDataType classType =  DaxDataType.fromClass(null);
        Assertions.assertEquals(DaxDataType.UNKNOWN.getCode(),classType.getCode());
    }

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