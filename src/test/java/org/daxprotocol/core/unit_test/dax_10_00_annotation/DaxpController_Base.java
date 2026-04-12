package org.daxprotocol.core.unit_test.dax_10_00_annotation;

import org.daxprotocol.core.annotation.DaxpController;
import org.daxprotocol.core.annotation.DaxpHandler;
import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.unit_test.dax_00_00_base_config.DaxConfigBaseTest;

@DaxpController
public class DaxpController_Base extends DaxConfigBaseTest {


    @DaxpHandler(DaxpSchema_Base.MSG_BASE_DTO_Req)
    public void getBaseData(DaxFrame incomeFrame, DaxFrame outcomeFrame){

        DaxDTO_Base dtoBase = new DaxDTO_Base("Test string",'H',456);

        DaxMessage message = msgFactory.toDaxRespondMessage(incomeFrame,DaxpSchema_Base.MSG_BASE_DTO_DATA,dtoBase);

        outcomeFrame.addMessage(message);
        outcomeFrame.addMessage(msgFactory.okMessageType());

    }

}
