package Dax_CC_Message_Test;

import Dax_00_Base_test.CustomerDaxDic;
import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.DaxMessageFactory;
import org.junit.jupiter.api.Test;

public class Dax_message_coder_test {

    @Test
    void testRequest() {
        DaxMessageFactory req = new DaxMessageFactory();
        DaxMessageCodec codec = new DaxMessageCodec();

        DaxMessage message = req.createDictionaryReq();

        System.out.println(message.getType());
        System.out.println(codec.encode(message));

    }

    @Test
    void testDicEncoder(){

        DaxMessageFactory factory = new DaxMessageFactory();

        DaxDictionary customerDic = new CustomerDaxDic();
        DaxMessage msg = factory.createDictionaryMsg(customerDic);
        DaxMessageCodec codec = new DaxMessageCodec();

        System.out.println(codec.encode(msg));


    }



}
