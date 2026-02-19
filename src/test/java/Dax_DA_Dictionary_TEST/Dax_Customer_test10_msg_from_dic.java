package Dax_DA_Dictionary_TEST;

import Dax_00_Base_test.DaxTestConfig;
import org.daxprotocol.core.model.DaxMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Dax_Customer_test10_msg_from_dic extends DaxTestConfig {

    @Test
    void testCustomerDicDecoder(){

        DaxMessage msg = cmrProvider.getMessageFactory()
                .dictionaryToMsg(cmrProvider.getDictionary());

        String     msgStr    = cmrProvider.getMessageCodec().encode(msg);
        DaxMessage msgAfter  = cmrProvider.getMessageCodec().decode(msgStr);

        Assertions.assertEquals(msg.getBlockCount(),msgAfter.getBlockCount());
        System.out.println(msg);
    }

}
