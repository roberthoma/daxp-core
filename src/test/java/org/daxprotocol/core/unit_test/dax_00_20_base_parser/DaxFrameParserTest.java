package org.daxprotocol.core.unit_test.dax_00_20_base_parser;

import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.encoding.DaxCharacterEncoding;
import org.daxprotocol.core.exceptions.DaxException;
import org.daxprotocol.core.exceptions.DaxFrameParserException;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.unit_test.dax_00_01_base_config.DaxConfigBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DaxFrameParserTest extends DaxConfigBaseTest {

    @Test
    void parsePreambleTest01(){
        //Task is parse only PREAMBLE
        String msgStr = "DAXP|V=v1.2.3|EN=UTF-16|NS=FIX|MQ=1|9=$:DR|2001=123|99=177|";
        DaxPreamble preamble ;
        try {
           // msgStr = DaxMessageNormalizer.normalize(msgStr);
            preamble    = frameParser.parsePreamble(msgStr);
            Assertions.assertEquals("V1.2.3",preamble.getProtocolVersion());
            Assertions.assertEquals(DaxCharacterEncoding.UTF_16, preamble.getEncoding());
            Assertions.assertEquals(namespaceMapper.getReferenceId("FIX"), preamble.getNamespaceId());
        } catch (DaxException e) {
            System.out.println(e.getDaxErrorCode());
            Assertions.fail();
        }
    }

    @Test
    void parsePreambleTest05(){
        String msgStr = "  DAXP|V=v0.1.0 |  EN =UTF-8|NS=CRM|MQ=1|9=$:DR|2001=123|99=177|";
        DaxPreamble preamble ;
        try {
            preamble    = frameParser.parsePreamble(msgStr);
            Assertions.assertEquals("V0.1.0",preamble.getProtocolVersion());
            Assertions.assertEquals(DaxCharacterEncoding.UTF_8, preamble.getEncoding());
            Assertions.assertEquals(namespaceMapper.getReferenceId("CRM"), preamble.getNamespaceId());
        } catch (DaxException e) {
            System.out.println(e.getDaxErrorCode());
            Assertions.fail();
        }
      }


    @Test
    void parsePreambleTest10_(){
        String msgStr = "  DAXP|V=v0.1.0 |  ExN =UTF-8|NS=CRM|MC=1|9=$:DR|2001=123|99=177|";
        Assertions.assertThrows(DaxException.class,()->frameParser.parsePreamble(msgStr));
    }

    @Test
    void parsePreambleTest50(){
            String msgStr = "DAXP|V=v0.1.0|EN=UTF-8|NS=CRM|";
            DaxPreamble preamble ;
            try {
                preamble    = frameParser.parsePreamble(msgStr);
                Assertions.assertEquals("V0.1.0",preamble.getProtocolVersion());
                Assertions.assertEquals(DaxCharacterEncoding.UTF_8, preamble.getEncoding());
                Assertions.assertEquals(namespaceMapper.getReferenceId("CRM"), preamble.getNamespaceId());
            } catch (DaxException e) {
                System.out.println(e.getDaxErrorCode());
                Assertions.fail();
            }
        }

    @Test
    void parsePreambleTest60(){
        String msgStr = "DxAXP|V=v0.1.0|EN=UTF-8|NS=CRM|";
        try {
            Assertions.assertThrowsExactly(DaxFrameParserException.class,() -> frameParser.parsePreamble(msgStr));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    void parseFrame_02(){
        try {

            String msgStr = "DAXP|V=v0.1.0|EN=UTF-8|NS=CRM|"+
                    "1=CDD|$:7=1|$:5=INST|$:8=2000|2080=Big bike|2001=123|2002=Robert|$:9=177|";
            Assertions.assertThrowsExactly(DaxFrameParserException.class, () ->   frameParser.parseFrame(msgStr));

        } catch (Exception e) {
            Assertions.fail();
        }

    }


    @Test
    void parseFrame_01(){

        String msgStr = "DAXP|V=v0.1.0|EN=UTF-8|NS=CRM|"+
                "$:1=CDD|$:5=INST|$:8=2000|2080=Big bike|2001=123|2002=Robert|$:9=165|";

        DaxFrame frame;
        try {
            frame = frameParser.parseFrame(msgStr);
            DaxMessage msg = frame.getFirstMessage();
            Assertions.assertEquals ("CDD", msg.getMsgType());
            DaxTag t2080tag = DaxTag.of(namespaceMapper.getReferenceId("CRM") ,2080);


            Assertions.assertEquals ("Big bike",  msg.getBody()
                                                              .getBlock(0)
                                                              .get(t2080tag).getStrValue());


        } catch (DaxException e) {
            System.out.println(e.getDaxErrorCode());
            System.out.println(e.getMessage());
            Assertions.fail();
        }

    }



    @Test
    void parseFrame_04(){

        String msgStr = "DAXP|V=v0.1.0|EN=UTF-8|NS=CRM|MQ=1|"+
                "$:1=CDD|$:7=1|$:5=INST|$:8=2000|2080=Big bike|2001=123|2002=Robert|$:9=107|";
        DaxFrame frame;
        try {
            frame = frameParser.parseFrame(msgStr);
            DaxMessage msg = frame.getFirstMessage();
            Assertions.assertEquals ("CDD", msg.getMsgType());


        } catch (DaxException e) {
            System.out.println(e.getDaxErrorCode());
            System.out.println(e.getMessage());
            Assertions.fail();
        }

    }

    @Test
    void parseFrame_05_msg_quantity(){

        String msgStr = "DAXP|V=v0.1.0|EN=UTF-8|NS=CRM|MQ=2|"+
                "$:1=CDD|$:5=INST|$:8=2000|2080=Big bike|2001=123|2002=Robert|$:9=165|"+
                "$:1=CDD|$:5=INST|$:8=2000|2080=A kuku|2001=334|2002=Ola|$:9=171|";
        DaxFrame frame;
        try {
            frame = frameParser.parseFrame(msgStr);
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

        String msgStr = "DAXP|V=v0.1.0|EN=UTF-8|NS=CRM|MC=3|"+
                "1=CDD|" +
                "7=1|5=INST|100=2000|2080=Big bike|2001=123|2002=Robert|9=177|"+
                "7=2|5=INST|100=2000|2080=Big bike|2001=125|2002=Marzena|9=134|";


        String finalMsgStr = msgStr;
        Assertions.assertThrowsExactly(DaxFrameParserException.class,() -> frameParser.parseFrame(finalMsgStr));

    }

    @Test
    void parseFrame_05_expected2Msg(){

        String msgStr = "DAXP|V=v0.1.0|EN=UTF-8|NS=CRM|MC=2|"+
                "1=CDD|" +
                "7=1|5=INST|100=2000|2080=Big bike|2001=123|2002=Robert|9=177|"+
                "7=2|5=INST|100=2000|2080=Big bike|2001=124|2002=Piotr|9=172|"+
                "7=3|5=INST|100=2000|2080=Big bike|2001=125|2002=Marzena|9=134|"
          ;
        Assertions.assertThrowsExactly(DaxFrameParserException.class,() -> frameParser.parseFrame(msgStr));

    }

    @Test
    void parseFrame_06(){

        String msgStr = "DAXP|V=v0.1.0|EN=UTF-8|NS=CRM|MQ=1|"+
                "$:1=CDD|$:7=1|$:5=INST|$:8=2000|2080=Big bike|2001=123|2002=Robert|2005@4;5|$:9=214|";
        DaxFrame frame;
        try {
            frame = frameParser.parseFrame(msgStr);
            DaxMessage msg = frame.getFirstMessage();
            Assertions.assertEquals ("CDD", msg.getMsgType());


        } catch (DaxException e) {
            System.out.println(e.getDaxErrorCode());
            System.out.println(e.getMessage());
            Assertions.fail();
        }

    }
}

