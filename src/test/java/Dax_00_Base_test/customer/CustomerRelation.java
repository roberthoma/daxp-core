package Dax_00_Base_test.customer;

import org.daxprotocol.core.annotation.DaxpDTO;


@DaxpDTO(tagId = CustomerDaxSchema.CUSTOMER_RELATION_ENUM, description = "Customer relationship")
public enum CustomerRelation {

    WORKER,
    CLIENT,
    CONSULTANT

}
