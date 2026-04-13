package org.daxprotocol.core.application;

import org.daxprotocol.core.annotation.DaxpController;
import org.daxprotocol.core.annotation.DaxpHandler;
import org.daxprotocol.core.factory.DaxMessageFactory;
import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.model.DaxMessage;

@DaxpController
public class DaxCoreController {
    DaxMessageFactory messageFactory;

    public DaxCoreController(DaxMessageFactory messageFactory){
        this.messageFactory = messageFactory;

    }


    @DaxpHandler(DaxCoreMessages.DIC_REQ)
    public void getBaseData(DaxFrame incomeFrame, DaxFrame outcomeFrame){

      //  DaxDTO_Base dtoBase = new DaxDTO_Base("Test string",'H',456);

        DaxMessage message = messageFactory.dictionaryToMsg();

        outcomeFrame.addMessage(message);

    }

}
