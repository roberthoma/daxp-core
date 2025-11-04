package Dax_CC_Message_Test;

import org.daxprotocol.core.codec.DaxCodecSymbols;
import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.codec.DaxDecodeService;
import org.daxprotocol.core.codec.DaxStringPair;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.preamble.DaxPreambleCodec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class DaxDecodeServiceTest {
    static String msg;

    @BeforeAll
    static void initTest() {
        msg = "DAXP=1|TF=DEC|EN=UTF8| \n" +
                "9=DD|6=7|"+
                "7=1|209=Id customer|100=2001|110=I|\n" +
                "7=2|209=First name|100=2002|110=S|\n" +
                "7=3|209=Surname|100=2003|110=S|" +
                "7=4|209=Year of birth|100=2005|110=I|" +
                "7=5|209=Telephone|100=2073|110=S|" +
                "7=6|209=Town|100=2074|110=S|" +
                "7=7|209=Email|100=2011|110=S|" +
                "99=123|";
    }

    @Test
    void preamblePairs_TEST(){

        Map<String,String> preamblePairs = DaxPreambleCodec.parsePreamble(msg );

        Assertions.assertEquals("DEC",preamblePairs.get("TF"));
        Assertions.assertEquals("UTF8",preamblePairs.get("EN"));
        Assertions.assertEquals("1",preamblePairs.get("DAXP"));
    }

    @Test
    void parseAndDecodeNumberPairsToString_TEST(){
        Map<String,String>   preamblePairs = DaxPreambleCodec.parsePreamble(msg);
        List<DaxStringPair>  pairsList     = DaxDecodeService.parsePairs(msg,DaxPreambleCodec.getPairPattern(msg));
        long equalChar = msg.chars()
                            .filter(c -> c== DaxCodecSymbols.EQUAL)
                            .count()
                       - preamblePairs.size();

        Assertions.assertEquals(equalChar,pairsList.size());
    }

    @Test
    void parseAndDecodeMSG_TEST(){
        DaxMessageCodec codec = new DaxMessageCodec();
        DaxMessage message = codec.decode(msg);
        Assertions.assertEquals("DD",  message.getMsgType());

        String afterMsgStr = codec.encode(message);
        DaxMessage afterMsg  = codec.decode(afterMsgStr);
        Assertions.assertEquals("DD",  afterMsg.getMsgType());


    }

}