package Dax_00_Base_test.customer;

import org.daxprotocol.core.annotation.DaxpType;


@DaxpType(tagId = CustomerDaxTag.CUSTOMER_RELATION_ENUM, description = "Customer relationship")
public enum CustomerRelation {

    WORKER,
    CLIENT,
    CONSULTANT

}
