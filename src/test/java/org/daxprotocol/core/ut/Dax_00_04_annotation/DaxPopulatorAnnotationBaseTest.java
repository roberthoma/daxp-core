package org.daxprotocol.core.ut.Dax_00_04_annotation;

import org.daxprotocol.core.field.DaxDataType;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.ut.Dax_00_00_base_config.DaxConfigBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

public class DaxPopulatorAnnotationBaseTest extends DaxConfigBaseTest {

    @BeforeAll
     static void  initAnnotation(){
        provider.getDaxPopulator().populateFromAnnotations(Dax_TestBase_Schema.class);

        DaxDataType dataType =  dictionary.getAtrDataType(new DaxTag( 1,Dax_TestBase_Schema.TEST_TAG_String));

        Assertions.assertEquals(DaxDataType.STRING,dataType);

    }

}
