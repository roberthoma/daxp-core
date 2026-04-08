package org.daxprotocol.core.ut.Dax_10_00_annotation;

import org.daxprotocol.core.field.DaxDataType;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.ut.Dax_00_00_base_config.DaxConfigBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DaxPopulatorAnnotationBaseTest extends DaxConfigBaseTest {

    @BeforeAll
     static void  initAnnotation(){
        daxEngine.populate(Dax_TestBase_Schema.class);

    }

    @Test
     void  checkIntAnnotation (){
        DaxDataType dataType =  dictionary.getAtrDataType(new DaxTag( 1,Dax_TestBase_Schema.TEST_TAG_int));
        Assertions.assertEquals(DaxDataType.INTEGER,dataType);
    }

    @Test
    void  checkStringAnnotation (){
        DaxDataType dataType =  dictionary.getAtrDataType(new DaxTag( 1,Dax_TestBase_Schema.TEST_TAG_String));
        Assertions.assertEquals(DaxDataType.STRING,dataType);
    }
    @Test
    void  checkCharAnnotation (){
        DaxDataType dataType =  dictionary.getAtrDataType(new DaxTag( 1,Dax_TestBase_Schema.TEST_TAG_char));
        Assertions.assertEquals(DaxDataType.CHAR,dataType);
    }

}
