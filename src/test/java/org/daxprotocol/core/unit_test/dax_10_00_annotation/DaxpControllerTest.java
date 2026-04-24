package org.daxprotocol.core.unit_test.dax_10_00_annotation;

import org.daxprotocol.core.annotation.DaxpController;
import org.daxprotocol.core.annotation.DaxpHandler;
import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.unit_test.dax_00_01_base_config.DaxConfigBaseTest;

@DaxpController
public class DaxpControllerTest extends DaxConfigBaseTest {


    @DaxpHandler(DaxpSchema_Base.MSG_BASE_DTO_Req)
    public void getBaseData(DaxFrame incomeFrame, DaxFrame outcomeFrame){

        DaxAnyTestEnity dtoBase = new DaxAnyTestEnity("Test string",'H',456);

        DaxMessage message = msgFactory.toDaxRespondMessage(incomeFrame,DaxpSchema_Base.MSG_BASE_DTO_DATA,dtoBase);

        outcomeFrame.addMessage(message);
        outcomeFrame.addMessage(msgFactory.okMessageType());

    }

}
