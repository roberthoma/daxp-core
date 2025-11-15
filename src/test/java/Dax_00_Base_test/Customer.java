package Dax_00_Base_test;

import org.daxprotocol.core.annotation.DaxpFieldGroup;
import org.daxprotocol.core.annotation.DaxpField;

@DaxpFieldGroup(id=GroupsTestList.GRP_CUSTOMER,
                name = "Customer",
                namespace = "crm",
                masterId = GroupsTestList.GRP_CRM)
public class Customer {

    @DaxpField(tag = CustomerDaxDic.CUSTOMER_ID, uiLabel = "Id")
    int customerId;

    @DaxpField(tag = CustomerDaxDic.CUSTOMER_NAME, uiLabel = "Name")
    String name;

    @DaxpField(tag = CustomerDaxDic.CUSTOMER_TOWN, uiLabel = "Town")
    String town;

    @DaxpField(tag = CustomerDaxDic.CUSTOMER_TYPE, uiLabel = "Type")
    CustomerType type;


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
