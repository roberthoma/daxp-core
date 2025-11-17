package Dax_Messages_DD_Req;

import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.factory.DaxMessageFactory;
import org.daxprotocol.core.model.DaxMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Dax_message_dic_req {


    @Test
    public void base_message_request(){
        String reqMsgStr = "DAXP=1|TF=DEC|EN=UTF8|9=$DR|99=123|";
        DaxMessageFactory factory = new DaxMessageFactory();
        DaxMessageCodec codec = new DaxMessageCodec();
        DaxMessage message = factory.createDictionaryReq();

        String msgStrAfter = codec.encode(message);
        msgStrAfter = msgStrAfter.replace("\n","");

        Assertions.assertEquals(reqMsgStr,msgStrAfter);

    }
}
