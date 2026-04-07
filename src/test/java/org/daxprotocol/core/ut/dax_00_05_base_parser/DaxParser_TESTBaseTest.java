package org.daxprotocol.core.ut.dax_00_05_base_parser;

import org.daxprotocol.core.dispatcher.DaxFrame;
import org.daxprotocol.core.encoding.DaxCharacterEncoding;
import org.daxprotocol.core.exceptions.DaxException;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.ut.Dax_00_00_base_config.DaxConfigBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DaxParser_TESTBaseTest extends DaxConfigBaseTest {

    @Test
    void parsePreambleTest01(){
        String msgStr = "DAXP=v1.2.3|EN=UTF-16|CX=FIX|MC=1|9=$:DR|2001=123|99=177|";
        DaxPreamble preamble ;
        try {
            preamble    = parser.parsePreamble(msgStr);
            Assertions.assertEquals("V1.2.3",preamble.getProtocolVersion());
            Assertions.assertEquals(DaxCharacterEncoding.UTF_16, preamble.getEncoding());
            Assertions.assertEquals(contextMapper.getReferenceId("FIX"), preamble.getMsgContextId());
        } catch (DaxException e) {
            System.out.println(e.getDaxErrorCode());
            Assertions.fail();
        }
    }

    @Test
    void parsePreambleTest05(){
        String msgStr = "  DAXP=v0.1.0 |  EN =UTF-8|CX=CRM|MC=1|9=$:DR|2001=123|99=177|";
        DaxPreamble preamble ;
        try {
            preamble    = parser.parsePreamble(msgStr);
            Assertions.assertEquals("V0.1.0",preamble.getProtocolVersion());
            Assertions.assertEquals(DaxCharacterEncoding.UTF_8, preamble.getEncoding());
            Assertions.assertEquals(contextMapper.getReferenceId("CRM"), preamble.getMsgContextId());
        } catch (DaxException e) {
            System.out.println(e.getDaxErrorCode());
            Assertions.fail();
        }
      }


    @Test
    void parsePreambleTest10(){
        String msgStr = "  DAXP=v0.1.0 |  ExN =UTF-8|CX=CRM|MC=1|9=$:DR|2001=123|99=177|";
        DaxPreamble preamble ;
        Assertions.assertThrows(DaxException.class,()->parser.parsePreamble(msgStr));
    }

    @Test
    void parsePreambleTest50(){
            String msgStr = "DAXP=v0.1.0|EN=UTF-8|CX=CRM|";
            DaxPreamble preamble ;
            try {
                preamble    = parser.parsePreamble(msgStr);
                Assertions.assertEquals("V0.1.0",preamble.getProtocolVersion());
                Assertions.assertEquals(DaxCharacterEncoding.UTF_8, preamble.getEncoding());
                Assertions.assertEquals(contextMapper.getReferenceId("CRM"), preamble.getMsgContextId());
            } catch (DaxException e) {
                System.out.println(e.getDaxErrorCode());
                Assertions.fail();
            }
        }


    @Test
    void parsepair_01(){

        String msgStr = "DAXP=v0.1.0|EN=UTF-8|CX=CRM|"+
             //   "9=CDD|5=INST|100=2000|2080=Big bike|2001=123|2002=Robert|99=177|";
                "9=CDD|7=1|5=INST|100=2000|2080=Big bike|2001=123|2002=Robert|99=177|";


        DaxFrame frame;
        try {
            frame = parser.parseFrame(msgStr);
            DaxMessage msg = frame.getFirstMessage();
            Assertions.assertEquals ("CDD", msg.getMsgType());


        } catch (DaxException e) {
            System.out.println(e.getDaxErrorCode());
            System.out.println(e.getMessage());
            Assertions.fail();
        }

    }



}
