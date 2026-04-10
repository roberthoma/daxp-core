package org.daxprotocol.core.unit_test.dax_00_20_base_parser;

import org.daxprotocol.core.decorator.DaxMessageNormalizer;
import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.encoding.DaxCharacterEncoding;
import org.daxprotocol.core.exceptions.DaxException;
import org.daxprotocol.core.exceptions.DaxMsgParserException;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.unit_test.dax_00_00_base_config.DaxConfigBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DaxFrameParserTest extends DaxConfigBaseTest {

    @Test
    void parsePreambleTest01(){
        String msgStr = "DAXP=v1.2.3|EN=UTF-16|CX=FIX|MC=1|9=$:DR|2001=123|99=177|";
        DaxPreamble preamble ;
        try {
            msgStr = DaxMessageNormalizer.normalize(msgStr);
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
            msgStr = DaxMessageNormalizer.normalize(msgStr);
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
        String msgStr = DaxMessageNormalizer.normalize("  DAXP=v0.1.0 |  ExN =UTF-8|CX=CRM|MC=1|9=$:DR|2001=123|99=177|");
        Assertions.assertThrows(DaxException.class,()->parser.parsePreamble(msgStr));
    }

    @Test
    void parsePreambleTest50(){
            String msgStr = "DAXP=v0.1.0|EN=UTF-8|CX=CRM|";
            msgStr = DaxMessageNormalizer.normalize(msgStr);
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
    void parsePreambleTest60(){
        String msgStr = DaxMessageNormalizer.normalize("DxAXP=v0.1.0|EN=UTF-8|CX=CRM|");
        try {
            Assertions.assertThrowsExactly(DaxMsgParserException.class,() -> parser.parsePreamble(msgStr));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }


    @Test
    void parseFrame_01(){

        String msgStr = "DAXP=v0.1.0|EN=UTF-8|CX=CRM|"+
                "9=CDD|5=INST|100=2000|2080=Big bike|2001=123|2002=Robert|99=177|";
        msgStr = DaxMessageNormalizer.normalize(msgStr);

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
    @Test
    void parseFrame_02(){

        String msgStr = "DAXP=v0.1.0|EN=UTF-8|CX=CRM|"+
                "9=CDD|7=1|5=INST|100=2000|2080=Big bike|2001=123|2002=Robert|99=177|";
        msgStr = DaxMessageNormalizer.normalize(msgStr);
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

    @Test
    void parseFrame_03(){

        String msgStr = "DAXP=v0.1.0|EN=UTF-8|CX=CRM|MC=1|"+
                "9=CDD|7=1|5=INST|100=2000|2080=Big bike|2001=123|2002=Robert|99=177|";
        msgStr = DaxMessageNormalizer.normalize(msgStr);
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

    @Test
    void parseFrame_04(){

        String msgStr = "DAXP=v0.1.0|EN=UTF-8|CX=CRM|MC=2|"+
                "9=CDD|" +
                "7=1|5=INST|100=2000|2080=Big bike|2001=123|2002=Robert|99=177|"+
                "7=2|5=INST|100=2000|2080=Big bike|2001=125|2002=Marzena|99=134|";
        msgStr = DaxMessageNormalizer.normalize(msgStr);
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

    @Test
    void parseFrame_05_expected3Msg(){

        String msgStr = "DAXP=v0.1.0|EN=UTF-8|CX=CRM|MC=3|"+
                "9=CDD|" +
                "7=1|5=INST|100=2000|2080=Big bike|2001=123|2002=Robert|99=177|"+
                "7=2|5=INST|100=2000|2080=Big bike|2001=125|2002=Marzena|99=134|";

        msgStr = DaxMessageNormalizer.normalize(msgStr);

        String finalMsgStr = msgStr;
        Assertions.assertThrowsExactly(DaxMsgParserException.class,() -> parser.parseFrame(finalMsgStr));

    }

    @Test
    void parseFrame_05_expected2Msg(){

        String msgStr = DaxMessageNormalizer.normalize("DAXP=v0.1.0|EN=UTF-8|CX=CRM|MC=2|"+
                "9=CDD|" +
                "7=1|5=INST|100=2000|2080=Big bike|2001=123|2002=Robert|99=177|"+
                "7=2|5=INST|100=2000|2080=Big bike|2001=124|2002=Piotr|99=172|"+
                "7=3|5=INST|100=2000|2080=Big bike|2001=125|2002=Marzena|99=134|"
          );
        Assertions.assertThrowsExactly(DaxMsgParserException.class,() -> parser.parseFrame(msgStr));

    }

}
