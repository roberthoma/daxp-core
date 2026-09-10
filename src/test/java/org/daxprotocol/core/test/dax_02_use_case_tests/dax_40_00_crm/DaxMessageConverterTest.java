package org.daxprotocol.core.test.dax_02_use_case_tests.dax_40_00_crm;

import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.test.dax_00_service.DaxMessageDecorator;
import org.daxprotocol.core.test.dax_01_unit_test.dax_00_01_base_config.DaxConfigBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DaxMessageConverterTest extends DaxConfigBaseTest {
    @Test
    void msgConverterTest(){
        String reqMsg = "DAXP|V=v0.1.0|EN=UTF-8|NS=XYZ|$:1="+ DaxAnySchemaRegister.MSG_BASE_ENTITY_Req +
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
        DaxAnyTestEntity entity = msgConverter.createFromMessage(frameResp.getFirstMessage(), DaxAnyTestEntity.class);
        DaxMessage messageAfter = msgFactory.toDaxMessage("DATA_EE", entity);
        System.out.println("ENTITY > " + DaxMessageDecorator.decorate(messageCodec.encode(messageAfter, frameReq.getPreamble()))); ;
//        System.out.println(" entity.anyString >>>"+ entity.anyString);
    }

}
