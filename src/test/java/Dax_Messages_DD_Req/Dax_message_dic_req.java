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
        String reqMsgStr = "DAXP="+ DaxpConfig.PROTOCOL_VERSION +"|EN=UTF-8|CX=CRM|9=SYS.DR|99=181|";
        DaxMessageFactory factory = crmProvider.getMessageFactory();

        DaxMessage message = factory.createDictionaryReq();

        String msgStrAfter = crmProvider.getMessageCodec().encode(message);

        msgStrAfter = msgStrAfter.replace("\n","");
        char separator = 0x0001;
        msgStrAfter = msgStrAfter.replace(separator,'|') ;

        Assertions.assertEquals(reqMsgStr,msgStrAfter);

    }
/*
    DAXP=1|TF=DEC|E=UTF8\n
4000=EURUSD|4001=1.05678|4012=ΔP/L|4013=ポジション|4014=Źródło=Dane Rynkowe|
            10=238|
*/
}
