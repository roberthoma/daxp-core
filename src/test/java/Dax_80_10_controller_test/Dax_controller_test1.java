package Dax_80_10_controller_test;

import Dax_00_Base_test.DaxTestConfig;
import Dax_00_Base_test.customer.CustomerDaxSchema;
import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.model.DaxMessage;
import org.junit.jupiter.api.Test;

public class Dax_controller_test1 extends DaxTestConfig {
    @Test
    public void testController(){
        DaxMessageCodec messageCodecAfter     = cmrProvider.getMessageCodec();
        DaxMessage msg = new DaxMessage(CustomerDaxSchema.CRM_DATA_REQ);

        DaxMessage msgResp =  cmrProvider.getDictionary().executor(msg);

        System.out.println("------------------------------->>");

        String msgAfter =  messageCodecAfter.encode(msgResp);
        System.out.println(msgAfter);

        System.out.println("<<--------------------------------");


    }
}
