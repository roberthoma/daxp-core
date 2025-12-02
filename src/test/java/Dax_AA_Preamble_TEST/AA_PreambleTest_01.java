package Dax_AA_Preamble_TEST;

import org.daxprotocol.core.codec.DaxCodecSymbol;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.preamble.DaxPreambleCodec;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AA_PreambleTest_01 {
    @Test
    void AA_shouldEncodeAndDecodePreamble() {
        DaxPreamble pre = new DaxPreamble();

        DaxPreambleCodec codec = new DaxPreambleCodec();

        String preambleStr = codec.encode(pre);
        preambleStr = preambleStr.replace(DaxCodecSymbol.PAIR_SEPARATOR,'|');

        assertEquals("DAXP=1|TF=DEC|EN=UTF8|", preambleStr);

        String wire = codec.encode(pre);
        DaxPreamble copy = codec.decode(wire);
        assertEquals(pre.getProtocolVersion(), copy.getProtocolVersion());
        assertEquals(pre.getTagFormat(), copy.getTagFormat());
        assertEquals(pre.getEncoding(), copy.getEncoding());
    }

}
