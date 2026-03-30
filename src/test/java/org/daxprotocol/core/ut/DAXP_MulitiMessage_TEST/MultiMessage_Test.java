package org.daxprotocol.core.ut.DAXP_MulitiMessage_TEST;

import org.daxprotocol.core.ut.Dax_00_Base_test.DaxTestConfig;
import org.daxprotocol.core.ut.Dax_00_Base_test.crm_application.customer.Customer;
import org.daxprotocol.core.factory.DaxMessageFactory;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.tag.DaxTag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class MultiMessage_Test extends DaxTestConfig {

    @Test
    void createMsgFromCustomer() {
        String msgStr = "DAXP|V=1|EN=UTF-8|CNT=4|" +
                "9=UCi|2001=123|2002=Robert3|99=123|" +
                "9=UCi|2001=124|2002=Robert4|99=123|" +
                "9=UCi|2001=125|2002=Robert5|99=123|" +
                "9=UCi|2001=126|2002=Robert6|99=123|"
                ;

        List<DaxMessage> msgList = crmProvider.getMessageCodec().decodeAll(msgStr);
        int appContextId = crmProvider.getConfig().getAppContextId();
        Assertions.assertEquals("Robert5",msgList.get(2)
                                                  .getBody()
                                                  .getPair(0,new DaxTag(appContextId ,2002)).getStrValue()
        );

        Customer customer = crmProvider.getMessageConverter().createFromMessage(msgList.get(1),Customer.class);

        System.out.println(msgStr);
        Assertions.assertEquals("Robert4", customer.getName());
    }

    @Test
    void createMsgFromCustomerList() {
        List<Customer> customerList = new ArrayList<>();

        customerList.add(new Customer(123, "Robert"));
        customerList.add(new Customer(124, "Ania"));
        customerList.add(new Customer(125, "Zofia"));

        DaxMessageFactory factory = crmProvider.getMessageFactory();
        DaxMessage message = factory.toDaxMessage("UCi", customerList);
        Assertions.assertEquals(customerList.size(), message.getBlockCount() );

        String messageEncode = crmProvider.getMessageCodec().encode(message);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(messageEncode);
        System.out.println("<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    @Test
    void createMsgFromCustomerWithAddress() {
        String msgStr = "DAXP=v0.1.0|EN=UTF-8|CX=CRM" +
                "|9=CDD|6=4" +
                "|7=1|5=INST|100=2000|2080=Big bike|2001=123|2002=Robert|2101=2|2085=23|2102=3|2075=INDIVIDUAL" +
                "|7=2|5=INST|100=2101|2114=Polna 7|2115=Warszawa|2111=345" +
                "|7=3|5=INST|100=2102|2114=Lipińskiego 1000|2115=Sanok|2120=4|2111=3346" +
                "|7=4|5=INST|100=2120|2121=43-444|2122=Zakopane|99=177|"+
                "|9=CDD|6=4" +
                "|7=1|5=INST|100=2000|2080=Small boll|2001=123|2002=Kasia|2101=2|2085=23|2102=3|2075=INDIVIDUAL" +
                "|7=2|5=INST|100=2101|2114=Polna 8|2115=Kraków|2111=333" +
                "|7=3|5=INST|100=2102|2114=Sowia 1000|2115=Lesko|2120=4|2111=3346" +
                "|7=4|5=INST|100=2120|2121=44-444|2122=Rzeszów|99=177|";
                ;

        //List<DaxMessage> msgList = crmProvider.getMessageCodec().decodeAll(msgStr);
        List<DaxMessage> msgList = crmProvider.getMessageCodec().decodeAll(msgStr);

        String msgAfter = crmProvider.getMessageCodec().encode(msgList.get(0));

        System.out.println(msgStr);
        System.out.println(msgAfter);

    }



}
