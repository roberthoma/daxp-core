package Dax_CC_Message_Test;

import org.daxprotocol.core.codec.DaxCodecSymbols;
import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.codec.DaxDecodeService;
import org.daxprotocol.core.codec.DaxStringPair;
import org.daxprotocol.core.model.DaxMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class DaxDecodeServiceTest {
    static String msg;

    @BeforeAll
    static void initTest() {
        msg = "DAXP=1|TF=DEC|EN=UTF8|" +
                "9=DD|6=7|" +
                "7=1|209=Id customer|100=2001|110=I|" +
                "7=2|209=First name|100=2002|110=S|" +
                "7=3|209=Surname|100=2003|110=S|" +
                "7=4|209=Year of birth|100=2005|110=I|" +
                "7=5|209=Telephone|100=2073|110=S|" +
                "7=6|209=Town|100=2074|110=S|" +
                "7=7|209=Email|100=2011|110=S|" +
                "99=123|";
        msg = msg.replace('|',DaxCodecSymbols.PAIR_SEPARATOR);
    }

    @Test
    void preamblePairs_TEST(){
        System.out.println(msg);
        Map<String,String> preamblePairs = DaxDecodeService.parsePreamble(msg);
        Assertions.assertEquals("DEC",preamblePairs.get("TF"));
        Assertions.assertEquals("UTF8",preamblePairs.get("EN"));
        Assertions.assertEquals("1",preamblePairs.get("DAXP"));
    }

    @Test
    void parseNumberPairsToString_TEST(){
        Map<String,String>   preamblePairs = DaxDecodeService.parsePreamble(msg);
        List<DaxStringPair>  pairsList     = DaxDecodeService.parsePairs(msg);
        long equalChar = msg.chars()
                            .filter(c -> c== DaxCodecSymbols.EQUAL)
                            .count()
                       - preamblePairs.size();

        Assertions.assertEquals(equalChar,pairsList.size());
    }

    @Test
    void parseMSG_TEST(){
        DaxMessageCodec codec = new DaxMessageCodec();
        DaxMessage message = codec.decode(msg);
        String afterMsg = codec.encode(message);
        Assertions.assertEquals(msg,afterMsg );


    }

}