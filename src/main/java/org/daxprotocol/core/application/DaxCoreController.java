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


    @DaxpHandler(DaxCoreMessages.DATA_MODEL_REQ)
    public void getDataModel(DaxMessage incomeMsg, DaxFrame outcomeFrame){

        DaxMessage message = messageFactory.dataModelToMsg();
        outcomeFrame.addMessage(message);
        DaxMessage messageLog = messageFactory.logMessage(1,"Test dictionary log");
        outcomeFrame.addMessage(messageLog);

    }


    @DaxpHandler(DaxCoreMessages.ABOUT_REQ)
    public void getAbout(DaxMessage incomeMsg, DaxFrame outcomeFrame){
        DaxMessage message = messageFactory.aboutToMsg();
        outcomeFrame.addMessage(message);
    }

}
