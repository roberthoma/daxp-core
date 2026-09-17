package org.daxprotocol.core.test.dax_03_integration_test.dax_30_00_AnnDaxpField;

import org.daxprotocol.core.annotation.DaxpEntity;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.application.DaxCoreMessages;
import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.test.dax_00_service.DaxMessageDecorator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class DaxBaseAnnotationTest extends DaxConfigBaseInit{

    @DaxpEntity(tagId = 1000, description = "Test Entity ")
    static class TestClass02{
        @DaxpField(tagId = 1001)
        public String testString1;
    }


    @Test
    void checkAnnDaxpField(){
        @DaxpEntity(tagId = 1000, description = "Test Entity ")
         class TestClass01{
            @DaxpField(tagId = 1001)
            public String testString1;
        }

        daxEngine.register(TestClass01 .class);

        DaxTag tag1000 =  DaxTag.of(config.getAppNamespaceId() , 1000);
        DaxTag tag1001 =  DaxTag.of(config.getAppNamespaceId() , 1001);

        Assertions.assertEquals(3,semanticRegistry.getTagAttributeMap().get(tag1000).size());
        Assertions.assertEquals(1,semanticRegistry.getTagAttributeMap().get(tag1001).size());
        Assertions.assertEquals(DaxDataType.ENTITY, semanticRegistry.getDataType(tag1000));
        Assertions.assertEquals(DaxDataType.STRING,  semanticRegistry.getDataType(tag1001));

    }


    @Test
    void executorTestDictionaryReq(){
        daxEngine.register(TestClass02 .class);
//        String reqMsg = "DAXP|V=v0.1.0|EN=UTF-8|NS=ABC|$:1="+ DaxCoreMessages.DATA_MODEL_REQ +"|$:9=148|";
        String reqMsg = "DAXP|$:1="+ DaxCoreMessages.DATA_MODEL_REQ +"|$:9=148|";


        //daxEngine.getMessageFactory()

        DaxFrame frameReq = frameParser.parseFrame(reqMsg);
        DaxFrame frameResp = new DaxFrame();

        frameResp.setPreamble(preambleFactory.createRespPreamble(frameReq));

        handlerRegistry.executor(frameReq, frameResp);

        DaxMessage respMsg = frameResp.getFirstMessage();
        String respDataType = respMsg.getMsgType();
        Assertions.assertEquals(DaxCoreMessages.DATA_MODEL_INST, respDataType);

        System.out.println("-------------------\n");
        System.out.println("REQ > " + reqMsg);
        System.out.println("RES > " + DaxMessageDecorator.decorate(frameCodec.encode(frameResp)));
        System.out.println("AFTER DIC");
    }

}
