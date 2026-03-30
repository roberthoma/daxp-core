package org.daxprotocol.core.ut.Dax_00_Base_test.crm_application.customer;


import org.daxprotocol.core.ut.Dax_00_Base_test.ContextConst;
import org.daxprotocol.core.ut.Dax_00_Base_test.address.Address;
import org.daxprotocol.core.ut.Dax_00_Base_test.fix.FixConstTag;
import org.daxprotocol.core.annotation.DaxpDTO;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.annotation.DaxpValue;

import java.util.Date;
import static org.daxprotocol.core.ut.Dax_00_Base_test.crm_application.customer.CustomerDaxSchema.*;
// TODO change to  @DaxpDto as Daxp Data Transfer Object

@DaxpDTO(tagId = CustomerDaxSchema.CUSTOMER_DTO,
           name = "Customer"
           )
public class Customer {


    @DaxpField(CUSTOMER_ID) //, uiLabel = "Id customer")
    int customerId;

    @DaxpField(CUSTOMER_NAME) //, uiLabel = "First name")
    String name;

    @DaxpField(CUSTOMER_SURNAME) //, uiLabel = "First name")
    String surname;

    @DaxpField(CUSTOMER_ADDRESS)
    public Address address;

    @DaxpField(CUSTOMER_ADDRESS_CORRESPONDENCE)
    public Address corresp_address;

    //    @Size(min=2)
    @DaxpField(CUSTOMER_TELEPHONE) //, uiLabel = "Telephone")
    String telephone;

    @DaxpField(CUSTOMER_TYPE)
    CustomerType type;

    @DaxpField(CUSTOMER_EMAIL)
    String email;



    @DaxpField(CUSTOMER_IS_CITIZEN)
    Boolean isCitizen;

    @DaxpField(CUSTOMER_YEAR_OF_BIRTH)
    Date birthDate;

    @DaxpField( context = ContextConst.CTX_FIX_PROTOCOL,
                  value = FixConstTag.FIX_CLIENT_ID,
                uiLabel = "FIX Customer Id ")
    String fixClientId;

    @DaxpField(value = CustomerDaxSchema.CUSTOMER_RELATION , uiLabel = "Relation")
    CustomerRelation relation;

    @DaxpValue(value = CustomerDaxSchema.BEST_TOY_M, uiLabel = "Best toy B")
    public String getBestToy(){
        return "Big bike";
    }

    @DaxpValue(value = CustomerDaxSchema.SHOE_SIZE, uiLabel = "Shoe Size ")
    public  Integer shoeSize;

    //---------------------------------------------------------------------------

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


    public Customer(){
        shoeSize = 23;
    }

    public Customer(int customerId, String name) {
        this();
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

    public Boolean getCitizen() {
        return isCitizen;
    }

    public void setCitizen(Boolean citizen) {
        isCitizen = citizen;
    }

}
