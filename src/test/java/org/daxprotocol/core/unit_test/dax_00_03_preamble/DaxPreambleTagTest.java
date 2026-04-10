package org.daxprotocol.core.unit_test.dax_00_03_preamble;

import org.daxprotocol.core.model.preamble.DaxPreambleTag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DaxPreambleTagTest {

    @Test
    void preamble_tag_test1(){

        Assertions.assertTrue(DaxPreambleTag.contains("DAXP"));
        Assertions.assertTrue(DaxPreambleTag.contains("EN"));
        Assertions.assertTrue(DaxPreambleTag.contains("CX"));
        Assertions.assertTrue(DaxPreambleTag.contains(" cx"));
        Assertions.assertFalse(DaxPreambleTag.contains("zxz123"));

    }

    @Test
    void preamble_test2(){

        Assertions.assertEquals(DaxPreambleTag.DAXP, DaxPreambleTag.fromTag("DAXP"));
        Assertions.assertEquals(DaxPreambleTag.DAXP, DaxPreambleTag.fromTag(" DAXP "));
        Assertions.assertEquals(DaxPreambleTag.MSG_CONTEXT, DaxPreambleTag.fromTag(" cx "));

    }

}
