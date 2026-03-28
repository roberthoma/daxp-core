package Dax_00_Base_test.crm_application.customer;

import org.daxprotocol.core.annotation.DaxpEnum;


@DaxpEnum(tagId = CustomerDaxSchema.CUSTOMER_RELATION_ENUM, description = "Customer relationship")
public enum CustomerRelation {

    WORKER,
    CLIENT,
    CONSULTANT

}
