package org.daxprotocol.core.field;

import Dax_00_Base_test.DaxTestConfig;
import Dax_00_Base_test.customer.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DaxAtrDataTypeTest extends DaxTestConfig {

    @Test
    void classToCharString() {

        DaxDataType classType =  DaxDataType.fromClass(String.class);
        Assertions.assertEquals('S',classType.getCode());
    }
    @Test
    void classToCharENUM() {

        DaxDataType classType =  DaxDataType.fromClass(Enum.class);
        Assertions.assertEquals('E',classType.getCode());
    }

    @Test
    void classCustomerToCharGroup() {

        DaxDataType classType =  DaxDataType.fromClass(Customer.class);
        Assertions.assertEquals('G',classType.getCode());
    }
}