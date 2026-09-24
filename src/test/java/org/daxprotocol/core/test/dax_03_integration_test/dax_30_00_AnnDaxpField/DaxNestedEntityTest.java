package org.daxprotocol.core.test.dax_03_integration_test.dax_30_00_AnnDaxpField;

import org.daxprotocol.core.annotation.DaxpEntity;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.model.tag.DaxTag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DaxNestedEntityTest extends DaxConfigBaseInit {

    @Test
    void checkEntityType() {
        @DaxpEntity(tagId = 1000)
        class InEntityClass {
            @DaxpField(tagId = 1001) String strTest;
            @DaxpField(tagId = 1002) Integer intTest;
        }

        @DaxpEntity(tagId = 1010)
        class MainEntityClass {
            @DaxpField(tagId = 1011) InEntityClass inEntity;
            @DaxpField(tagId = 1022) Integer intTest22;
        }

        daxEngine.register(InEntityClass.class);
        daxEngine.register(MainEntityClass.class);
        printSemanticRegister();

        DaxTag tag1011 = DaxTag.of(config.getAppNamespaceId(), 1011);
//        Assertions.assertEquals(1000, semanticInspector.getRefTagId(tag1011).getTagId());
//        Assertions.assertEquals(DaxDataType.ENTITY, semanticInspector.getRefDataType(tag1011));
    }
}

/*
$:4=3|$:5=T|$:6=1010|$:20=ENT|$:11=MainEntityClass|
$:4=4|$:5=T|$:6=1011|$:21=1000|$:22=TYPE|
$:4=5|$:5=T|$:6=1000|$:20=ENT|$:11=InEntityClass|
$:4=6|$:5=T|$:6=1001|$:20=STR|
$:4=7|$:5=T|$:6=1002|$:20=INT|
$:4=8|$:5=T|$:6=1022|$:20=INT|
$:4=9|$:5=F|$:6=1011|$:7=1010|$:11=inEntity|
$:4=10|$:5=F|$:6=1022|$:7=1010|$:11=intTest22|
$:4=11|$:5=F|$:6=1001|$:7=1000|$:11=strTest|
$:4=12|$:5=F|$:6=1002|$:7=1000|$:11=intTest|
$:4=13|$:5=E|$:6=1010|$:115=1011;1022|
$:4=14|$:5=E|$:6=1000|$:115=1001;1002|
*
*
* */