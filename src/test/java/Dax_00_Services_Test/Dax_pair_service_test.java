package Dax_00_Services_Test;

import Dax_00_Base_test.DaxTestConfig;
import org.daxprotocol.core.codec.DaxPairCodec;
import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.model.pair.DaxStringPair;
import org.daxprotocol.core.parser.DaxPatternService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Dax_pair_service_test extends DaxTestConfig {


    static String prbPairsStr1 = "NO_DAX|V=1|EN=UTF-8|";
    static String prbPairsStr2 = "DAXP|V=1|EN=UTF-8|";
    static String msgPairsStr1 = "123=ValueSYS|ABC:123=ValueABC|CBA:234=ValueCBA|SYS:128=ValueSYS|";
    static String msgPairsStr2 = "123=Value1|234=Value3|";

    @Test
    public void prb_pair_parse_wrongPreamble(){
        Pattern pattern = DaxPatternService.getPreamblePairPattern('|');
        try {

            Map<String, String> pairList = cmrProvider.getPreambleCodec().parsePreamble(prbPairsStr1, pattern);
            Assertions.fail("NO_DAX| << it is not DAXP message");
        }
        catch (RuntimeException e){
            System.out.println("IS OK.");
        }

    }
    @Test
    public void prb_pair_parse_test(){
        Pattern pattern = DaxPatternService.getPreamblePairPattern('|');
        Map<String, String> pairList = cmrProvider.getPreambleCodec().parsePreamble(prbPairsStr2,pattern);

        pairList.forEach((s, s2) ->
                System.out.println(s+"="+s2));

    }


    @Test
    public void pair_parse_test(){
        Pattern pattern = DaxPatternService.getMessagePairPattern('|');
        DaxPairCodec pairCodec = cmrProvider.getPairCodec();
        DaxpConfig   crmConfig = cmrProvider.getConfig();
        List<DaxStringPair> pairList = pairCodec.parsePairs(msgPairsStr1,pattern,crmConfig.getAppContextId());

        pairList.forEach(System.out::println);

    }


}
