package Dax_00_Base_test.customer;

import Dax_00_Base_test.*;
import Dax_00_Base_test.fix.FixConstTag;
import jakarta.validation.constraints.Size;
import org.daxprotocol.core.annotation.DaxpType;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.annotation.DaxpValue;

import java.util.Date;
// TODO change to  @DaxpDto as Daxp Data Transfer Object

@DaxpType(tagId = CustomerDaxSchema.CUSTOMER_GRP,
           name = "Customer"
           )
public class Customer {


    @DaxpField(tagId = CustomerDaxSchema.CUSTOMER_ID) //, uiLabel = "Id customer")
    int customerId;

    @DaxpField(tagId = CustomerDaxSchema.CUSTOMER_NAME) //, uiLabel = "First name")
    String name;

    @DaxpField(tagId = CustomerDaxSchema.CUSTOMER_SURNAME) //, uiLabel = "First name")
    String surname;

    @Size(min=2)
    @DaxpField(tagId = CustomerDaxSchema.CUSTOMER_TOWN, uiLabel = "Town")
    String town;

//    @Size(min=2)
    @DaxpField(tagId = CustomerDaxSchema.CUSTOMER_TELEPHONE) //, uiLabel = "Telephone")
    String telephone;

    @DaxpField(tagId = CustomerDaxSchema.CUSTOMER_TYPE, uiLabel = "Type")
    CustomerType type;

    @DaxpField(tagId = CustomerDaxSchema.CUSTOMER_EMAIL, uiLabel = "Email")
    String email;



    @DaxpField(tagId = CustomerDaxSchema.CUSTOMER_IS_CITIZEN, uiLabel = "Citizen")
    Boolean isCitizen;

    @DaxpField(tagId = CustomerDaxSchema.CUSTOMER_YEAR_OF_BIRTH , uiLabel = "Date of birth")
    Date birthDate;

    @DaxpField( context = ContextConst.CTX_FIX_PROTOCOL,
                  tagId = FixConstTag.FIX_CLIENT_ID,
                uiLabel = "FIX Customer Id ")
    String fixClientId;

    @DaxpField(tagId = CustomerDaxSchema.CUSTOMER_RELATION , uiLabel = "Relation")
    CustomerRelation relation;

    @DaxpValue(tagId = CustomerDaxSchema.BEST_TOY_M, uiLabel = "Best toy B")
    public String getBestToy(){
        return "Big bike";
    }

//    @DaxpValue(tagId = CustomerDaxSchema.SHOE_SIZE, uiLabel = "Shoe Size ")
//    public  String getShoeSize(){
//        return shoeSize;
//    }


    @DaxpValue(tagId = CustomerDaxSchema.SHOE_SIZE, uiLabel = "Shoe Size ")
    public  Integer shoeSize = 42;

    //---------------------------------------------------------------------------

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
