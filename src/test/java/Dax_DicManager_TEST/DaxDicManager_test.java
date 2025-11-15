package Dax_DicManager_TEST;

import Dax_00_Base_test.Customer;
import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.dictionary.DaxDictionaryManager;
import org.daxprotocol.core.factory.DaxMessageFactory;
import org.daxprotocol.core.model.DaxMessage;
import org.junit.jupiter.api.Test;

public class DaxDicManager_test {


    @Test
    void test1(){

        DaxDictionaryManager manager = new DaxDictionaryManager();
        DaxDictionary dictionary = new DaxDictionary();
        DaxMessageFactory factory = new DaxMessageFactory();
        DaxMessageCodec codec = new DaxMessageCodec();
        System.out.println("------------DOC populate --------");
        manager.populateFromAnnotations(dictionary, Customer.class);

        DaxMessage message = factory.createDictionaryMsg(dictionary);
        String msgStr = codec.encode(message);
        System.out.println(msgStr);
        System.out.println("------------End OF DOC populate --------");
    }

}
