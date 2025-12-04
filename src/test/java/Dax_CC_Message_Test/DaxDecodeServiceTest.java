package Dax_CC_Message_Test;

import Dax_00_Base_test.DaxTestConfig;
import org.daxprotocol.core.codec.DaxCodecSymbol;
import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.codec.DaxDecodeService;
import org.daxprotocol.core.model.pair.DaxStringPair;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.preamble.DaxPreambleCodec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class DaxDecodeServiceTest extends DaxTestConfig {
    static String msg;

    @BeforeAll
    static void initTest() {
        msg = "DAXP|V=1|EN=UTF8|\n" +  //CNT=1|\n" +
                "9=DD|6=7|\n"+
                "7=1|5=F|209=Id customer|100=2001|110=I|\n" +
                "7=2|5=F|209=First name|100=2002|110=S|\n" +
                "7=3|5=F|209=Surname|100=2003|110=S|\n" +
                "7=4|5=F|209=Year of birth|100=2005|110=I|\n" +
                "7=5|5=F|209=Telephone|100=2073|110=S|\n" +
                "7=6|5=F|209=Town|100=2074|110=S|\n" +
                "7=7|5=F|209=Email|100=2011|110=S|\n" +
                "99=123|";
    }

    @Test
    void preamblePairs_TEST(){

        Map<String,String> preamblePairs = crmProvider.getPreambleCodec().parsePreamble(msg );

        Assertions.assertEquals("1",preamblePairs.get("V"));
        Assertions.assertEquals("UTF8",preamblePairs.get("EN"));
    }

    @Test
    void parseAndDecodeNumberPairsToString_TEST(){
        Map<String,String>   preamblePairs = crmProvider.getPreambleCodec().parsePreamble(msg);
        List<DaxStringPair>  pairsList     = DaxDecodeService.parsePairs(msg,crmProvider.getPreambleCodec().getPairPattern(msg));
        long equalChar = msg.chars()
                            .filter(c -> c== DaxCodecSymbol.EQUAL)
                            .count()
                       - preamblePairs.size();

        Assertions.assertEquals(equalChar,pairsList.size());
    }

    @Test
    void decodeMSG_TEST(){
        DaxMessageCodec codec = new DaxMessageCodec(crmProvider.getConfig());
        DaxMessage message = codec.decode(msg);
        Assertions.assertEquals("DD",  message.getMsgType());
        Assertions.assertEquals(7,  message.getBlockCount());
    }

    @Test
    void encodeMSG_TEST(){
        DaxMessageCodec codec = crmProvider.getMessageCodec();
        DaxMessage message = codec.decode(msg);
        String afterMsgStr = codec.encode(message);
        if(afterMsgStr.contains("\n7=0|")){
            Assertions.fail("ERROR . Message contains 7=0 !!!! MSG: \n"+afterMsgStr);
        }
        DaxMessage afterMsg  = codec.decode(afterMsgStr);
        Assertions.assertEquals("DD",  afterMsg.getMsgType());
        Assertions.assertEquals(7,  afterMsg.getBlockCount());
    }
}