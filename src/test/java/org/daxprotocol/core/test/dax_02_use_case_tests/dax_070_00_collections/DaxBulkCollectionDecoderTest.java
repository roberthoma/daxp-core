package org.daxprotocol.core.test.dax_02_use_case_tests.dax_070_00_collections;

import org.daxprotocol.core.annotation.DaxpEntity;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.test.dax_00_service.DaxMessageDecorator;
import org.daxprotocol.core.test.dax_03_integration_test.dax_00_00_base.DaxNamespaceConfigBaseInit;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DaxBulkCollectionDecoderTest extends DaxNamespaceConfigBaseInit {

    @DaxpEntity(tagId = 900)
    public static class VinKey {
        @DaxpField(tagId = 902) public String vinNumber;
        @DaxpField(tagId = 903) public String countryCode;
        @DaxpField("FIX:904") public Integer batchId;

        public VinKey() {}

        public VinKey(String vinNumber, String countryCode, Integer batchId) {
            this.vinNumber = vinNumber;
            this.countryCode = countryCode;
            this.batchId = batchId;
        }
    }

    @DaxpEntity(tagId = 1000)
    public static class CarModel {
        @DaxpField(tagId = 1002) public String brand;
        @DaxpField(tagId = 1003) public String model;
        @DaxpField("FIX:1004") public Integer horsepower;

        public CarModel() {}

        public CarModel(String brand, String model, Integer horsepower) {
            this.brand = brand;
            this.model = model;
            this.horsepower = horsepower;
        }
    }

    @Test
    void testDecodePrimitiveCollectionFromRawPayload() {
        int namespaceId = config.getAppNamespaceId();

        // Manual assembly of the raw frame for the primitive types collection
        String payload = FS + "Volvo" + RS + "Porsche" + RS + "Nissan" + RS + "Ferrari" + EM + FS;

        System.out.println(DaxMessageDecorator.decorate(payload));
        List<String> decodedList = bulkCollectionDecoder.decodeCollection(payload, String.class, namespaceId);

        assertNotNull(decodedList);
        assertEquals(4, decodedList.size());
        assertEquals(Arrays.asList("Volvo", "Porsche", "Nissan", "Ferrari"), decodedList);
    }

    @Test
    void testDecodeMapWithEntityKeyAndEntityValueFromRawPayload() {
        int namespaceId = config.getAppNamespaceId();
        daxEngine.register(VinKey.class);
        daxEngine.register(CarModel.class);

        // Manual assembly of the raw DAXP frame for Map<VinKey, CarModel>
        StringBuilder sb = new StringBuilder();
        sb.append(FS);

        // 1. Map Header: Key Header + Value Header
        sb.append(GS).append("902").append(US).append("903").append(US).append("FIX:904").append(EM).append(GS);
        sb.append(GS).append("1002").append(US).append("1003").append(US).append("FIX:1004").append(EM).append(GS);

        // 2. Data record 1: VIN200481, DE, 101 -> Porsche, Taycan, 616
        sb.append(RS);
        sb.append(GS).append("VIN200481").append(US).append("DE").append(US).append("101").append(EM).append(GS);
        sb.append(GS).append("Porsche").append(US).append("Taycan").append(US).append("616").append(EM).append(GS);

        // 3. Data record 2: VIN100283, US -> Tesla, Model S
        sb.append(RS);
        sb.append(GS).append("VIN100283").append(US).append("US").append(US).append(EM).append(GS);
        sb.append(GS).append("Tesla").append(US).append("Model S").append(US).append(EM).append(GS);

        sb.append(EM);
        sb.append(FS);

        String rawPayload = sb.toString();

        // Optional inspection using the decorator
        System.out.println("--- RAW DECODED PAYLOAD INSPECTION ---");
        System.out.println(DaxMessageDecorator.decorate(rawPayload));

        // Call the decoder directly on the raw string payload
        Map<VinKey, CarModel> decodedMap = bulkCollectionDecoder.decodeMap(rawPayload, VinKey.class, CarModel.class, namespaceId);

        assertNotNull(decodedMap);
        assertEquals(2, decodedMap.size());

        // Verify the correctness of the decoded objects
        boolean foundPorsche = false;
        for (Map.Entry<VinKey, CarModel> entry : decodedMap.entrySet()) {
            VinKey key = entry.getKey();
            CarModel val = entry.getValue();

            if ("VIN200481".equals(key.vinNumber)) {
                assertEquals("DE", key.countryCode);
                assertEquals(101, key.batchId);
                assertEquals("Porsche", val.brand);
                assertEquals("Taycan", val.model);
                assertEquals(616, val.horsepower);
                foundPorsche = true;
                break;
            }
        }
        assertTrue(foundPorsche, "The decoder should correctly reconstruct the Porsche object from the VIN200481 key");
    }

    // =========================================================================================
    // TEST DOMAIN ENTITIES
    // =========================================================================================

}