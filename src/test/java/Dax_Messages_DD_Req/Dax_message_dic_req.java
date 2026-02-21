package Dax_Messages_DD_Req;

import Dax_00_Base_test.DaxTestConfig;
import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.factory.DaxMessageFactory;
import org.daxprotocol.core.model.DaxMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Dax_message_dic_req extends DaxTestConfig {


    @Test
    public void base_message_request(){
        String reqMsgStr = "DAXP|V="+ DaxpConfig.PROTOCOL_VERSION +"|EN=UTF-8|CX=CMR|9=SYS.DR|99=123|";
        DaxMessageFactory factory = cmrProvider.getMessageFactory();

        DaxMessage message = factory.createDictionaryReq();

        String msgStrAfter = cmrProvider.getMessageCodec().encode(message);

        msgStrAfter = msgStrAfter.replace("\n","");
        char separator = 0x0001;
        msgStrAfter = msgStrAfter.replace(separator,'|') ;

        Assertions.assertEquals(reqMsgStr,msgStrAfter);

    }
}
