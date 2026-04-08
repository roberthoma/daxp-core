package org.daxprotocol.core.ut.Dax_10_00_annotation;

import org.daxprotocol.core.annotation.DaxpController;
import org.daxprotocol.core.annotation.DaxpHandler;
import org.daxprotocol.core.dispatcher.DaxFrame;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.ut.dax_00_00_base_config.DaxConfigBaseTest;

@DaxpController
public class DaxpController_Base extends DaxConfigBaseTest {

    @DaxpHandler(DaxpSchema_Base.BASE_DTO_Req)
    public DaxFrame getBaseData(DaxFrame incomeFrame){
        System.out.println("CALLing getBaseData ");

        DaxDTO_Base dtoBase = new DaxDTO_Base("Test string",'H',456);
        DaxFrame frame = new DaxFrame();
        DaxMessage message = msgFactory.toDaxMessage(DaxpSchema_Base.BASE_DTO_DATA,dtoBase);
        frame.addMessage(message);

        return frame;
    }

}
