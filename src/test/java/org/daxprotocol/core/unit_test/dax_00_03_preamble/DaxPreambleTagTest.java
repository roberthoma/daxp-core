package org.daxprotocol.core.unit_test.dax_00_03_preamble;

import org.daxprotocol.core.exceptions.DaxPreambleException;
import org.daxprotocol.core.exceptions.DaxTagException;
import org.daxprotocol.core.model.preamble.DaxPreambleTag;
import org.daxprotocol.core.model.tag.DaxTag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DaxPreambleTagTest {

    @Test
    void preamble_tag_test1(){

        Assertions.assertTrue(DaxPreambleTag.contains("DAXP"));
        Assertions.assertTrue(DaxPreambleTag.contains("EN"));
        Assertions.assertTrue(DaxPreambleTag.contains("NS"));
        Assertions.assertTrue(DaxPreambleTag.contains(" ns"));
        Assertions.assertFalse(DaxPreambleTag.contains("zxz123"));

    }

    @Test
    void preamble_test2(){

        Assertions.assertEquals(DaxPreambleTag.DAXP, DaxPreambleTag.fromTag("DAXP"));
        Assertions.assertEquals(DaxPreambleTag.DAXP, DaxPreambleTag.fromTag(" DAXP "));
        Assertions.assertEquals(DaxPreambleTag.MSG_NAMESPACE, DaxPreambleTag.fromTag(" ns "));

    }

//    @Test
//    void preamble_Exception(){
//
//        Assertions.assertEquals(DaxPreambleTag.MSG_NAMESPACE.ge, "");
//        Assertions.assertThrowsExactly(DaxPreambleException.class, DaxPreambleTag.fromTag("XX"));
//
//    }
//
//    @Test
//    void testExceptionMessage() {
//        UserService service = new UserService();
//
//        // Capture the thrown exception instance
//        IllegalArgumentException exception = assertThrows(
//                IllegalArgumentException.class,
//                () -> service.getUserById(-1)
//        );
//
//        // Assert details on the exception object
//        assertEquals("User ID cannot be negative", exception.getMessage());
//    }

}
