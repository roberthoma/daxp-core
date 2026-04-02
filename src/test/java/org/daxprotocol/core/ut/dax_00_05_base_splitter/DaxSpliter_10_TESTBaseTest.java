package org.daxprotocol.core.ut.dax_00_05_base_splitter;

import org.daxprotocol.core.encoding.DaxCharacterEncoding;
import org.daxprotocol.core.mapper.DaxContextMapper;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.preamble.DaxPreambleTag;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.parsers.DaxParserTag;
import org.daxprotocol.core.parsers.DaxSplitterService;
import org.daxprotocol.core.ut.Dax_00_00_base_config.DaxConfigBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DaxSpliter_10_TESTBaseTest extends DaxConfigBaseTest {

    DaxSplitterService splitterService = new DaxSplitterService();
    DaxParserTag tagParser = new DaxParserTag(provider.getContextMapper());
    DaxContextMapper contextMapper = provider.getContextMapper();



    @Test
    void msgSplitterTest1(){

        String msgStr = "  DAXP=v0.1.0 |  EN =UTF-8|CX=CRM|MC=2|"+
                "9=CDD|6=4" +
                "|7=1|5=INST|$:100=2000|2080=Big bike|2001=123|\n2002=Robert| "+
        "2101=2|FIX:2085=345|2102=3|2075=INDIVIDUAL" +
                "|7=2|5=INST|100=2101|  2114 =Polna 7|\n ADR:2115=Warszawa|2111=345" +
                "|7=3|5=INST|$:100=2102|2114=Lipińskiego 1000|2115=Sanok|2120=4|2111=3346" +
                "|7=4|5=INST|100=2120|2121=43-444|2122=Zakopane|99=177"+
                "|9=CDD|6=4" +
                "|7=1|5=INST|100=2000|FIX:2080=Small boll|2001=123|FIX:2002=Kasia|2101=2|FIX:2085=23|2102=3|2075=INDIVIDUAL" +
                "|7=2|5=INST|100=2101|2114=Polna 8|ADR:2115=Kraków|2111=333" +
                "|7=3|5=INST|100=2102|2114=Sowia 1000|2115=Lesko\n testline|2120=4|2111=3346" +
                "|7=4|5=INST|100=2120|2121=44-444|2122=Rzeszów|99=177|      ";

        List<Integer> indList = splitterService.getPipeIndices(msgStr);

        String daxpSymbol ="...";
        System.out.println("-------------------------------");

        int prevIdx = 0;
        int msgSize = msgStr.length();
        int inxSize = indList.size();
        int lastIdx = indList.get(inxSize-1);
        boolean isPreableParsing = true;

        DaxPreamble preamble = new DaxPreamble();

        for (int idx : indList){


            int equalChar = msgStr.substring(prevIdx,idx).indexOf('=')+prevIdx;
            if (prevIdx> equalChar){
                throw new RuntimeException("IS ANY INCOMPATIBLE MESSAGE !!");
            }


            String tagStr = msgStr.substring(prevIdx,equalChar);
            if(prevIdx == 0){
              daxpSymbol = tagStr;
              if (!daxpSymbol.trim().equals("DAXP")){
                  throw new RuntimeException("IT IS NOT DAXP MESSAGE");
              }
            }

            String valueStr = msgStr.substring(equalChar+1,idx);

            System.out.println(">"+tagStr +"<:>"+valueStr+"<"        );

            try {
                if (isPreableParsing && DaxPreambleTag.contains(tagStr)) {
                    System.out.println(" JEST PREAMBLE  " +tagStr);

                    DaxPreambleTag tag = DaxPreambleTag.fromTag(tagStr);
                    switch (tag) {
                        case DAXP        -> System.out.println("Protocol identified");
                        case ENCODING    ->
                            DaxCharacterEncoding.fromName(valueStr).ifPresent(preamble::setEncoding);
                        case MSG_COUNT   -> preamble.setMsgCnt(Integer.parseInt(valueStr));
                        case MSG_CONTEXT -> preamble.setMsgContextId(contextMapper.getReferenceId(valueStr));
   //                        case MSG_SENDER  -> preamble.setSe System.out.println("Sender: " + value);
                //        case null        -> System.out.println("Received an invalid or null tag");
                    }


                }
                else {
                    isPreableParsing = false; ///Only preamble then finish

                    DaxTag tag = tagParser.parseDaxTag(tagStr, provider.getConfig().getAppContextId());

                    System.out.println(" JEST TAG : ctxId="+tag.getContextId()+" tagId=" + tag.getTagId());

                }
            }
            catch (Exception e){
                System.out.println(">>>>>>>>> ERROR to nie tag="+tagStr+"  value="+valueStr);

            }
            System.out.println("---");



//            System.out.println(msgStr.substring(prevIdx,idx)        );

            prevIdx = idx+1;

        }
        System.out.println("-------------------------------");
        Assertions.assertEquals("DAXP", daxpSymbol.trim());


    }




}
