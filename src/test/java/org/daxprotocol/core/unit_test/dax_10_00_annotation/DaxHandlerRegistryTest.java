package org.daxprotocol.core.unit_test.dax_10_00_annotation;

import org.daxprotocol.core.application.DaxCoreMessages;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.decorator.DaxMessageDecorator;
import org.daxprotocol.core.decorator.DaxMessageNormalizer;
import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.unit_test.dax_00_00_base_config.DaxConfigBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DaxHandlerRegistryTest extends DaxConfigBaseTest {

    @Test
    void executorTestDictionaryReq(){
        String reqMsg = "DAXP=v0.1.0|EN=UTF-8|CX=XYZ|9="+ DaxCoreMessages.DIC_REQ +"|99=123|";
        String msgStr = DaxMessageNormalizer.normalize(reqMsg);
        DaxFrame frameReq = frameParser.parseFrame(msgStr);
        DaxFrame frameResp = new DaxFrame();

        frameResp.setPreamble(preambleFactory.createPreamble());

        handlerRegistry.executor(frameReq, frameResp);

        DaxMessage respMsg = frameResp.getFirstMessage();
        String respDataType = respMsg.getMsgType();
        Assertions.assertEquals(DaxCoreMessages.DATA_DIC, respDataType);

        System.out.println("-------------------\n");
        System.out.println("REQ > " + reqMsg);
        System.out.println("RES > " + DaxMessageDecorator.decorate(frameCodec.encode(frameResp)));

   }



}
