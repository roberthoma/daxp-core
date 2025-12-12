package DAXP_MulitiMessage_TEST;

import Dax_00_Base_test.DaxTestConfig;
import Dax_00_Base_test.customer.Customer;
import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.conventer.DaxMessageConverter;
import org.daxprotocol.core.factory.DaxMessageFactory;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.tag.DaxTag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class MultiMessage_Test extends DaxTestConfig {

//@Test
    void createMsgFromCustomer() {
        String msgStr = "DAXP|V=1|EN=UTF8|CNT=4|\n" +
                "9=UCi|2001=123|2002=Robert3|99=123|\n" +
                "9=UCi|2001=124|2002=Robert4|99=123|\n" +
                "9=UCi|2001=125|2002=Robert5|99=123|\n" +
                "9=UCi|2001=126|2002=Robert6|99=123|"
                ;

        List<DaxMessage> msgList = crmProvider.getMessageCodec().decodeAll(msgStr);
        int appContextId = crmProvider.getConfig().getApplicationContextId();
        Assertions.assertEquals("Robert5",msgList.get(2)
                                                  .getBody()
                                                  .getPair(0,new DaxTag(appContextId ,2002)).getStrValue()
        );

        Customer customer = crmProvider.getMessageConverter().createFromMessage(msgList.get(1),Customer.class);

        Assertions.assertEquals("Robert4", customer.getName());
    }

//@Test
    void createMsgFromCustomerList() {
        List<Customer> customerList = new ArrayList<>();

        customerList.add(new Customer(123, "Robert"));
        customerList.add(new Customer(124, "Ania"));
        customerList.add(new Customer(125, "Zofia"));

        DaxMessageFactory factory = crmProvider.getMessageFactory();
        DaxMessage message = factory.toDaxMessage("UCi", customerList);
//        Assertions.assertEquals(customerList.size(), message.getBlockCount() );  fix block.. for MessageCnt
    }


}
