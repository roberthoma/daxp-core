package Dax_AA_Preamble_TEST;

import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.preamble.DaxPreambleCodec;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AA_PreambleTest_01 {
    @Test
    void AA_shouldEncodeAndDecodePreamble() {
        DaxPreamble pre = new DaxPreamble();

        DaxPreambleCodec codec = new DaxPreambleCodec();

        String wireStr = codec.encode(pre);
        wireStr = wireStr.replace((char)0x0001,'|');

        assertEquals("DAXP=1|TF=DEC|EN=UTF8|\n", wireStr);

        String wire = codec.encode(pre);
        DaxPreamble copy = codec.decode(wire);
        assertEquals(pre.getProtocolVersion(), copy.getProtocolVersion());
        assertEquals(pre.getTagFormat(), copy.getTagFormat());
        assertEquals(pre.getEncoding(), copy.getEncoding());
    }

}
