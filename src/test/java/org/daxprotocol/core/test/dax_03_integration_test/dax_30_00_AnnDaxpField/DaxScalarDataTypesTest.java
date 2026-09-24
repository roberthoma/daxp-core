package org.daxprotocol.core.test.dax_03_integration_test.dax_30_00_AnnDaxpField;

import org.daxprotocol.core.annotation.DaxpEntity;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.model.tag.DaxTag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class DaxScalarDataTypesTest extends DaxConfigBaseInit {

    @Test
    void checkAnnDaxpEntity() {
        @DaxpEntity(tagId = 1000)
        class TestClass01 {}

        daxEngine.register(TestClass01.class);
        DaxTag tag1000 = DaxTag.of(config.getAppNamespaceId(), 1000);

        Assertions.assertEquals(2, semanticRegistry.getTagAttributeMap().get(tag1000).size());
        Assertions.assertEquals(DaxDataType.ENTITY, semanticInspector.getDataType(tag1000));
    }

    @Test
    void checkCoreDataTypes() {
        @DaxpEntity(tagId = 1000, description = "Test Entity ")
        class TestClass01 {
            @DaxpField(tagId = 1010) public String testString1;
            @DaxpField(tagId = 1020, name = "INTEGER_NAME") public Integer testInteger;
            @DaxpField(tagId = 1021, name = "Primitive_INTEGER_NAME") public int testPrimitiveInt;
            @DaxpField(tagId = 1030, name = "LONG_NAME") public Long testLong;
            @DaxpField(tagId = 1031, name = "Primitive_LONG_NAME") public long testPrimLong;
            @DaxpField(tagId = 1040, name = "DECIMAL_NAME") public BigDecimal testDecimal;
            @DaxpField(tagId = 1050, name = "CHAR_NAME") public Character testCharacter;
            @DaxpField(tagId = 1051, name = "Primitive_CHAR_NAME") public char testPrimCharacter;
            @DaxpField(tagId = 1060, name = "DOUBLE_NAME") public Double testDouble;
            @DaxpField(tagId = 1061, name = "Primitive_DOUBLE_NAME") public double testPrimDouble;
            @DaxpField(tagId = 1070, name = "BOOLEAN_NAME") public Boolean testBoolean;
            @DaxpField(tagId = 1071, name = "Primitive_BOOLEAN_NAME") public boolean testPrimBoolean;
        }

        daxEngine.register(TestClass01.class);
        printSemanticRegister();

        Assertions.assertEquals(DaxDataType.STRING, semanticInspector.getDataType(DaxTag.of(config.getAppNamespaceId(), 1010)));
        Assertions.assertEquals(DaxDataType.INTEGER, semanticInspector.getDataType(DaxTag.of(config.getAppNamespaceId(), 1020)));
        Assertions.assertEquals(DaxDataType.INTEGER, semanticInspector.getDataType(DaxTag.of(config.getAppNamespaceId(), 1021)));
        Assertions.assertEquals(DaxDataType.LONG, semanticInspector.getDataType(DaxTag.of(config.getAppNamespaceId(), 1030)));
        Assertions.assertEquals(DaxDataType.LONG, semanticInspector.getDataType(DaxTag.of(config.getAppNamespaceId(), 1031)));
        Assertions.assertEquals(DaxDataType.DECIMAL, semanticInspector.getDataType(DaxTag.of(config.getAppNamespaceId(), 1040)));
        Assertions.assertEquals(DaxDataType.CHARACTER, semanticInspector.getDataType(DaxTag.of(config.getAppNamespaceId(), 1050)));
        Assertions.assertEquals(DaxDataType.CHARACTER, semanticInspector.getDataType(DaxTag.of(config.getAppNamespaceId(), 1051)));
        Assertions.assertEquals(DaxDataType.DOUBLE, semanticInspector.getDataType(DaxTag.of(config.getAppNamespaceId(), 1060)));
        Assertions.assertEquals(DaxDataType.DOUBLE, semanticInspector.getDataType(DaxTag.of(config.getAppNamespaceId(), 1061)));
        Assertions.assertEquals(DaxDataType.BOOLEAN, semanticInspector.getDataType(DaxTag.of(config.getAppNamespaceId(), 1070)));
        Assertions.assertEquals(DaxDataType.BOOLEAN, semanticInspector.getDataType(DaxTag.of(config.getAppNamespaceId(), 1071)));
    }
}