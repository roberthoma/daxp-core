package org.daxprotocol.core.test.dax_03_integration_test.dax_30_00_AnnDaxpField;

import org.daxprotocol.core.annotation.DaxpEntity;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.application.DaxCoreMessages;
import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.application.DaxEngine;
import org.daxprotocol.core.codec.DaxFrameCodec;
import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.codec.DaxPreambleCodec;
import org.daxprotocol.core.codec.DaxTagCodec;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.config.DaxpConfigFactory;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.datatype.DaxDataTypeCodec;
import org.daxprotocol.core.datatype.DaxDataTypeService;
import org.daxprotocol.core.dispatcher.DaxDispatcher;
import org.daxprotocol.core.factory.DaxMessageFactory;
import org.daxprotocol.core.factory.DaxPreambleFactory;
import org.daxprotocol.core.mapper.DaxNamespaceMapper;
import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.parsers.DaxFrameParser;
import org.daxprotocol.core.parsers.DaxTagParser;
import org.daxprotocol.core.registries.DaxHandlerRegistry;
import org.daxprotocol.core.registries.DaxMessageConverter;
import org.daxprotocol.core.registries.DaxSemanticRegistry;
import org.daxprotocol.core.test.dax_00_service.DaxMessageDecorator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class DaxBaseAnnotationTest extends DaxConfigBaseInit{

    @DaxpEntity(tagId = 1000, description = "Test Entity ")
    static class TestClass01{
        @DaxpField(tagId = 1001)
        public String testString1;
    }


    @Test
    void checkAnnDaxpField(){
        daxEngine.register(TestClass01 .class);

        DaxTag tag =  DaxTag.of(config.getAppNamespaceId() , 1000);
        DaxTag tag2 =  DaxTag.of(config.getAppNamespaceId() , 1001);

        Map<DaxTag, DaxPair<?>> mapAtr=
        semanticRegistry.getTagAttributeMap()
                .get(tag);



       DaxDataType type = semanticRegistry.getDataType(tag);

//       DaxDataType type = semanticRegistry.getTagAttributeMap()
//                                          .get(tag).get(DaxCoreTags.ATR_DATA_TYPE).getDataTypeValue();

        DaxDataType type2 = semanticRegistry.getTagAttributeMap()
                .get(tag2).get(DaxCoreTags.ATR_DATA_TYPE).getDataTypeValue();


        Assertions.assertEquals(DaxDataType.ENTITY, type);
        Assertions.assertEquals(DaxDataType.STRING, type2);

    }


    @Test
    void executorTestDictionaryReq(){
        daxEngine.register(TestClass01 .class);
        String reqMsg = "DAXP|V=v0.1.0|EN=UTF-8|NS=XYZ|$:1="+ DaxCoreMessages.DATA_MODEL_REQ +"|$:9=148|";
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
