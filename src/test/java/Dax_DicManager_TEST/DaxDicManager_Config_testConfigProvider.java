package Dax_DicManager_TEST;

import Dax_00_Base_test.DaxTestConfig;
import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.dictionary.DaxDictionaryPopulator;
import org.daxprotocol.core.factory.DaxMessageFactory;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.provider.DaxProvider;
import org.daxprotocol.core.provider.DaxProviderImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        DaxProvider providerAfter             =  new DaxProviderImpl(cmrProvider.getConfig());
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
        //TODO Expand tests
        Assertions.assertEquals(messageDicAfter.getBlockCount(), messageOrg.getBlockCount());

        Assertions.assertEquals(cmrDictionary.getAttributMap().size(),  dicAfter.getAttributMap().size());
        Assertions.assertEquals(cmrDictionary.getContextMap().size(),   dicAfter.getContextMap().size());
        Assertions.assertEquals(cmrDictionary.getTagSet().size(),       dicAfter.getTagSet().size());
        Assertions.assertEquals(cmrDictionary.getEnumMap().size(),      dicAfter.getEnumMap().size());
        Assertions.assertEquals(cmrDictionary.getEnumValueMap().size(), dicAfter.getEnumValueMap().size());
        Assertions.assertEquals(cmrDictionary.getGroupMap().size(),     dicAfter.getGroupMap().size());



        System.out.println("------------    End OF DOC populate -------- ");
    }

}
