package Dax_00_Base_test.address;


import Dax_00_Base_test.crm_application.customer.CustomerDaxSchema;
import jakarta.validation.constraints.Size;
import org.daxprotocol.core.annotation.DaxpDTO;
import org.daxprotocol.core.annotation.DaxpField;

@DaxpDTO(tagId = CustomerDaxSchema.ADDRESS_DTO, name = "Address")
public class Address {

    @DaxpField(value = CustomerDaxSchema.ADDRESS_ID, uiLabel = "Town")
    public int addressId;

    @Size(min=2)
    @DaxpField(value = CustomerDaxSchema.STREET, uiLabel = "Street")
    public String street;

    @Size(min=2)
    @DaxpField(value = CustomerDaxSchema.TOWN, uiLabel = "Town")
    public String town;

    @DaxpField(CustomerDaxSchema.DISTRICT_DTO)
    public District district;

}
