package Dax_DicManager_TEST;

import Dax_00_Base_test.AppMessage;
import Dax_00_Base_test.Customer;
import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.dictionary.DaxAllContextDic;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.dictionary.DaxDictionaryManager;
import org.daxprotocol.core.factory.DaxMessageFactory;
import org.daxprotocol.core.model.DaxMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DaxDicManager_test {


    @Test
    void test1(){
        DaxAllContextDic  ctxDic = new DaxAllContextDic();

        System.out.println("CTX default = "+ctxDic.getDefaultContextId());

        DaxDictionaryManager manager = new DaxDictionaryManager();
        DaxMessageFactory factory = new DaxMessageFactory();
        DaxMessageCodec codec = new DaxMessageCodec();
        AppMessage appMessage = new AppMessage();

        DaxDictionary dictionary = new DaxDictionary();
        DaxDictionary dicAfter = new DaxDictionary();

        System.out.println("------------DOC populate --------");
        manager.populateFromAnnotations(dictionary, Customer.class);
        appMessage.init(dictionary);

        DaxMessage message = factory.createDictionaryMsg(dictionary);
        String msgStr = codec.encode(message);
        System.out.println("- original -");
        System.out.println(msgStr);

        manager.populateFromMessage(dicAfter, message);

        DaxMessage messageAfter = factory.createDictionaryMsg(dicAfter);
        String msgDicAfter =  codec.encode(messageAfter);
        System.out.println(" -- AFTER -- ");
        System.out.println(msgDicAfter);
        Assertions.assertEquals(msgStr,msgDicAfter);


        System.out.println("------------End OF DOC populate --------");
    }

}
