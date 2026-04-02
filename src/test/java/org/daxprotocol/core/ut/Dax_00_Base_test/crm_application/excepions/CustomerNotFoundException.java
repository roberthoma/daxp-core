package org.daxprotocol.core.ut.Dax_00_Base_test.crm_application.excepions;

import org.daxprotocol.core.exceptions.DaxAppException;

public class CustomerNotFoundException extends DaxAppException {
    public CustomerNotFoundException(long customerId) {
        super(5001, "Customer ID " + customerId + " not found in Sanok database.");
    }
}