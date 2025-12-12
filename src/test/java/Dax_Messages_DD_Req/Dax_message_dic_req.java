package Dax_Messages_DD_Req;

import Dax_00_Base_test.DaxTestConfig;
import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.factory.DaxMessageFactory;
import org.daxprotocol.core.model.DaxMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Dax_message_dic_req extends DaxTestConfig {


    @Test
    public void base_message_request(){
        String reqMsgStr = "DAXP|V=1|EN=UTF8|9=SYS.DR|99=123|";
        DaxMessageFactory factory = crmProvider.getMessageFactory();

        DaxMessage message = factory.createDictionaryReq();

        String msgStrAfter = crmProvider.getMessageCodec().encode(message);
        msgStrAfter = msgStrAfter.replace("\n","");
        char separator = 0x0001;
        msgStrAfter = msgStrAfter.replace(separator,'|') ;

        Assertions.assertEquals(reqMsgStr,msgStrAfter);

    }
}
