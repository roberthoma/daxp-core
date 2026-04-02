package org.daxprotocol.core.ut.dax_00_05_base_splitter;

import org.daxprotocol.core.exceptions.DaxException;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.ut.Dax_00_00_base_config.DaxConfigBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DaxParser_TESTBaseTest extends DaxConfigBaseTest {


    @Test
    void parsePreamble(){

        String msgStr = "  DAXP=v0.1.0 |  EN =UTF-8|CX=CRM|MC=2|"+
                "9=CDD|6=4" +
                "|7=1|5=INST|$:100=2000|2080=Big bike|2001=123|\n2002=Robert| "+
                 "2101=2|FIX:2085=345|2102=3|2075=INDIVIDUAL" +
                "|7=2|5=INST|100=2101|  2114 =Polna 7|\n ADR:2115=Warszawa|2111=345" +
                "|7=3|5=INST|$:100=2102|2114=Lipińskiego 1000|2115=Sanok|2120=4|2111=3346" +
                "|7=4|5=INST|100=2120|2121=43-444|2122=Zakopane|99=177|"+
                "|9=CDD|6=4" +
                "|7=1|5=INST|100=2000|FIX:2080=Small boll|2001=123|FIX:2002=Kasia|2101=2|FIX:2085=23|2102=3|2075=INDIVIDUAL" +
                "|7=2|5=INST|100=2101|2114=Polna 8|ADR:2115=Kraków|2111=333" +
                "|7=3|5=INST|100=2102|2114=Sowia 1000|2115=Lesko\n testline|2120=4|2111=3346" +
                "|7=4|5=INST|100=2120|2121=44-444|2122=Rzeszów|99=177|      ";



        DaxPreamble preamble ;
        List<DaxMessage> messageList;

        try {
            preamble    = parser.parsePreamble(msgStr);
            System.out.println(preambleCodec.encode(preamble));
           // messageList = parser.parseMessageList(msgStr);
        } catch (DaxException e) {
            System.out.println(e.getErrorCode());
            Assertions.fail();
        }

      }

    @Test
    void parseMessage_01(){

        String msgStr = "  DAXP=v0.1.0 |  EN =UTF-8|CX=CRM|"+
                "9=CDD|6=4" +
                "|7=1|5=INST|$:100=2000|2080=Big bike|2001=123|\n2002=Robert| "+
                "2101=2|FIX:2085=345|2102=3|2075=INDIVIDUAL" +
                "|7=2|5=INST|100=2101|  2114 =Polna 7|\n ADR:2115=Warszawa|2111=345" +
                "|7=3|5=INST|$:100=2102|2114=Lipińskiego 1000|2115=Sanok|2120=4|2111=3346" +
                "|7=4|5=INST|100=2120|2121=43-444|2122=Zakopane|99=177|"+
                "|9=CDD|6=4" +
                "|7=1|5=INST|100=2000|FIX:2080=Small boll|2001=123|FIX:2002=Kasia|2101=2|FIX:2085=23|2102=3|2075=INDIVIDUAL" +
                "|7=2|5=INST|100=2101|2114=Polna 8|ADR:2115=Kraków|2111=333" +
                "|7=3|5=INST|100=2102|2114=Sowia 1000|2115=Lesko\n testline|2120=4|2111=3346" +
                "|7=4|5=INST|100=2120|2121=44-444|2122=Rzeszów|99=177|      ";



        DaxPreamble preamble ;
        List<DaxMessage> messageList;

        try {
            preamble    = parser.parsePreamble(msgStr);
            System.out.println(preambleCodec.encode(preamble));
            // messageList = parser.parseMessageList(msgStr);
        } catch (DaxException e) {
            System.out.println(e.getErrorCode());
            Assertions.fail();
        }

    }



}
