package org.daxprotocol.core.unit_test.dax_10_00_annotation;

import org.daxprotocol.core.decorator.DaxMessageDecorator;
import org.daxprotocol.core.decorator.DaxMessageNormalizer;
import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.field.DaxDataType;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.unit_test.dax_00_00_base_config.DaxConfigBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DaxAnnotationRegisterBaseTest extends DaxConfigBaseTest {

    @BeforeAll
     static void  initAnnotation(){
        daxEngine.register(DaxpSchema_Base.class);
        daxEngine.register(DaxDTO_Base.class);
        daxEngine.register(DaxpController_Base.class);
        handlerRegistry.registerCtrl(new DaxpController_Base());  //Autowire in spring

    }

    @Test
     void  checkIntAnnotation (){
        DaxDataType dataType =  dictionary.getAtrDataType(DaxTag.of( appContextId, DaxpSchema_Base.TEST_TAG_int));
        Assertions.assertEquals(DaxDataType.INTEGER,dataType);
    }

    @Test
    void  checkStringAnnotation (){
        DaxDataType dataType =  dictionary.getAtrDataType(DaxTag.of( appContextId, DaxpSchema_Base.TEST_TAG_String));
        Assertions.assertEquals(DaxDataType.STRING,dataType);
    }
    @Test
    void  checkCharAnnotation (){
        DaxDataType dataType =  dictionary.getAtrDataType(DaxTag.of( appContextId, DaxpSchema_Base.TEST_TAG_char));
        Assertions.assertEquals(DaxDataType.CHAR,dataType);
    }
    @Test
    void executorTestSimpleReq(){
        String reqMsg = "DAXP=v0.1.0|$:1="+DaxpSchema_Base.MSG_BASE_DTO_Req +"|$:9=123|";
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
        String reqMsg = "DAXP=v0.1.0|EN=UTF-8|CX=XYZ|$:1="+DaxpSchema_Base.MSG_BASE_DTO_Req +
                "|$:108=5001;5002|$:9=123|";
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
