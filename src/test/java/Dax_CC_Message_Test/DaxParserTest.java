package Dax_CC_Message_Test;

import org.daxprotocol.core.codec.DaxParser;
import org.daxprotocol.core.codec.DaxStringPair;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.preamble.DaxPreamblePair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class DaxParserTest {
    static String msg;

    @BeforeAll
    static void initTest() {
        msg = "DAXP=1\u0001TF=DEC\u0001EN=UTF8\u0001\n" +
                "9=DD\u00016=7\u0001\n" +
                "7=1\u0001209=Id customer\u0001100=2001\u0001110=I\u0001\n" +
                "7=2\u0001209=First name\u0001100=2002\u0001110=S\u0001\n" +
                "7=3\u0001209=Surname\u0001100=2003\u0001110=S\u0001\n" +
                "7=4\u0001209=Year of birth\u0001100=2005\u0001110=I\u0001\n" +
                "7=5\u0001209=Telephone\u0001100=2073\u0001110=S\u0001\n" +
                "7=6\u0001209=Town\u0001100=2074\u0001110=S\u0001\n" +
                "7=7\u0001209=Email\u0001100=2011\u0001110=S\u0001\n" +
                "99=123\u0001\n";
    }

    @Test
    void preamblePairs_TEST(){
        Map<String,String> preamblePairs = DaxParser.parsePreamble(msg);
        preamblePairs.forEach((t, s) -> System.out.println(t+"="+s));

    }
    @Test
    void parsePairsToString_TEST(){
        List<DaxStringPair>   preamblePairs = DaxParser.parsePairs(msg);
        preamblePairs.forEach(System.out::println);

    }

    @Test
    void parseMSG_TEST(){
        DaxMessage message = DaxParser.parse(msg);
        System.out.println("MSG_TYPE =  "+message.getType());
        System.out.println("Blocks =  "+message.getBlockCount());

        //preamblePairs.forEach(System.out::println);

    }

}