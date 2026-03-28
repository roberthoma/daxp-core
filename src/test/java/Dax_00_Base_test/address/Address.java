package Dax_00_Base_test.address;


import Dax_00_Base_test.customer.CustomerDaxSchema;
import jakarta.validation.constraints.Size;
import org.daxprotocol.core.annotation.DaxpDTO;
import org.daxprotocol.core.annotation.DaxpField;

@DaxpDTO(tagId = CustomerDaxSchema.CUSTOMER_ADDRESS, name = "Address")
public class Address {

    @DaxpField(tagId = CustomerDaxSchema.ADDRESS_ID, uiLabel = "Town")
    int addressId;

    @Size(min=2)
    @DaxpField(tagId = CustomerDaxSchema.STREET, uiLabel = "Street")
    String street;

    @Size(min=2)
    @DaxpField(tagId = CustomerDaxSchema.TOWN, uiLabel = "Town")
    String town;

}
