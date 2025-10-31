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
        msg = "DAXP=1\u0001TF=DEC\u0001EN=UTF8\u0001\n" +
                "9=DD\u00016=7\u0001" +
                "7=1\u0001209=Id customer\u0001100=2001\u0001110=I\u0001" +
                "7=2\u0001209=First name\u0001100=2002\u0001110=S\u0001" +
                "7=3\u0001209=Surname\u0001100=2003\u0001110=S\u0001" +
                "7=4\u0001209=Year of birth\u0001100=2005\u0001110=I\u0001" +
                "7=5\u0001209=Telephone\u0001100=2073\u0001110=S\u0001" +
                "7=6\u0001209=Town\u0001100=2074\u0001110=S\u0001" +
                "7=7\u0001209=Email\u0001100=2011\u0001110=S\u0001" +
                "99=123\u0001";
    }

    @Test
    void preamblePairs_TEST(){
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