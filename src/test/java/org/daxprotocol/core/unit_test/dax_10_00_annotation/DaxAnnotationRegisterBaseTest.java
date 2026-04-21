package org.daxprotocol.core.unit_test.dax_10_00_annotation;

import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.unit_test.dax_00_01_base_config.DaxConfigBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DaxAnnotationRegisterBaseTest extends DaxConfigBaseTest {

    @BeforeAll
     static void  initAnnotation(){
        daxEngine.register(DaxpSchema_Base.class);
        daxEngine.register(DaxDTO_Base.class);
        daxEngine.register(DaxpControllerTest.class);
        daxEngine.register(DaxEnumSample.class);
        daxEngine.register(DaxSubDTO.class);

        handlerRegistry.registerCtrl(new DaxpControllerTest());  //Autowire in spring

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



}
