package org.daxprotocol.core.test.dax_01_unit_test.dax_00_03_preamble;

import org.daxprotocol.core.model.preamble.DaxPreambleTag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.daxprotocol.core.test.dax_00_service.DaxTestLogger.printLog; // Adjust package path if needed

class DaxPreambleTagTest {

    @Test
    void preamble_tag_test1() {
        printLog("Executing preamble_tag_test1 - checking basic tag contains logic");

        Assertions.assertTrue(DaxPreambleTag.contains("DAXP"));
        Assertions.assertTrue(DaxPreambleTag.contains("EN"));
        Assertions.assertTrue(DaxPreambleTag.contains("NS"));
        Assertions.assertTrue(DaxPreambleTag.contains(" ns"));
        Assertions.assertFalse(DaxPreambleTag.contains("zxz123"));

        printLog("Finished preamble_tag_test1 successfully");
    }

    @Test
    void preamble_test2() {
        printLog("Executing preamble_test2 - checking tag conversion logic");

        Assertions.assertEquals(DaxPreambleTag.DAXP, DaxPreambleTag.fromTag("DAXP"));
        Assertions.assertEquals(DaxPreambleTag.DAXP, DaxPreambleTag.fromTag(" DAXP "));
        Assertions.assertEquals(DaxPreambleTag.MSG_NAMESPACE, DaxPreambleTag.fromTag(" ns "));

        printLog("Finished preamble_test2 successfully");
    }

    @Test
    void preamble_tag_test3_all_enum_mappings() {
        printLog("Executing preamble_tag_test3 - verifying all enum mappings and getters");

        Assertions.assertEquals(DaxPreambleTag.DAXP, DaxPreambleTag.fromTag("DAXP"));
        Assertions.assertEquals(DaxPreambleTag.VERSION, DaxPreambleTag.fromTag("V"));
        Assertions.assertEquals(DaxPreambleTag.IMPLEMENTATION, DaxPreambleTag.fromTag("I"));
        Assertions.assertEquals(DaxPreambleTag.ENCODING, DaxPreambleTag.fromTag("EN"));
        Assertions.assertEquals(DaxPreambleTag.MSG_NAMESPACE, DaxPreambleTag.fromTag("NS"));
        Assertions.assertEquals(DaxPreambleTag.MSG_QUANTITY, DaxPreambleTag.fromTag("MQ"));
        Assertions.assertEquals(DaxPreambleTag.MSG_SENDER, DaxPreambleTag.fromTag("SN"));

        Assertions.assertEquals("DAXP", DaxPreambleTag.DAXP.getTag());
        Assertions.assertEquals("V", DaxPreambleTag.VERSION.getTag());
        Assertions.assertEquals("I", DaxPreambleTag.IMPLEMENTATION.getTag());
        Assertions.assertEquals("EN", DaxPreambleTag.ENCODING.getTag());
        Assertions.assertEquals("NS", DaxPreambleTag.MSG_NAMESPACE.getTag());
        Assertions.assertEquals("MQ", DaxPreambleTag.MSG_QUANTITY.getTag());
        Assertions.assertEquals("SN", DaxPreambleTag.MSG_SENDER.getTag());

        printLog("Finished preamble_tag_test3 successfully");
    }

    @Test
    void preamble_test4_null_and_invalid_inputs() {
        printLog("Executing preamble_test4 - checking null, empty, and invalid string handling");

        Assertions.assertFalse(DaxPreambleTag.contains(null));
        Assertions.assertNull(DaxPreambleTag.fromTag(null));

        Assertions.assertFalse(DaxPreambleTag.contains(""));
        Assertions.assertFalse(DaxPreambleTag.contains("   "));
        Assertions.assertNull(DaxPreambleTag.fromTag(""));
        Assertions.assertNull(DaxPreambleTag.fromTag("   "));

        Assertions.assertFalse(DaxPreambleTag.contains("INVALID"));
        Assertions.assertNull(DaxPreambleTag.fromTag("UNKNOWN_TAG"));

        printLog("Finished preamble_test4 successfully");
    }
}