package Dax_DicManager_TEST;

import Dax_00_Base_test.DaxTestConfig;
import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.codec.DaxTagConst;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.dictionary.DaxDictionaryPopulator;
import org.daxprotocol.core.factory.DaxMessageFactory;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.provider.DaxProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class DaxDicManager_Config_testConfigProvider extends DaxTestConfig {


    @Test
    void dictionaryPopulator_Test(){

        DaxMessageCodec cmrMsgCodec = cmrProvider.getMessageCodec();
//        DaxDictionaryPopulator dictionaryPopulator = cmrProvider.getDictionaryPopulator();
//        DaxMessageFactory factoryOrg = cmrProvider.getMessageFactory();
        DaxDictionary cmrDictionary = cmrProvider.getDictionary();
        DaxMessage messageOrg       = cmrProvider.getMessageFactory().dictionaryToMsg(cmrDictionary);
        String msgStrOrg            = cmrMsgCodec.encode(messageOrg);

        System.out.println("- Cmr Original -");
        System.out.println(msgStrOrg);

        System.out.println(" -- after -- ");
        DaxProvider providerAfter             =  new DaxProvider(cmrProvider.getConfig());
        DaxDictionary dicAfter                = providerAfter.getDictionary();
        DaxMessageCodec messageCodecAfter     = providerAfter.getMessageCodec();
        DaxDictionaryPopulator populatorAfter = providerAfter.getDictionaryPopulator();
        DaxMessageFactory messageFactoryAfter = providerAfter.getMessageFactory();

        DaxMessage messageAfter  = messageCodecAfter.decode(msgStrOrg);
        populatorAfter.populateFromMessage(dicAfter, messageAfter);

        DaxMessage messageDicAfter = messageFactoryAfter.dictionaryToMsg(dicAfter);

        String msgDicAfter =  messageCodecAfter.encode(messageDicAfter);
        System.out.println(msgDicAfter);

        //Assertions.assertEquals(msgStrOrg,msgDicAfter);
        //TODO Extend tests
        Assertions.assertEquals(messageDicAfter.getBlockCount(), messageOrg.getBlockCount());

        Assertions.assertEquals(cmrDictionary.getAttributMap().size(),  dicAfter.getAttributMap().size());
        Assertions.assertEquals(cmrDictionary.getContextMap().size(),   dicAfter.getContextMap().size());
        Assertions.assertEquals(cmrDictionary.getTagSet().size(),       dicAfter.getTagSet().size());
        Assertions.assertEquals(cmrDictionary.getGroupMap().size(),     dicAfter.getGroupMap().size());

        // TODO assertion  with context
        //Assertions.assertEquals(cmrDictionary.getEnumMap().size(),      dicAfter.getEnumMap().size());
        //Assertions.assertEquals(cmrDictionary.getEnumValueMap().size(), dicAfter.getEnumValueMap().size());

        for (Map.Entry<Integer, Map<DaxTag, DaxPair<?>>> entry : messageDicAfter.getBody().getBlockMap().entrySet()) {
            Integer idx = entry.getKey();
            Map<DaxTag, DaxPair<?>> daxTagDaxPairMap = entry.getValue();
            if (daxTagDaxPairMap.containsKey(DaxTagConst.FIELD_DATA_TYPE)) {
                if (daxTagDaxPairMap.get(DaxTagConst.FIELD_DATA_TYPE).getStrValue().equals("?")) {
                    Assertions.fail("No FIELD_DATA_TYPE at BLOCK_INDEX = : " + (idx + 1));
                }
            }
        }


        System.out.println("------------    End OF DOC populate -------- ");
    }

}
