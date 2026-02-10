package Dax_AA_Preamble_TEST;

import Dax_00_Base_test.DaxTestConfig;
import org.daxprotocol.core.codec.DaxCodecSymbol;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.preamble.DaxPreambleCodec;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Dax_AA_Preamble_Test_01 extends DaxTestConfig {
    @Test
    void AA_shouldEncodeAndDecodePreamble() {
        DaxPreamble pre = new DaxPreamble();

        DaxPreambleCodec codec = crmProvider.getPreambleCodec();


        String preambleStr = codec.encode(pre);

        preambleStr = preambleStr.replace(DaxCodecSymbol.PAIR_SEPARATOR,'|');

        assertEquals("DAXP|V=1|EN=UTF8|CX=CRM|\n", preambleStr);

        String wire = codec.encode(pre);
        DaxPreamble copy = codec.decode(wire);
        assertEquals(pre.getProtocolVersion(), copy.getProtocolVersion());
        assertEquals(pre.getEncoding(), copy.getEncoding());
    }

}
