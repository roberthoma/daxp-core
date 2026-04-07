package org.daxprotocol.core.ut.Dax_00_10_Parser;

import org.daxprotocol.core.ut.Dax_00_00_base_config.DaxConfigBaseTest;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.parsers.DaxParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DaxParserV1TestBaseTest extends DaxConfigBaseTest {



    @Test
    void Parser2BaseTest() {
        String msgStr = "DAXP=v0.1.0|EN=UTF-8|CX=CRM" +
                "|9=CDD|6=4" +
                "|7=1|5=INST|100=2000|2080=Big bike|2001=123|\n2002=Robert|2101=2|FIX:2085=345|2102=3|2075=INDIVIDUAL" +
                "|7=2|5=INST|100=2101|  2114 =Polna 7|\n ADR:2115=Warszawa|2111=345" +
                "|7=3|5=INST|100=2102|2114=Lipińskiego 1000|2115=Sanok|2120=4|2111=3346" +
                "|7=4|5=INST|100=2120|2121=43-444|2122=Zakopane|99=177|"+
                "|9=CDD|6=4" +
                "|7=1|5=INST|100=2000|2080=Small boll|2001=123|FIX:2002=Kasia|2101=2|FIX:2085=23|2102=3|2075=INDIVIDUAL" +
                "|7=2|5=INST|100=2101|2114=Polna 8|ADR:2115=Kraków|2111=333" +
                "|7=3|5=INST|100=2102|2114=Sowia 1000|2115=Lesko\n testline|2120=4|2111=3346" +
                "|7=4|5=INST|100=2120|2121=44-444|2122=Rzeszów|99=177|";
        ;

//        DaxParser parserService = daxEngine.getParser();
//
//        List<DaxPair<?>>  pairList = parserService. parsePairs(msgStr,  4 );
//
//        pairList.forEach((pair) -> System.out.println(pair.getTag().getContextId()+":"+
//                pair.getTag().getTagId()+"="+pair.getStrValue()));
//
//        Assertions.assertEquals(10,pairList.size());
    }

//    @Test
//    void parseAndDecodeNumberPairsToString_TEST(){
//        DaxpConfig config = crmProvider.getConfig();
//        Map<String,String> preamblePairs = crmProvider.getPreambleCodec().parsePreamble(msg);
//
//        DaxPairCodec pairCodec = crmProvider.getPairCodec();
//
//        DaxParserService parserService = crmProvider.getParserService();
//
//        List<DaxPair<?>> pairsList = parserService
//                .parsePairs(msgPairs, config.getAppContextId());//(DaxPatternFactory.compileMessagePairPattern('|') ,
//
//        long equalChar = msgPairs.chars()
//                .filter(c -> c == '|')
//                .count();
//        Assertions.assertEquals(equalChar,pairsList.size());
//    }


    //    @Test
//    void ParserBaseTest() {
//
//    DaxParserService parserService = crmProvider.getParserService();
//
//    Map<DaxTag, String> pairStrMap = parserService.parseBlock("|7=1|5=INST|100=2000|2080=Small boll|2001=123|2002=Kasia|2101=2|2085=23|2102=3|2075=INDIVIDUAL");
//        pairStrMap.forEach((daxTag, s) -> System.out.println(daxTag+"  = "+s));
//
//    }



}
