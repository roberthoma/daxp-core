package org.daxprotocol.core.ut.Dax_AA_Preamble_TEST;

import org.daxprotocol.core.ut.Dax_00_Base_test.DaxTestConfig;
import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.codec.DaxPreambleCodec;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Dax_AA_Preamble_Test_01 extends DaxTestConfig {
    @Test
    void AA_shouldEncodeAndDecodePreamble() {
        System.out.println("------------------------------------------");
        System.out.println("* Encode And Decode Preamble *");

        DaxPreambleCodec codec = crmProvider.getPreambleCodec();
        DaxpConfig config  = crmProvider.getConfig();

        DaxPreamble pre = new DaxPreamble();
        pre.setEncoding(crmProvider.getConfig().getDefaultEncoding());
        pre.setMsgContextId(config.getAppContextId());

        String preambleStr = codec.encode(pre);

        preambleStr = preambleStr.replace(DaxpConfig.PAIR_SEPARATOR,'|');

        assertEquals("DAXP="+ DaxpConfig.PROTOCOL_VERSION +"|EN=UTF-8|CX=CRM|", preambleStr);

        String wire = codec.encode(pre);
        DaxPreamble copy = codec.decode(wire);
        assertEquals(pre.getProtocolVersion(), copy.getProtocolVersion());
        assertEquals(pre.getEncoding(), copy.getEncoding());

        System.out.println("PREAMBLE: "+wire);

    }

}
