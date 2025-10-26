package Dax_00_Base_test;

import org.daxprotocol.core.annotations.DaxpTag;

public class Customer {

    @DaxpTag(tag = CustomerDaxDic.CUSTOMER_ID)
    int customerId;

    @DaxpTag(tag = CustomerDaxDic.CUSTOMER_NAME)
    String name;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
