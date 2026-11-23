package org.daxprotocol.core.test.dax_03_integration_test.dax_40_00_data_model;

import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.test.dax_00_service.DaxMessageDecorator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DaxMessageConverterTest extends DaxNamespaceConfigFullDataModelTest {
    @Test
    void msgConverterBaseTest(){
        String reqMsg = "DAXP{V=v0.1.0|EN=UTF-8|NS=XYZ|PS=0x7C}$:1="+ DaxAnySchemaRegister.MSG_BASE_ENTITY_Req +
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

        DaxMessage firstMsg = frameResp.getFirstMessage();

        DaxAnyTestEntity entity = msgConverter.createFromMessage(firstMsg, DaxAnyTestEntity.class);
        DaxMessage messageAfter = msgFactory.toDaxMessage("DATA_EE", entity);
        System.out.println("ENTITY > " + DaxMessageDecorator.decorate(messageCodec.encode(messageAfter, frameReq.getPreamble()))); ;
//        System.out.println(" entity.anyString >>>"+ entity.anyString);
    }
    @Test
    void msgConverterTest() {
        DaxAnyTestEntity original = new DaxAnyTestEntity("Test string", 'H', 456);
        String reqMsg = "DAXP{V=v0.1.0|EN=UTF-8|NS=XYZ|PS=0x7C}$:1="
                + DaxAnySchemaRegister.MSG_BASE_ENTITY_Req + "|$:9=43|";
        DaxFrame frameReq = frameParser.parseFrame(reqMsg);

        DaxFrame frameResp = new DaxFrame();
        frameResp.setPreamble(preambleFactory.createRespPreamble(frameReq));
        frameResp.addMessage(msgFactory.toDaxMessage(
                frameReq.getFirstMessage(), DaxAnySchemaRegister.MSG_BASE_ENTITY_DATA, original));

        DaxMessage response = frameResp.getFirstMessage();
        Assertions.assertEquals(DaxAnySchemaRegister.MSG_BASE_ENTITY_DATA, response.getMsgType());

        DaxAnyTestEntity rebuilt = msgConverter.createFromMessage(response, DaxAnyTestEntity.class);

        assertThat(rebuilt).isNotSameAs(original);
        Assertions.assertEquals(original.getAnyInt_5001(), rebuilt.getAnyInt_5001(), "anyInt_5001");
        Assertions.assertEquals(original.getAnyString_5002(), rebuilt.getAnyString_5002(), "anyString_5002");
        Assertions.assertEquals(original.getAnyChar_5003(), rebuilt.getAnyChar_5003(), "anyChar_5003");
        Assertions.assertEquals(original.getAnyBoolean(), rebuilt.getAnyBoolean(), "anyBoolean");
        Assertions.assertEquals(original.getFixTestValue_FIX_51(), rebuilt.getFixTestValue_FIX_51(), "fixTestValue_FIX_51");
        Assertions.assertEquals(original.getEnumValue_5032(), rebuilt.getEnumValue_5032(), "enumValue_5032");
        Assertions.assertEquals(original.getAnyStr(), rebuilt.getAnyStr(), "anyStr");

        DaxSubEntity expected = original.getSubEntity_5077();
        DaxSubEntity actual = rebuilt.getSubEntity_5077();
        Assertions.assertNotNull(actual, "subEntity_5077");
        Assertions.assertEquals(expected.getValue_1(), actual.getValue_1(), "subEntity_5077.value_1");
        Assertions.assertEquals(expected.getValue2(), actual.getValue2(), "subEntity_5077.value2");
        Assertions.assertEquals(expected.getDd(), actual.getDd(), "subEntity_5077.dd");

        var expectedMap = original.getSubEntityMap_5116();
        var actualMap = rebuilt.getSubEntityMap_5116();
        Assertions.assertNotNull(actualMap, "subEntityMap_5116");
        Assertions.assertEquals(expectedMap.keySet(), actualMap.keySet(), "subEntityMap_5116 keys");
        for (var entry : expectedMap.entrySet()) {
            DaxSubEntity expectedValue = entry.getValue();
            DaxSubEntity actualValue = actualMap.get(entry.getKey());
            String path = "subEntityMap_5116[" + entry.getKey() + "]";
            Assertions.assertNotNull(actualValue, path);
            Assertions.assertEquals(expectedValue.getValue_1(), actualValue.getValue_1(), path + ".value_1");
            Assertions.assertEquals(expectedValue.getValue2(), actualValue.getValue2(), path + ".value2");
            Assertions.assertEquals(expectedValue.getDd(), actualValue.getDd(), path + ".dd");
            Assertions.assertEquals(expectedValue.anyChar_5003, actualValue.anyChar_5003, path + ".anyChar_5003");
        }
    }
    static class MapFields {
        @org.daxprotocol.core.annotation.DaxpField(tagId = 5116)
        java.util.Map<Integer, DaxSubEntity> subEntityMap;
    }

    @Test
    void convertsRawAndEncodedBulkMap() {
        DaxAnyTestEntity original = new DaxAnyTestEntity("Test string", 'H', 456);
        DaxFrame request = frameParser.parseFrame(
                "DAXP{V=v0.1.0|EN=UTF-8|NS=XYZ|PS=0x7C}$:1="
                        + DaxAnySchemaRegister.MSG_BASE_ENTITY_Req + "|$:9=43|");
        DaxFrame response = new DaxFrame();
        response.setPreamble(preambleFactory.createRespPreamble(request));
        response.addMessage(msgFactory.toDaxMessage(request.getFirstMessage(),
                DaxAnySchemaRegister.MSG_BASE_ENTITY_DATA, original));

        DaxMessage rawMessage = response.getFirstMessage();
        DaxMessage encodedMessage = msgFactory.toDaxMessage(request.getFirstMessage(),
                DaxAnySchemaRegister.MSG_BASE_ENTITY_DATA, original);
        for (int blockIdx = 0; blockIdx < encodedMessage.getBody().getBlocksCount(); blockIdx++) {
            var block = encodedMessage.getBody().getBlockMap(blockIdx);
            var bulkTag = org.daxprotocol.core.application.DaxCoreTags.COLLECTION_BULK_DATA;
            var pair = block.get(bulkTag);
            if (pair != null && pair.getValue() instanceof java.util.Map<?, ?> map) {
                block.put(bulkTag, new org.daxprotocol.core.model.pair.DaxPairString(
                        bulkTag, daxEngine.getBulkCollectionBuilder().mapEncode(map)));
            }
        }
        for (DaxMessage message : java.util.List.of(rawMessage, encodedMessage)) {
            MapFields rebuilt = msgConverter.createFromMessage(message, MapFields.class);
            Assertions.assertNotNull(rebuilt.subEntityMap);
            Assertions.assertEquals(original.getSubEntityMap_5116().keySet(), rebuilt.subEntityMap.keySet());
            Assertions.assertNotSame(original.getSubEntityMap_5116(), rebuilt.subEntityMap);
            for (var entry : original.getSubEntityMap_5116().entrySet()) {
                DaxSubEntity actual = rebuilt.subEntityMap.get(entry.getKey());
                Assertions.assertNotNull(actual);
                Assertions.assertEquals(entry.getValue().getValue_1(), actual.getValue_1());
                Assertions.assertEquals(entry.getValue().getValue2(), actual.getValue2());
                Assertions.assertEquals(entry.getValue().getDd(), actual.getDd());
                Assertions.assertEquals(entry.getValue().anyChar_5003, actual.anyChar_5003);
            }
        }
    }

}
