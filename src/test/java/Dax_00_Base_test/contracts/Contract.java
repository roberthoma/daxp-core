package Dax_00_Base_test.contracts;

import Dax_00_Base_test.ContextConst;
import Dax_00_Base_test.customer.CustomerDaxSchema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.annotation.DaxpType;

@DaxpType(tagId = ContractDaxSchema.GRP_CONTRACT , name = "Contract")
public class Contract {

    @DaxpField(tagId = ContractDaxSchema.CONTRACT_ID, uiLabel = "Id")
    Long id;

    @DaxpField( context =  ContextConst.CTX_CUSTOMER , tagId = CustomerDaxSchema.CUSTOMER_ID)
    Long customerId;

    @NotNull
    @DaxpField(tagId = ContractDaxSchema.CONTRACT_NO,uiLabel = "Contract No")
    @Size(min = 2, max = 20)
    String  contract_no;

    @NotNull
    @DaxpField(tagId = ContractDaxSchema.CONTRACT_AMOUNT,uiLabel = "Amount")
    Long amount;

    @DaxpField(tagId = ContractDaxSchema.CONTRACT_STATUS,uiLabel = "Status")
    Character status ;


}
