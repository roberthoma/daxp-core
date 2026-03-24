package Dax_80_10_controller_test;

import Dax_00_Base_test.DaxTestConfig;
import Dax_00_Base_test.customer.CustomerDaxSchema;
import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.model.DaxMessage;
import org.junit.jupiter.api.Test;

public class Dax_controller_test1 extends DaxTestConfig {
    @Test
    public void testControllerGetter(){
        DaxMessageCodec messageCodecAfter     = crmProvider.getMessageCodec();
        DaxMessage msg = new DaxMessage(CustomerDaxSchema.CRM_DATA_REQ);

        DaxMessage msgResp =  crmProvider.getDictionary().executor(msg);

        System.out.println("------------------------------->>");

        String msgAfter =  messageCodecAfter.encode(msgResp);
        System.out.println(msgAfter);

        System.out.println("<<--------------------------------");


    }
    @Test
    public void testControllerSetter(){
        DaxMessageCodec messageCodecAfter     = crmProvider.getMessageCodec();
        DaxMessage msg = new DaxMessage(CustomerDaxSchema.CRM_DATA_UPD);

        DaxMessage msgResp =  crmProvider.getDictionary().executor(msg);

        System.out.println("------------------------------->>");

        String msgAfter =  messageCodecAfter.encode(msgResp);
        System.out.println(msgAfter);

        System.out.println("<<--------------------------------");


    }
}
