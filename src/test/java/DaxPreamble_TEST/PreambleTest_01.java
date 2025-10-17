package DaxPreamble_TEST;

import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.preamble.DaxPreambleBuilder;
import org.daxprotocol.core.model.preamble.DaxPreambleCodec;
import org.daxprotocol.core.types.DaxEncoding;
import org.daxprotocol.core.types.DaxTagFormat;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PreambleTest_01 {
    @Test
    void shouldEncodeAndDecodePreamble() {
        DaxPreamble pre = new DaxPreambleBuilder()
                .version("1")
                .tagFormat(DaxTagFormat.DEC)
                .encoding(DaxEncoding.UTF8)
                .build();

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
