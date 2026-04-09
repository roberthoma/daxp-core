package org.daxprotocol.core.ut.Dax_10_00_annotation;

import org.daxprotocol.core.dispatcher.DaxFrame;
import org.daxprotocol.core.field.DaxDataType;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.ut.dax_00_00_base_config.DaxConfigBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DaxPopulatorAnnotationBaseTest extends DaxConfigBaseTest {

    @BeforeAll
     static void  initAnnotation(){
        daxEngine.populate(DaxpSchema_Base.class);
        daxEngine.populate(DaxDTO_Base.class);
        daxEngine.populate(DaxpController_Base.class);

        handlerRegistry.registerCtrl(new DaxpController_Base());  //Autowire in spring

    }

    @Test
    void printDictionary(){
        DaxMessage dicMsg = msgFactory.dictionaryToMsg();
        System.out.println(messageCodec.encode(dicMsg));

    }

    @Test
     void  checkIntAnnotation (){
        DaxDataType dataType =  dictionary.getAtrDataType(new DaxTag( 1, DaxpSchema_Base.TEST_TAG_int));
        Assertions.assertEquals(DaxDataType.INTEGER,dataType);
    }

    @Test
    void  checkStringAnnotation (){
        DaxDataType dataType =  dictionary.getAtrDataType(new DaxTag( 1, DaxpSchema_Base.TEST_TAG_String));
        Assertions.assertEquals(DaxDataType.STRING,dataType);
    }
    @Test
    void  checkCharAnnotation (){
        DaxDataType dataType =  dictionary.getAtrDataType(new DaxTag( 1, DaxpSchema_Base.TEST_TAG_char));
        Assertions.assertEquals(DaxDataType.CHAR,dataType);
    }

    @Test
    void executorTest(){

        DaxFrame frameReq = parser.parseFromString("DAXP=v0.1.0|EN=UTF-8|CX=CRM|9="+DaxpSchema_Base.BASE_DTO_Req+"|99=123|");

        DaxFrame frameResp =  handlerRegistry.executor(frameReq);
        String respDataType = frameResp.getFirstMessage().getMsgType();

        Assertions.assertEquals(DaxpSchema_Base.BASE_DTO_DATA, respDataType);
    }

}
