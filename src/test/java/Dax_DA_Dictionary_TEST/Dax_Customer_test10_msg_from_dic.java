package Dax_DA_Dictionary_TEST;

import Dax_00_Base_test.DaxTestConfig;
import Dax_00_Base_test.customer.CustomerDaxTag;
import org.daxprotocol.core.codec.DaxTagConst;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.tag.DaxTag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class Dax_Customer_test10_msg_from_dic extends DaxTestConfig {

    @Test
    void testCustomerDicDecoder(){

        DaxMessage msg = crmProvider.getMessageFactory()
                .dictionaryToMsg(crmProvider.getDictionary());

        String     msgStr    = crmProvider.getMessageCodec().encode(msg);
        DaxMessage msgAfter  = crmProvider.getMessageCodec().decode(msgStr);

        Assertions.assertEquals(msg.getBlockCount(),msgAfter.getBlockCount());
        System.out.println(msg);
    }

}
