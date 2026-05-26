package org.daxprotocol.core.unit_test.dax_10_00_annotation;

import org.daxprotocol.core.annotation.DaxpController;
import org.daxprotocol.core.annotation.DaxpHandler;
import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.unit_test.dax_00_01_base_config.DaxConfigBaseTest;

@DaxpController
public class DaxpControllerTest extends DaxConfigBaseTest {


    @DaxpHandler(DaxpManifest_Base.MSG_BASE_DTO_Req)
    public void getBaseData(DaxFrame incomeFrame, DaxFrame outcomeFrame){

        DaxAnyTestEntity testEntity = new DaxAnyTestEntity("Test string",'H',456);

        DaxMessage message = msgFactory.toDaxRespondMessage(incomeFrame, DaxpManifest_Base.MSG_BASE_ENTITY_DATA,testEntity);

        outcomeFrame.addMessage(message);
        outcomeFrame.addMessage(msgFactory.okMessageType());

    }

}
