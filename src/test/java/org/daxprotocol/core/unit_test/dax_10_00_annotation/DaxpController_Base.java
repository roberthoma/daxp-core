package org.daxprotocol.core.unit_test.dax_10_00_annotation;

import org.daxprotocol.core.annotation.DaxpController;
import org.daxprotocol.core.annotation.DaxpHandler;
import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.unit_test.dax_00_00_base_config.DaxConfigBaseTest;

@DaxpController
public class DaxpController_Base extends DaxConfigBaseTest {

    @DaxpHandler(DaxpSchema_Base.BASE_DTO_Req)
    public DaxFrame getBaseData(DaxFrame incomeFrame){

        DaxDTO_Base dtoBase = new DaxDTO_Base("Test string",'H',456);
        DaxFrame frame = new DaxFrame();

        DaxMessage message = msgFactory.toDaxRespondMessage(incomeFrame,DaxpSchema_Base.BASE_DTO_DATA,dtoBase);

        frame.setPreamble(new DaxPreamble());
        frame.addMessage(message);

        return frame;
    }

}
