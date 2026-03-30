package org.daxprotocol.core.ut.Dax_80_10_controller_test;

import org.daxprotocol.core.ut.Dax_00_Base_test.DaxTestConfig;
import org.daxprotocol.core.ut.Dax_00_Base_test.crm_application.customer.CustomerDaxSchema;
import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.model.DaxMessage;
import org.junit.jupiter.api.Test;

public class Dax_80_10_Api_Controller_test extends DaxTestConfig {


    @Test
    public void testControllerGetter(){
        String packageName = this.getClass().getPackageName();
        System.out.println("  PACKAGE > "+ packageName);
        System.out.println("  --------");

        DaxMessageCodec messageCodecAfter     = crmProvider.getMessageCodec();
        DaxMessage msg = new DaxMessage(CustomerDaxSchema.CRM_DATA_REQ);

        DaxMessage msgResp =  crmProvider.getDictionary().executor(msg);


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
