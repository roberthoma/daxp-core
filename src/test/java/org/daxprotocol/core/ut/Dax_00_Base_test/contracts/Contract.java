package org.daxprotocol.core.ut.Dax_00_Base_test.contracts;

import org.daxprotocol.core.ut.Dax_00_Base_test.ContextConst;
import org.daxprotocol.core.ut.Dax_00_Base_test.crm_application.customer.CustomerDaxSchema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.annotation.DaxpDTO;

@DaxpDTO(tagId = ContractDaxSchema.GRP_CONTRACT , name = "Contract")
public class Contract {

    @DaxpField(value = ContractDaxSchema.CONTRACT_ID, uiLabel = "Id")
    Long id;

    @DaxpField( context =  ContextConst.CTX_CUSTOMER , value = CustomerDaxSchema.CUSTOMER_ID)
    Long customerId;

    @NotNull
    @DaxpField(value = ContractDaxSchema.CONTRACT_NO,uiLabel = "Contract No")
    @Size(min = 2, max = 20)
    String  contract_no;

    @NotNull
    @DaxpField(value = ContractDaxSchema.CONTRACT_AMOUNT,uiLabel = "Amount")
    Long amount;

    @DaxpField(value = ContractDaxSchema.CONTRACT_STATUS,uiLabel = "Status")
    Character status ;


}
