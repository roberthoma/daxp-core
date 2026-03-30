package org.daxprotocol.core.ut.Dax_00_Base_test.address;


import org.daxprotocol.core.annotation.DaxpDTO;
import org.daxprotocol.core.annotation.DaxpField;

import static org.daxprotocol.core.ut.Dax_00_Base_test.crm_application.customer.CustomerDaxSchema.*;

@DaxpDTO(tagId = DISTRICT_DTO)
public class District {
    @DaxpField(DISTRICT_CODE)
    String code;
    @DaxpField(DISTRICT_NAME)
    String name;

    public District(String code, String name) {
        this.code = code;
        this.name = name;
    }
}


