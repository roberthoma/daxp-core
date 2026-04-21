package org.daxprotocol.core.unit_test.dax_10_00_annotation;

import org.daxprotocol.core.application.DaxCoreMessages;
import org.daxprotocol.core.unit_test.dax_00_00_service.DaxMessageDecorator;
import org.daxprotocol.core.unit_test.dax_00_00_service.DaxMessageNormalizer;
import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.unit_test.dax_00_01_base_config.DaxConfigBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DaxExecutorTest extends DaxConfigBaseTest {

    @Test
    void executorTestDictionaryReq(){
        String reqMsg = "DAXP|V=v0.1.0|EN=UTF-8|CX=XYZ|$:1="+ DaxCoreMessages.DIC_REQ +"|$:9=123|";
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

    @Test
    void executorTestSimpleReq(){
        String reqMsg = "DAXP|V=v0.1.0|$:1="+DaxpSchema_Base.MSG_BASE_DTO_Req +"|$:9=123|";
        String msgStr = DaxMessageNormalizer.normalize(reqMsg);

        DaxFrame frameReq = frameParser.parseFrame(msgStr);
        DaxFrame frameResp = new DaxFrame();
        frameResp.setPreamble(preambleFactory.createPreamble());

        handlerRegistry.executor(frameReq, frameResp);
        DaxMessage respMsg = frameResp.getFirstMessage();
        String respDataType = respMsg.getMsgType();
        Assertions.assertEquals(DaxpSchema_Base.MSG_BASE_DTO_DATA, respDataType);

        System.out.println("-------------------\n");
        System.out.println("REQ > " + reqMsg);
        System.out.println("RES > " + DaxMessageDecorator.decorate(frameCodec.encode(frameResp)));
    }

    @Test
    void executorTestSelectReq(){
        String reqMsg = "DAXP|V=v0.1.0|EN=UTF-8|CX=XYZ|$:1="+DaxpSchema_Base.MSG_BASE_DTO_Req +
                "|$:108=5001;5002|$:9=123|";
        String msgStr = DaxMessageNormalizer.normalize(reqMsg);
        DaxFrame reqFrame = frameParser.parseFrame(msgStr);

        DaxFrame frameResp = new DaxFrame();
        frameResp.setPreamble(preambleFactory.createRespPreamble(reqFrame));

        handlerRegistry.executor(reqFrame, frameResp);

        DaxMessage respMsg = frameResp.getFirstMessage();
        String respDataType = respMsg.getMsgType();
        Assertions.assertEquals(DaxpSchema_Base.MSG_BASE_DTO_DATA, respDataType);

        System.out.println("-------------------\n");
        System.out.println("REQ > " + reqMsg);
        System.out.println("RES > " + DaxMessageDecorator.decorate(frameCodec.encode(frameResp)));

    }
    @Test
    void executorTestSelectReqSubDto(){
        String reqMsg = "DAXP|V=v0.1.0|EN=UTF-8|CX=XYZ|$:1="+DaxpSchema_Base.MSG_BASE_DTO_Req +
                "|$:108=5001;8000|$:9=123|";
        String msgStr = DaxMessageNormalizer.normalize(reqMsg);
        DaxFrame frameReq = frameParser.parseFrame(msgStr);

        DaxFrame frameResp = new DaxFrame();
        frameResp.setPreamble(preambleFactory.createPreamble());

        handlerRegistry.executor(frameReq, frameResp);

        DaxMessage respMsg = frameResp.getFirstMessage();
        String respDataType = respMsg.getMsgType();
        Assertions.assertEquals(DaxpSchema_Base.MSG_BASE_DTO_DATA, respDataType);

        System.out.println("-------------------\n");
        System.out.println("REQ > " + reqMsg);
        System.out.println("RES > " + DaxMessageDecorator.decorate(frameCodec.encode(frameResp)));

    }


}
