package Dax_BB_Head_TEST;

import org.daxprotocol.core.model.head.DaxHead;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HeadTest_01 {

    @Test
    void baseHadConstructor() {
        DaxHead head = new DaxHead("AA");
        head.setBlockCount(10);
        Assertions.assertEquals("AA",head.getMsgType());
        Assertions.assertEquals(10,head.getBlockCount());
    }

    @Test
    void shouldEncodeAndDecodeHead() {
        DaxHead head = new DaxHead("AA");
        Assertions.assertEquals("AA",head.getMsgType());
    }

}
