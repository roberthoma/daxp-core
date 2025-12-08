package Dax_DicManager_TEST;

import Dax_00_Base_test.DaxTestConfig;
import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.dictionary.DaxDictionaryPopulator;
import org.daxprotocol.core.factory.DaxMessageFactory;
import org.daxprotocol.core.model.DaxMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DaxDicManager_Config_testConfigProvider extends DaxTestConfig {


    @Test
    void test1(){

        DaxMessageCodec codec = crmProvider.getMessageCodec();
        DaxDictionaryPopulator dictionaryPopulator = crmProvider.getDictionaryPopulator();
        DaxMessageFactory factory = crmProvider.getMessageFactory();
        DaxDictionary dicOrg = crmProvider.getDictionary();
        //--------------
        DaxMessage messageOrg =  crmProvider.getMessageFactory().dictionaryToMsg(dicOrg);

        String msgStrOrg =  codec.encode(messageOrg);
        System.out.println("- original -");
        System.out.println(msgStrOrg);

        System.out.println(" -- AFTER -- ");
        DaxDictionary dicAfter = new DaxDictionary(cntProvider.getConfig());

        DaxMessage messageAfter = codec.decode(msgStrOrg);

        dictionaryPopulator.populateFromMessage(dicAfter, messageAfter);

        DaxMessage messageDicAfter = factory.dictionaryToMsg(dicAfter);

        String msgDicAfter =  codec.encode(messageAfter);
        System.out.println(msgDicAfter);
        Assertions.assertEquals(msgStrOrg,msgDicAfter);


        System.out.println("------------End OF DOC populate --------");
    }

}
