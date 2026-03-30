package org.daxprotocol.core.ut.Dax_DA_Dictionary_TEST;

import org.daxprotocol.core.ut.Dax_00_Base_test.DaxTestConfig;
import org.daxprotocol.core.model.DaxMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
