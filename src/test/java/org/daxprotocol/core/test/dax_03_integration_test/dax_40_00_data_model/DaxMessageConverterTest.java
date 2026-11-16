package org.daxprotocol.core.test.dax_03_integration_test.dax_40_00_data_model;

import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.test.dax_00_service.DaxMessageDecorator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DaxMessageConverterTest extends DaxNamespaceConfigFullDataModelTest {
    @Test
    void msgConverterBaseTest(){
        String reqMsg = "DAXP{V=v0.1.0|EN=UTF-8|NS=XYZ|PS=0x7C}$:1="+ DaxAnySchemaRegister.MSG_BASE_ENTITY_Req +
                "|$:9=43|";
        DaxFrame frameReq = frameParser.parseFrame(reqMsg);

        DaxFrame frameResp = new DaxFrame();
        frameResp.setPreamble(preambleFactory.createRespPreamble(frameReq));

        handlerRegistry.executor(frameReq, frameResp);

        DaxMessage respMsg = frameResp.getFirstMessage();
        String respDataType = respMsg.getMsgType();
        Assertions.assertEquals(DaxAnySchemaRegister.MSG_BASE_ENTITY_DATA, respDataType);

        System.out.println("-------------------\n");
        System.out.println("REQ > " + reqMsg);
        System.out.println("RES > " + DaxMessageDecorator.decorate(frameCodec.encode(frameResp)));

        DaxMessage firstMsg = frameResp.getFirstMessage();
        DaxAnyTestEntity entity = msgConverter.createFromMessage(firstMsg, DaxAnyTestEntity.class);
        DaxMessage messageAfter = msgFactory.toDaxMessage("DATA_EE", entity);
        System.out.println("ENTITY > " + DaxMessageDecorator.decorate(messageCodec.encode(messageAfter, frameReq.getPreamble()))); ;
//        System.out.println(" entity.anyString >>>"+ entity.anyString);
    }
    @Test
    void msgConverterTest(){
        String reqMsg = "DAXP{V=v0.1.0|EN=UTF-8|NS=XYZ|PS=0x7C}$:1="+ DaxAnySchemaRegister.MSG_BASE_ENTITY_Req +
                "|$:9=43|";
        DaxFrame frameReq = frameParser.parseFrame(reqMsg);

        DaxFrame frameResp = new DaxFrame();
        frameResp.setPreamble(preambleFactory.createRespPreamble(frameReq));

        handlerRegistry.executor(frameReq, frameResp);

        DaxMessage respMsg = frameResp.getFirstMessage();
        String respDataType = respMsg.getMsgType();
        Assertions.assertEquals(DaxAnySchemaRegister.MSG_BASE_ENTITY_DATA, respDataType);

        System.out.println("-------------------\n");
        System.out.println("REQ > " + reqMsg);
        System.out.println("RES > " + DaxMessageDecorator.decorate(frameCodec.encode(frameResp)));

        DaxMessage firstMsg = frameResp.getFirstMessage();
        DaxAnyTestEntity entity = msgConverter.createFromMessage(firstMsg, DaxAnyTestEntity.class);
        DaxMessage messageAfter = msgFactory.toDaxMessage("DATA_EE", entity);
        System.out.println("ENTITY > " + DaxMessageDecorator.decorate(messageCodec.encode(messageAfter, frameReq.getPreamble()))); ;
//        System.out.println(" entity.anyString >>>"+ entity.anyString);
    }
}
