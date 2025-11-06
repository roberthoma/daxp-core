package Dax_00_Base_test;

import org.daxprotocol.core.annotation.DaxpDic;
import org.daxprotocol.core.annotation.DaxpField;

@DaxpDic(name = "Customer", version = "1.0", namespace = "crm")
public class Customer {

    @DaxpField(tag = CustomerDaxDic.CUSTOMER_ID)
    int customerId;

    @DaxpField(tag = CustomerDaxDic.CUSTOMER_NAME, uiLabel = "Name")
    String name;

    @DaxpField(tag = CustomerDaxDic.CUSTOMER_TOWN, uiLabel = "Town")
    String town;

    public Customer(){

    }

    public Customer(int customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

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
