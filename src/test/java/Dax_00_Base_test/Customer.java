package Dax_00_Base_test;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import org.daxprotocol.core.annotation.DaxpFieldGroup;
import org.daxprotocol.core.annotation.DaxpField;

import java.util.Date;

@DaxpFieldGroup(id=GroupsTestList.GRP_CUSTOMER,
                name = "Customer",
                namespace = "crm",
                masterId = GroupsTestList.GRP_CRM)
public class Customer {

    @DaxpField(tag = CustomerDaxDic.CUSTOMER_ID, uiLabel = "Id")
    int customerId;

    @Size(min = 2 ,max = 120)
    @DaxpField(tag = CustomerDaxDic.CUSTOMER_NAME, uiLabel = "Name")
    String name;

    @Size(min=2)
    @DaxpField(tag = CustomerDaxDic.CUSTOMER_TOWN, uiLabel = "Town")
    String town;

    @DaxpField(tag = CustomerDaxDic.CUSTOMER_TYPE, uiLabel = "Type")
    CustomerType type;


    @DaxpField(tag = CustomerDaxDic.CUSTOMER_YEAR_OF_BIRTH , uiLabel = "Date of birth")
    Date birthDate;

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public CustomerType getType() {
        return type;
    }

    public void setType(CustomerType type) {
        this.type = type;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public CustomerRelation getRelation() {
        return relation;
    }

    public void setRelation(CustomerRelation relation) {
        this.relation = relation;
    }

    @DaxpField(tag = CustomerDaxDic.CUSTOMER_RELATION , uiLabel = "Relation")
    CustomerRelation relation;

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
